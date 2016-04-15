package com.dp.bigdata.taurus.springmvc.controller;

import com.dp.bigdata.taurus.restlet.shared.AttemptDTO;
import com.dp.bigdata.taurus.restlet.shared.TaskDTO;
import com.dp.bigdata.taurus.restlet.utils.LionConfigUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.restlet.resource.ClientResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
public class AttemptController {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final String ATTEMPT = "attempt";
	
	@RequestMapping(value = "/attempt.do", method = RequestMethod.POST)
	public void attemptDoPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		log.info("--------------init the attemptDoPost------------");
		
		String action = request.getParameter("action");
        ClientResource cr = null;

        if (ATTEMPT.equals(action)) { // 作业调度历史 attempt.ftl
        	cr = new ClientResource(LionConfigUtil.RESTLET_API_BASE + "registedTasks");
        	Map<String ,TaskDTO> map = cr.get(HashMap.class);

            OutputStream output = response.getOutputStream();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            JsonArray jsonArray = new JsonArray();
            String taskID = request.getParameter("taskID");
            String url = LionConfigUtil.RESTLET_API_BASE + "attempt?task_id=" + taskID;
            cr = new ClientResource(url);
            cr.setRequestEntityBuffering(true);
            ArrayList<AttemptDTO> attempts = cr.get(ArrayList.class);
            TaskDTO task = map.get(taskID);
            String taskName = task.getName();
            
            boolean isViewLog = false;
            
            if (InitController.ZABBIX_SWITCH.equals("false")){
                isViewLog = false;
            }else{
            	isViewLog = AttemptProxyController.isHostOverLoad(task.getHostname());
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
