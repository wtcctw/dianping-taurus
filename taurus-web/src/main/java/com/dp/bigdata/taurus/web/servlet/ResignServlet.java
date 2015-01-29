package com.dp.bigdata.taurus.web.servlet;

import com.dianping.cat.Cat;
import com.dianping.lion.EnvZooKeeperConfig;
import com.dianping.lion.client.ConfigCache;
import com.dianping.lion.client.LionException;
import com.dp.bigdata.taurus.restlet.resource.IClearDependencyPassTask;
import com.dp.bigdata.taurus.restlet.resource.IGetUserId;
import com.dp.bigdata.taurus.restlet.resource.IUpdateAlertRule;
import com.dp.bigdata.taurus.web.utils.ReFlashHostLoadTaskTimer;
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

/**
 * Created by kirinli on 15/1/28.
 */
public class ResignServlet extends HttpServlet {
    private static final Log LOGGER = LogFactory.getLog(ResignServlet.class);
    private static final String RESIGN = "resign";
    private String RESTLET_URL_BASE;

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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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


            cr = new ClientResource(RESTLET_URL_BASE + "updatecreator/" + creator.trim() + "/" + taskName.trim() + "/resign");
            IClearDependencyPassTask clearTasks = cr.wrap(IClearDependencyPassTask.class);
            cr.accept(MediaType.APPLICATION_XML);
            int result = clearTasks.retrieve();


            cr = new ClientResource(RESTLET_URL_BASE + "getuserid/" + creator.trim());
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
                case STATUS_IS_NOT_RIGHT:
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

                        if (tmpUserId.indexOf(creator) > -1) {
                            isHaveAlert = true;
                        }

                        String[] tmpUsers = tmpUserId.split(";");
                        String newUserId = "";
                        if (isHaveAlert) {
                            for (int j = 0; j < tmpUsers.length; j++) {
                                String user = tmpUsers[j];
                                cr = new ClientResource(RESTLET_URL_BASE + "getuserid/" + user.trim());
                                getUserId = cr.wrap(IGetUserId.class);
                                cr.accept(MediaType.APPLICATION_XML);
                                int userIdAlert = getUserId.retrieve();
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
                        } else {
                            for (int j = 0; j < tmpUsers.length; j++) {
                                String user = tmpUsers[j];

                                cr = new ClientResource(RESTLET_URL_BASE + "getuserid/" + user.trim());
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

                        cr = new ClientResource(RESTLET_URL_BASE + "updatealert/" + newUserId.trim() + "/" + tmpJobId.trim() + "");
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
                    LOGGER.info(logInfo);
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
