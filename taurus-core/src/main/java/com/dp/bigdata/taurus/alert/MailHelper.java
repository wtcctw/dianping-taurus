/**
 * @author renyuan.sun
 * @version 2011-3-2 
 */

package com.dp.bigdata.taurus.alert;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import com.dp.bigdata.taurus.utils.RestCallUtils;

public class MailHelper {
	
	private final static String mailUrl = "http://web.paas.dp/mail/send";
	
	public static void sendMail(MailInfo mailInfo) {
		FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
    	formDataMultiPart.field("title", mailInfo.getSubject());
    	formDataMultiPart.field("recipients", mailInfo.getTo());
    	formDataMultiPart.field("body", mailInfo.getContent());
    	
    	try {
			RestCallUtils.postRestCall(mailUrl, formDataMultiPart, String.class, 2000, 2000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
	}
	/**
	* send email
	* @throws javax.mail.MessagingException
	* @throws Exception
	*/
	public static void sendMailWith51ping(MailInfo mailInfo) throws MessagingException{
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

    public static void sendMail(String to, String content, String subject) throws MessagingException {
        MailInfo mail = new MailInfo();
        mail.setTo(to);
        mail.setContent(content);
        mail.setFormat("text/html");
        mail.setSubject(subject);
        MailHelper.sendMail(mail);
    }
}

   
