package com.cip.crane.springmvc.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.apache.commons.lang.StringUtils;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cip.crane.restlet.shared.AttemptDTO;
import com.cip.crane.restlet.shared.HostDTO;
import com.cip.crane.restlet.utils.LionConfigUtil;

@Controller
public class AttemptProxyController {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final String KILL = "kill"; // attempt.js
    private final String RUNLOG = "runlog"; // hosts.js & viewlog.js
    private final String ISEND = "isend"; // viewlog.js
    private final String ISNEW = "isnew";
    private final String STATUS = "status"; // viewlog.js
    private final String LOG = "view-log";
    private final String ISVIEWLOG = "isviewlog";
    private final String ISEXIST_RUNNING_TASK = "runningtask"; // schedule.js
    
    @RequestMapping(value = "/attempts.do", method = RequestMethod.POST)
	public void attemptsDoPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		log.info("--------------init the attemptsDoPost------------");
		String attemptID = request.getParameter("id");
        String action = request.getParameter("action") == null ? "" : request.getParameter("action").toLowerCase();

        ClientResource attemptCr = new ClientResource(LionConfigUtil.RESTLET_API_BASE + "attempt/" + attemptID);

        if (action.equals(KILL)) {
        	attemptCr.delete();
            response.setStatus(attemptCr.getStatus().getCode());
        } else if (action.equals(LOG)) {
            response.setContentType("text/html;charset=utf-8");
            try {
                Representation rep = attemptCr.get(MediaType.TEXT_HTML);
                if (attemptCr.getStatus().getCode() == 200) {
                    OutputStream output = response.getOutputStream();
                    rep.write(output);
                    output.close();
                } else {
                	request.getRequestDispatcher(InitController.ERROR_PAGE).forward(request, response);
                }
            } catch (Exception e) {
            	request.getRequestDispatcher(InitController.ERROR_PAGE).forward(request, response);
            }
        } else if (action.equals(RUNLOG)) {

            String contentLenStr;                                   //从agent取来的日志长度（String的）
            String hostIp = "";                                     //agent 主机ip
            String tureStatus = "";                                 //任务的真实状态 （从参数获得的可能会过期）
            String fileSizeAttribute;                          //区别是log的文件偏移属性 还是error-log的文件偏移

            long lastTimeFileSize;                                  //文件偏移

            String queryType = request.getParameter("querytype");   //区分js请求是log，还是error log

            Date startTime = null;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            ClientResource attemptLogCr = new ClientResource(LionConfigUtil.RESTLET_API_BASE + "getattemptbyid/" + attemptID);

            try {

                if (queryType.equals("log")) {
                    fileSizeAttribute = "lastTimeFileSize";
                    contentLenStr = (String) request.getSession().getAttribute(fileSizeAttribute);
                } else if (queryType.equals("errorlog")) {
                    fileSizeAttribute = "errorLastTimeFileSize";
                    contentLenStr = (String) request.getSession().getAttribute(fileSizeAttribute);
                } else {
                    fileSizeAttribute = "agentlogs";
                    String hostName = request.getParameter("hostname");
                    ClientResource cr = new ClientResource(LionConfigUtil.RESTLET_API_BASE + "host/" + hostName);
                    HostDTO hostDTO = cr.get(HostDTO.class);
                    String agentPort = "";
                    if (hostDTO.getInfo().getAgentVersion().equals("0.5.0")) {
                        agentPort = InitController.AGENT_PORT;
                    } else {
                        agentPort = InitController.NEW_AGENT_PORT;
                    }

                    contentLenStr = (String) request.getSession().getAttribute(fileSizeAttribute);
                    String logurl = "http://" + hostName + ":" + agentPort
                            + "/agentrest.do?action=getlog"
                            + "&flag=NORMAL"
                            + "&query_type=" + queryType;
                    String context = getAgentRestService(logurl);
                    String retStr;                                              //格式化日志 以便在web显示是换行的
                    OutputStream output = response.getOutputStream();

                    if (StringUtils.isBlank(context)) {                                     //时间间隔短，日志尚未生成可能获得null
                        retStr = " ";
                    } else {
                        retStr = context.replace("\n", "<br>");
                    }
                    output.write(retStr.getBytes());
                    output.close();
                }

                if (contentLenStr == null) {
                    lastTimeFileSize = 0;
                } else {
                    lastTimeFileSize = Long.parseLong(contentLenStr);
                }

                AttemptDTO dto = attemptLogCr.get(AttemptDTO.class);

                if (dto != null) {
                    hostIp = dto.getExecHost();
                    startTime = dto.getStartTime();
                    tureStatus = dto.getStatus();

                }

                String date;                                  //取哪天的日志
                String isEnd;                                 //标记任务是否执行完成
                boolean acceptContentWay = false;             //false :NORMAL 全量接受；true：INC 增量接受

                if (startTime == null) {
                    date = format.format(new Date());
                } else {
                    date = format.format(startTime);
                }

                if (hostIp.isEmpty()) {
                    OutputStream output = response.getOutputStream();
                    output.close();
                } else {

                    try {
                        ClientResource cr = new ClientResource(LionConfigUtil.RESTLET_API_BASE + "host/" + hostIp);
                        HostDTO hostDTO = cr.get(HostDTO.class);
                        String agentPort;
                        if (hostDTO.getInfo().getAgentVersion().equals("0.5.0")) {
                            agentPort = InitController.AGENT_PORT;
                        } else {
                            agentPort = InitController.NEW_AGENT_PORT;
                        }

                        String url;                //请求agent restlet的URI

                        if (lastTimeFileSize == 0 && !tureStatus.equals("RUNNING")) {    //如果任务真实状态不是运行中的，并且 文件偏移为0 ，说明是历史任务，直接全量获取日志
                            url = "http://" + hostIp + ":" + agentPort
                                    + "/agentrest.do?action=getlog&date="
                                    + date
                                    + "&attemptId=" + attemptID
                                    + "&flag=NORMAL"
                                    + "&query_type=" + queryType;
                        } else {                                                        //增量获取日志
                            url = "http://" + hostIp + ":" + agentPort
                                    + "/agentrest.do?action=getlog&date="
                                    + date
                                    + "&attemptId=" + attemptID
                                    + "&flag=INC"
                                    + "&query_type=" + queryType;

                            acceptContentWay = true;
                        }

                        String context = getAgentRestService(url);

                        if (StringUtils.isNotBlank(context)) {
                            lastTimeFileSize += context.length();
                        } else {
                            if (!acceptContentWay) {
                                context = "无日志数据";
                            }
                        }

                        String isEndUrl = "http://" + hostIp
                                + ":" + agentPort
                                + "/agentrest.do?action=isend&attemptId="
                                + attemptID;
                        isEnd = getAgentRestService(isEndUrl);

                        if (acceptContentWay && isEnd.equals("false")) {
                            request.getSession().setAttribute(fileSizeAttribute, ((Long) lastTimeFileSize).toString());
                        } else if (isEnd.trim().equals("true")) {
                            request.getSession().setAttribute(fileSizeAttribute, "0");
                        }

                        //格式化日志 以便在web显示是换行的
                        OutputStream output = response.getOutputStream();


                        if (StringUtils.isBlank(context)) {                                     //时间间隔短，日志尚未生成可能获得null
                            context = " ";
                        } else {
                           // context = context.replace("\n", "<br>");
                        }

                        output.write(context.getBytes());
                        output.close();
                    } catch (Exception e) {
                        String exceptMessage = e.getMessage();
                        if (exceptMessage.equals("Connection Error") || exceptMessage.equals("Not Found")) {
                            response.setContentType("text/html;charset=utf-8");
                            ClientResource oldAgentCr = new ClientResource(LionConfigUtil.RESTLET_API_BASE + "attempt/" + attemptID);
                            try {
                                Representation rep = oldAgentCr.get(MediaType.TEXT_HTML);
                                if (attemptCr.getStatus().getCode() == 200) {
                                    OutputStream output = response.getOutputStream();
                                    rep.write(output);
                                    output.close();
                                } else {
                                    request.getRequestDispatcher(InitController.ERROR_PAGE).forward(request, response);
                                }
                            } catch (Exception except) {
                            	request.getRequestDispatcher(InitController.ERROR_PAGE).forward(request, response);
                            }
                        } else {
                        	request.getRequestDispatcher(InitController.ERROR_PAGE).forward(request, response);
                        }
                    }
                }
            } catch (Exception e) {
                String exceptMessage = e.getMessage();
                if (exceptMessage != null && exceptMessage.equals("Connection Error")) {
                    //说明agent端的restlet连不上：1.网络原因 .2agent 挂了
                    String responseStr = "<font color=red>【服务异常】- -# agent 连接有问题，请联系管理员</font>";
                    OutputStream output = response.getOutputStream();
                    output.write(responseStr.getBytes());
                    output.close();
                }
            }

        } else if (action.equals(ISEND)) {
            String host = "";
            ClientResource attemptLogCr = new ClientResource(LionConfigUtil.RESTLET_API_BASE + "getattemptbyid/" + attemptID);
            AttemptDTO dto = attemptLogCr.get(AttemptDTO.class);

            if (dto != null) {
                host = dto.getExecHost();
            }

            String respStr = "";
            if (host == null || host.isEmpty()) {
                OutputStream output = response.getOutputStream();
                output.close();
            } else {
                ClientResource cr = new ClientResource(LionConfigUtil.RESTLET_API_BASE + "host/" + host);
                HostDTO hostDTO = cr.get(HostDTO.class);
                String agentPort = "";
                if (hostDTO.getInfo().getAgentVersion().equals("0.5.0")) {
                    agentPort = InitController.AGENT_PORT;
                } else {
                    agentPort = InitController.NEW_AGENT_PORT;
                }

                String url = "http://" + host + ":" + agentPort + "/agentrest.do?action=isend&attemptId=" + attemptID;
                respStr = getAgentRestService(url);
            }
            if (respStr.equals("null")) {
                respStr = "old";
            }
            OutputStream output = response.getOutputStream();
            output.write(respStr.getBytes());
            output.close();
        } else if (action.equals(STATUS)) {
            String taskStatus = "";
            ClientResource attemptLogCr = new ClientResource(LionConfigUtil.RESTLET_API_BASE + "getattemptbyid/" + attemptID);
            AttemptDTO dto = attemptLogCr.get(AttemptDTO.class);

            if (dto != null) {
                taskStatus = dto.getStatus();
            }

            OutputStream output = response.getOutputStream();
            output.write(taskStatus.getBytes());
            output.close();
        } else if (action.equals(ISNEW)) {
            OutputStream output = response.getOutputStream();
            ClientResource attemptLogCr = new ClientResource(LionConfigUtil.RESTLET_API_BASE + "getattemptbyid/" + attemptID);
            AttemptDTO dto = attemptLogCr.get(AttemptDTO.class);
            String isNew;
            if (dto != null) {
                ClientResource cr = new ClientResource(LionConfigUtil.RESTLET_API_BASE + "host/" + dto.getExecHost());
                HostDTO hostDTO = cr.get(HostDTO.class);
                String agentPort = "";
                if (hostDTO.getInfo().getAgentVersion().equals("0.5.0")) {
                    agentPort = InitController.AGENT_PORT;
                } else {
                    agentPort = InitController.NEW_AGENT_PORT;
                }

                String url = "http://" + dto.getExecHost() + ":" + agentPort + "/agentrest.do?action=isnew";
                isNew = getAgentRestService(url);
                if (isNew.isEmpty()) {
                    isNew = "null";
                }
            } else {
                isNew = "null";
            }

            output.write(isNew.getBytes());
            output.close();
        } else if (ISVIEWLOG.equals(action)) {
            OutputStream output = response.getOutputStream();
            String isView;
            String ip = request.getParameter("ip");
            boolean isviewlog = isHostOverLoad(ip);

            if (isviewlog) {
                isView = "false";
            } else {
                isView = "true";
            }

            output.write(isView.getBytes());
            output.close();
        } else if (ISEXIST_RUNNING_TASK.equals(action)) {
            OutputStream output = response.getOutputStream();

            String taskId = request.getParameter("taskId");

            ClientResource runningTaskCr = new ClientResource(LionConfigUtil.RESTLET_API_BASE + "runningtask/" + taskId);
            String isExist = runningTaskCr.get(String.class);

            output.write(isExist.getBytes());
            output.close();
        }
	}
    
    
    public static String getAgentRestService(String restUrl) {
    	
    	ClientConfig configuration = new ClientConfig();
    	configuration = configuration.property(ClientProperties.CONNECT_TIMEOUT, 1000);
    	configuration = configuration.property(ClientProperties.READ_TIMEOUT, 1000);
    	Client client = ClientBuilder.newClient(configuration);
    	WebTarget target = client.target(restUrl);
    	return target.request().get(String.class);
    	
        /*Client client = Client.create();
        client.setConnectTimeout(1000);
        client.setReadTimeout(1000);
        WebResource webResource = client.resource(restUrl);
        return webResource.get(String.class);*/
    }
    
    private static String getHostName(String ip) {

        String hostNameurl = "http://api.cmdb.dp/api/v0.1/ci/s?q=_type:(server;vserver),private_ip:" + ip + "&fl=hostname";
        String jsonData = getAgentRestService(hostNameurl);
        JSONObject jsonMap;
        String jsonHostName;
        String hostName = "";
        try {
            jsonMap = new JSONObject(jsonData);
            jsonHostName = jsonMap.getString("result");
            if (jsonHostName.contains("[") && jsonHostName.contains("]")) {
                JSONObject itemJson = new JSONObject(jsonHostName.replace("[", "").replace("]", ""));
                hostName = itemJson.getString("hostname");
            } else {
                hostName = null;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (StringUtils.isNotBlank(hostName)) {
            return hostName;
        } else {
            return null;
        }


    }

    public static boolean isHostOverLoad(String ip) {
        
    	String hostName = getHostName(ip);
        ClientResource cr = new ClientResource(LionConfigUtil.RESTLET_API_BASE + "reflashHostLoad/" + hostName);
        
        return cr.get(boolean.class);
    }
}
