package com.dp.bigdata.taurus.web.servlet;

import com.dianping.cat.Cat;
import com.dianping.lion.EnvZooKeeperConfig;
import com.dianping.lion.client.ConfigCache;
import com.dianping.lion.client.LionException;
import com.dp.bigdata.taurus.core.MultiInstanceFilter;
import com.dp.bigdata.taurus.restlet.resource.IClearDependencyPassTask;
import com.dp.bigdata.taurus.web.utils.ReFlashHostLoadTaskTimer;
import com.dp.bigdata.taurus.zookeeper.common.infochannel.ZooKeeperCleaner;
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
public class DBAdminServlet  extends HttpServlet {
    private String RESTLET_URL_BASE;

    private static final String SQL_QUERY = "sqlquery";
    private static final String UPDATE_CREATOR = "updatecreator";
    private static final String CLEAR_ZOOKEEPER_NODES = "clearzknodes";

    private static final int SERVICE_EXCEPTION = -1;
    private static final int TASKID_IS_NOT_FOUND = -2;
    private static final int STATUS_IS_NOT_RIGHT = -3;
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
        if (SQL_QUERY.equals(action)) {
            String user = (String) request.getSession().getAttribute("taurus-user");
            String adminUser;
            try {
                adminUser = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.dbadmin.user");
            } catch (LionException e) {
                adminUser = "kirin.li";
            }
            OutputStream output = response.getOutputStream();
            String reusult_str = "";

            if (adminUser.contains(user)) {
                String taskId = request.getParameter("taskId");
                String status = request.getParameter("status");

                cr = new ClientResource(RESTLET_URL_BASE + "deletedependency/" + taskId + "/" + status);
                IClearDependencyPassTask clearTasks = cr.wrap(IClearDependencyPassTask.class);
                cr.accept(MediaType.APPLICATION_XML);
                int result = clearTasks.retrieve();

                switch (result) {
                    case SERVICE_EXCEPTION:
                        reusult_str = "后台服务异常!";
                        break;
                    case TASKID_IS_NOT_FOUND:
                        reusult_str = "taskId 不存在!";
                        break;
                    case STATUS_IS_NOT_RIGHT:
                        reusult_str = "status 错误!";
                        break;
                    default:
                        reusult_str = "执行成功~";
                        MultiInstanceFilter.jobAlert.remove(taskId.trim());
                        break;

                }
            } else {
                reusult_str = "无权限执行操作!";
            }


            output.write(reusult_str.getBytes());
            output.close();
        }else if (CLEAR_ZOOKEEPER_NODES.equals(action)) {
            OutputStream output = response.getOutputStream();
            String user = (String) request.getSession().getAttribute("taurus-user");
            String adminUser;
            try {
                adminUser = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.dbadmin.user");
            } catch (LionException e) {
                adminUser = "kirin.li";
            }
            String reusult_str = "";

            if (adminUser.contains(user)) {
                String start_str = request.getParameter("start");
                String end_str = request.getParameter("end");

                try {
                    int start = Integer.parseInt(start_str);
                    int end = Integer.parseInt(end_str);

                    ZooKeeperCleaner.clearNodes(start, end);
                    reusult_str = "清理成功！";
                } catch (Exception e) {
                    output.write("failed".getBytes());
                    output.close();
                }
            } else {
                reusult_str = "无权限执行操作!";
            }
            output.write(reusult_str.getBytes());
            output.close();

        } else if (UPDATE_CREATOR.equals(action)) {
            OutputStream output = response.getOutputStream();
            String taskName = request.getParameter("taskName");
            String creator = request.getParameter("creator");

            String user = (String) request.getSession().getAttribute("taurus-user");
            String adminUser;
            try {
                adminUser = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.dbadmin.user");
            } catch (LionException e) {
                adminUser = "kirin.li";
            }
            String reusult_str = "";

            if (adminUser.contains(user)) {

                cr = new ClientResource(RESTLET_URL_BASE + "updatecreator/" + creator + "/" + taskName + "/update");
                IClearDependencyPassTask clearTasks = cr.wrap(IClearDependencyPassTask.class);
                cr.accept(MediaType.APPLICATION_XML);
                int result = clearTasks.retrieve();

                switch (result) {
                    case SERVICE_EXCEPTION:
                        reusult_str = "后台服务异常!";
                        break;
                    case TASKID_IS_NOT_FOUND:
                        reusult_str = "taskName 不存在!";
                        break;
                    case STATUS_IS_NOT_RIGHT:
                        reusult_str = "creator 错误!";
                        break;
                    default:
                        reusult_str = "执行成功~";
                        break;

                }
            } else {
                reusult_str = "无权限执行操作!";
            }


            output.write(reusult_str.getBytes());
            output.close();


        }

    }
}
