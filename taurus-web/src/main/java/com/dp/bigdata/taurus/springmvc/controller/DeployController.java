package com.dp.bigdata.taurus.springmvc.controller;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dp.bigdata.taurus.restlet.resource.impl.DeployResource;

@Controller
public class DeployController {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private DeployResource deployResource;
	
	private final String STATUS = "status";
    private final String DEPLOY = "deploy";
	
	@RequestMapping(value = "/deploy.do", method = RequestMethod.POST)
	public void deployDoPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		log.info("--------------init the deployDoPost------------");
		
		String action = request.getParameter("action") == null ? "" : request.getParameter("action").toLowerCase();
		
		if (action.equals(STATUS)){
		    String deployId = request.getParameter("deployId");
		    String name = request.getParameter("appName");
		    String status =   deployResource.status(deployId, name);
		
		    OutputStream output = response.getOutputStream();
		    output.write(status.getBytes());
		    output.close();
		}else if (action.equals(DEPLOY)){
			String deployId = request.getParameter("deployId");
			String ip = request.getParameter("ip");
			String file = request.getParameter("file");
			String url = request.getParameter("url");
			String name = request.getParameter("name");
			deployResource.deployer(deployId, ip, file, url, name);
			OutputStream output = response.getOutputStream();
			output.close();
		}
	}

}
