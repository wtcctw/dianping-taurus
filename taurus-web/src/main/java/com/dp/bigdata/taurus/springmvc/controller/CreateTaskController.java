package com.dp.bigdata.taurus.springmvc.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.restlet.resource.ClientResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dp.bigdata.taurus.restlet.resource.INameResource;

@Controller
public class CreateTaskController {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping(value = "/create_task", method = RequestMethod.POST)
	public void createTaskPost(HttpServletRequest req, HttpServletResponse resp) 
												throws IOException, ServletException 
	{
		log.info("--------------init the createTaskPost------------");
		HttpClient httpclient = new DefaultHttpClient();
        // Determine final URL
        StringBuffer uri = new StringBuffer();
        
        if(req.getParameter("update") != null){
            uri.append(InitController.RESTLET_URL_BASE + "task").append("/").append(req.getParameter("update"));
        } else {
            uri.append(InitController.RESTLET_URL_BASE + "task");
        }
        log.info("Access URI : " + uri.toString());
        // Get HTTP method
        final String method = req.getMethod();
        // Create new HTTP request container
        HttpRequestBase request = null;
                
        // Get content length
        int contentLength = req.getContentLength();
        // Unknown content length ...
        // if (contentLength == -1)
        // throw new ServletException("Cannot handle unknown content length");
        // If we don't have an entity body, things are quite simple
        if (contentLength < 1) {
            request = new HttpRequestBase() {
                public String getMethod() {
                    return method;
                }
            };
        } else {
            // Prepare request
            HttpEntityEnclosingRequestBase tmpRequest = new HttpEntityEnclosingRequestBase() {
                public String getMethod() {
                    return method;
                }
            };
            // Transfer entity body from the received request to the new request
            InputStreamEntity entity = new InputStreamEntity(
                    req.getInputStream(), contentLength);
            tmpRequest.setEntity(entity);
            request = tmpRequest;
        }

        // Set URI
        try {
            request.setURI(new URI(uri.toString()));
        } catch (URISyntaxException e) {
            throw new ServletException("URISyntaxException: " + e.getMessage());
        }

        // Copy headers from old request to new request
        // @todo not sure how this handles multiple headers with the same name
        Enumeration<?> headers = req.getHeaderNames();
        while (headers.hasMoreElements()) {
            String headerName = (String)headers.nextElement();
            String headerValue = req.getHeader(headerName);
            //LOG.info("header: " + headerName + " value: " + headerValue);
            // Skip Content-Length and Host
            String lowerHeader = headerName.toLowerCase();
            if(lowerHeader.equals("content-type")) {
                request.addHeader(headerName, headerValue+";charset=\"utf-8\"");
            } else if (!lowerHeader.equals("content-length")
                    && !lowerHeader.equals("host")
                    ) {
                request.addHeader(headerName, headerValue);
            }
        }
        
        // Execute the request
        HttpResponse response = httpclient.execute(request);
        // Transfer status code to the response
        StatusLine status = response.getStatusLine();
        resp.setStatus(status.getStatusCode());

        // Transfer headers to the response
        Header[] responseHeaders = response.getAllHeaders();
        for (int i = 0; i < responseHeaders.length; i++) {
            Header header = responseHeaders[i];
            if(!header.getName().equals("Transfer-Encoding"))
                resp.addHeader(header.getName(), header.getValue());
        }

        // Transfer proxy response entity to the servlet response
        HttpEntity entity = response.getEntity();
        InputStream input = entity.getContent();
        OutputStream output = resp.getOutputStream();
        
        byte buffer[] = new byte[50];
        while(input.read(buffer)!=-1){
            output.write(buffer);
        }  
//        int b = input.read();
//        while (b != -1) {
//            output.write(b);
//            b = input.read();
//        }
        // Clean up
        input.close();
        output.close();
        httpclient.getConnectionManager().shutdown();
	}
	
	@RequestMapping(value = "/create_task", method = RequestMethod.GET)
	public void createTaskGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		log.info("--------------init the createTaskGet------------");
		
		StringBuffer uri = new StringBuffer();
        
		if (req.getParameter("name") != null){
            uri.append(InitController.RESTLET_URL_BASE + "name?task_name=").append(req.getParameter("name"));
            log.info("Access URI : " + uri.toString());
            ClientResource cr = new ClientResource(uri.toString());
            INameResource nameResource = cr.wrap(INameResource.class);
            resp.setContentType("text/html");
            if(nameResource.hasName()) {
                resp.getWriter().write("1");
            } else {
                resp.getWriter().write("0");
            }
        }
	}
}
