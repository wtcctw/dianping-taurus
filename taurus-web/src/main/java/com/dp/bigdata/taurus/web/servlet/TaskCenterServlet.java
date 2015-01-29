package com.dp.bigdata.taurus.web.servlet;

import com.dianping.cat.Cat;
import com.dianping.lion.EnvZooKeeperConfig;
import com.dianping.lion.client.ConfigCache;
import com.dianping.lion.client.LionException;
import com.dp.bigdata.taurus.restlet.resource.IFailedTaskLoad;
import com.dp.bigdata.taurus.restlet.resource.ITotalTask;
import com.dp.bigdata.taurus.restlet.resource.ITotalTaskLoad;
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
public class TaskCenterServlet  extends HttpServlet {
    private String RESTLET_URL_BASE;

    private static final String TOTAL_TASK_LOAD = "totaltaskload";
    private static final String TOTAL_TASK = "totaltask";
    private static final String FAILED_TASK_LOAD = "failedtaskload";

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

        if (TOTAL_TASK_LOAD.equals(action)) {
            OutputStream output = response.getOutputStream();
            String start = request.getParameter("start");
            String end = request.getParameter("end");
            String apiUrl = RESTLET_URL_BASE + "totaltaskload/" + start + "/" + end;
            ClientResource crToal = new ClientResource(apiUrl);
            ITotalTaskLoad totalTaskLoad = crToal.wrap(ITotalTaskLoad.class);

            String jsonString = totalTaskLoad.retrieve();
            output.write(jsonString.getBytes());
            output.close();

        } else if (FAILED_TASK_LOAD.equals(action)) {
            OutputStream output = response.getOutputStream();
            String start = request.getParameter("start");
            String end = request.getParameter("end");
            String apiUrl = RESTLET_URL_BASE + "failedtaskload/" + start + "/" + end;
            ClientResource crFail = new ClientResource(apiUrl);
            IFailedTaskLoad failedTaskLoad = crFail.wrap(IFailedTaskLoad.class);

            String jsonString = failedTaskLoad.retrieve();
            output.write(jsonString.getBytes());
            output.close();

        }  else if (TOTAL_TASK.equals(action)) {
            OutputStream output = response.getOutputStream();
            String start = request.getParameter("start");
            String end = request.getParameter("end");

            cr = new ClientResource(RESTLET_URL_BASE + "totaltasks/" + start + "/" + end);
            ITotalTask totalTasks = cr.wrap(ITotalTask.class);
            cr.accept(MediaType.APPLICATION_XML);
            String jsonString = totalTasks.retrieve();
            output.write(jsonString.getBytes());
            output.close();
        }

    }
}
