package com.dp.bigdata.taurus.web.servlet;

import com.dp.bigdata.taurus.restlet.resource.impl.DeployResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by kirinli on 14-8-28.
 */
public class DeployServlet  extends HttpServlet {
    private static final String STATUS = "status";
    private static final String DEPLOY = "deploy";

    private DeployResource deployResource;

    @Override
    public void init() throws ServletException {
        deployResource = (DeployResource)WebApplicationContextUtils.getWebApplicationContext(getServletContext()).getBean("deployResource");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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
