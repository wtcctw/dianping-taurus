package com.dp.bigdata.taurus.web.servlet;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dianping.lion.EnvZooKeeperConfig;
import com.dianping.lion.client.ConfigCache;
import com.dianping.lion.client.LionException;
import com.dp.bigdata.taurus.restlet.resource.IAttemptResource;
import com.dp.bigdata.taurus.restlet.resource.ILogResource;
import com.dp.bigdata.taurus.restlet.shared.AttemptDTO;
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

    private String RESTLET_URL_BASE;
    private String ERROR_PAGE;
    private String AGENT_PORT;


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext context = getServletContext();

        ERROR_PAGE = context.getInitParameter("ERROR_PAGE");
        try {
            RESTLET_URL_BASE =ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.web.restlet.url");// context.getInitParameter("RESTLET_SERVER");
            AGENT_PORT = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.agent.restlet.port");//context.getInitParameter("AGENT_SERVER_PORT");
        } catch (LionException e) {
            RESTLET_URL_BASE = context.getInitParameter("RESTLET_SERVER");
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("AttemptProxyServlet EERROR++++++++:"+e.getMessage());
        }
    }

    private static String getAgentRestService(String restUrl) {

        InputStream inputStream = null;
        String msgContent = "";
        try {
            URL url = new URL(restUrl);
            inputStream = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";

            while ((line = br.readLine()) != null) {
                msgContent += line +"\n";
            }
            inputStream.close();
            return msgContent;
        } catch (IOException e) {
            msgContent= "null";
        }
        return msgContent;
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
            String fileSizeAttribute = "";                          //区别是log的文件偏移属性 还是error-log的文件偏移

            long lastTimeFileSize;                                  //文件偏移

            String queryType = request.getParameter("querytype");   //区分js请求是log，还是error log

            Date endTime = null;                                    //这个是取该任务的最后执行日期的日志，如果是RUNNING，endTime为空，则取当天日志
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            ClientResource attemptLogCr = new ClientResource(RESTLET_URL_BASE + "getattemptbyid/" + attemptID);

            try {

                if (queryType.equals("log")) {
                    fileSizeAttribute = "lastTimeFileSize";
                    contentLenStr = (String) request.getSession().getAttribute(fileSizeAttribute);
                } else if(queryType.equals("errorlog")){
                    fileSizeAttribute = "errorLastTimeFileSize";
                    contentLenStr = (String) request.getSession().getAttribute(fileSizeAttribute);
                }else {
                    fileSizeAttribute = "agentlogs";
                    String hostName = request.getParameter("hostname");
                    contentLenStr = (String) request.getSession().getAttribute(fileSizeAttribute);
                   String  logurl = "http://" + hostName + ":" + AGENT_PORT
                            + "/agentrest.do?action=getlog"
                            + "&flag=NORMAL"
                            + "&query_type=" + queryType;
                    String context = getAgentRestService(logurl);
                    String retStr;                                              //格式化日志 以便在web显示是换行的
                    String logStr = context;
                    OutputStream output = response.getOutputStream();

                    if (logStr == null) {                                     //时间间隔短，日志尚未生成可能获得null
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
                    endTime = dto.getEndTime();
                    tureStatus = dto.getStatus();

                }

                String date;                                  //取哪天的日志
                String isEnd;                                 //标记任务是否执行完成
                boolean acceptContentWay = false;             //false :NORMAL 全量接受；true：INC 增量接受

                if (endTime == null) {
                    date = format.format(new Date());
                } else {
                    date = format.format(endTime);
                }

                if (hostIp.isEmpty()) {
                    OutputStream output = response.getOutputStream();
                    output.close();
                } else {

                    try {

                        String isNewUrl= "http://" + hostIp + ":" + AGENT_PORT + "/agentrest.do?action=isnew";
                        String isNew =  getAgentRestService(isNewUrl).trim();
                        if (!isNew.equals("true")/*||isNew.equals("null")*/) {
                            String oldUrl = "/attempts.do?id=" + attemptID + "&action=view-log";
                            response.sendRedirect(oldUrl);


                        } else {
                            String url = "";                //请求agent restlet的URI

                            if (lastTimeFileSize == 0 && !tureStatus.equals("RUNNING")) {    //如果任务真实状态不是运行中的，并且 文件偏移为0 ，说明是历史任务，直接全量获取日志
                                url = "http://" + hostIp + ":" + AGENT_PORT
                                        + "/agentrest.do?action=getlog&date="
                                        + date
                                        + "&attemptId=" + attemptID
                                        + "&flag=NORMAL"
                                        + "&query_type=" + queryType;
                            } else {                                                        //增量获取日志
                                url = "http://" + hostIp + ":" + AGENT_PORT
                                        + "/agentrest.do?action=getlog&date="
                                        + date
                                        + "&attemptId=" + attemptID
                                        + "&flag=INC"
                                        + "&query_type=" + queryType;

                                acceptContentWay = true;
                            }

                         //   getLogCr = new ClientResource(url);
                            long start = System.currentTimeMillis();
                            String context = getAgentRestService(url);//getLogCr.get().getText();

                            if (context != null) {
                                lastTimeFileSize += context.length();
                            } else {
                                if (!acceptContentWay) {
                                    context = "无日志数据";
                                }
                            }

                            String isEndUrl = "http://" + hostIp
                                    + ":" + AGENT_PORT
                                    + "/agentrest.do?action=isend&attemptId="
                                    + attemptID;
                             isEnd = getAgentRestService(isEndUrl);

                            if (acceptContentWay && isEnd.equals("false")) {
                                request.getSession().setAttribute(fileSizeAttribute, ((Long) lastTimeFileSize).toString());
                            } else if (isEnd.trim().equals("true")) {
                                request.getSession().setAttribute(fileSizeAttribute, "0");
                            }

                            String retStr;                                              //格式化日志 以便在web显示是换行的
                            String logStr = context;
                            OutputStream output = response.getOutputStream();

                            if (logStr == null) {                                     //时间间隔短，日志尚未生成可能获得null
                                retStr = " ";
                            } else {
                                retStr = logStr.replace("\n", "<br>");
                            }

                            long end = System.currentTimeMillis();

                            System.out.println("#######"+queryType+"###Length: "+retStr.length()+"#####TIME:"+(end - start) );
                            output.write(retStr.getBytes());
                            output.close();
                        }

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
                if (exceptMessage.equals("Connection Error")) {
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
            if (host.isEmpty()) {
                OutputStream output = response.getOutputStream();
                output.close();
            } else {
                //String url = "http://" + host + ":" + AGENT_PORT + "/api/isend/" + attemptID;
                //getLogCr = new ClientResource(url);
                String url= "http://" + host + ":" + AGENT_PORT + "/agentrest.do?action=isend&attemptId=" + attemptID;
                respStr = getAgentRestService(url);
            }
            if (respStr .equals("null")){
                respStr = "old";
            }
            //Representation repLog = getLogCr.get(MediaType.TEXT_HTML);
            OutputStream output = response.getOutputStream();
            //repLog.write(output);
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
            if (dto !=null){

                String url= "http://" + dto.getExecHost() + ":" + AGENT_PORT + "/agentrest.do?action=isnew";
                isNew =  getAgentRestService(url);
                if (isNew.isEmpty()) {
                    isNew = "null";
                }
            }else {
                isNew = "null";
            }

            output.write(isNew.getBytes());
            output.close();
        }

    }


}
