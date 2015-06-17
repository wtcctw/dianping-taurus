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

import com.dp.bigdata.taurus.restlet.resource.IGroupTasks;
import com.dp.bigdata.taurus.restlet.resource.IUserTasks;

@Controller
public class MonitorCenterController {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final String USER_TASK = "usertask";
    private final String GROUP_TASK = "grouptask";
	
	@RequestMapping(value = "/monitor_center.do", method = RequestMethod.POST)
	public void monitorCenterDoPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		log.info("--------------init the monitorCenterDoPost------------");
		
		String action = request.getParameter("action");
        ClientResource cr;

        if (USER_TASK.equals(action)) {
            OutputStream output = response.getOutputStream();
            String username = request.getParameter("username");
            String start = request.getParameter("start");
            String end = request.getParameter("end");

            cr = new ClientResource(InitController.RESTLET_URL_BASE + "usertasks/" + username + "/" + start + "/" + end);
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

            cr = new ClientResource(InitController.RESTLET_URL_BASE + "grouptasks/" + username + "/" + start + "/" + end);
            IGroupTasks groupTasks = cr.wrap(IGroupTasks.class);
            cr.accept(MediaType.APPLICATION_XML);
            String jsonString = groupTasks.retrieve();
            output.write(jsonString.getBytes());
            output.close();
        }
	}
}
