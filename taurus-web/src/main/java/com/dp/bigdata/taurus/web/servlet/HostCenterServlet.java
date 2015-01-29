package com.dp.bigdata.taurus.web.servlet;

import com.dianping.cat.Cat;
import com.dianping.lion.EnvZooKeeperConfig;
import com.dianping.lion.client.ConfigCache;
import com.dianping.lion.client.LionException;
import com.dp.bigdata.taurus.restlet.resource.IHostsResource;
import com.dp.bigdata.taurus.restlet.shared.HostDTO;
import com.dp.bigdata.taurus.web.utils.ReFlashHostLoadTask;
import com.dp.bigdata.taurus.web.utils.ReFlashHostLoadTaskTimer;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import java.util.ArrayList;

/**
 * Created by kirinli on 15/1/28.
 */
public class HostCenterServlet  extends HttpServlet {
    private String RESTLET_URL_BASE;
    private String AGENT_PORT;

    private static final String HOST = "host";
    private static final String HOST_LOAD = "hostload";
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                config.getServletContext());
        ReFlashHostLoadTaskTimer.getReFlashHostLoadManager().start();
        ServletContext context = getServletContext();
        try {
            RESTLET_URL_BASE = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.web.restlet.url");
            AGENT_PORT = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.agent.restlet.port");
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
        ClientResource cr = new ClientResource(RESTLET_URL_BASE + "host");
        IHostsResource hostsResource = cr.wrap(IHostsResource.class);
        cr.accept(MediaType.APPLICATION_XML);
        ArrayList<HostDTO> hosts = hostsResource.retrieve();

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

            String jsonString = ReFlashHostLoadTask.hostLoadJsonData;

            if (StringUtils.isNotBlank(queryType) && "reflash".equals(queryType)) {
                jsonString = ReFlashHostLoadTask.read();
            }

            if (StringUtils.isBlank(jsonString)) {
                jsonString = ReFlashHostLoadTask.read();
            }

            output.write(jsonString.getBytes());
            output.close();
        }
    }
}
