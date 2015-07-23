package com.dp.bigdata.taurus.springmvc.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.restlet.resource.ClientResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dp.bigdata.taurus.restlet.shared.HostDTO;

@Controller
public class HostCenterController {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final String HOST = "host";
    private final String HOST_LOAD = "hostload";

	@RequestMapping(value = "/host_center.do", method = RequestMethod.POST)
	public void hostCenterDoPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		log.info("--------------init the hostCenterDoPost------------");
		
		String action = request.getParameter("action");
        ClientResource cr = new ClientResource(InitController.RESTLET_URL_BASE + "host");
        ArrayList<HostDTO> hosts = cr.get(ArrayList.class);

        if (HOST.equals(action)) {
            String type = request.getParameter("gettype");

            StringBuffer onLineHosts = new StringBuffer();
            StringBuffer offLineHosts = new StringBuffer();
            StringBuffer exceptionHosts = new StringBuffer();

            OutputStream output = response.getOutputStream();

            for (HostDTO dto : hosts) {

                if (!dto.isOnline()) {
                    offLineHosts.append(dto.getIp());
                    offLineHosts.append(",");
                } else if (dto.isConnected()) {
                    onLineHosts.append(dto.getIp());
                    onLineHosts.append(",");
                } else {
                    exceptionHosts.append(dto.getIp());
                    exceptionHosts.append(",");
                }

            }
            if ("online".equals(type)) {

                String tmpOnLineHosts = onLineHosts.substring(0, onLineHosts.length() - 1);

                output.write(tmpOnLineHosts.getBytes());
                output.close();
            } else if ("offline".equals(type)) {
                String  tmpOffLineHosts = offLineHosts.substring(0, offLineHosts.length() - 1);

                output.write(tmpOffLineHosts.getBytes());
                output.close();
            } else if ("exception".equals(type)) {
                String tmpExceptionHosts = exceptionHosts.substring(0, exceptionHosts.length() - 1);

                output.write(tmpExceptionHosts.getBytes());
                output.close();
            } else {
                String tmpOnLineHosts;
                if (onLineHosts.length() > 0) {
                    tmpOnLineHosts = onLineHosts.substring(0, onLineHosts.length() - 1);
                } else {
                    tmpOnLineHosts = "NULL";
                }

                String tmpExceptionHosts;
                if (exceptionHosts.length() > 0) {
                    tmpExceptionHosts = exceptionHosts.substring(0, exceptionHosts.length() - 1);
                } else {
                    tmpExceptionHosts = "NULL";
                }

                StringBuffer allInfo = new StringBuffer();
                allInfo.append(tmpOnLineHosts);
                allInfo.append("#");
                allInfo.append(tmpExceptionHosts);

                output.write(allInfo.toString().getBytes());
                output.close();

            }
        } else if (HOST_LOAD.equals(action)) {
            OutputStream output = response.getOutputStream();

            String queryType = request.getParameter("queryType");
            
            cr = new ClientResource(InitController.RESTLET_URL_BASE + "reflashHostLoad");

            String jsonString = cr.get(String.class);

            if (StringUtils.isNotBlank(queryType) && "reflash".equals(queryType)) {
                jsonString = cr.post(null, String.class);
            }

            if (StringUtils.isBlank(jsonString)) {
                jsonString = cr.post(null, String.class);
            }

            output.write(jsonString.getBytes());
            output.close();
        }
	}
}
