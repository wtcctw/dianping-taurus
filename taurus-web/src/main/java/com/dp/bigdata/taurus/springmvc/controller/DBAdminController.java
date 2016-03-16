package com.dp.bigdata.taurus.springmvc.controller;

import com.dp.bigdata.taurus.alert.TaurusAlert;
import com.dp.bigdata.taurus.core.AttemptStatusMonitor;
import com.dp.bigdata.taurus.core.Engine;
import com.dp.bigdata.taurus.restlet.utils.LionConfigUtil;
import com.dp.bigdata.taurus.zookeeper.common.infochannel.ZooKeeperCleaner;
import org.restlet.resource.ClientResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Controller
public class DBAdminController {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final String SQL_QUERY = "sqlquery";
    private final String UPDATE_CREATOR = "updatecreator";
    private final String CLEAR_ZOOKEEPER_NODES = "clearzknodes";

    private final int SERVICE_EXCEPTION = -1;
    private final int TASKID_IS_NOT_FOUND = -2;
    private final int STATUS_IS_NOT_RIGHT = -3;
    
    @Autowired
    public Engine engine;
    @Autowired
    public TaurusAlert alert;
    @Autowired
    public AttemptStatusMonitor statusMonitor;
	
//    @RequestMapping(value = "/dbadmin/changeServer", method = RequestMethod.POST)
//    @ResponseBody
//    public WebResult changeServer(HttpServletRequest request, HttpServletResponse response) throws InterruptedException{
//    	log.info("--------------init the dbadmin/changeServer------------");
//
//    	WebResult result = new WebResult(request);
//    	String adminUser = InitController.ADMIN_USER;
//    	String user = (String) request.getSession().getAttribute("taurus-user");
//
//    	if (adminUser.contains(user)){
//
//        	if(LionConfigUtil.loadServerConf()){
//
//        		//设置为master server并启动调度
//            	if(LionConfigUtil.SERVER_MASTER_IP.equals(IPUtils.getFirstNoLoopbackIP4Address())){
//            		engine.load();
//            		alert.load();
//            		alert.isInterrupt(false);
//            		engine.isInterrupt(false);
//
//            		if(ClearLogsTimerManager.getClearLogsTimerManager().getTimer() == null){
//            			ClearLogsTimerManager.getClearLogsTimerManager().start();
//            		}
//
//            		if(MonitorAgentOffLineTaskTimer.getMonitorAgentOffLineTimeManager().getTimer() == null){
//            			MonitorAgentOffLineTaskTimer.getMonitorAgentOffLineTimeManager().start();
//            		}
//
//            		if(ReFlashHostLoadTaskTimer.getReFlashHostLoadManager().getTimer() == null){
//            			ReFlashHostLoadTaskTimer.getReFlashHostLoadManager().start();
//            		}
//
//            		result.setMessage("Set localhost(" + LionConfigUtil.SERVER_MASTER_IP + ") master server");
//            	}else{//关闭调度并指向master server的restlet
//            		alert.isInterrupt(true);
//            		engine.isInterrupt(true);
//
//            		if(ClearLogsTimerManager.getClearLogsTimerManager().getTimer() != null){
//            			ClearLogsTimerManager.getClearLogsTimerManager().stop();
//            		}
//
//            		if(MonitorAgentOffLineTaskTimer.getMonitorAgentOffLineTimeManager().getTimer() != null){
//            			MonitorAgentOffLineTaskTimer.getMonitorAgentOffLineTimeManager().stop();
//            		}
//
//            		if(ReFlashHostLoadTaskTimer.getReFlashHostLoadManager().getTimer() != null){
//            			ReFlashHostLoadTaskTimer.getReFlashHostLoadManager().stop();
//            		}
//
//            		while(!(engine.isTriggleThreadRestFlag()
//            				&& statusMonitor.isAttemptStatusMonitorRestFlag())){
//            			//wait the engine to finish last schedule
//            		}
//
//            		result.setMessage("Change master server to " + LionConfigUtil.SERVER_MASTER_IP);
//            	}
//
//        	}else{
//        		result.setMessage("Error!!cannot read from lion server.");
//        	}
//
//    	}else{
//    		result.setMessage("You are not admin user.");
//    	}
//
//		return result;
//    }
    
    
	@RequestMapping(value = "/db_admin.do", method = RequestMethod.POST)
	public void dbAdminDoPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		log.info("--------------init the dbAdminDoPost------------");
		
		String action = request.getParameter("action");
        ClientResource cr;
        if (SQL_QUERY.equals(action)) {
            String user = (String) request.getSession().getAttribute("taurus-user");
            String adminUser = InitController.ADMIN_USER;
            OutputStream output = response.getOutputStream();
            String reusult_str = "";

            if (adminUser.contains(user)) {
                String taskId = request.getParameter("taskId");
                String status = request.getParameter("status");

                cr = new ClientResource(LionConfigUtil.RESTLET_API_BASE + "deletedependency/" + taskId + "/" + status);
                int result = cr.get(int.class);

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
            String adminUser = InitController.ADMIN_USER;
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
            String adminUser = InitController.ADMIN_USER;
            String reusult_str = "";

            if (adminUser.contains(user)) {

                cr = new ClientResource(LionConfigUtil.RESTLET_API_BASE + "updatecreator/" + creator + "/" + taskName + "/update");
                int result = cr.get(int.class);

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
