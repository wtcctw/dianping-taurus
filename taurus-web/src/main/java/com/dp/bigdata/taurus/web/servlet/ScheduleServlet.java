package com.dp.bigdata.taurus.web.servlet;

import com.dianping.cat.Cat;
import com.dianping.lion.EnvZooKeeperConfig;
import com.dianping.lion.client.ConfigCache;
import com.dianping.lion.client.LionException;
import com.dp.bigdata.taurus.core.AttemptStatus;
import com.dp.bigdata.taurus.restlet.resource.IGetTaskLastStatus;
import com.dp.bigdata.taurus.restlet.resource.ITasksResource;
import com.dp.bigdata.taurus.restlet.shared.TaskDTO;
import com.dp.bigdata.taurus.web.utils.ReFlashHostLoadTaskTimer;
import com.google.gson.JsonArray;
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

/**
 * Created by kirinli on 15/1/28.
 */
public class ScheduleServlet extends HttpServlet {

    private String RESTLET_URL_BASE;
    private static final String SCHEDULE = "schedule";
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
            Cat.logError("LionException", e);
        } catch (Exception e) {
            Cat.logError("LionException", e);
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        ClientResource cr;
        if (SCHEDULE.equals(action)) {
            OutputStream output = response.getOutputStream();

            String task_api = RESTLET_URL_BASE + "task";
            String status_api = RESTLET_URL_BASE + "getlaststatus";

            String name = request.getParameter("name");
            String appname = request.getParameter("appname");
            String currentUser = request.getParameter("currentUser");
            String isAdmin = request.getParameter("isAdmin");

            JsonArray sechedulesArray = new JsonArray();

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
                JsonObject sechedule = new JsonObject();
                String state = dto.getStatus();
                String lastTaskStatus;

                if (isAdmin != null && isAdmin.equals("false")&& !state.equals("SUSPEND")) {
                    String status;
                    try {
                        cr = new ClientResource(status_api + "/" + dto.getTaskid());
                        IGetTaskLastStatus statusResource = cr.wrap(IGetTaskLastStatus.class);
                        cr.accept(MediaType.APPLICATION_XML);
                        status = statusResource.retrieve();
                    } catch (Exception e) {
                        status = null;
                    }


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


                    } else {
                        lastTaskStatus = "NULL";
                    }
                } else {
                    lastTaskStatus = "NULL";
                }


                sechedule.addProperty("state", state);

                sechedule.addProperty("taskId", dto.getTaskid());
                sechedule.addProperty("taskName", dto.getName());
                sechedule.addProperty("hostName", dto.getHostname());
                sechedule.addProperty("creator", dto.getCreator());
                sechedule.addProperty("proxyUser", dto.getProxyuser());
                sechedule.addProperty("addTime", formatter.format(dto.getAddtime()));
                sechedule.addProperty("crontab", dto.getCrontab());
                sechedule.addProperty("lastTaskStatus", lastTaskStatus);
                sechedulesArray.add(sechedule);
            }

            output.write(sechedulesArray.toString().getBytes());
            output.close();
        }
    }
}
