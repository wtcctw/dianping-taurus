package com.cip.crane.springmvc.controller;

import com.cip.crane.restlet.shared.TaskDTO;
import com.cip.crane.restlet.utils.LionConfigUtil;
import com.google.gson.Gson;
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
import java.io.PrintWriter;

@Controller
public class TaskProxyController {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final String DELETE = "delete";
	private final String SUSPEND = "suspend";
	private final String EXECUTE = "execute";
	private final String RESUME = "resume";
	private final String DETAIL = "detail";

	@RequestMapping(value = "/tasks.do", method = RequestMethod.POST)
	public void tasksDoPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		log.info("--------------init the tasksDoPost------------");
		
		String action = request.getParameter("action").toLowerCase();
        String taskID = request.getParameter("id").trim();
        
        ClientResource taskCr = new ClientResource(LionConfigUtil.RESTLET_API_BASE + "task/" + taskID);
        ClientResource manualCr = new ClientResource(LionConfigUtil.RESTLET_API_BASE + "manualtask/" + taskID);

        if(action.equals(DELETE)){
        	taskCr.delete();
            System.out.println("Delete result code : " + taskCr.getStatus().getCode());
            response.setStatus(taskCr.getStatus().getCode());
        }else if(action.equals(SUSPEND)){
        	manualCr.put(null);
            System.out.println("Suspend result code : " + manualCr.getStatus().getCode());
            response.setStatus(manualCr.getStatus().getCode());
        }else if(action.equals(EXECUTE)){
        	manualCr.get();
            System.out.println("Execute result code : " + manualCr.getStatus().getCode());
            response.setStatus(manualCr.getStatus().getCode());
        } else if (action.equals(RESUME)) {
        	manualCr.post(null);
            System.out.println("Resume result code : " + manualCr.getStatus().getCode());
            response.setStatus(manualCr.getStatus().getCode());
        } else if (action.equals(DETAIL)){
            TaskDTO task = taskCr.get(TaskDTO.class);
            response.setContentType("application/json");
            Gson gson = new Gson();  
            String json = gson.toJson(task);    
           
            // Get the printwriter object from response to write the required json object to the output stream      
            PrintWriter out = response.getWriter();
            // Assuming your json object is **jsonObject**, perform the following, it will return your json object  
            out.write(json);
            out.flush();
            response.setStatus(200);
        }
	}
}
