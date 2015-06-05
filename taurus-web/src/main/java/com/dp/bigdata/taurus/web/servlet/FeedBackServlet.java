package com.dp.bigdata.taurus.web.servlet;

import com.dianping.lion.EnvZooKeeperConfig;
import com.dianping.lion.client.ConfigCache;
import com.dianping.lion.client.LionException;
import com.dp.bigdata.taurus.alert.MailHelper;
import com.dp.bigdata.taurus.alert.MailInfo;
import com.dp.bigdata.taurus.alert.WeChatHelper;
import com.dp.bigdata.taurus.web.utils.ReFlashHostLoadTaskTimer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.mail.MessagingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by kirinli on 14/12/9.
 */
public class FeedBackServlet  extends HttpServlet {
    private static final Log LOGGER = LogFactory.getLog(FeedBackServlet.class);
    private static final String FEEDBACK = "feedback";
    private static final String FEEDERROR = "feederror";



    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");
        if (FEEDBACK.equals(action)) {

            OutputStream output = response.getOutputStream();

            String user = request.getParameter("user");
            String feedback = request.getParameter("feedback");
            String content = "<h4><p>Hi,"
                            + user
                            + ":</p></h4><p>你提交的反馈:</p><br><p style=\"color:#08c\">"
                            + feedback
                            + "</p> <p>反馈信息我们已经收到，我们会及时处理，谢谢你的支持！<p>"
                            + " <p style=\"text-align:right\">❃ 点评工具组 ❃</p>";
            String wccontent = "Hi,"
                    + user
                    + ":你提交的反馈:"
                    + feedback
                    + "反馈信息我们已经收到，我们会及时处理，谢谢你的支持！"
                    + " ❃ 点评工具组 ❃";
            String to="";
            try {
                to = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.feedback.mail.to");
            } catch (LionException e) {
                e.printStackTrace();
                to = "kirin.li@dianping.com";
            }
            to += ","+ user +"@dianping.com";

            try {
                MailHelper.sendMail(to,"Taurus反馈服务",content);
                WeChatHelper.sendWeChat("kirin.li",wccontent);
            } catch (MessagingException e) {
                output.write("error".getBytes());
                output.close();
            }

            output.write("success".getBytes());
            output.close();

            }else if (FEEDERROR.equals(action)){
            OutputStream output = response.getOutputStream();

            String user = request.getParameter("user");
            String attemptId = request.getParameter("attemptId");
            String taskId = request.getParameter("taskId");
            String taskName = request.getParameter("taskName");
            String status = request.getParameter("status");
            String ip = request.getParameter("ip");
            String mailTo = request.getParameter("mailTo");
            String feedType = request.getParameter("feedtype");

            String domain ="";
            try {
                domain = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.web.deploy.weburl");
            } catch (LionException e) {
                domain="taurus.dp";
                e.printStackTrace();
            }
String logUrl =  "http://"
        + domain
        + "/mvc/viewlog?id="
        + attemptId;

            if(feedType.equals("mail")){
                String content = "<h4><p>Hi,"
                        + mailTo
                        + ":</p></h4><p>我的任务出现了问题，需要你们的帮助！</p><br><p style=\"color:#08c\">"
                        + "<h4><p>任务名："
                        + taskName
                        +"</p></h4>"
                        + "<h4><p>任务ID："
                        + taskId
                        +"</p></h4>"
                        + "<h4><p>任务状态："
                        + status
                        +"</p></h4>"
                        + "<h4><p>部署主机："
                        + ip
                        +"</p></h4>"
                        + "<h4><p>任务日志："
                        + logUrl
                        +"</p></h4>"
                        +"<a href="
                        + logUrl
                        +" target= 'blank'>查看详情</a>"
                        + " <p style=\"text-align:right\">"
                        + user
                        + "</p>";
                String wccontent = "Hi,"
                        + mailTo
                        + ":我的任务出现了问题，需要你们的帮助！"
                        + "\n任务名："
                        + taskName
                        + "\n任务ID："
                        + taskId
                        + "\n任务状态："
                        + status
                        + "\n部署主机："
                        + ip
                        + "\n任务日志："
                        + logUrl
                        +"<a href="
                        + logUrl
                        +" target= 'blank'>查看详情</a>";

                String to ="";
                String[] toList = mailTo.split(",");

                for (int i = 0; i < toList.length; i ++){
                    if (i == toList.length - 1){
                        to += toList[i] +"@dianping.com";
                    }else {
                        to += toList[i] +"@dianping.com ,";
                    }
                }

                String reply = "<h4><p>Hi,"
                        + user
                        + ":</p></h4><p>你提交的报错:</p><br><p style=\"color:#08c\">"
                        + "<h4><p>任务名："
                        + taskName
                        +"</p></h4>"
                        + "<h4><p>任务状态："
                        + status
                        +"</p></h4>"
                        + "<h4><p>部署主机："
                        + ip
                        +"</p></h4>"
                        + "<h4><p>任务日志："
                        + logUrl
                        +"</p></h4>"
                        + " <p>我们已经收到，我们会及时处理，谢谢你的支持！<p>"
                        + " <p style=\"text-align:right\">❃ 点评工具组 ❃</p>";
                String wcreply = "Hi,"
                        + user
                        + ":\n你提交的报错:\n"
                        + "任务名："
                        + taskName
                        +"\n"
                        + "任务状态："
                        + status
                        +"\n"
                        + "部署主机："
                        + ip
                        +"\n"
                        + "任务日志："
                        + logUrl
                        +"\n"
                        + "我们已经收到，我们会及时处理，谢谢你的支持"
                        + "\n❃ 点评工具组 ❃";
                String replyTo= user + "@dianping.com";

                try {


                    for (int i = 0; i < toList.length; i ++){
                        WeChatHelper.sendWeChat(toList[i],wccontent);
                    }

                    WeChatHelper.sendWeChat(user,wcreply);

                    MailHelper.sendMail(to,"Taurus报错服务",content);
                    MailHelper.sendMail(replyTo,"Taurus报错服务",reply);
                } catch (MessagingException e) {
                    output.write("error".getBytes());
                    output.close();
                }
            }else{
                String wccontent = "※Taurus 任务微信报错服务※"
                        +"\n"
                        +"-------------------------------"
                        +"\n"
                        +"Hi,"
                        + mailTo
                        + ":你的任务出现了问题，希望能及时处理"
                        + "\n任务名："
                        + taskName
                        + "\n任务ID："
                        + taskId
                        + "\n任务状态："
                        + status
                        + "\n部署主机："
                        + ip
                        +"\n"
                        +"<a href='"
                        + logUrl
                        +"'>查看详情</a>"
                        +"\n"
                        +"-------------------------------"
                        +"※点评工具组※";
                String[] toList = mailTo.split(",");
                for (int i = 0; i < toList.length; i ++){
                    WeChatHelper.sendWeChat(toList[i],wccontent);
                }
            }


            output.write("success".getBytes());
            output.close();
        }
        }
}
