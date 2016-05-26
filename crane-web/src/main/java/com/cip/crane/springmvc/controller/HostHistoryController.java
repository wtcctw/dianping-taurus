package com.cip.crane.springmvc.controller;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cip.crane.restlet.utils.LionConfigUtil;
import org.apache.commons.lang.StringUtils;
import org.restlet.resource.ClientResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HostHistoryController {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final String HOST_HISTORY = "host_history";
	
	@RequestMapping(value = "/host_history.do", method = RequestMethod.POST)
	public void hostHistoryDoPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		log.info("--------------init the hostHistoryDoPost------------");
		
		String action = request.getParameter("action");
        ClientResource cr;

        if (HOST_HISTORY.equals(action)){
            OutputStream output = response.getOutputStream();
            String ip = request.getParameter("ip");
            String time = request.getParameter("time");
            cr = new ClientResource(LionConfigUtil.RESTLET_API_BASE + "runningMap/" + time + "/" + ip);
            try {
                String jsonString = cr.get(String.class);
                if (StringUtils.isBlank(jsonString)){
                    output.write("[]".getBytes());
                }else {
                    output.write(jsonString.getBytes());
                }

            }catch (Exception e){
                log.error(e.getMessage());
            }

            output.close();
        }
	}

}
