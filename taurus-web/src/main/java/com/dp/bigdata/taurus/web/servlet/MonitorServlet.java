package com.dp.bigdata.taurus.web.servlet;

import com.dianping.lion.EnvZooKeeperConfig;
import com.dianping.lion.client.ConfigCache;
import com.dianping.lion.client.LionException;
import com.dp.bigdata.taurus.generated.mapper.TaskAttemptMapper;
import com.dp.bigdata.taurus.generated.mapper.TaskMapper;
import com.dp.bigdata.taurus.restlet.resource.*;
import com.dp.bigdata.taurus.restlet.shared.HostDTO;
import com.dp.bigdata.taurus.web.utils.ReFlashHostLoadTask;
import com.dp.bigdata.taurus.web.utils.ReFlashHostLoadTaskTimer;
import com.dp.bigdata.taurus.web.utils.ZabbixUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.resource.ClientResource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by kirinli on 14-9-29.
 */
public class MonitorServlet extends HttpServlet {
    private String RESTLET_URL_BASE;
    private String AGENT_PORT;
    private static final String HOST = "host";
    private static final String TOTALTASKLOAD = "totaltaskload";
    private static final String GROUPTASK = "grouptask";
    private static final String FAILEDTASKLOAD = "failedtaskload";
    private static final String USERTASK = "usertask";
    private static final String HOSTLOAD = "hostload";
    private static final String SQLQUERY = "sqlquery";
    private static final String JOBDETAIL = "jobdetail";
    private static final int SERVICE_EXCEPTION = -1;
    private static final int TASKID_IS_NOT_FOUND = -2;
    private static final int STATUS_IS_NOT_RIGHT = -3;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ReFlashHostLoadTaskTimer.getReFlashHostLoadManager().start();
        ServletContext context = getServletContext();
        try {
            RESTLET_URL_BASE = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.web.restlet.url");// context.getInitParameter("RESTLET_SERVER");
            AGENT_PORT = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.agent.restlet.port");//context.getInitParameter("AGENT_SERVER_PORT");
        } catch (LionException e) {
            RESTLET_URL_BASE = context.getInitParameter("RESTLET_SERVER");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("MonitorServlet EERROR++++++++:" + e.getMessage());
        }

    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ClientResource cr = new ClientResource(RESTLET_URL_BASE + "host");
        IHostsResource hostsResource = cr.wrap(IHostsResource.class);
        cr.accept(MediaType.APPLICATION_XML);
        ArrayList<HostDTO> hosts = hostsResource.retrieve();
        String action = request.getParameter("action");
        if (HOST.equals(action)) {
            String type = request.getParameter("gettype");
            int onLineNums = 0;
            int offLineNums = 0;
            int exceptionNums = 0;
            String onLineHosts = "";
            String offLineHosts = "";
            String exceptionHosts = "";
            String hostInfo = "";
            OutputStream output = response.getOutputStream();

            for (HostDTO dto : hosts) {
                cr = new ClientResource(RESTLET_URL_BASE + "host/" + dto.getName());
                IHostResource hostResource = cr.wrap(IHostResource.class);
                cr.accept(MediaType.APPLICATION_XML);
                HostDTO dtos = hostResource.retrieve();

                if (!dto.isOnline()) {
                    offLineNums++;
                    offLineHosts += dto.getIp() + ",";
                } else if (dto.isConnected()) {
                    onLineNums++;
                    onLineHosts += dto.getIp() + ",";
                } else {
                    exceptionNums++;
                    exceptionHosts += dto.getIp() + ",";
                }

            }
            if ("online".equals(type)) {
                onLineHosts = onLineHosts.substring(0, onLineHosts.length() - 1);

                output.write(onLineHosts.getBytes());
                output.close();
            } else if ("offline".equals(type)) {
                offLineHosts = offLineHosts.substring(0, offLineHosts.length() - 1);

                output.write(offLineHosts.getBytes());
                output.close();
            } else if ("exception".equals(type)) {
                exceptionHosts = exceptionHosts.substring(0, exceptionHosts.length() - 1);

                output.write(exceptionHosts.getBytes());
                output.close();
            } else {
                if (onLineHosts.length() > 0) {
                    onLineHosts = onLineHosts.substring(0, onLineHosts.length() - 1);
                } else {
                    onLineHosts = "NULL";
                }

                if (exceptionHosts.length() > 0) {
                    exceptionHosts = exceptionHosts.substring(0, exceptionHosts.length() - 1);
                } else {
                    exceptionHosts = "NULL";
                }

                String allInfo = onLineHosts + "#" + exceptionHosts;
                output.write(allInfo.getBytes());
                output.close();

            }
        } else if (TOTALTASKLOAD.equals(action)) {
            OutputStream output = response.getOutputStream();
            String start = request.getParameter("start");
            String end = request.getParameter("end");
            String apiUrl = RESTLET_URL_BASE + "totaltaskload/" + start + "/" + end;
            ClientResource crToal = new ClientResource(apiUrl);
            ITotalTaskLoad totalTaskLoad = crToal.wrap(ITotalTaskLoad.class);

            String jsonString = totalTaskLoad.retrieve();
            output.write(jsonString.getBytes());
            output.close();

        } else if (FAILEDTASKLOAD.equals(action)) {
            OutputStream output = response.getOutputStream();
            String start = request.getParameter("start");
            String end = request.getParameter("end");
            String apiUrl = RESTLET_URL_BASE + "failedtaskload/" + start + "/" + end;
            ClientResource crFail = new ClientResource(apiUrl);
            IFailedTaskLoad failedTaskLoad = crFail.wrap(IFailedTaskLoad.class);

            String jsonString = failedTaskLoad.retrieve();
            output.write(jsonString.getBytes());
            output.close();

        } else if (HOSTLOAD.equals(action)) {
            OutputStream output = response.getOutputStream();

            String queryType = request.getParameter("queryType");

            String jsonString = ReFlashHostLoadTask.hostLoadJsonData;//"[{\"hostId\":\"13543\",\"hostName\":\"poidupshopdetect-service01.nh\",\"cpuLoad\":\"0.9400\",\"memLoad\":\"97MB\"},{\"hostId\":\"13544\",\"hostName\":\"java-jobs01.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"235MB\"},{\"hostId\":\"13545\",\"hostName\":\"poidupshopdetect-service02.nh\",\"cpuLoad\":\"0.1800\",\"memLoad\":\"125MB\"},{\"hostId\":\"13546\",\"hostName\":\"java-jobs02.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"1418MB\"},{\"hostId\":\"13547\",\"hostName\":\"mobi-ossweb01.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"1286MB\"},{\"hostId\":\"13548\",\"hostName\":\"poi-job01.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"311MB\"},{\"hostId\":\"13549\",\"hostName\":\"credit-hive01.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"6505MB\"},{\"hostId\":\"13550\",\"hostName\":\"search-hisreview02.nh\",\"cpuLoad\":\"0.9900\",\"memLoad\":\"9069MB\"},{\"hostId\":\"13551\",\"hostName\":\"search-qu-job01.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"25307MB\"},{\"hostId\":\"13552\",\"hostName\":\"mobile-oss-message-job02.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"1202MB\"},{\"hostId\":\"13553\",\"hostName\":\"midas-material-worker02.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"143MB\"},{\"hostId\":\"13554\",\"hostName\":\"dw2www-job01.nh\",\"cpuLoad\":\"0.0100\",\"memLoad\":\"1223MB\"},{\"hostId\":\"13555\",\"hostName\":\"midas-merchant-worker01.nh\",\"cpuLoad\":\"1.8200\",\"memLoad\":\"145MB\"},{\"hostId\":\"13556\",\"hostName\":\"user-cash-job01.nh\",\"cpuLoad\":\"0.0200\",\"memLoad\":\"1329MB\"},{\"hostId\":\"13557\",\"hostName\":\"java-jobs04.nh\",\"cpuLoad\":\"0.0200\",\"memLoad\":\"855MB\"},{\"hostId\":\"13558\",\"hostName\":\"mobile-oss-custompush-job04.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"869MB\"},{\"hostId\":\"13559\",\"hostName\":\"mobile-oss-custompush-job03.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"280MB\"},{\"hostId\":\"13560\",\"hostName\":\"search-job02.nh\",\"cpuLoad\":\"1.2000\",\"memLoad\":\"787MB\"},{\"hostId\":\"13561\",\"hostName\":\"search-online-job01.nh\",\"cpuLoad\":\"0.0500\",\"memLoad\":\"4876MB\"},{\"hostId\":\"13562\",\"hostName\":\"ba-finance-job01.nh\",\"cpuLoad\":\"0.6600\",\"memLoad\":\"5255MB\"},{\"hostId\":\"13563\",\"hostName\":\"midas-logserver-worker01.nh\",\"cpuLoad\":\"0.0800\",\"memLoad\":\"240MB\"},{\"hostId\":\"13564\",\"hostName\":\"midas-merchant-worker02.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"534MB\"},{\"hostId\":\"13565\",\"hostName\":\"midas-logserver-worker02.nh\",\"cpuLoad\":\"0.1000\",\"memLoad\":\"403MB\"},{\"hostId\":\"13566\",\"hostName\":\"midas-sellkeyword-worker01.nh\",\"cpuLoad\":\"0.2000\",\"memLoad\":\"536MB\"},{\"hostId\":\"13567\",\"hostName\":\"midas-material-worker01.nh\",\"cpuLoad\":\"0.0200\",\"memLoad\":\"635MB\"},{\"hostId\":\"13568\",\"hostName\":\"ugc-job01.nh\",\"cpuLoad\":\"11.3200\",\"memLoad\":\"328MB\"},{\"hostId\":\"13569\",\"hostName\":\"mobile-oss-message-job01.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"1487MB\"},{\"hostId\":\"13570\",\"hostName\":\"poi-importance-service02.nh\",\"cpuLoad\":\"0.0600\",\"memLoad\":\"633MB\"},{\"hostId\":\"13571\",\"hostName\":\"poi-importance-service01.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"386MB\"},{\"hostId\":\"13572\",\"hostName\":\"search-indexer-pushuser01.nh\",\"cpuLoad\":\"0.0300\",\"memLoad\":\"8401MB\"},{\"hostId\":\"13573\",\"hostName\":\"aladdin-job01.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"105MB\"},{\"hostId\":\"13574\",\"hostName\":\"dppush-distribute-job02.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"167MB\"},{\"hostId\":\"13575\",\"hostName\":\"java-jobs03.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"849MB\"},{\"hostId\":\"13576\",\"hostName\":\"poi-data01.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"498MB\"},{\"hostId\":\"13577\",\"hostName\":\"mobile-oss-custompush-job02.nh\",\"cpuLoad\":\"0.1000\",\"memLoad\":\"1511MB\"},{\"hostId\":\"13578\",\"hostName\":\"dppush-distribute-job01.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"170MB\"},{\"hostId\":\"13579\",\"hostName\":\"mobile-oss-custompush-job01.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"1589MB\"},{\"hostId\":\"10820\",\"hostName\":\"tuangou-algo-task02.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"3699MB\"},{\"hostId\":\"13580\",\"hostName\":\"h161.hadoop\",\"cpuLoad\":null,\"memLoad\":\"异常数据\"},{\"hostId\":\"10189\",\"hostName\":\"hotel-log-job01.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"233MB\"},{\"hostId\":\"11234\",\"hostName\":\"to-member-job01.nh\",\"cpuLoad\":\"0.1500\",\"memLoad\":\"1164MB\"},{\"hostId\":\"10201\",\"hostName\":\"shop-hotel-job01.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"220MB\"},{\"hostId\":\"11606\",\"hostName\":\"to-event-job01.nh\",\"cpuLoad\":\"0.1100\",\"memLoad\":\"772MB\"},{\"hostId\":\"11768\",\"hostName\":\"takeaway-job01.nh\",\"cpuLoad\":\"0.1500\",\"memLoad\":\"794MB\"},{\"hostId\":\"11659\",\"hostName\":\"ts-settle-job01.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"588MB\"},{\"hostId\":\"10202\",\"hostName\":\"shop-hotel-job02.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"622MB\"},{\"hostId\":\"12953\",\"hostName\":\"tp-hotel-job01.nh\",\"cpuLoad\":\"0.0400\",\"memLoad\":\"204MB\"},{\"hostId\":\"13165\",\"hostName\":\"ts-monitor-job01.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"364MB\"},{\"hostId\":\"10161\",\"hostName\":\"wedding-job01.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"1960MB\"},{\"hostId\":\"11270\",\"hostName\":\"mobi-mcard-job01.nh\",\"cpuLoad\":\"0.0000\",\"memLoad\":\"8557MB\"}]";

            if (queryType != null && "reflash".equals(queryType)) {
                jsonString = ReFlashHostLoadTask.read();
            }

            if (jsonString == null || jsonString == "") {
                jsonString = ReFlashHostLoadTask.read();
            }

            output.write(jsonString.getBytes());
            output.close();
        } else if (USERTASK.equals(action)) {
            OutputStream output = response.getOutputStream();
            String username = request.getParameter("username");
            String start = request.getParameter("start");
            String end = request.getParameter("end");

            cr = new ClientResource(RESTLET_URL_BASE + "usertasks/" + username + "/" + start + "/" + end);
            IUserTasks userTasks = cr.wrap(IUserTasks.class);
            cr.accept(MediaType.APPLICATION_XML);
            String jsonString = userTasks.retrieve();
            output.write(jsonString.getBytes());
            output.close();

        } else if (GROUPTASK.equals(action)) {
            OutputStream output = response.getOutputStream();
            String username = request.getParameter("username");
            String start = request.getParameter("start");
            String end = request.getParameter("end");

            cr = new ClientResource(RESTLET_URL_BASE + "grouptasks/" + username + "/" + start + "/" + end);
            IUserTasks userTasks = cr.wrap(IUserTasks.class);
            cr.accept(MediaType.APPLICATION_XML);
            String jsonString = userTasks.retrieve();
            output.write(jsonString.getBytes());
            output.close();
        } else if (SQLQUERY.equals(action)) {
            String user = (String) request.getSession().getAttribute("taurus-user");
            String adminUser;
            try {
                adminUser= ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.dbadmin.user");
            } catch (LionException e) {
                adminUser = "kirin.li";
            }
            OutputStream output = response.getOutputStream();
            String reusult_str = "";

            if (adminUser.contains(user)) {
                String taskId = request.getParameter("taskId");
                String status = request.getParameter("status");

                cr = new ClientResource(RESTLET_URL_BASE + "deletedependency/" + taskId + "/"+status);
                IClearDependencyPassTask clearTasks = cr.wrap(IClearDependencyPassTask.class);
                cr.accept(MediaType.APPLICATION_XML);
                int result = clearTasks.retrieve();

                switch (result) {
                    case SERVICE_EXCEPTION:
                        reusult_str = "后台服务异常!";
                        break;
                    case TASKID_IS_NOT_FOUND:
                        reusult_str = "taskId 不存在!";
                        break;
                    case STATUS_IS_NOT_RIGHT:
                        reusult_str = "status 错误!";
                        break;
                    default:
                        reusult_str = "执行成功~";
                        break;

                }
            }else {
                reusult_str = "无权限执行操作!";
            }


            output.write(reusult_str.getBytes());
            output.close();
        }else if (JOBDETAIL.equals(action)){
            OutputStream output = response.getOutputStream();
            String start = request.getParameter("start");
            String end = request.getParameter("end");

            cr = new ClientResource(RESTLET_URL_BASE + "jobdetail/" + "/" + start + "/" + end);
            IUserTasks userTasks = cr.wrap(IUserTasks.class);
            cr.accept(MediaType.APPLICATION_XML);
            String jsonString = userTasks.retrieve();
            output.write(jsonString.getBytes());
            output.close();
        }
    }


}
