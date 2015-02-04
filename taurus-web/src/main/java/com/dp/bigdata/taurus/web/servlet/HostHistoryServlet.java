package com.dp.bigdata.taurus.web.servlet;

import com.dianping.lion.EnvZooKeeperConfig;
import com.dianping.lion.client.ConfigCache;
import com.dianping.lion.client.LionException;
import com.dp.bigdata.taurus.restlet.resource.IHostTaskExecTime;
import com.dp.bigdata.taurus.restlet.resource.IUserTasks;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.restlet.data.MediaType;
import org.restlet.resource.ClientResource;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by kirinli on 15/2/3.
 */
public class HostHistoryServlet extends HttpServlet {
    private String RESTLET_URL_BASE;
    private static final Log LOG = LogFactory.getLog(HostHistoryServlet.class);

    private static final String HOST_HISTORY = "host_history";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext context = getServletContext();
        try {
            RESTLET_URL_BASE = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.web.restlet.url");
        } catch (LionException e) {
            RESTLET_URL_BASE = context.getInitParameter("RESTLET_SERVER");
            e.printStackTrace();
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

        if (HOST_HISTORY.equals(action)){
            OutputStream output = response.getOutputStream();
            String ip = request.getParameter("ip");
            String time = request.getParameter("time");
            cr = new ClientResource(RESTLET_URL_BASE + "runningMap/" + time + "/" + ip);
            IHostTaskExecTime hostTaskExecTime = cr.wrap(IHostTaskExecTime.class);
            cr.accept(MediaType.APPLICATION_XML);
            String jsonString = hostTaskExecTime.retrieve();
            if (StringUtils.isBlank(jsonString)){
                output.write("[]".getBytes());
            }else {
                output.write(jsonString.getBytes());
            }

            output.close();
        }
    }
}
