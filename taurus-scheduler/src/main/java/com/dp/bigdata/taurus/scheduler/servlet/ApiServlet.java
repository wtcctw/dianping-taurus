package com.dp.bigdata.taurus.scheduler.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.dianping.ba.es.qyweixin.adapter.api.dto.MessageDto;
import com.dianping.ba.es.qyweixin.adapter.api.dto.media.TextDto;
import com.dianping.ba.es.qyweixin.adapter.api.exception.QyWeixinAdaperException;
import com.dianping.ba.es.qyweixin.adapter.api.service.MessageService;
import com.dianping.ba.hris.md.api.dto.EmployeeDto;
import com.dianping.ba.hris.md.api.service.EmployeeService;
import com.dianping.mailremote.remote.MailService;
import com.dianping.pigeon.remoting.ServiceFactory;
import com.dp.bigdata.taurus.scheduler.lion.ConfigHolder;
import com.dp.bigdata.taurus.scheduler.lion.LionKeys;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ApiServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5414595942703057721L;

    private Logger log = LogManager.getLogger();
	
	private static final String PUSH = "push";
	private static final String MAIL = "mail";
	private static final int maxRecipientsNum = 15;
	
    private static EmployeeService employeeService;
    private static MessageService messageService;
    private static MailService mailService;
	
	@Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        employeeService = ServiceFactory.getService("http://service.dianping.com/ba/hris/masterdata/EmployeeService_1.0.0",EmployeeService.class , 5000);
        messageService = ServiceFactory.getService("http://service.dianping.com/ba/es/qyweixin/adapter/MessageService_1.0.0", MessageService.class, 5000);
        mailService = ServiceFactory.getService("http://service.dianping.com/mailService/mailService_1.0.0",MailService.class , 5000);
    }
	
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        OutputStream output = response.getOutputStream();
        JsonObject result = new JsonObject();
        if (PUSH.equals(action)) {

            String content = request.getParameter("content");
            String keyword = request.getParameter("keyword");
            String aIdStr = request.getParameter("agentid");
            int agentid = 12;
            try {
                agentid = Integer.parseInt(aIdStr);
            } catch (NumberFormatException e) {
                log.warn(aIdStr + " is not the valid number!");
                agentid = Integer.parseInt(ConfigHolder.get(LionKeys.MESSAGE_AGENTID));
            }

            boolean isBlack = isBlackList(keyword);

            if (isBlack) {
                result.addProperty("result", "Permission Denied!");
                result.addProperty("status", 403);
                output.write(result.toString().getBytes());
                output.close();
            } else {
        		
            	List<EmployeeDto> employeeDtos = employeeService.queryEmployeeByKeyword(keyword);
        		EmployeeDto employeeDto = null;
        		
        		if(employeeDtos != null && employeeDtos.size() == 1) {
        			employeeDto = employeeDtos.get(0);
        		} else {
        			result.addProperty("result", "Params Invalid! Can not found only user");
                    result.addProperty("status", 400);
                    output.write(result.toString().getBytes());
                    output.close();
                    return;
        		}
        		
        		String user = employeeDto.getEmployeeId();
        		List<String> users = new ArrayList<String>();
        		users.add(user);
        		
        		MessageDto messageDto = new MessageDto();
        		TextDto textDto = new TextDto();
                textDto.setContent(content);
                
                messageDto.setMediaDto(textDto);
                messageDto.setPriority(MessageDto.BATCH_MSG);
                messageDto.setTouser(users);
                messageDto.setSafe(0);
                messageDto.setAgentid(agentid);
                
                try {
        			messageService.sendMessage(messageDto);
        			result.addProperty("result", "send wechat message success!");
                    result.addProperty("status", 200);
                    
        		} catch (QyWeixinAdaperException e) {
        			e.printStackTrace();
        			result.addProperty("result", "send wechat message error");
                    result.addProperty("status", 400);
        		}
                
                output.write(result.toString().getBytes());
                output.close();

            }

        }else if(MAIL.equals(action)) {
        	String title = request.getParameter("title");
        	String recipients = request.getParameter("recipients");
        	String body = request.getParameter("body");
        	
        	if(StringUtils.isBlank(recipients) || StringUtils.isBlank(title)) {
        		result.addProperty("result", "Email or content cannot be blank!");
                result.addProperty("status", 403);
                output.write(result.toString().getBytes());
                output.close();
                return;
        	}
        	
        	Map<String, String> subPair = new HashMap<String, String>();
    		subPair.put("title", title);
    		subPair.put("body", body);
    		List<String> recipientList = new ArrayList<String>(Arrays.asList(recipients.split(",")));
    		boolean mailStatus = sendEmail(1500, recipientList, title, body, null);
    		
    		if(mailStatus) {
    			result.addProperty("result", "send email success!");
                result.addProperty("status", 200);
    		} else {
    			result.addProperty("result", "send email error");
                result.addProperty("status", 400);
    		}
    		
    		output.write(result.toString().getBytes());
            output.close();
    		
        } else {

            result.addProperty("result", "Permission Denied!");
            result.addProperty("status", 403);
            output.write(result.toString().getBytes());
            output.close();
        }


    }
    
    /**
	 * @author chenchongze
	 */
	private boolean sendEmail(int typeCode, List<String> recipients, String title, String body, String reEmail) {
		Map<String, String> subPair = new HashMap<String, String>();
		subPair.put("title", title);
		subPair.put("body", body);
		List<List<String>> recipientList = splitList(recipients, maxRecipientsNum);
		boolean mailStatus = false;
		for (int i = 0; i < recipientList.size(); i++) {
			List<String> list = new ArrayList<String>(recipientList.get(i));
			HashSet<String> h = new HashSet<String>(recipients);
			list.clear();
			list.addAll(h);
			mailStatus = mailService.send(typeCode, normalizeRecipients(list), subPair, reEmail);
			if (mailStatus == false) {
				return mailStatus;
			}
		}
		return mailStatus;
	}

	/**
	 * @author chenchongze
	 * @return
	 */
	private List<String> normalizeRecipients(List<String> origin) {
		List<String> l = new ArrayList<String>();
		for (String receiver : origin) {
			receiver = receiver.trim();
			if (receiver.length() > 0 && receiver.indexOf('@') > 0) {
				l.add(receiver);
			} else {
				System.out.println("Unrecognized mail receiver: " + receiver);
			}
		}
		return l;
	}

	/**
	 * @author chenchongze
	 * @param recipients
	 * @return
	 */
	private List<List<String>> splitList(List<String> recipients, int maxSize) {
		int total = recipients.size();
		int sendCount = total % maxSize == 0 ? total / maxSize : total / maxSize + 1;
		List<List<String>> result = new ArrayList<List<String>>(sendCount);
		for (int i = 0; i < sendCount; i++) {
			int start = i * maxRecipientsNum;
			int end = start + maxRecipientsNum > total ? total : start + maxRecipientsNum;
			List<String> subList = recipients.subList(start, end);
			result.add(subList);
		}
		return result;
	}

	public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
    
    public static boolean isBlackList(String keyWord) {
        boolean isblack = false;
        boolean isId = isInteger(keyWord);

        if (isId) {
            int id = Integer.parseInt(keyWord);
            String[] idLists = ConfigHolder.get(LionKeys.BLACK_LIST_ID).split(",");

            for (int i = 0; i < idLists.length; i++) {
                int tmpId = Integer.parseInt(idLists[i]);

                if (tmpId == id) {
                    isblack = true;
                    break;
                }
            }

            return isblack;
        } else {
            String[] nameLists = ConfigHolder.get(LionKeys.BLACK_LIST_NAME).split(",");


            for (int i = 0; i < nameLists.length; i++) {
                String tmpName = nameLists[i].trim();

                if (tmpName.equals(keyWord)) {
                    isblack = true;
                    break;
                }
            }

            if (isblack) {
                return isblack;
            } else {
                String[] emailLists = ConfigHolder.get(LionKeys.BLACK_LIST_EMAIL).split(",");
                for (int i = 0; i < emailLists.length; i++) {
                    String tmpEmail = emailLists[i].trim();

                    if (tmpEmail.equals(keyWord)) {
                        isblack = true;
                        break;
                    }
                }

                return isblack;

            }

        }
    }

}
