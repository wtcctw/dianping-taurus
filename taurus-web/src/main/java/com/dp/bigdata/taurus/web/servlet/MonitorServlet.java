package com.dp.bigdata.taurus.web.servlet;

import com.dianping.lion.EnvZooKeeperConfig;
import com.dianping.lion.client.ConfigCache;
import com.dianping.lion.client.LionException;
import com.dp.bigdata.taurus.generated.mapper.TaskAttemptMapper;
import com.dp.bigdata.taurus.generated.mapper.TaskMapper;
import com.dp.bigdata.taurus.restlet.resource.IFailedTaskLoad;
import com.dp.bigdata.taurus.restlet.resource.IHostResource;
import com.dp.bigdata.taurus.restlet.resource.IHostsResource;
import com.dp.bigdata.taurus.restlet.resource.ITotalTaskLoad;
import com.dp.bigdata.taurus.restlet.shared.HostDTO;
import com.dp.bigdata.taurus.web.utils.ZabbixUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.resource.ClientResource;
import org.springframework.beans.factory.annotation.Autowired;

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
 * Created by kirinli on 14-9-29.
 */
public class MonitorServlet extends HttpServlet {
    private String RESTLET_URL_BASE;
    private String AGENT_PORT;
    private static final String HOST = "host";
    private static final String TOTALTASKLOAD = "totaltaskload";
    private static final String RUNNINGTASKLOAD = "runningtaskload";
    private static final String FAILEDTASKLOAD = "failedtaskload";
    private static final String USERTASK = "usertask";
    private static final String HOSTLOAD = "hostload";


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext context = getServletContext();
        try {
            RESTLET_URL_BASE = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.web.restlet.url");// context.getInitParameter("RESTLET_SERVER");
            AGENT_PORT = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.agent.restlet.port");//context.getInitParameter("AGENT_SERVER_PORT");
        } catch (LionException e) {
            RESTLET_URL_BASE = context.getInitParameter("RESTLET_SERVER");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("MonitorServlet EERROR++++++++:" + e.getMessage());
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ClientResource cr = new ClientResource(RESTLET_URL_BASE + "host");
        IHostsResource hostsResource = cr.wrap(IHostsResource.class);
        cr.accept(MediaType.APPLICATION_XML);
        ArrayList<HostDTO> hosts = hostsResource.retrieve();
        String action = request.getParameter("action");
        if (HOST.equals(action)) {
            String type = request.getParameter("gettype");
            int onLineNums = 0;
            int offLineNums = 0;
            int exceptionNums = 0;
            String onLineHosts = "";
            String offLineHosts = "";
            String exceptionHosts = "";
            String hostInfo = "";
            OutputStream output = response.getOutputStream();

            for (HostDTO dto : hosts) {
                cr = new ClientResource(RESTLET_URL_BASE + "host/" + dto.getName());
                IHostResource hostResource = cr.wrap(IHostResource.class);
                cr.accept(MediaType.APPLICATION_XML);
                HostDTO dtos = hostResource.retrieve();

                if (!dto.isOnline()) {
                    offLineNums++;
                    offLineHosts += dto.getIp() + ",";
                } else if (dto.isConnected()) {
                    onLineNums++;
                    onLineHosts += dto.getIp() + ",";
                } else {
                    exceptionNums++;
                    exceptionHosts += dto.getIp() + ",";
                }

            }
            if ("online".equals(type)) {
                onLineHosts = onLineHosts.substring(0, onLineHosts.length() - 1);

                output.write(onLineHosts.getBytes());
                output.close();
            } else if ("offline".equals(type)) {
                offLineHosts = offLineHosts.substring(0, offLineHosts.length() - 1);

                output.write(offLineHosts.getBytes());
                output.close();
            } else if ("exception".equals(type)) {
                exceptionHosts = exceptionHosts.substring(0, exceptionHosts.length() - 1);

                output.write(exceptionHosts.getBytes());
                output.close();
            } else {
                if (onLineHosts.length() > 0) {
                    onLineHosts = onLineHosts.substring(0, onLineHosts.length() - 1);
                } else {
                    onLineHosts = "NULL";
                }

                if (exceptionHosts.length() > 0) {
                    exceptionHosts = exceptionHosts.substring(0, exceptionHosts.length() - 1);
                } else {
                    exceptionHosts = "NULL";
                }

                String allInfo = onLineHosts + "#" + exceptionHosts;
                output.write(allInfo.getBytes());
                output.close();

            }
        } else if (TOTALTASKLOAD.equals(action)) {
            OutputStream output = response.getOutputStream();
            String start = request.getParameter("start");
            String end = request.getParameter("end");
            String apiUrl = RESTLET_URL_BASE + "totaltaskload/" + start + "/" + end;
            ClientResource crToal = new ClientResource(apiUrl);
            ITotalTaskLoad totalTaskLoad = crToal.wrap(ITotalTaskLoad.class);

            String jsonString = totalTaskLoad.retrieve();
            output.write(jsonString.getBytes());
            output.close();

        } else if (FAILEDTASKLOAD.equals(action)) {
            OutputStream output = response.getOutputStream();
            String start = request.getParameter("start");
            String end = request.getParameter("end");
            String apiUrl = RESTLET_URL_BASE + "failedtaskload/" + start + "/" + end;
            ClientResource crFail = new ClientResource(apiUrl);
            IFailedTaskLoad failedTaskLoad = crFail.wrap(IFailedTaskLoad.class);

            String jsonString = failedTaskLoad.retrieve();
            output.write(jsonString.getBytes());
            output.close();

        }else if (HOSTLOAD.equals(action)) {
            String jsonData = ZabbixUtil.getHosts();
            JsonArray hostLoadJsonData = new JsonArray();
            OutputStream output = response.getOutputStream();

           try {
                JSONArray hostJson = new JSONArray(jsonData);
                int len = hostJson.length();

                for (int i = 0 ; i < len; i++){
                    JSONObject jsonObject = (JSONObject) hostJson.get(i);
                    String hostId = jsonObject.getString("hostid");
                    String name = jsonObject.getString("name");
                    String cpuLoad = ZabbixUtil.getCpuLoadInfo(hostId) ;
                    String memeryLoad = ZabbixUtil.getMemeryLoadInfo(hostId);
                    if (memeryLoad != null){
                        Float memLoadFloat = Float.parseFloat(memeryLoad);
                        memeryLoad= (int)(memLoadFloat / (1024*1024)) +"MB";
                    }else{
                        memeryLoad ="异常数据";
                    }

                    JsonObject hostLoadJson = new JsonObject();
                    hostLoadJson.addProperty("hostId",hostId);
                    hostLoadJson.addProperty("hostName",name);
                    hostLoadJson.addProperty("cpuLoad",cpuLoad);
                    hostLoadJson.addProperty("memLoad",memeryLoad);
                    hostLoadJsonData.add(hostLoadJson);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            String jsonString = hostLoadJsonData.toString();
            output.write(jsonString.getBytes());
            output.close();
        }else if (USERTASK.equals(action)){

        }
    }
}
