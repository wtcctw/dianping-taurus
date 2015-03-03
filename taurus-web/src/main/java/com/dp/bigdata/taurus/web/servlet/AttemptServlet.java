package com.dp.bigdata.taurus.web.servlet;

import com.dianping.cat.Cat;
import com.dianping.lion.EnvZooKeeperConfig;
import com.dianping.lion.client.ConfigCache;
import com.dianping.lion.client.LionException;
import com.dp.bigdata.taurus.core.Scheduler;
import com.dp.bigdata.taurus.generated.module.Task;
import com.dp.bigdata.taurus.restlet.resource.IAttemptsResource;
import com.dp.bigdata.taurus.restlet.resource.IGetTasks;
import com.dp.bigdata.taurus.restlet.shared.AttemptDTO;
import com.dp.bigdata.taurus.web.utils.ReFlashHostLoadTask;
import com.dp.bigdata.taurus.web.utils.ReFlashHostLoadTaskTimer;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
import java.util.Map;

/**
 * Created by kirinli on 15/1/28.
 */
public class AttemptServlet extends HttpServlet {
    private String RESTLET_URL_BASE;
    private static final String ATTEMPT = "attempt";

    @Autowired
    private Scheduler scheduler;
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

        if (ATTEMPT.equals(action)) {
            Map<String ,Task> map = scheduler.getAllRegistedTask();

            OutputStream output = response.getOutputStream();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            JsonArray jsonArray = new JsonArray();
            String taskID = request.getParameter("taskID");
            String url = RESTLET_URL_BASE + "attempt?task_id=" + taskID;
            cr = new ClientResource(url);
            cr.setRequestEntityBuffering(true);
            IAttemptsResource resource = cr.wrap(IAttemptsResource.class);
            cr.accept(MediaType.APPLICATION_XML);
            ArrayList<AttemptDTO> attempts = resource.retrieve();
            Task task = map.get(taskID);
            String taskName = task.getName();
            String zabbixSwitch;
            try {
                zabbixSwitch = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.zabbix.switch");
            }catch (LionException e){
                zabbixSwitch = "true";
            }
            boolean isViewLog = AttemptProxyServlet.isHostOverLoad(task.getHostname());
            if (zabbixSwitch.equals("false")){
                isViewLog = false;
            }

            for (AttemptDTO dto : attempts) {
                JsonObject jsonObject = new JsonObject();

                String state = dto.getStatus();
                jsonObject.addProperty("state", state);


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

                jsonObject.addProperty("isViewLog", isViewLog);

                jsonArray.add(jsonObject);

            }

            output.write(jsonArray.toString().getBytes());
            output.close();


        }
    }

}
