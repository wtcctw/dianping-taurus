package com.dp.bigdata.taurus.springmvc.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.restlet.resource.ClientResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dp.bigdata.taurus.restlet.resource.IManualTaskResource;
import com.dp.bigdata.taurus.restlet.resource.ITaskResource;
import com.dp.bigdata.taurus.restlet.shared.TaskDTO;
import com.google.gson.Gson;

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
        
        ClientResource taskCr = new ClientResource(InitController.RESTLET_URL_BASE + "task/" + taskID);
        ITaskResource taskResource = taskCr.wrap(ITaskResource.class);

        ClientResource manualCr = new ClientResource(InitController.RESTLET_URL_BASE + "manualtask/" + taskID);
        IManualTaskResource manualResource = manualCr.wrap(IManualTaskResource.class);

        if(action.equals(DELETE)){
            taskResource.remove();
            System.out.println("Delete result code : " + manualCr.getStatus().getCode());
            response.setStatus(taskCr.getStatus().getCode());
        }else if(action.equals(SUSPEND)){
            manualResource.suspend();
            System.out.println("Suspend result code : " + manualCr.getStatus().getCode());
            response.setStatus(manualCr.getStatus().getCode());
        }else if(action.equals(EXECUTE)){
            manualResource.start();
            System.out.println("Execute result code : " + manualCr.getStatus().getCode());
            response.setStatus(manualCr.getStatus().getCode());
        } else if (action.equals(RESUME)) {
            manualResource.resume();
            System.out.println("Resume result code : " + manualCr.getStatus().getCode());
            response.setStatus(manualCr.getStatus().getCode());
        } else if (action.equals(DETAIL)){
            TaskDTO task = taskResource.retrieve();
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
