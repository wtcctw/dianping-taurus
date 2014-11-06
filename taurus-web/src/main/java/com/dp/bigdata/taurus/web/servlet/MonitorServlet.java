package com.dp.bigdata.taurus.web.servlet;

import com.dianping.lion.EnvZooKeeperConfig;
import com.dianping.lion.client.ConfigCache;
import com.dianping.lion.client.LionException;
import com.dp.bigdata.taurus.generated.mapper.TaskAttemptMapper;
import com.dp.bigdata.taurus.generated.mapper.TaskMapper;
import com.dp.bigdata.taurus.generated.module.Task;
import com.dp.bigdata.taurus.restlet.resource.*;
import com.dp.bigdata.taurus.restlet.shared.AttemptDTO;
import com.dp.bigdata.taurus.restlet.shared.HostDTO;
import com.dp.bigdata.taurus.restlet.shared.TaskDTO;
import com.dp.bigdata.taurus.web.utils.ReFlashHostLoadTask;
import com.dp.bigdata.taurus.web.utils.ReFlashHostLoadTaskTimer;
import com.dp.bigdata.taurus.web.utils.ZabbixUtil;
import com.dp.bigdata.taurus.zookeeper.common.infochannel.ZooKeeperCleaner;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.resource.ClientResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by kirinli on 14-9-29.
 */
public class MonitorServlet extends HttpServlet {
    private String RESTLET_URL_BASE;
    private String AGENT_PORT;
    private static final String HOST = "host";
    private static final String TOTAL_TASK_LOAD = "totaltaskload";
    private static final String GROUP_TASK = "grouptask";
    private static final String FAILED_TASK_LOAD = "failedtaskload";
    private static final String USER_TASK = "usertask";
    private static final String HOST_LOAD = "hostload";
    private static final String SQL_QUERY = "sqlquery";
    private static final String JOB_DETAIL = "jobdetail";
    private static final String CLEAR_ZOOKEEPER_NODES = "clearzknodes";
    private static final String RUNNING_TASKS = "runningtasks";
    private static final String FAILED_TASKS = "failedtasks";
    private static final String SUBMIT_FAIL_TASK = "submitfail";
    private static final String DEPENDENCY_PASS_TASK = "dependencypass";
    private static final String DEPENDENCY_TIMEOUT_TASK = "dependencytimeout";
    private static final String TIMEOUT_TASK = "timeout";
    private static final String SCHEDULE = "schedule";
    private static final String ATTEMPT = "attempt";


    private static final int SERVICE_EXCEPTION = -1;
    private static final int TASKID_IS_NOT_FOUND = -2;
    private static final int STATUS_IS_NOT_RIGHT = -3;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                config.getServletContext());
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
        } else if (TOTAL_TASK_LOAD.equals(action)) {
            OutputStream output = response.getOutputStream();
            String start = request.getParameter("start");
            String end = request.getParameter("end");
            String apiUrl = RESTLET_URL_BASE + "totaltaskload/" + start + "/" + end;
            ClientResource crToal = new ClientResource(apiUrl);
            ITotalTaskLoad totalTaskLoad = crToal.wrap(ITotalTaskLoad.class);

            String jsonString = totalTaskLoad.retrieve();
            output.write(jsonString.getBytes());
            output.close();

        } else if (FAILED_TASK_LOAD.equals(action)) {
            OutputStream output = response.getOutputStream();
            String start = request.getParameter("start");
            String end = request.getParameter("end");
            String apiUrl = RESTLET_URL_BASE + "failedtaskload/" + start + "/" + end;
            ClientResource crFail = new ClientResource(apiUrl);
            IFailedTaskLoad failedTaskLoad = crFail.wrap(IFailedTaskLoad.class);

            String jsonString = failedTaskLoad.retrieve();
            output.write(jsonString.getBytes());
            output.close();

        } else if (HOST_LOAD.equals(action)) {
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
        } else if (USER_TASK.equals(action)) {
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

        } else if (GROUP_TASK.equals(action)) {
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
        } else if (SQL_QUERY.equals(action)) {
            String user = (String) request.getSession().getAttribute("taurus-user");
            String adminUser;
            try {
                adminUser = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.dbadmin.user");
            } catch (LionException e) {
                adminUser = "kirin.li";
            }
            OutputStream output = response.getOutputStream();
            String reusult_str = "";

            if (adminUser.contains(user)) {
                String taskId = request.getParameter("taskId");
                String status = request.getParameter("status");

                cr = new ClientResource(RESTLET_URL_BASE + "deletedependency/" + taskId + "/" + status);
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
            } else {
                reusult_str = "无权限执行操作!";
            }


            output.write(reusult_str.getBytes());
            output.close();
        } else if (JOB_DETAIL.equals(action)) {
            OutputStream output = response.getOutputStream();
            String start = request.getParameter("start");
            String end = request.getParameter("end");

            cr = new ClientResource(RESTLET_URL_BASE + "jobdetail/" + "/" + start + "/" + end);
            IUserTasks userTasks = cr.wrap(IUserTasks.class);
            cr.accept(MediaType.APPLICATION_XML);
            String jsonString = userTasks.retrieve();
            output.write(jsonString.getBytes());
            output.close();
        } else if (CLEAR_ZOOKEEPER_NODES.equals(action)) {
            OutputStream output = response.getOutputStream();
            String start_str = request.getParameter("start");
            String end_str = request.getParameter("end");

            try {
                int start = Integer.parseInt(start_str);
                int end = Integer.parseInt(end_str);

                ZooKeeperCleaner.clearNodes(start, end);
                output.write("success".getBytes());
                output.close();
            } catch (Exception e) {
                output.write("failed".getBytes());
                output.close();
            }


        } else if (RUNNING_TASKS.equals(action)) {
            OutputStream output = response.getOutputStream();
            String hourTimeStr = request.getParameter("hourTime");
            long hourTime = 60 * 60 * 1000;
            if (hourTimeStr != null && hourTimeStr.isEmpty()) {
                hourTime = Long.parseLong(hourTimeStr);

            }

            ClientResource crTask = new ClientResource(RESTLET_URL_BASE + "gettasks");
            IGetTasks taskResource = crTask.wrap(IGetTasks.class);
            ArrayList<Task> tasks = taskResource.retrieve();


            String id = request.getParameter("id");
            String taskTime = "";

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String url = RESTLET_URL_BASE + "getattemptsbystatus/";

            if (id == null) {

                taskTime = formatter.format(new Date(new Date().getTime() - 24 * hourTime));

            } else if (id.equals("1")) {
                Integer count = (Integer) request.getSession().getAttribute("count");
                if (count == null)
                    count = 0;
                Integer changeCount = count + 1;
                request.getSession().setAttribute("count", changeCount);

                taskTime = formatter.format(new Date(new Date().getTime() - (count + 1) * hourTime));
            } else if (id.equals("24")) {
                taskTime = formatter.format(new Date(new Date().getTime() - 24 * hourTime));
                request.getSession().setAttribute("count", 0);
            }


            cr = new ClientResource(url + taskTime);
            IGetAttemptsByStatus resource = cr.wrap(IGetAttemptsByStatus.class);
            ArrayList<AttemptDTO> attempts = resource.retrieve();

            String result = "";
            if (attempts != null)
                for (AttemptDTO dto : attempts) {
                    String state = dto.getStatus();
                    if (state.equals("RUNNING")) {
                        String taskName = "";
                        for (Task task : tasks) {
                            if (task.getTaskid().equals(dto.getTaskID())) {
                                taskName = task.getName();
                                break;
                            }
                        }

                        result += " <tr id = " + dto.getAttemptID() + " >"
                                + "<td >"
                                + dto.getTaskID()
                                + "</td >"
                                + "<td >"
                                + taskName
                                + "</td >";


                        if (dto.getStartTime() != null) {
                            result += "<td >"
                                    + formatter.format(dto.getStartTime())
                                    + "</td >";

                        } else {
                            result += "<td >"
                                    + "NULL"
                                    + "</td >";
                        }

                        if (dto.getEndTime() != null) {
                            result += "<td >"
                                    + formatter.format(dto.getEndTime())
                                    + "</td >";
                        } else {
                            result += "<td >"
                                    + "NULL"
                                    + "</td >";
                        }
                        if (dto.getScheduleTime() != null) {
                            result += "<td >"
                                    + formatter.format(dto.getScheduleTime())
                                    + "</td >";
                        } else {
                            result += "<td >"
                                    + "NULL"
                                    + "</td >";
                        }
                        if (dto.getExecHost() != null) {
                            result += "<td >"
                                    + dto.getExecHost()
                                    + "</td >";
                        } else {
                            result += "<td >"
                                    + "NULL"
                                    + "</td >";
                        }
                        boolean isViewLog = AttemptProxyServlet.isHostOverLoad(dto.getExecHost());
                        if (!isViewLog) {

                            result += "<td >"
                                    + " <a target = \"_blank\"\n" +
                                    "                            href = \"viewlog.jsp?id=<%=dto.getAttemptID()%>&status=<%=dto.getStatus()%>\" > 日志 </a >"
                                    + "</td >";


                        } else {
                            result += "<td >"
                                    + "Job机负载过高，无法查看实时日志"
                                    + "</td >";

                        }
                        result += "</tr>";
                    }
                }
            output.write(result.getBytes());
            output.close();

        } else if (FAILED_TASKS.equals(action)) {

            OutputStream output = response.getOutputStream();
            String hourTimeStr = request.getParameter("hourTime");
            long hourTime = 60 * 60 * 1000;
            if (hourTimeStr != null && hourTimeStr.isEmpty()) {
                hourTime = Long.parseLong(hourTimeStr);

            }

            ArrayList<Task> tasks = ReFlashHostLoadTask.getTasks();
            if (tasks == null) {
                ClientResource crTask = new ClientResource(RESTLET_URL_BASE + "gettasks");
                IGetTasks taskResource = crTask.wrap(IGetTasks.class);
                tasks = taskResource.retrieve();
                ReFlashHostLoadTask.allTasks = tasks;
                ReFlashHostLoadTask.lastReadDataTime = new Date().getTime();
            }


            String id = request.getParameter("id");
            String taskTime = "";

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String url = RESTLET_URL_BASE + "getattemptsbystatus/";

            if (id == null) {

                taskTime = formatter.format(new Date(new Date().getTime() - 24 * hourTime));

            } else if (id.equals("1")) {
                Integer count = (Integer) request.getSession().getAttribute("count");
                if (count == null)
                    count = 0;
                Integer changeCount = count + 1;
                request.getSession().setAttribute("count", changeCount);

                taskTime = formatter.format(new Date(new Date().getTime() - (count + 1) * hourTime));
            } else if (id.equals("24")) {
                taskTime = formatter.format(new Date(new Date().getTime() - 24 * hourTime));
                request.getSession().setAttribute("count", 0);
            } else {
                taskTime = formatter.format(new Date(new Date().getTime() - 24 * hourTime));
            }


            cr = new ClientResource(url + taskTime);
            IGetAttemptsByStatus resource = cr.wrap(IGetAttemptsByStatus.class);
            ArrayList<AttemptDTO> attempts = resource.retrieve();

            String result = "";
            if (attempts != null)
                for (AttemptDTO dto : attempts) {
                    String state = dto.getStatus();


                    if (state.equals("FAILED")) {
                        String taskName = "";
                        for (Task task : tasks) {
                            if (task.getTaskid().equals(dto.getTaskID())) {
                                taskName = task.getName();
                                break;
                            }
                        }
                        result += " <tr id = " + dto.getAttemptID() + " >"
                                + "<td >"
                                + dto.getTaskID()
                                + "</td >"
                                + "<td >"
                                + taskName
                                + "</td >";


                        if (dto.getStartTime() != null) {
                            result += "<td >"
                                    + formatter.format(dto.getStartTime())
                                    + "</td >";

                        } else {
                            result += "<td >"
                                    + "NULL"
                                    + "</td >";
                        }

                        if (dto.getEndTime() != null) {
                            result += "<td >"
                                    + formatter.format(dto.getEndTime())
                                    + "</td >";
                        } else {
                            result += "<td >"
                                    + "NULL"
                                    + "</td >";
                        }
                        if (dto.getScheduleTime() != null) {
                            result += "<td >"
                                    + formatter.format(dto.getScheduleTime())
                                    + "</td >";
                        } else {
                            result += "<td >"
                                    + "NULL"
                                    + "</td >";
                        }
                        if (dto.getExecHost() != null) {
                            result += "<td >"
                                    + dto.getExecHost()
                                    + "</td >";
                        } else {
                            result += "<td >"
                                    + "NULL"
                                    + "</td >";
                        }

                        result += "<td >"
                                + "  <a target=\"_blank\" href=\"viewlog.jsp?id=<%=dto.getAttemptID()%>&status=<%=dto.getStatus()%>\">日志</a>"
                                + "</td >";


                        result += "</tr>";
                    }
                }
            output.write(result.getBytes());
            output.close();

        } else if (SUBMIT_FAIL_TASK.equals(action)) {
            OutputStream output = response.getOutputStream();
            String hourTimeStr = request.getParameter("hourTime");
            long hourTime = 60 * 60 * 1000;
            if (hourTimeStr != null && hourTimeStr.isEmpty()) {
                hourTime = Long.parseLong(hourTimeStr);

            }

            ArrayList<Task> tasks = ReFlashHostLoadTask.getTasks();
            if (tasks == null) {
                ClientResource crTask = new ClientResource(RESTLET_URL_BASE + "gettasks");
                IGetTasks taskResource = crTask.wrap(IGetTasks.class);
                tasks = taskResource.retrieve();
                ReFlashHostLoadTask.allTasks = tasks;
                ReFlashHostLoadTask.lastReadDataTime = new Date().getTime();
            }


            String id = request.getParameter("id");
            String taskTime = "";

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String url = RESTLET_URL_BASE + "getattemptsbystatus/";

            if (id == null) {

                taskTime = formatter.format(new Date(new Date().getTime() - 24 * hourTime));

            } else if (id.equals("1")) {
                Integer count = (Integer) request.getSession().getAttribute("count");
                if (count == null)
                    count = 0;
                Integer changeCount = count + 1;
                request.getSession().setAttribute("count", changeCount);

                taskTime = formatter.format(new Date(new Date().getTime() - (count + 1) * hourTime));
            } else if (id.equals("24")) {
                taskTime = formatter.format(new Date(new Date().getTime() - 24 * hourTime));
                request.getSession().setAttribute("count", 0);
            } else {
                taskTime = formatter.format(new Date(new Date().getTime() - 24 * hourTime));
            }


            cr = new ClientResource(url + taskTime);
            IGetAttemptsByStatus resource = cr.wrap(IGetAttemptsByStatus.class);
            ArrayList<AttemptDTO> attempts = resource.retrieve();

            String result = "";
            if (attempts != null)
                for (AttemptDTO dto : attempts) {
                    String state = dto.getStatus();


                    if (state.equals("SUBMIT_FAIL")) {
                        String taskName = "";
                        for (Task task : tasks) {
                            if (task.getTaskid().equals(dto.getTaskID())) {
                                taskName = task.getName();
                                break;
                            }
                        }
                        result += " <tr id = " + dto.getAttemptID() + " >"
                                + "<td >"
                                + dto.getTaskID()
                                + "</td >"
                                + "<td >"
                                + taskName
                                + "</td >";


                        if (dto.getStartTime() != null) {
                            result += "<td >"
                                    + formatter.format(dto.getStartTime())
                                    + "</td >";

                        } else {
                            result += "<td >"
                                    + "NULL"
                                    + "</td >";
                        }

                        if (dto.getEndTime() != null) {
                            result += "<td >"
                                    + formatter.format(dto.getEndTime())
                                    + "</td >";
                        } else {
                            result += "<td >"
                                    + "NULL"
                                    + "</td >";
                        }
                        if (dto.getScheduleTime() != null) {
                            result += "<td >"
                                    + formatter.format(dto.getScheduleTime())
                                    + "</td >";
                        } else {
                            result += "<td >"
                                    + "NULL"
                                    + "</td >";
                        }
                        if (dto.getExecHost() != null) {
                            result += "<td >"
                                    + dto.getExecHost()
                                    + "</td >";
                        } else {
                            result += "<td >"
                                    + "NULL"
                                    + "</td >";
                        }

                        result += "</tr>";
                    }
                }
            output.write(result.getBytes());
            output.close();


        } else if (DEPENDENCY_PASS_TASK.equals(action)) {

            OutputStream output = response.getOutputStream();
            String hourTimeStr = request.getParameter("hourTime");
            long hourTime = 60 * 60 * 1000;
            if (hourTimeStr != null && hourTimeStr.isEmpty()) {
                hourTime = Long.parseLong(hourTimeStr);

            }

            ArrayList<Task> tasks = ReFlashHostLoadTask.getTasks();
            if (tasks == null) {
                ClientResource crTask = new ClientResource(RESTLET_URL_BASE + "gettasks");
                IGetTasks taskResource = crTask.wrap(IGetTasks.class);
                tasks = taskResource.retrieve();
                ReFlashHostLoadTask.allTasks = tasks;
                ReFlashHostLoadTask.lastReadDataTime = new Date().getTime();
            }


            String id = request.getParameter("id");
            String taskTime = "";

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String url = RESTLET_URL_BASE + "getattemptsbystatus/";

            if (id == null) {

                taskTime = formatter.format(new Date(new Date().getTime() - 24 * hourTime));

            } else if (id.equals("1")) {
                Integer count = (Integer) request.getSession().getAttribute("count");
                if (count == null)
                    count = 0;
                Integer changeCount = count + 1;
                request.getSession().setAttribute("count", changeCount);

                taskTime = formatter.format(new Date(new Date().getTime() - (count + 1) * hourTime));
            } else if (id.equals("24")) {
                taskTime = formatter.format(new Date(new Date().getTime() - 24 * hourTime));
                request.getSession().setAttribute("count", 0);
            } else {
                taskTime = formatter.format(new Date(new Date().getTime() - 24 * hourTime));
            }


            cr = new ClientResource(url + taskTime);
            IGetAttemptsByStatus resource = cr.wrap(IGetAttemptsByStatus.class);
            ArrayList<AttemptDTO> attempts = resource.retrieve();

            String result = "";
            if (attempts != null)
                for (AttemptDTO dto : attempts) {
                    String state = dto.getStatus();


                    if (state.equals("SUBMIT_FAIL")) {
                        String taskName = "";
                        for (Task task : tasks) {
                            if (task.getTaskid().equals(dto.getTaskID())) {
                                taskName = task.getName();
                                break;
                            }
                        }
                        result += " <tr id = " + dto.getAttemptID() + " >"
                                + "<td >"
                                + dto.getTaskID()
                                + "</td >"
                                + "<td >"
                                + taskName
                                + "</td >";


                        if (dto.getStartTime() != null) {
                            result += "<td >"
                                    + formatter.format(dto.getStartTime())
                                    + "</td >";

                        } else {
                            result += "<td >"
                                    + "NULL"
                                    + "</td >";
                        }

                        if (dto.getEndTime() != null) {
                            result += "<td >"
                                    + formatter.format(dto.getEndTime())
                                    + "</td >";
                        } else {
                            result += "<td >"
                                    + "NULL"
                                    + "</td >";
                        }
                        if (dto.getScheduleTime() != null) {
                            result += "<td >"
                                    + formatter.format(dto.getScheduleTime())
                                    + "</td >";
                        } else {
                            result += "<td >"
                                    + "NULL"
                                    + "</td >";
                        }
                        if (dto.getExecHost() != null) {
                            result += "<td >"
                                    + dto.getExecHost()
                                    + "</td >";
                        } else {
                            result += "<td >"
                                    + "NULL"
                                    + "</td >";
                        }

                        result += "</tr>";
                    }
                }
            output.write(result.getBytes());
            output.close();

        } else if (DEPENDENCY_TIMEOUT_TASK.equals(action)) {

            OutputStream output = response.getOutputStream();
            String hourTimeStr = request.getParameter("hourTime");
            long hourTime = 60 * 60 * 1000;
            if (hourTimeStr != null && hourTimeStr.isEmpty()) {
                hourTime = Long.parseLong(hourTimeStr);

            }

            ArrayList<Task> tasks = ReFlashHostLoadTask.getTasks();
            if (tasks == null) {
                ClientResource crTask = new ClientResource(RESTLET_URL_BASE + "gettasks");
                IGetTasks taskResource = crTask.wrap(IGetTasks.class);
                tasks = taskResource.retrieve();
                ReFlashHostLoadTask.allTasks = tasks;
                ReFlashHostLoadTask.lastReadDataTime = new Date().getTime();
            }


            String id = request.getParameter("id");
            String taskTime = "";

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String url = RESTLET_URL_BASE + "getattemptsbystatus/";

            if (id == null) {

                taskTime = formatter.format(new Date(new Date().getTime() - 24 * hourTime));

            } else if (id.equals("1")) {
                Integer count = (Integer) request.getSession().getAttribute("count");
                if (count == null)
                    count = 0;
                Integer changeCount = count + 1;
                request.getSession().setAttribute("count", changeCount);

                taskTime = formatter.format(new Date(new Date().getTime() - (count + 1) * hourTime));
            } else if (id.equals("24")) {
                taskTime = formatter.format(new Date(new Date().getTime() - 24 * hourTime));
                request.getSession().setAttribute("count", 0);
            } else {
                taskTime = formatter.format(new Date(new Date().getTime() - 24 * hourTime));
            }


            cr = new ClientResource(url + taskTime);
            IGetAttemptsByStatus resource = cr.wrap(IGetAttemptsByStatus.class);
            ArrayList<AttemptDTO> attempts = resource.retrieve();

            String result = "";
            if (attempts != null)
                for (AttemptDTO dto : attempts) {
                    String state = dto.getStatus();


                    if (state.equals("DEPENDENCY_TIMEOUT")) {
                        String taskName = "";
                        for (Task task : tasks) {
                            if (task.getTaskid().equals(dto.getTaskID())) {
                                taskName = task.getName();
                                break;
                            }
                        }
                        result += " <tr id = " + dto.getAttemptID() + " >"
                                + "<td >"
                                + dto.getTaskID()
                                + "</td >"
                                + "<td >"
                                + taskName
                                + "</td >";


                        if (dto.getStartTime() != null) {
                            result += "<td >"
                                    + formatter.format(dto.getStartTime())
                                    + "</td >";

                        } else {
                            result += "<td >"
                                    + "NULL"
                                    + "</td >";
                        }

                        if (dto.getEndTime() != null) {
                            result += "<td >"
                                    + formatter.format(dto.getEndTime())
                                    + "</td >";
                        } else {
                            result += "<td >"
                                    + "NULL"
                                    + "</td >";
                        }
                        if (dto.getScheduleTime() != null) {
                            result += "<td >"
                                    + formatter.format(dto.getScheduleTime())
                                    + "</td >";
                        } else {
                            result += "<td >"
                                    + "NULL"
                                    + "</td >";
                        }
                        if (dto.getExecHost() != null) {
                            result += "<td >"
                                    + dto.getExecHost()
                                    + "</td >";
                        } else {
                            result += "<td >"
                                    + "NULL"
                                    + "</td >";
                        }

                        result += "</tr>";
                    }
                }
            output.write(result.getBytes());
            output.close();

        } else if (TIMEOUT_TASK.equals(action)) {
            OutputStream output = response.getOutputStream();
            String hourTimeStr = request.getParameter("hourTime");
            long hourTime = 60 * 60 * 1000;
            if (hourTimeStr != null && hourTimeStr.isEmpty()) {
                hourTime = Long.parseLong(hourTimeStr);

            }

            ArrayList<Task> tasks = ReFlashHostLoadTask.getTasks();
            if (tasks == null) {
                ClientResource crTask = new ClientResource(RESTLET_URL_BASE + "gettasks");
                IGetTasks taskResource = crTask.wrap(IGetTasks.class);
                tasks = taskResource.retrieve();
                ReFlashHostLoadTask.allTasks = tasks;
                ReFlashHostLoadTask.lastReadDataTime = new Date().getTime();
            }


            String id = request.getParameter("id");
            String taskTime = "";

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String url = RESTLET_URL_BASE + "getattemptsbystatus/";

            if (id == null) {

                taskTime = formatter.format(new Date(new Date().getTime() - 24 * hourTime));

            } else if (id.equals("1")) {
                Integer count = (Integer) request.getSession().getAttribute("count");
                if (count == null)
                    count = 0;
                Integer changeCount = count + 1;
                request.getSession().setAttribute("count", changeCount);

                taskTime = formatter.format(new Date(new Date().getTime() - (count + 1) * hourTime));
            } else if (id.equals("24")) {
                taskTime = formatter.format(new Date(new Date().getTime() - 24 * hourTime));
                request.getSession().setAttribute("count", 0);
            } else {
                taskTime = formatter.format(new Date(new Date().getTime() - 24 * hourTime));
            }


            cr = new ClientResource(url + taskTime);
            IGetAttemptsByStatus resource = cr.wrap(IGetAttemptsByStatus.class);
            ArrayList<AttemptDTO> attempts = resource.retrieve();

            String result = "";
            if (attempts != null)
                for (AttemptDTO dto : attempts) {
                    String state = dto.getStatus();


                    if (state.equals("TIMEOUT")) {
                        String taskName = "";
                        for (Task task : tasks) {
                            if (task.getTaskid().equals(dto.getTaskID())) {
                                taskName = task.getName();
                                break;
                            }
                        }
                        result += " <tr id = " + dto.getAttemptID() + " >"
                                + "<td >"
                                + dto.getTaskID()
                                + "</td >"
                                + "<td >"
                                + taskName
                                + "</td >";


                        if (dto.getStartTime() != null) {
                            result += "<td >"
                                    + formatter.format(dto.getStartTime())
                                    + "</td >";

                        } else {
                            result += "<td >"
                                    + "NULL"
                                    + "</td >";
                        }

                        if (dto.getEndTime() != null) {
                            result += "<td >"
                                    + formatter.format(dto.getEndTime())
                                    + "</td >";
                        } else {
                            result += "<td >"
                                    + "NULL"
                                    + "</td >";
                        }
                        if (dto.getScheduleTime() != null) {
                            result += "<td >"
                                    + formatter.format(dto.getScheduleTime())
                                    + "</td >";
                        } else {
                            result += "<td >"
                                    + "NULL"
                                    + "</td >";
                        }
                        if (dto.getExecHost() != null) {
                            result += "<td >"
                                    + dto.getExecHost()
                                    + "</td >";
                        } else {
                            result += "<td >"
                                    + "NULL"
                                    + "</td >";
                        }

                        result += "</tr>";
                    }
                }
            output.write(result.getBytes());
            output.close();
        } else if (SCHEDULE.equals(action)) {
            OutputStream output = response.getOutputStream();
            String task_api = RESTLET_URL_BASE + "task";
            String name = request.getParameter("name");
            String path = request.getParameter("path");
            String appname = request.getParameter("appname");
            String currentUser = request.getParameter("currentUser");
            JsonArray jsonArray = new JsonArray();
            if (currentUser != null) {
                task_api = task_api + "?user=" + currentUser;
            }

            if (name != null && !name.isEmpty() && !name.equals("null")) {
                task_api = task_api + "&name=" + name;
            }
            if (appname != null && !appname.equals("null")) {
                task_api = task_api + "&appname=" + appname;
            }
            cr = new ClientResource(task_api);
            ITasksResource resource = cr.wrap(ITasksResource.class);
            cr.accept(MediaType.APPLICATION_XML);
            ArrayList<TaskDTO> tasks = resource.retrieve();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            for (TaskDTO dto : tasks) {
                JsonObject jsonObject = new JsonObject();
                String state = dto.getStatus();
                jsonObject.addProperty("state",state);

                jsonObject.addProperty("taskId", dto.getTaskid());
                jsonObject.addProperty("taskName",dto.getName());
                jsonObject.addProperty("hostName", dto.getHostname());
                jsonObject.addProperty("creator",dto.getCreator());
                jsonObject.addProperty("proxyUser",dto.getProxyuser());
                jsonObject.addProperty("addTime",formatter.format(dto.getAddtime()));
                jsonObject.addProperty("crontab",dto.getCrontab());
                jsonArray.add(jsonObject);
            }

            output.write(jsonArray.toString().getBytes());
            output.close();
        }else if (ATTEMPT.equals(action)){
            OutputStream output = response.getOutputStream();
            ArrayList<Task> tasks = ReFlashHostLoadTask.getTasks();
            if (tasks == null) {
                ClientResource crTask = new ClientResource(RESTLET_URL_BASE + "gettasks");
                IGetTasks taskResource = crTask.wrap(IGetTasks.class);
                tasks = taskResource.retrieve();
                ReFlashHostLoadTask.allTasks = tasks;
                ReFlashHostLoadTask.lastReadDataTime = new Date().getTime();
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            JsonArray jsonArray = new JsonArray();
            String taskID = request.getParameter("taskID");
            String url = RESTLET_URL_BASE + "attempt?task_id=" + taskID;
            cr = new ClientResource(url);
            cr.setRequestEntityBuffering(true);
            IAttemptsResource resource = cr.wrap(IAttemptsResource.class);
            cr.accept(MediaType.APPLICATION_XML);
            ArrayList<AttemptDTO> attempts = resource.retrieve();

            for (AttemptDTO dto : attempts) {
                JsonObject jsonObject = new JsonObject();

                String state = dto.getStatus();
                jsonObject.addProperty("state", state);

                String taskName = "";
                for (Task task : tasks) {
                    if (task.getTaskid().equals(dto.getTaskID())) {
                        taskName = task.getName();
                        break;
                    }
                }

                jsonObject.addProperty("attemptId", dto.getAttemptID());
                jsonObject.addProperty("id", dto.getId());
                if (taskName != null) {
                    jsonObject.addProperty("taskName", taskName);
                } else {
                    jsonObject.addProperty("taskName", "NULL");
                }

                if (dto.getStartTime() != null) {
                    jsonObject.addProperty("startTime", formatter.format(dto.getStartTime()));
                } else {
                    jsonObject.addProperty("startTime", "NULL");
                }

                if (dto.getEndTime() != null) {
                    jsonObject.addProperty("endTime", formatter.format(dto.getEndTime()));
                } else {
                    jsonObject.addProperty("endTime", "NULL");
                }

                if (dto.getScheduleTime() != null) {
                    jsonObject.addProperty("scheduleTime", formatter.format(dto.getScheduleTime()));
                } else {
                    jsonObject.addProperty("scheduleTime", "NULL");
                }
                if (dto.getExecHost() != null) {
                    jsonObject.addProperty("exeHost", dto.getExecHost());
                } else {
                    jsonObject.addProperty("exeHost", "NULL");
                }
                jsonObject.addProperty("returnValue", dto.getReturnValue());
                boolean isViewLog = AttemptProxyServlet.isHostOverLoad(dto.getExecHost());
                jsonObject.addProperty("isViewLog",isViewLog);

                jsonArray.add(jsonObject);

            }

            output.write(jsonArray.toString().getBytes());
            output.close();



        }
    }


}
