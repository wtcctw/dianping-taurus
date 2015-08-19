package com.dp.bigdata.taurus.springmvc.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.restlet.resource.ClientResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dp.bigdata.taurus.restlet.shared.TaskDTO;
import com.dp.bigdata.taurus.restlet.utils.LionConfigUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Controller
public class ScheduleController {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final String SCHEDULE = "schedule";

	@RequestMapping(value = "/schedule.do", method = RequestMethod.POST)
	public void scheduleDoPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		log.info("--------------init the scheduleDoPost------------");
		
		String action = request.getParameter("action");
        ClientResource cr;
        if (SCHEDULE.equals(action)) {
            OutputStream output = response.getOutputStream();

            String task_api = LionConfigUtil.RESTLET_API_BASE + "task";
            //String status_api = LionConfigUtil.RESTLET_API_BASE + "getlaststatus";

            String name = request.getParameter("name");
            String appname = request.getParameter("appname");
            String currentUser = request.getParameter("currentUser");
            //String isAdmin = request.getParameter("isAdmin");

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
            ArrayList<TaskDTO> tasks = cr.get(ArrayList.class);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            for (TaskDTO dto : tasks) {
                JsonObject sechedule = new JsonObject();
                String state = dto.getStatus();
                /*String lastTaskStatus;

                if (isAdmin != null && isAdmin.equals("false")&& !state.equals("SUSPEND")) {
                    String status;
                    try {
                        cr = new ClientResource(status_api + "/" + dto.getTaskid());
                        status = cr.get(String.class);
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

                            lastTaskStatus = ExecuteStatus.getInstanceRunState(taskState);
                        } catch (Exception e) {
                            lastTaskStatus = "NULL";
                        }


                    } else {
                        lastTaskStatus = "NULL";
                    }
                } else {
                    lastTaskStatus = "NULL";
                }*/


                sechedule.addProperty("state", state);

                sechedule.addProperty("taskId", dto.getTaskid());
                sechedule.addProperty("taskName", dto.getName());
                sechedule.addProperty("hostName", dto.getHostname());
                sechedule.addProperty("creator", dto.getCreator());
                sechedule.addProperty("proxyUser", dto.getProxyuser());
                sechedule.addProperty("addTime", formatter.format(dto.getAddtime()));
                sechedule.addProperty("crontab", dto.getCrontab());
                //sechedule.addProperty("lastTaskStatus", lastTaskStatus);
                sechedulesArray.add(sechedule);
            }

            output.write(sechedulesArray.toString().getBytes());
            output.close();
        }
	}
}
