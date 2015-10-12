package com.dp.bigdata.taurus.scheduler.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dianping.ba.es.qyweixin.adapter.api.dto.MessageDto;
import com.dianping.ba.es.qyweixin.adapter.api.dto.media.TextDto;
import com.dianping.ba.es.qyweixin.adapter.api.exception.QyWeixinAdaperException;
import com.dianping.ba.es.qyweixin.adapter.api.service.MessageService;
import com.dianping.ba.hris.md.api.dto.EmployeeDto;
import com.dianping.ba.hris.md.api.service.EmployeeService;
import com.dianping.lion.EnvZooKeeperConfig;
import com.dianping.lion.client.ConfigCache;
import com.dianping.lion.client.LionException;
import com.dianping.pigeon.remoting.ServiceFactory;
import com.google.gson.JsonObject;

public class ApiServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5414595942703057721L;
	
	private static final String PUSH = "push";
	
    private static String BLACK_LIST_ID = "";
    private static String BLACK_LIST_NAME = "";
    private static String BLACK_LIST_EMAIL = "";
	
	@Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
        try {
            BLACK_LIST_ID = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("push-wechat-service.blacklist.id");
            BLACK_LIST_NAME = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("push-wechat-service.blacklist.name");
            BLACK_LIST_EMAIL = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("push-wechat-service.blacklist.email");
        } catch (LionException e) {
            e.printStackTrace();
            BLACK_LIST_ID = "0000001,0000033,0008232,0000569,0000028,0010637,0013490,0006739,0002356,0000024,0000006,0008077";
            BLACK_LIST_NAME = "tao.zhang,shihai.gong,jerry.huang,yueping.jiang,jason.li,elaine,daofeng.luo,lei.sun,ray.wang,shuhong.ye,bo.zhang,peter.zheng";
            BLACK_LIST_EMAIL = "tao.zhang@dianping.com,shihai.gong@dianping.com,jerry.huang@dianping.com,yueping.jiang@dianping.com,jason.li@dianping.com,elaine@dianping.com,daofeng.luo@dianping.com,lei.sun@dianping.com,ray.wang@dianping.com,shuhong.ye@dianping.com,bo.zhang@dianping.com,peter.zheng@dianping.com";
        }
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

            boolean isBlack = isBlackList(keyword);

            if (isBlack) {
                result.addProperty("result", "Permission Denied!");
                result.addProperty("status", 403);
                output.write(result.toString().getBytes());
                output.close();
            } else {
            	EmployeeService employeeService = ServiceFactory.getService("http://service.dianping.com/ba/hris/masterdata/EmployeeService_1.0.0",EmployeeService.class , 5000);
        		
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
                messageDto.setAgentid(15);
                
                MessageService messageService = ServiceFactory.getService("http://service.dianping.com/ba/es/qyweixin/adapter/MessageService_1.0.0", MessageService.class, 5000);
                
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

        }  else {

            result.addProperty("result", "Permission Denied!");
            result.addProperty("status", 403);
            output.write(result.toString().getBytes());
            output.close();
        }


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
            String[] idLists = BLACK_LIST_ID.split(",");

            for (int i = 0; i < idLists.length; i++) {
                int tmpId = Integer.parseInt(idLists[i]);

                if (tmpId == id) {
                    isblack = true;
                    break;
                }
            }

            return isblack;
        } else {
            String[] nameLists = BLACK_LIST_NAME.split(",");


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
                String[] emailLists = BLACK_LIST_EMAIL.split(",");
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
