package com.dp.bigdata.taurus.web.servlet;

import com.dianping.cat.Cat;
import com.dianping.lion.EnvZooKeeperConfig;
import com.dianping.lion.client.ConfigCache;
import com.dianping.lion.client.LionException;
import com.dp.bigdata.taurus.restlet.resource.IGroupTasks;
import com.dp.bigdata.taurus.restlet.resource.IUserTasks;
import com.dp.bigdata.taurus.web.utils.ReFlashHostLoadTaskTimer;
import org.restlet.data.MediaType;
import org.restlet.resource.ClientResource;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by kirinli on 15/1/28.
 */
public class MonitorCenterServlet  extends HttpServlet {

    private String RESTLET_URL_BASE;

    private static final String USER_TASK = "usertask";

    private static final String GROUP_TASK = "grouptask";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                config.getServletContext());
        ReFlashHostLoadTaskTimer.getReFlashHostLoadManager().start();
        ServletContext context = getServletContext();
        try {
            RESTLET_URL_BASE = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.web.restlet.url");
        } catch (LionException e) {
            RESTLET_URL_BASE = context.getInitParameter("RESTLET_SERVER");
            Cat.logError("LionException", e);
        } catch (Exception e) {
            Cat.logError("LionException", e);
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");
        ClientResource cr;

        if (USER_TASK.equals(action)) {
            OutputStream output = response.getOutputStream();
            String username = request.getParameter("username");
            String start = request.getParameter("start");
            String end = request.getParameter("end");

            cr = new ClientResource(RESTLET_URL_BASE + "usertasks/" + username + "/" + start + "/" + end);
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

            cr = new ClientResource(RESTLET_URL_BASE + "grouptasks/" + username + "/" + start + "/" + end);
            IGroupTasks groupTasks = cr.wrap(IGroupTasks.class);
            cr.accept(MediaType.APPLICATION_XML);
            String jsonString = groupTasks.retrieve();
            output.write(jsonString.getBytes());
            output.close();
        }
    }
}
