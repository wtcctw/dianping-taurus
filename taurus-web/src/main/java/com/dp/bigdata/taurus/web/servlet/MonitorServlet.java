package com.dp.bigdata.taurus.web.servlet;

import com.dianping.cat.Cat;
import com.dianping.lion.EnvZooKeeperConfig;
import com.dianping.lion.client.ConfigCache;
import com.dianping.lion.client.LionException;
import com.dp.bigdata.taurus.core.AttemptStatus;
import com.dp.bigdata.taurus.generated.module.Task;
import com.dp.bigdata.taurus.restlet.resource.*;
import com.dp.bigdata.taurus.restlet.shared.AttemptDTO;
import com.dp.bigdata.taurus.restlet.shared.HostDTO;
import com.dp.bigdata.taurus.web.utils.ReFlashHostLoadTask;
import com.dp.bigdata.taurus.web.utils.ReFlashHostLoadTaskTimer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.restlet.data.MediaType;
import org.restlet.resource.ClientResource;
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





    private static final String JOB_DETAIL = "jobdetail";

    private static final String RUNNING_TASKS = "runningtasks";
    private static final String FAILED_TASKS = "failedtasks";
    private static final String SUBMIT_FAIL_TASK = "submitfail";
    private static final String DEPENDENCY_PASS_TASK = "dependencypass";
    private static final String DEPENDENCY_TIMEOUT_TASK = "dependencytimeout";
    private static final String TIMEOUT_TASK = "timeout";




    private static final String REFLASH_ATTEMPTS = "reflash_attempts";

    private static ArrayList<AttemptDTO> attempts;
    private static boolean is_flash = false;



    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                config.getServletContext());
        ReFlashHostLoadTaskTimer.getReFlashHostLoadManager().start();
        ServletContext context = getServletContext();
        try {
            RESTLET_URL_BASE = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.web.restlet.url");
        } catch (LionException e) {
            RESTLET_URL_BASE = context.getInitParameter("RESTLET_SERVER");
            Cat.logError("LionException",e);
        } catch (Exception e) {
            Cat.logError("LionException", e);
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
         if (JOB_DETAIL.equals(action)) {
            OutputStream output = response.getOutputStream();
            String start = request.getParameter("start");
            String end = request.getParameter("end");

            cr = new ClientResource(RESTLET_URL_BASE + "jobdetail/" + "/" + start + "/" + end);
            IUserTasks userTasks = cr.wrap(IUserTasks.class);
            cr.accept(MediaType.APPLICATION_XML);
            String jsonString = userTasks.retrieve();
            output.write(jsonString.getBytes());
            output.close();
        } else if (REFLASH_ATTEMPTS.equals(action)) {
            OutputStream output = response.getOutputStream();
            String start = request.getParameter("start");
            String taskTime = start;

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String url = RESTLET_URL_BASE + "getattemptsbystatus/";


            cr = new ClientResource(url + taskTime);
            IGetAttemptsByStatus resource = cr.wrap(IGetAttemptsByStatus.class);
            attempts = resource.retrieve();

            output.write("success".getBytes());
            output.close();

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

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

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

                        String zabbixSwitch = "";
                        try {
                            zabbixSwitch = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.zabbix.switch");
                        }catch (LionException e){
                            zabbixSwitch = "true";
                        }

                        if (zabbixSwitch.equals("false")){
                            isViewLog = false;
                        }

                        if (!isViewLog) {

                            result += "<td >"
                                    + "  <a target=\"_blank\" href=\"viewlog.jsp?id="
                                    + dto.getAttemptID() + "&status=" + dto.getStatus()
                                    + "\">日志</a>"
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

            ArrayList<Task> tasks = ReFlashHostLoadTask.getTasks();
            if (tasks == null) {
                ClientResource crTask = new ClientResource(RESTLET_URL_BASE + "gettasks");
                IGetTasks taskResource = crTask.wrap(IGetTasks.class);
                tasks = taskResource.retrieve();
                ReFlashHostLoadTask.allTasks = tasks;
                ReFlashHostLoadTask.lastReadDataTime = new Date().getTime();
            }


            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            String result = "";
            if (attempts != null)
                for (AttemptDTO dto : attempts) {
                    String state = dto.getStatus();


                    if (state.equals("FAILED")) {
                        String status_api = RESTLET_URL_BASE + "getlaststatus";
                        String status;
                        try {
                            cr = new ClientResource(status_api + "/" + dto.getTaskID());
                            IGetTaskLastStatus statusResource = cr.wrap(IGetTaskLastStatus.class);
                            cr.accept(MediaType.APPLICATION_XML);
                            status = statusResource.retrieve();
                        } catch (Exception e) {
                            status = null;
                        }

                        String lastTaskStatus ="";
                        int taskState = -1;
                        if (status != null) {
                            try {
                                JsonParser parser = new JsonParser();
                                JsonElement statusElement = parser.parse(status);
                                JsonObject statusObject = statusElement.getAsJsonObject();
                                JsonElement statusValue = statusObject.get("status");

                                taskState = statusValue.getAsInt();

                                lastTaskStatus = AttemptStatus.getInstanceRunState(taskState);
                            } catch (Exception e) {
                                lastTaskStatus = "NULL";
                            }
                        }
                        if(state == "SUCCEEDED"){
                            lastTaskStatus = "<span class='label label-info'>"
                                    + lastTaskStatus
                                    + "</span>";
                        }else{
                            lastTaskStatus = "<span class='label label-important'>"
                                    + lastTaskStatus
                                    + "</span>";
                        }

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
                                + lastTaskStatus
                                + "</td >";
                        result += "<td >"
                                + "  <a target=\"_blank\" href=\"viewlog.jsp?id="
                                + dto.getAttemptID() + "&status=" + dto.getStatus()
                                + "\">日志</a>"
                                + "</td >";

                        result += "<td> <a id ='failedFeedBtn' class='feedBtn'  href='feederror.jsp?id="
                                + dto.getAttemptID()
                                + "&status="
                                + dto.getStatus()
                                + "&taskName="
                                + taskName
                                + "&ip="
                                + dto.getExecHost()
                                + "&taskId="
                                + dto.getTaskID()
                                + "&feedtype=wechat"
                                + "&from=monitor"
                                + "'><img border='0' src='img/wechat.png'  width='20' height='20' color='blue' alt='点我报错' title='点我报错'/></a> |"
                                + "<a id ='failedFeedQQBtn' class='feedBtn'  href='feederror.jsp?id="
                                + dto.getAttemptID()
                                + "&status="
                                + dto.getStatus()
                                + "&taskName="
                                + taskName
                                + "&ip="
                                + dto.getExecHost()
                                + "&taskId="
                                + dto.getTaskID()
                                + "&feedtype=qq"
                                + "&from=monitor"
                                + "'><img border='0' src='img/qq.png'  width='20' height='20' color='blue' alt='点我报错' title='点我报错'/></a></td>";


                        result += "</tr>";
                    }
                }
            output.write(result.getBytes());
            output.close();

        } else if (SUBMIT_FAIL_TASK.equals(action)) {
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

            String result = "";
            if (attempts != null)
                for (AttemptDTO dto : attempts) {
                    String state = dto.getStatus();


                    if (state.equals("SUBMIT_FAIL")) {
                        String status_api = RESTLET_URL_BASE + "getlaststatus";
                        String status;
                        try {
                            cr = new ClientResource(status_api + "/" + dto.getTaskID());
                            IGetTaskLastStatus statusResource = cr.wrap(IGetTaskLastStatus.class);
                            cr.accept(MediaType.APPLICATION_XML);
                            status = statusResource.retrieve();
                        } catch (Exception e) {
                            status = null;
                        }

                        String lastTaskStatus ="";
                        int taskState = -1;
                        if (status != null) {
                            try {
                                JsonParser parser = new JsonParser();
                                JsonElement statusElement = parser.parse(status);
                                JsonObject statusObject = statusElement.getAsJsonObject();
                                JsonElement statusValue = statusObject.get("status");

                                taskState = statusValue.getAsInt();

                                lastTaskStatus = AttemptStatus.getInstanceRunState(taskState);
                            } catch (Exception e) {
                                lastTaskStatus = "NULL";
                            }
                        }
                        if(lastTaskStatus.equals("SUCCEEDED") || lastTaskStatus.equals("RUNNING") ){
                            lastTaskStatus = "<span class='label label-info'>"
                                    + lastTaskStatus
                                    + "</span>";
                        }else{
                            lastTaskStatus = "<span class='label label-important'>"
                                    + lastTaskStatus
                                    + "</span>";
                        }

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
                                + lastTaskStatus
                                + "</td >";
                        result += "<td> <a id ='submitFeedBtn' class='feedBtn'  href='feederror.jsp?id="
                                + dto.getAttemptID()
                                + "&status="
                                + dto.getStatus()
                                + "&taskName="
                                + taskName
                                + "&ip="
                                + dto.getExecHost()
                                + "&taskId="
                                + dto.getTaskID()
                                + "&feedtype=wechat"
                                + "&from=monitor"
                                + "'><img border='0' src='img/wechat.png'  width='20' height='20' color='blue' alt='点我报错' title='点我报错'/></a> |"
                                + "<a id ='submitFeedQQBtn' class='feedBtn'  href='feederror.jsp?id="
                                + dto.getAttemptID()
                                + "&status="
                                + dto.getStatus()
                                + "&taskName="
                                + taskName
                                + "&ip="
                                + dto.getExecHost()
                                + "&taskId="
                                + dto.getTaskID()
                                + "&feedtype=qq"
                                + "&from=monitor"
                                + "'><img border='0' src='img/qq.png'  width='20' height='20' color='blue' alt='点我报错' title='点我报错'/></a></td>";
                        result += "</tr>";
                    }
                }
            output.write(result.getBytes());
            output.close();


        } else if (DEPENDENCY_PASS_TASK.equals(action)) {

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

            String result = "";
            if (attempts != null)
                for (AttemptDTO dto : attempts) {
                    String state = dto.getStatus();


                    if (state.equals("DEPENDENCY_PASS")) {
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

                        result += "<td> <a id ='denpencyFeedBtn' class='feedBtn'  href='feederror.jsp?id="
                                + dto.getAttemptID()
                                + "&status="
                                + dto.getStatus()
                                + "&taskName="
                                + taskName
                                + "&ip="
                                + dto.getExecHost()
                                + "&taskId="
                                + dto.getTaskID()
                                + "&feedtype=wechat"
                                + "&from=monitor"
                                + "'><img border='0' src='img/wechat.png'  width='20' height='20' color='blue' alt='点我报错' title='点我报错'/></a> |"
                                + "<a id ='denpencyFeedQQBtn' class='feedBtn'  href='feederror.jsp?id="
                                + dto.getAttemptID()
                                + "&status="
                                + dto.getStatus()
                                + "&taskName="
                                + taskName
                                + "&ip="
                                + dto.getExecHost()
                                + "&taskId="
                                + dto.getTaskID()
                                + "&feedtype=qq"
                                + "&from=monitor"
                                + "'><img border='0' src='img/qq.png'  width='20' height='20' color='blue' alt='点我报错' title='点我报错'/></a></td>";

                        result += "</tr>";
                    }
                }
            output.write(result.getBytes());
            output.close();

        } else if (DEPENDENCY_TIMEOUT_TASK.equals(action)) {

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
                        result += "<td >"
                                + "  <a target=\"_blank\" href=\"viewlog.jsp?id="
                                + dto.getAttemptID() + "&status=" + dto.getStatus()
                                + "\">日志</a>"
                                + "</td >";

                        result += "<td> <a id ='denpencyTimeoutFeedBtn' class='feedBtn'  href='feederror.jsp?id="
                                + dto.getAttemptID()
                                + "&status="
                                + dto.getStatus()
                                + "&taskName="
                                + taskName
                                + "&ip="
                                + dto.getExecHost()
                                + "&taskId="
                                + dto.getTaskID()
                                + "&feedtype=wechat"
                                + "&from=monitor"
                                + "'><img border='0' src='img/wechat.png'  width='20' height='20' color='blue' alt='点我报错' title='点我报错'/></a> |"
                                + "<a id ='denpencyTimeoutFeedQQBtn' class='feedBtn'  href='feederror.jsp?id="
                                + dto.getAttemptID()
                                + "&status="
                                + dto.getStatus()
                                + "&taskName="
                                + taskName
                                + "&ip="
                                + dto.getExecHost()
                                + "&taskId="
                                + dto.getTaskID()
                                + "&feedtype=qq"
                                + "&from=monitor"
                                + "'><img border='0' src='img/qq.png'  width='20' height='20' color='blue' alt='点我报错' title='点我报错'/></a></td>";

                        result += "</tr>";
                    }
                }
            output.write(result.getBytes());
            output.close();

        } else if (TIMEOUT_TASK.equals(action)) {
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

            String result = "";
            if (attempts != null)
                for (AttemptDTO dto : attempts) {
                    String state = dto.getStatus();


                    if (state.equals("TIMEOUT")) {
                        String status_api = RESTLET_URL_BASE + "getlaststatus";
                        String status;
                        try {
                            cr = new ClientResource(status_api + "/" + dto.getTaskID());
                            IGetTaskLastStatus statusResource = cr.wrap(IGetTaskLastStatus.class);
                            cr.accept(MediaType.APPLICATION_XML);
                            status = statusResource.retrieve();
                        } catch (Exception e) {
                            status = null;
                        }

                        String lastTaskStatus ="";
                        int taskState = -1;
                        if (status != null) {
                            try {
                                JsonParser parser = new JsonParser();
                                JsonElement statusElement = parser.parse(status);
                                JsonObject statusObject = statusElement.getAsJsonObject();
                                JsonElement statusValue = statusObject.get("status");

                                taskState = statusValue.getAsInt();

                                lastTaskStatus = AttemptStatus.getInstanceRunState(taskState);
                            } catch (Exception e) {
                                lastTaskStatus = "NULL";
                            }
                        }
                        if(state == "SUCCEEDED"){
                            lastTaskStatus = "<span class='label label-info'>"
                                    + lastTaskStatus
                                    + "</span>";
                        }else{
                            lastTaskStatus = "<span class='label label-important'>"
                                    + lastTaskStatus
                                    + "</span>";
                        }

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
                                + lastTaskStatus
                                + "</td >";
                        result += "<td >"
                                + "  <a target=\"_blank\" href=\"viewlog.jsp?id="
                                + dto.getAttemptID() + "&status=" + dto.getStatus()
                                + "\">日志</a>"
                                + "</td >";

                        result += "<td> "
                                + "<a id ='timeOutFeedBtn' class='feedBtn'  href='feederror.jsp?id="
                                + dto.getAttemptID()
                                + "&status="
                                + dto.getStatus()
                                + "&taskName="
                                + taskName
                                + "&ip="
                                + dto.getExecHost()
                                + "&taskId="
                                + dto.getTaskID()
                                + "&feedtype=wechat"
                                + "&from=monitor"
                                + "'><img border='0' src='img/wechat.png'  width='20' height='20' color='blue' alt='点我报错' title='点我报错'/></a> |"
                                + "<a id ='timeOutFeedQQBtn' class='feedBtn'  href='feederror.jsp?id="
                                + dto.getAttemptID()
                                + "&status="
                                + dto.getStatus()
                                + "&taskName="
                                + taskName
                                + "&ip="
                                + dto.getExecHost()
                                + "&taskId="
                                + dto.getTaskID()
                                + "&feedtype=qq"
                                + "&from=monitor"
                                + "'><img border='0' src='img/qq.png'  width='20' height='20' color='blue' alt='点我报错' title='点我报错'/></a></td>";

                        result += "</tr>";
                    }
                }
            output.write(result.getBytes());
            output.close();
        }
    }



}
