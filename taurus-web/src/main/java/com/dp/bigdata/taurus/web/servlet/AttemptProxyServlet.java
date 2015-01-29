package com.dp.bigdata.taurus.web.servlet;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dianping.cat.Cat;
import com.dianping.lion.EnvZooKeeperConfig;
import com.dianping.lion.client.ConfigCache;
import com.dianping.lion.client.LionException;
import com.dp.bigdata.taurus.restlet.resource.IAttemptResource;
import com.dp.bigdata.taurus.restlet.resource.IExistTaskRunning;
import com.dp.bigdata.taurus.restlet.resource.IHostResource;
import com.dp.bigdata.taurus.restlet.resource.ILogResource;
import com.dp.bigdata.taurus.restlet.shared.AttemptDTO;
import com.dp.bigdata.taurus.restlet.shared.HostDTO;
import com.dp.bigdata.taurus.web.utils.ReFlashHostLoadTask;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

/**
 * AttemptProxyServlet
 *
 * @author damon.zhu
 */
public class AttemptProxyServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = -2924647981910768516L;

    private static final String KILL = "kill";
    private static final String RUNLOG = "runlog";
    private static final String ISEND = "isend";
    private static final String ISNEW = "isnew";
    private static final String STATUS = "status";
    private static final String LOG = "view-log";
    private static final String ISVIEWLOG = "isviewlog";
    private static final String ISEXIST_RUNNING_TASK = "runningtask";

    public static String RESTLET_URL_BASE;
    private String ERROR_PAGE;
    private String AGENT_PORT;
    private String NEW_AGENT_PORT;


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext context = getServletContext();

        ERROR_PAGE = context.getInitParameter("ERROR_PAGE");
        try {
            RESTLET_URL_BASE = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.web.restlet.url");
            AGENT_PORT = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.agent.restlet.port");
            NEW_AGENT_PORT = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.agent.restlet.new.port");
        } catch (LionException e) {
            RESTLET_URL_BASE = context.getInitParameter("RESTLET_SERVER");
            NEW_AGENT_PORT = "8088";
            Cat.logError("AttemptProxyServlet init LionException", e);
        } catch (Exception e) {
            Cat.logError("AttemptProxyServlet init Exception", e);
        }
    }

    public static String getAgentRestService(String restUrl) {

        InputStream inputStream = null;
        String msgContent = "";

        URL getUrl = null;
        try {
            getUrl = new URL(restUrl);

            HttpURLConnection connection = (HttpURLConnection) getUrl
                    .openConnection();
            connection.setConnectTimeout(1000);
            connection.connect();
            StringWriter writer = new StringWriter();
            inputStream = connection.getInputStream();
            IOUtils.copy(inputStream, writer, "UTF-8");
            msgContent = writer.toString();


            // 断开连接
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return msgContent;
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
        boolean result = true;
        if (hostName == null || hostName.isEmpty()) {
            result = true;
        } else {
            String jsonString = ReFlashHostLoadTask.hostLoadJsonData;
            if (jsonString == null || jsonString.isEmpty()) {
                result = true;
            } else {
                try {
                    JSONArray jsonArray = new JSONArray(jsonString);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jo = (JSONObject) jsonArray.get(i);
                        if (jo == null) {
                            result = true;
                            return result;
                        }

                        String zabbixHostName = "";

                        if (jo.get("hostName") != null) {
                            zabbixHostName = jo.get("hostName").toString();
                        }

                        if (StringUtils.isNotBlank(zabbixHostName)) {
                            if (zabbixHostName.equals(hostName)) {
                                String cpuLoad = "";
                                if (jo.get("cpuLoad") != null) {
                                    cpuLoad = jo.get("cpuLoad").toString();
                                }

                                if (StringUtils.isNotBlank(cpuLoad)) {
                                    Double highValue;
                                    if (cpuLoad.trim().equals("null")) {
                                        highValue = 10.0;
                                    } else {
                                        highValue = Double.parseDouble(cpuLoad);
                                    }

                                    if (highValue <= 4.0) {
                                        result = false;
                                    } else {
                                        result = true;
                                    }

                                } else {
                                    result = true;
                                }

                                break;
                            }

                        } else {
                            result = true;
                        }

                    }

                    return result;
                } catch (JSONException e) {
                    return true;
                }
            }
        }
        return result;
    }


    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String attemptID = request.getParameter("id");
        String action = request.getParameter("action") == null ? "" : request.getParameter("action").toLowerCase();

        ClientResource attemptCr = new ClientResource(RESTLET_URL_BASE + "attempt/" + attemptID);
        ILogResource attemptResource = attemptCr.wrap(ILogResource.class);

        if (action.equals(KILL)) {
            attemptResource.kill();
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
                    getServletContext().getRequestDispatcher(ERROR_PAGE).forward(request, response);
                }
            } catch (Exception e) {
                getServletContext().getRequestDispatcher(ERROR_PAGE).forward(request, response);
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
            ClientResource attemptLogCr = new ClientResource(RESTLET_URL_BASE + "getattemptbyid/" + attemptID);

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
                    ClientResource cr = new ClientResource(RESTLET_URL_BASE + "host/" + hostName);
                    IHostResource hostResource = cr.wrap(IHostResource.class);
                    cr.accept(MediaType.APPLICATION_XML);
                    HostDTO hostDTO = hostResource.retrieve();
                    String agentPort = "";
                    if (hostDTO.getInfo().getAgentVersion().equals("0.5.0")) {
                        agentPort = AGENT_PORT;
                    } else {
                        agentPort = NEW_AGENT_PORT;
                    }

                    contentLenStr = (String) request.getSession().getAttribute(fileSizeAttribute);
                    String logurl = "http://" + hostName + ":" + agentPort
                            + "/agentrest.do?action=getlog"
                            + "&flag=NORMAL"
                            + "&query_type=" + queryType;
                    String context = getAgentRestService(logurl);
                    String retStr;                                              //格式化日志 以便在web显示是换行的
                    String logStr = context;
                    OutputStream output = response.getOutputStream();

                    if (StringUtils.isBlank(logStr)) {                                     //时间间隔短，日志尚未生成可能获得null
                        retStr = " ";
                    } else {
                        retStr = logStr.replace("\n", "<br>");
                    }
                    output.write(retStr.getBytes());
                    output.close();
                }

                if (contentLenStr == null) {
                    lastTimeFileSize = 0;
                } else {
                    lastTimeFileSize = Long.parseLong(contentLenStr);
                }

                IAttemptResource attemptLogResource = attemptLogCr.wrap(IAttemptResource.class);
                AttemptDTO dto = attemptLogResource.retrieve();

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
                        ClientResource cr = new ClientResource(RESTLET_URL_BASE + "host/" + hostIp);
                        IHostResource hostResource = cr.wrap(IHostResource.class);
                        cr.accept(MediaType.APPLICATION_XML);
                        HostDTO hostDTO = hostResource.retrieve();
                        String agentPort;
                        if (hostDTO.getInfo().getAgentVersion().equals("0.5.0")) {
                            agentPort = AGENT_PORT;
                        } else {
                            agentPort = NEW_AGENT_PORT;
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

                        if (StringUtils.isBlank(context)) {
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

                        String retStr;
                        //格式化日志 以便在web显示是换行的
                        OutputStream output = response.getOutputStream();


                        if (StringUtils.isBlank(context)) {                                     //时间间隔短，日志尚未生成可能获得null
                            retStr = " ";
                        } else {
                            retStr = context.replace("\n", "<br>");
                        }

                        output.write(retStr.getBytes());
                        output.close();
                    } catch (Exception e) {
                        String exceptMessage = e.getMessage();
                        if (exceptMessage.equals("Connection Error") || exceptMessage.equals("Not Found")) {
                            response.setContentType("text/html;charset=utf-8");
                            ClientResource oldAgentCr = new ClientResource(RESTLET_URL_BASE + "attempt/" + attemptID);
                            try {
                                Representation rep = oldAgentCr.get(MediaType.TEXT_HTML);
                                if (attemptCr.getStatus().getCode() == 200) {
                                    OutputStream output = response.getOutputStream();
                                    rep.write(output);
                                    output.close();
                                } else {
                                    getServletContext().getRequestDispatcher(ERROR_PAGE).forward(request, response);
                                }
                            } catch (Exception except) {
                                getServletContext().getRequestDispatcher(ERROR_PAGE).forward(request, response);
                            }
                        } else {
                            getServletContext().getRequestDispatcher(ERROR_PAGE).forward(request, response);
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
            ClientResource attemptLogCr = new ClientResource(RESTLET_URL_BASE + "getattemptbyid/" + attemptID);
            IAttemptResource attemptLogResource = attemptLogCr.wrap(IAttemptResource.class);
            AttemptDTO dto = attemptLogResource.retrieve();

            if (dto != null) {
                host = dto.getExecHost();
            }

            String respStr = "";
            if (host == null || host.isEmpty()) {
                OutputStream output = response.getOutputStream();
                output.close();
            } else {
                ClientResource cr = new ClientResource(RESTLET_URL_BASE + "host/" + host);
                IHostResource hostResource = cr.wrap(IHostResource.class);
                cr.accept(MediaType.APPLICATION_XML);
                HostDTO hostDTO = hostResource.retrieve();
                String agentPort = "";
                if (hostDTO.getInfo().getAgentVersion().equals("0.5.0")) {
                    agentPort = AGENT_PORT;
                } else {
                    agentPort = NEW_AGENT_PORT;
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
            ClientResource attemptLogCr = new ClientResource(RESTLET_URL_BASE + "getattemptbyid/" + attemptID);
            IAttemptResource attemptLogResource = attemptLogCr.wrap(IAttemptResource.class);
            AttemptDTO dto = attemptLogResource.retrieve();

            if (dto != null) {
                taskStatus = dto.getStatus();
            }

            OutputStream output = response.getOutputStream();
            output.write(taskStatus.getBytes());
            output.close();
        } else if (action.equals(ISNEW)) {
            OutputStream output = response.getOutputStream();
            ClientResource attemptLogCr = new ClientResource(RESTLET_URL_BASE + "getattemptbyid/" + attemptID);
            IAttemptResource attemptLogResource = attemptLogCr.wrap(IAttemptResource.class);
            AttemptDTO dto = attemptLogResource.retrieve();
            String isNew;
            if (dto != null) {
                ClientResource cr = new ClientResource(RESTLET_URL_BASE + "host/" + dto.getExecHost());
                IHostResource hostResource = cr.wrap(IHostResource.class);
                cr.accept(MediaType.APPLICATION_XML);
                HostDTO hostDTO = hostResource.retrieve();
                String agentPort = "";
                if (hostDTO.getInfo().getAgentVersion().equals("0.5.0")) {
                    agentPort = AGENT_PORT;
                } else {
                    agentPort = NEW_AGENT_PORT;
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

            ClientResource runningTaskCr = new ClientResource(RESTLET_URL_BASE + "runningtask/" + taskId);
            IExistTaskRunning taskRunningResource = runningTaskCr.wrap(IExistTaskRunning.class);
            String isExist = taskRunningResource.retrieve();

            output.write(isExist.getBytes());
            output.close();
        }

    }


}
