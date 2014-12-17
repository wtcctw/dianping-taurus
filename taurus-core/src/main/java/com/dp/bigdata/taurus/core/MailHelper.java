/**
 * @author renyuan.sun
 * @version 2011-3-2 
 */

package com.dp.bigdata.taurus.core;


import com.dianping.lion.EnvZooKeeperConfig;
import com.dianping.lion.client.ConfigCache;
import com.dianping.lion.client.LionException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;


public class MailHelper{
	/**
	* send email
	* @throws javax.mail.MessagingException
	* @throws Exception
	*/
	public static void sendMail(MailInfo mailInfo) throws MessagingException{
		Properties props = new Properties();
		props.put("mail.smtp.host", mailInfo.getHost());
		props.put("mail.smtp.auth", "true");
		if(mailInfo.getHost().indexOf("smtp.gmail.com") >= 0)	//Google SMTP server use 465 or 587 port
		{
			props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.setProperty("mail.smtp.socketFactory.fallback", "false");
			props.setProperty("mail.smtp.port", "465");
			props.setProperty("mail.smtp.socketFactory.port", "465");
		}
		Session mailSession = Session.getDefaultInstance(props);
		
		Message message = new MimeMessage(mailSession);
		message.setFrom(new InternetAddress(mailInfo.getFrom()));
		String []receivers = mailInfo.getTo().split(",");
		for(String oneReceiver:receivers){
			message.addRecipient(Message.RecipientType.TO,new InternetAddress(oneReceiver));// add receivers
		}
		message.setSubject(mailInfo.getSubject());	
		String display = mailInfo.getFormat() + ";charset=" + mailInfo.getCharset();
		if(mailInfo.getFormat().equals("")||mailInfo.getCharset().equals(""))
			message.setText(mailInfo.getContent());				//set mail text
		else
			message.setContent(mailInfo.getContent(),display);	// set mail text and charset
		
		message.saveChanges();
		
		Transport transport=mailSession.getTransport("smtp");
		transport.connect(mailInfo.getUser(),mailInfo.getPassword());
		transport.sendMessage(message,message.getAllRecipients());
		transport.close();
	}
    public static void sendMail(String to, String content) throws MessagingException {
        MailInfo mail = new MailInfo();
        mail.setTo(to);
        mail.setContent(content);
        mail.setFormat("text/html");
        mail.setSubject("Taurus-Agent主机失联系告警服务");
        MailHelper.sendMail(mail);
    }
    public static void sendWeChat(String user,String content){
        String wechat_url = "";
        try {
            wechat_url = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.wechat.api");
        } catch (LionException e) {
            e.printStackTrace();
            wechat_url = "http://10.101.2.28:8080";
        }

        String wechat_api = wechat_url+ "/api";

        String params = "action=push&sysName=ezc&keyword=" + user.trim()
                + "&title=Taurus-Agent主机失联系告警服务&content= Taurus-Agent主机失联系告警服务\n" + content.trim();

        sendPost(wechat_api, params);


    }

    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }
}

   
