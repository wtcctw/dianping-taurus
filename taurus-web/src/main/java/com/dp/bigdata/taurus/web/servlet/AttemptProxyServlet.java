package com.dp.bigdata.taurus.web.servlet;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
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
import com.dp.bigdata.taurus.web.utils.ReFlashHostLoadTask;
import com.google.gson.JsonArray;
import org.apache.commons.io.IOUtils;
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

    public static String getAgentRestService(String restUrl) {

        InputStream inputStream = null;
        String msgContent = "";

        URL getUrl = null;
        try {
            getUrl = new URL(restUrl);

        HttpURLConnection connection = (HttpURLConnection) getUrl
                .openConnection();
        connection.connect();
        StringWriter writer = new StringWriter();
        inputStream =  connection.getInputStream();
        IOUtils.copy(inputStream, writer, "UTF-8");
         msgContent = writer.toString();


        // 断开连接
        connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return msgContent;
    }

    private static String getHostName(String ip){

        String  hostNameurl = "http://api.cmdb.dp/api/v0.1/ci/s?q=_type:(server;vserver),private_ip:"+ip+"&fl=hostname";
        String jsonData = getAgentRestService(hostNameurl);
        JSONObject jsonMap = null;
        String jsonHostName = null;
        String hostName="";
        try {
            jsonMap = new JSONObject(jsonData);
            jsonHostName = jsonMap.getString("result");
            JSONObject itemJson = new JSONObject(jsonHostName.replace("[","").replace("]",""));
            hostName= itemJson.getString("hostname");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (hostName !=null || !hostName.isEmpty()){
            return hostName;
        }else{
            return null;
        }


    }

    public static boolean isHostOverLoad(String ip){
        String hostName = getHostName(ip);
        boolean result = true;
        if (hostName==null || hostName.isEmpty()){
            result =  true;
        }else {
           // String jsonString = ReFlashHostLoadTask.hostLoadJsonData;
            String jsonString = "[{\"hostId\":\"13543\",\"hostName\":\"poidupshopdetect-service01.nh\",\"cpuLoad\":\"0.9400\",\"memLoad\":\"97MB\"},{\"hostId\":\"13544\",\"hostName\":\"java-jobs01.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"235MB\"},{\"hostId\":\"13545\",\"hostName\":\"poidupshopdetect-service02.nh\",\"cpuLoad\":\"0.1800\",\"memLoad\":\"125MB\"},{\"hostId\":\"13546\",\"hostName\":\"java-jobs02.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"1418MB\"},{\"hostId\":\"13547\",\"hostName\":\"mobi-ossweb01.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"1286MB\"},{\"hostId\":\"13548\",\"hostName\":\"poi-job01.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"311MB\"},{\"hostId\":\"13549\",\"hostName\":\"credit-hive01.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"6505MB\"},{\"hostId\":\"13550\",\"hostName\":\"search-hisreview02.nh\",\"cpuLoad\":\"0.9900\",\"memLoad\":\"9069MB\"},{\"hostId\":\"13551\",\"hostName\":\"search-qu-job01.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"25307MB\"},{\"hostId\":\"13552\",\"hostName\":\"mobile-oss-message-job02.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"1202MB\"},{\"hostId\":\"13553\",\"hostName\":\"midas-material-worker02.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"143MB\"},{\"hostId\":\"13554\",\"hostName\":\"dw2www-job01.nh\",\"cpuLoad\":\"0.0100\",\"memLoad\":\"1223MB\"},{\"hostId\":\"13555\",\"hostName\":\"midas-merchant-worker01.nh\",\"cpuLoad\":\"1.8200\",\"memLoad\":\"145MB\"},{\"hostId\":\"13556\",\"hostName\":\"user-cash-job01.nh\",\"cpuLoad\":\"0.0200\",\"memLoad\":\"1329MB\"},{\"hostId\":\"13557\",\"hostName\":\"java-jobs04.nh\",\"cpuLoad\":\"0.0200\",\"memLoad\":\"855MB\"},{\"hostId\":\"13558\",\"hostName\":\"mobile-oss-custompush-job04.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"869MB\"},{\"hostId\":\"13559\",\"hostName\":\"mobile-oss-custompush-job03.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"280MB\"},{\"hostId\":\"13560\",\"hostName\":\"search-job02.nh\",\"cpuLoad\":\"1.2000\",\"memLoad\":\"787MB\"},{\"hostId\":\"13561\",\"hostName\":\"search-online-job01.nh\",\"cpuLoad\":\"0.0500\",\"memLoad\":\"4876MB\"},{\"hostId\":\"13562\",\"hostName\":\"ba-finance-job01.nh\",\"cpuLoad\":\"0.6600\",\"memLoad\":\"5255MB\"},{\"hostId\":\"13563\",\"hostName\":\"midas-logserver-worker01.nh\",\"cpuLoad\":\"0.0800\",\"memLoad\":\"240MB\"},{\"hostId\":\"13564\",\"hostName\":\"midas-merchant-worker02.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"534MB\"},{\"hostId\":\"13565\",\"hostName\":\"midas-logserver-worker02.nh\",\"cpuLoad\":\"0.1000\",\"memLoad\":\"403MB\"},{\"hostId\":\"13566\",\"hostName\":\"midas-sellkeyword-worker01.nh\",\"cpuLoad\":\"0.2000\",\"memLoad\":\"536MB\"},{\"hostId\":\"13567\",\"hostName\":\"midas-material-worker01.nh\",\"cpuLoad\":\"0.0200\",\"memLoad\":\"635MB\"},{\"hostId\":\"13568\",\"hostName\":\"ugc-job01.nh\",\"cpuLoad\":\"11.3200\",\"memLoad\":\"328MB\"},{\"hostId\":\"13569\",\"hostName\":\"mobile-oss-message-job01.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"1487MB\"},{\"hostId\":\"13570\",\"hostName\":\"poi-importance-service02.nh\",\"cpuLoad\":\"0.0600\",\"memLoad\":\"633MB\"},{\"hostId\":\"13571\",\"hostName\":\"poi-importance-service01.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"386MB\"},{\"hostId\":\"13572\",\"hostName\":\"search-indexer-pushuser01.nh\",\"cpuLoad\":\"0.0300\",\"memLoad\":\"8401MB\"},{\"hostId\":\"13573\",\"hostName\":\"aladdin-job01.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"105MB\"},{\"hostId\":\"13574\",\"hostName\":\"dppush-distribute-job02.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"167MB\"},{\"hostId\":\"13575\",\"hostName\":\"java-jobs03.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"849MB\"},{\"hostId\":\"13576\",\"hostName\":\"poi-data01.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"498MB\"},{\"hostId\":\"13577\",\"hostName\":\"mobile-oss-custompush-job02.nh\",\"cpuLoad\":\"0.1000\",\"memLoad\":\"1511MB\"},{\"hostId\":\"13578\",\"hostName\":\"dppush-distribute-job01.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"170MB\"},{\"hostId\":\"13579\",\"hostName\":\"mobile-oss-custompush-job01.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"1589MB\"},{\"hostId\":\"10820\",\"hostName\":\"tuangou-algo-task02.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"3699MB\"},{\"hostId\":\"13580\",\"hostName\":\"h161.hadoop\",\"cpuLoad\":null,\"memLoad\":\"异常数据\"},{\"hostId\":\"10189\",\"hostName\":\"hotel-log-job01.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"233MB\"},{\"hostId\":\"11234\",\"hostName\":\"to-member-job01.nh\",\"cpuLoad\":\"0.1500\",\"memLoad\":\"1164MB\"},{\"hostId\":\"10201\",\"hostName\":\"shop-hotel-job01.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"220MB\"},{\"hostId\":\"11606\",\"hostName\":\"to-event-job01.nh\",\"cpuLoad\":\"0.1100\",\"memLoad\":\"772MB\"},{\"hostId\":\"11768\",\"hostName\":\"takeaway-job01.nh\",\"cpuLoad\":\"0.1500\",\"memLoad\":\"794MB\"},{\"hostId\":\"11659\",\"hostName\":\"ts-settle-job01.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"588MB\"},{\"hostId\":\"10202\",\"hostName\":\"shop-hotel-job02.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"622MB\"},{\"hostId\":\"12953\",\"hostName\":\"tp-hotel-job01.nh\",\"cpuLoad\":\"0.0400\",\"memLoad\":\"204MB\"},{\"hostId\":\"13165\",\"hostName\":\"ts-monitor-job01.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"364MB\"},{\"hostId\":\"10161\",\"hostName\":\"wedding-job01.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"1960MB\"},{\"hostId\":\"11270\",\"hostName\":\"mobi-mcard-job01.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"8557MB\"}]";
            if(jsonString == null || jsonString.isEmpty()){
                result = true;
            }else {
                try {
                    JSONArray jsonArray = new JSONArray(jsonString);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jo = (JSONObject) jsonArray.get(i);
                        if (jo == null){
                            result = true;
                        }
                        String zabbixHostName = (String) jo.get("hostName");
                        if (zabbixHostName != null || zabbixHostName.isEmpty()){
                            if (zabbixHostName.equals(hostName)){
                                String cpuLoad = (String) jo.get("cpuLoad");
                                if (cpuLoad != null || cpuLoad.isEmpty()){
                                    Double highValue = Double.parseDouble(cpuLoad);
                                    if (highValue <= 1.0){
                                        result = false;
                                    }else {
                                        result = true;
                                    }

                                }else {
                                    result = true;
                                }

                                break;
                            }

                        }else {
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
            String fileSizeAttribute = "";                          //区别是log的文件偏移属性 还是error-log的文件偏移

            long lastTimeFileSize;                                  //文件偏移

            String queryType = request.getParameter("querytype");   //区分js请求是log，还是error log

            Date startTime = null;
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

                            long start = System.currentTimeMillis();
                            String context = getAgentRestService(url);//getLogCr.get().getText();
                            long end1 = System.currentTimeMillis();

                            System.out.println("#######"+queryType+"#####TIME1:"+(end1 - start) );
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
                            long end2 = System.currentTimeMillis();

                            System.out.println("#######"+queryType+"#####TIME2:"+(end2 - start) );

                            if (acceptContentWay && isEnd.equals("false")) {
                                request.getSession().setAttribute(fileSizeAttribute, ((Long) lastTimeFileSize).toString());
                            } else if (isEnd.trim().equals("true")) {
                                request.getSession().setAttribute(fileSizeAttribute, "0");
                            }

                            String retStr;
                                                                   //格式化日志 以便在web显示是换行的
                            OutputStream output = response.getOutputStream();


                            if (context == null) {                                     //时间间隔短，日志尚未生成可能获得null
                                retStr = " ";
                            } else {
                                retStr = context.replace("\n","<br>");
                            }

                            long end = System.currentTimeMillis();

                            System.out.println("#######"+queryType+"###Length: "+retStr.length()+"#####TIME:"+(end - start) );
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
        }else if(ISVIEWLOG.equals(action)){
            OutputStream output = response.getOutputStream();
            String isView = "";
            String ip = (String)request.getParameter("ip");
            boolean isviewlog = isHostOverLoad(ip);

            if (isviewlog){
                isView = "false";
            }else {
                isView = "true";
            }

            output.write(isView.getBytes());
            output.close();
        }

    }


}
