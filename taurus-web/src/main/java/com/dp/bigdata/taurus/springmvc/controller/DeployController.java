package com.dp.bigdata.taurus.springmvc.controller;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dp.bigdata.taurus.restlet.utils.LionConfigUtil;
import com.dp.bigdata.taurus.zookeeper.deploy.helper.DeployStatus;

@Controller
public class DeployController {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final String STATUS = "status";
    private final String DEPLOY = "deploy";
	
	@RequestMapping(value = "/deploy.do", method = RequestMethod.POST)
	public void deployDoPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		log.info("--------------init the deployDoPost------------");
		
		String action = request.getParameter("action") == null ? "" : request.getParameter("action").toLowerCase();
		ClientResource cr = null;
		
		if (action.equals(STATUS)){
		    String deployId = request.getParameter("deployId");
		    String name = request.getParameter("appName");
		    cr = new ClientResource(LionConfigUtil.RESTLET_API_BASE + "deploy?" + request.getQueryString());
		    Representation result = cr.get();
		    JsonRepresentation jr = new JsonRepresentation(result);
		    
		    JSONObject jsonObj;
		    String status = String.valueOf(DeployStatus.UNKNOWN);
			try {
				jsonObj = jr.getJsonObject();
				status =  (String) jsonObj.get("status");
			} catch (JSONException e) {
				e.printStackTrace();
			} finally{
				OutputStream output = response.getOutputStream();
			    output.write(status.getBytes());
			    output.close();
			}
		    
		}else if (action.equals(DEPLOY)){
			String deployId = request.getParameter("deployId");
			String ip = request.getParameter("ip");
			String file = request.getParameter("file");
			String url = request.getParameter("url");
			String name = request.getParameter("name");
			
			Form form = new Form();
			form.add(deployId, "deployId");
			form.add(ip, "ip");
			form.add(file, "file");
			form.add(url, "url");
			form.add(name, "name");
			Representation re = form.getWebRepresentation();
			//re.setMediaType(MediaType.APPLICATION_XML);
			cr.post(re);
			Status status = cr.getResponse().getStatus();
			
			OutputStream output = response.getOutputStream();
			output.close();
		}
	}

}
