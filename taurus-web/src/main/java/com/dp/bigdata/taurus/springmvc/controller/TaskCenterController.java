package com.dp.bigdata.taurus.springmvc.controller;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.restlet.data.MediaType;
import org.restlet.resource.ClientResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dp.bigdata.taurus.restlet.resource.IFailedTaskLoad;
import com.dp.bigdata.taurus.restlet.resource.ITotalTask;
import com.dp.bigdata.taurus.restlet.resource.ITotalTaskLoad;

@Controller
public class TaskCenterController {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final String TOTAL_TASK_LOAD = "totaltaskload";
    private final String TOTAL_TASK = "totaltask";
    private final String FAILED_TASK_LOAD = "failedtaskload";
	
	@RequestMapping(value = "/task_center.do", method = RequestMethod.POST)
	public void taskCenterDoPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		log.info("--------------init the taskCenterDoPost------------");
		
		String action = request.getParameter("action");
        ClientResource cr;

        if (TOTAL_TASK_LOAD.equals(action)) {
            OutputStream output = response.getOutputStream();
            String start = request.getParameter("start");
            String end = request.getParameter("end");
            String apiUrl = InitController.RESTLET_URL_BASE + "totaltaskload/" + start + "/" + end;
            ClientResource crToal = new ClientResource(apiUrl);
            ITotalTaskLoad totalTaskLoad = crToal.wrap(ITotalTaskLoad.class);

            String jsonString = totalTaskLoad.retrieve();
            output.write(jsonString.getBytes());
            output.close();

        } else if (FAILED_TASK_LOAD.equals(action)) {
            OutputStream output = response.getOutputStream();
            String start = request.getParameter("start");
            String end = request.getParameter("end");
            String apiUrl = InitController.RESTLET_URL_BASE + "failedtaskload/" + start + "/" + end;
            ClientResource crFail = new ClientResource(apiUrl);
            IFailedTaskLoad failedTaskLoad = crFail.wrap(IFailedTaskLoad.class);

            String jsonString = failedTaskLoad.retrieve();
            output.write(jsonString.getBytes());
            output.close();

        }  else if (TOTAL_TASK.equals(action)) {
            OutputStream output = response.getOutputStream();
            String start = request.getParameter("start");
            String end = request.getParameter("end");

            cr = new ClientResource(InitController.RESTLET_URL_BASE + "totaltasks/" + start + "/" + end);
            ITotalTask totalTasks = cr.wrap(ITotalTask.class);
            cr.accept(MediaType.APPLICATION_XML);
            String jsonString = totalTasks.retrieve();
            output.write(jsonString.getBytes());
            output.close();
        }
	}

}
