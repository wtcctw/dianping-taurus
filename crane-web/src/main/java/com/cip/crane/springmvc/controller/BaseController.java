package com.cip.crane.springmvc.controller;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.restlet.resource.ClientResource;
import org.springframework.ui.ModelMap;

import com.dianping.lion.client.Lion;
import com.cip.crane.common.lion.ConfigHolder;
import com.cip.crane.common.lion.LionKeys;
import com.cip.crane.restlet.shared.UserDTO;
import com.cip.crane.restlet.utils.LionConfigUtil;
import com.cip.crane.springmvc.utils.GlobalViewVariable;

/**
 * @author chenchongze
 *
 */
public abstract class BaseController {

	/**
	 * 重构jsp/common-nav.jsp
	 * @param modelMap
	 * @param request
	 */
	public void commonnav(HttpServletRequest request,GlobalViewVariable globalViewVariable){
		
		globalViewVariable.currentUser = (String) request.getSession().getAttribute(InitController.USER_NAME);
		globalViewVariable.userId = -1;
		
		globalViewVariable.host = LionConfigUtil.RESTLET_API_BASE;
		
		globalViewVariable.isAdmin = false;
		globalViewVariable.cr = new ClientResource(globalViewVariable.host + "user");
		globalViewVariable.users = globalViewVariable.cr.get(ArrayList.class);
		globalViewVariable.userMap = new HashMap<String, UserDTO>();
		
		String adminUserStr = Lion.get("taurus.dbadmin.user");
		String[] adminUsers = adminUserStr.split(",");
		for(String adminUser : adminUsers) {
			if(adminUser.equalsIgnoreCase(globalViewVariable.currentUser)) { 
				globalViewVariable.isAdmin = true;
				return;
			}
		}
		
		for (UserDTO user : globalViewVariable.users) {
			globalViewVariable.userMap.put(user.getName(),user);
			if (user.getName().equals(globalViewVariable.currentUser)) {
				
				globalViewVariable.userId = user.getId();
				// support multi group
				String[] userGroups = user.getGroup().split(",");
				for(String userGroup : userGroups){
					if ("admin".equals(userGroup)) {
						globalViewVariable.isAdmin = true;
						break;
					} else {
						globalViewVariable.isAdmin = false;
					}
				}
				

			}
		}
	    
	}
	
	public void commonAttr(ModelMap modelMap){
		String on_duty_name = ConfigHolder.get(LionKeys.ON_DUTY_ADMIN);
		String on_duty_qyqq = ConfigHolder.get(LionKeys.ON_DUTY_QYQQ);
		String on_duty_phone = ConfigHolder.get(LionKeys.ON_DUTY_PHONE);
		
		modelMap.addAttribute("on_duty_name", on_duty_name);
		modelMap.addAttribute("on_duty_qyqq", on_duty_qyqq);
		modelMap.addAttribute("on_duty_phone", on_duty_phone);
	}
}
