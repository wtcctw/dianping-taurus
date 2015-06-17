package com.dp.bigdata.taurus.springmvc.controller;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.restlet.data.MediaType;
import org.restlet.resource.ClientResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dianping.lion.EnvZooKeeperConfig;
import com.dianping.lion.client.ConfigCache;
import com.dianping.lion.client.LionException;
import com.dp.bigdata.taurus.core.MultiInstanceFilter;
import com.dp.bigdata.taurus.restlet.resource.IClearDependencyPassTask;
import com.dp.bigdata.taurus.zookeeper.common.infochannel.ZooKeeperCleaner;

@Controller
public class DBAdminController {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final String SQL_QUERY = "sqlquery";
    private final String UPDATE_CREATOR = "updatecreator";
    private final String CLEAR_ZOOKEEPER_NODES = "clearzknodes";

    private final int SERVICE_EXCEPTION = -1;
    private final int TASKID_IS_NOT_FOUND = -2;
    private final int STATUS_IS_NOT_RIGHT = -3;
	
	@RequestMapping(value = "/db_admin.do", method = RequestMethod.POST)
	public void dbAdminDoPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		log.info("--------------init the dbAdminDoPost------------");
		
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

                cr = new ClientResource(InitController.RESTLET_URL_BASE + "deletedependency/" + taskId + "/" + status);
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

                cr = new ClientResource(InitController.RESTLET_URL_BASE + "updatecreator/" + creator + "/" + taskName + "/update");
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
