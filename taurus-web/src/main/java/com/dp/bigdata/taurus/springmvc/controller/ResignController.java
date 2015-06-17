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

import com.dp.bigdata.taurus.restlet.resource.IGetUserId;
import com.dp.bigdata.taurus.restlet.resource.IUpdateAlertRule;
import com.dp.bigdata.taurus.restlet.resource.IUpdateCreator;

@Controller
public class ResignController {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final String RESIGN = "resign";
    private final int SERVICE_EXCEPTION = -1;
    private final int TASKID_IS_NOT_FOUND = -2;
    private final int CREATOR_IS_NOT_RIGHT = -3;
	
	@RequestMapping(value = "/resign.do", method = RequestMethod.POST)
	public void resignDoPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		log.info("--------------init the resignDoPost------------");
		
		String action = request.getParameter("action");
        ClientResource cr;
        if (RESIGN.equals(action)) {
            OutputStream output = response.getOutputStream();
            String taskName = request.getParameter("taskName");
            String creator = request.getParameter("creator");
            String currentUser = request.getParameter("currentUser");
            String oldcreators = request.getParameter("oldcreators");
            String userId = request.getParameter("userId");
            String jobId = request.getParameter("jobId");
            String alertUser = request.getParameter("alertUser");

            String reusult_str = "";


            cr = new ClientResource(InitController.RESTLET_URL_BASE + "updatecreator/" + creator.trim() + "/" + taskName.trim() + "/resign");
            IUpdateCreator updateCreator = cr.wrap(IUpdateCreator.class);
            cr.accept(MediaType.APPLICATION_XML);
            int result = updateCreator.retrieve();
            /*IClearDependencyPassTask clearTasks = cr.wrap(IClearDependencyPassTask.class);
            cr.accept(MediaType.APPLICATION_XML);
            int result = clearTasks.retrieve();*/


            cr = new ClientResource(InitController.RESTLET_URL_BASE + "getuserid/" + creator.trim());
            IGetUserId getUserId = cr.wrap(IGetUserId.class);
            cr.accept(MediaType.APPLICATION_XML);
            int creatorId = getUserId.retrieve();


            switch (result) {
                case SERVICE_EXCEPTION:
                    reusult_str = "后台服务异常!";
                    break;
                case TASKID_IS_NOT_FOUND:
                    reusult_str = "taskName 不存在!";
                    break;
                case CREATOR_IS_NOT_RIGHT:
                    reusult_str = "creator 错误!";
                    break;
                default:
                    reusult_str = "执行成功~";
                    
                    //替换告警人
                    String[] tmpUserList = alertUser.split(","); //现有的alert user
                    String[] tmpJobIdList = jobId.split(",");
                    String[] oldCreatorsList = oldcreators.split(","); //之前的用户


                    for (int i = 0; i < tmpJobIdList.length; i++) {

                        String tmpJobId = tmpJobIdList[i];
                        String tmpUserId = tmpUserList[i];
                        String older = oldCreatorsList[i];

                        boolean isHaveAlert = false;

                        //告警人列表中包含作业原creator
                        if (tmpUserId.indexOf(creator) > -1) {
                            isHaveAlert = true;
                        }

                        String[] tmpUsers = tmpUserId.split(";");
                        String newUserId = "";
                        if (isHaveAlert) {
                            for (int j = 0; j < tmpUsers.length; j++) {
                                String user = tmpUsers[j];
                                cr = new ClientResource(InitController.RESTLET_URL_BASE + "getuserid/" + user.trim());
                                getUserId = cr.wrap(IGetUserId.class);
                                cr.accept(MediaType.APPLICATION_XML);
                                int userIdAlert = getUserId.retrieve();
                                
                                //告警人列表不包含作业原creator
                                if (j == tmpUsers.length - 1) {
                                    if (!user.equals(older)) {
                                        newUserId += userIdAlert;
                                    }
                                } else {
                                    if (!user.equals(older)) {
                                        newUserId += userIdAlert + ";";
                                    }
                                }


                            }
                        } else { // isHaveAlert == false , 告警人列表加上作业原creator
                            for (int j = 0; j < tmpUsers.length; j++) {
                                String user = tmpUsers[j];

                                cr = new ClientResource(InitController.RESTLET_URL_BASE + "getuserid/" + user.trim());
                                getUserId = cr.wrap(IGetUserId.class);
                                cr.accept(MediaType.APPLICATION_XML);

                                int userIdAlert = getUserId.retrieve();

                                if (j == tmpUsers.length - 1) {
                                    if (user.equals(older)) {
                                        newUserId += creatorId;
                                    } else {
                                        newUserId += userIdAlert;
                                    }
                                } else {
                                    if (user.equals(older)) {
                                        newUserId += creatorId + ";";
                                    } else {
                                        newUserId += userIdAlert + ";";
                                    }
                                }


                            }
                        }

                        cr = new ClientResource(InitController.RESTLET_URL_BASE + "updatealert/" + newUserId.trim() + "/" + tmpJobId.trim() + "");
                        IUpdateAlertRule updateAlertRule = cr.wrap(IUpdateAlertRule.class);
                        cr.accept(MediaType.APPLICATION_XML);
                        updateAlertRule.retrieve();

                    }

                    String clientIp = getIpAddr(request);
                    StringBuffer logInfo = new StringBuffer();
                    logInfo.append("####RESGIN OP #### IP:");
                    logInfo.append(clientIp);
                    logInfo.append(" 用户【");
                    logInfo.append(currentUser);
                    logInfo.append("】把任务名为：【 ");
                    logInfo.append(taskName);
                    logInfo.append("】的任务原对应调度人分别为【");
                    logInfo.append(oldcreators);
                    logInfo.append("】 都指派给了 【");
                    logInfo.append(creator);
                    logInfo.append("】");
                    log.info(logInfo.toString());
                    break;

            }


            output.write(reusult_str.getBytes());
            output.close();


        }
	}

	public String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }
}
