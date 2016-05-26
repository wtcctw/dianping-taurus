package com.cip.crane.springmvc.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cip.crane.restlet.utils.LionConfigUtil;
import org.restlet.data.Status;
import org.restlet.resource.ClientResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HostController {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final String HOST_NAME = "hostName";
	private final String OP = "op";

	@RequestMapping(value = "/updateHost", method = RequestMethod.GET)
	public void updateHostGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		log.info("--------------init the updateHostGet------------");
		
		try {
			String hostName = req.getParameter(HOST_NAME);
			String op = req.getParameter(OP);
			if (hostName == null || op == null || hostName.isEmpty()
					|| op.isEmpty()) {
				log.error("hostName = " + hostName + ",op = " + op);
				req.setAttribute("statusCode", "500");
				return;
			}
			ClientResource cr = new ClientResource(LionConfigUtil.RESTLET_API_BASE + "host/"
					+ req.getParameter("hostName"));
			cr.post(op);
			Status status = cr.getResponse().getStatus();
			if(Status.SUCCESS_OK.equals(status)){
				req.setAttribute("statusCode", "200");
			} else {
				req.setAttribute("statusCode", "500");
			}
		} catch (Exception e) {
			log.error(e.toString(), e);
			req.setAttribute("statusCode", "500");
		}
		RequestDispatcher requestDispatcher = req
				.getRequestDispatcher(req.getContextPath() + "/hosts");
		requestDispatcher.forward(req, resp);
	}
	
	
}
