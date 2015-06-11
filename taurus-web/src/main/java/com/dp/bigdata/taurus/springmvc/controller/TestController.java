package com.dp.bigdata.taurus.springmvc.controller;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dp.bigdata.taurus.restlet.resource.IUserGroupsResource;
import com.dp.bigdata.taurus.restlet.resource.IUsersResource;
import com.dp.bigdata.taurus.restlet.shared.UserDTO;
import com.dp.bigdata.taurus.restlet.shared.UserGroupDTO;
import com.dp.bigdata.taurus.springmvc.bean.WebResult;
import com.dp.bigdata.taurus.springmvc.service.ITestService;


@Controller
@RequestMapping("/test")
public class TestController {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private ITestService testService;
	
	private int MAX_USERGROUP_NUM = 3;

	@RequestMapping(value = "/saveUser", method = RequestMethod.POST)
	@ResponseBody
	public WebResult saveUser(HttpServletRequest request,HttpServletResponse response) {
		log.info("--------------init the test/saveUser------------");
		
		WebResult result = new WebResult(request);
		ClientResource cr = new ClientResource(InitController.RESTLET_URL_BASE + "user/" + request.getParameter("userName"));

		Form form = new Form();
		
		for (UserProperty p : UserProperty.values()) {
			String formFieldName = p.getName();
			String formFieldValue = request.getParameter(p.getName());
			
			if(formFieldName.equals(UserProperty.GROUPNAME.getName())){
				String[] userGroups = formFieldValue.split(",");
				if (userGroups.length > MAX_USERGROUP_NUM) {
					Status status = Status.CLIENT_ERROR_BAD_REQUEST;
					result.setStatus(status.getCode());
					return result;
				} else {
					for(String userGroup : userGroups){
						//可添加admin，仅验证未分组
						if(userGroup.equals("未分组")){
							Status status = Status.CLIENT_ERROR_BAD_REQUEST;
							result.setStatus(status.getCode());
							return result;
						}
					}
				}
			}
			form.add(formFieldName, formFieldValue);
			
		}
		Representation re = form.getWebRepresentation();
		re.setMediaType(MediaType.APPLICATION_XML);
		cr.post(re);
		Status status = cr.getResponse().getStatus();
		//response.setStatus(status.getCode());
		
		result.setStatus(status.getCode());
		return result;
	}

	@RequestMapping(value = "/showGroups", method = RequestMethod.GET)
	@ResponseBody
	public WebResult showGroups(HttpServletRequest request, HttpServletResponse response) {
		log.info("--------------init the showGroups------------");
		
		WebResult result = new WebResult(request);
		GlobalViewVariable globalViewVariable = new GlobalViewVariable();
		commonnav(request,globalViewVariable);
		//result.addAttr("users", globalViewVariable.users);
		
		globalViewVariable.cr = new ClientResource(globalViewVariable.host + "group");
		IUserGroupsResource groupResource = globalViewVariable.cr.wrap(IUserGroupsResource.class);
		globalViewVariable.cr.accept(MediaType.APPLICATION_XML);
		ArrayList<UserGroupDTO> groups = groupResource.retrieve();
		result.addAttr("groups", groups);
		
		
		return result;
	}
	
	@RequestMapping(value = "/delGroupById/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public WebResult delGroupById(HttpServletRequest request, 
									HttpServletResponse response,
									@PathVariable String id) 
	{
		log.info("--------------init the delGroupById------------");
		
		WebResult result = new WebResult(request);
		GlobalViewVariable globalViewVariable = new GlobalViewVariable();
		commonnav(request,globalViewVariable);
		//result.addAttr("users", globalViewVariable.users);
		
		int affectedRowNum = testService.deleteById(Integer.parseInt(id));
		result.addAttr("affectedRowNum", affectedRowNum);
		return result;
	}
	
	private enum UserProperty {
		ID("id"), USERNAME("userName"), GROUPNAME("groupName"), EMAIL("email"), TEL(
				"tel"),QQ("qq");

		private String name;

		private UserProperty(String name) {
			this.name = name;
		}

		private String getName() {
			return name;
		}
	}
	
public void commonnav(HttpServletRequest request,GlobalViewVariable globalViewVariable){
		
		globalViewVariable.currentUser = (String) request.getSession().getAttribute(InitController.USER_NAME);
		globalViewVariable.userId = -1;
		
		globalViewVariable.host = InitController.RESTLET_URL_BASE;
		
		globalViewVariable.isAdmin = false;
		globalViewVariable.cr = new ClientResource(globalViewVariable.host + "user");
		globalViewVariable.userResource = globalViewVariable.cr.wrap(IUsersResource.class);
		globalViewVariable.cr.accept(MediaType.APPLICATION_XML);
		globalViewVariable.users = globalViewVariable.userResource.retrieve();
		globalViewVariable.userMap = new HashMap<String, UserDTO>();
		
		for (UserDTO user : globalViewVariable.users) {
			globalViewVariable.userMap.put(user.getName(),user);
			if (user.getName().equals(globalViewVariable.currentUser)) {
				
				globalViewVariable.userId = user.getId();
				//TODO support multi group(完成)
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
	
	/**
	 * jsp/common-nav.jsp所需公共变量
	 * @author chenchongze
	 *
	 */
	public class GlobalViewVariable{
		
		private String currentUser = null;
		private String host = null;
		private int userId = -1;
		private boolean isAdmin = false;
		private ClientResource cr = null;
		private IUsersResource userResource = null;
		private ArrayList<UserDTO> users = null;
		private HashMap<String, UserDTO> userMap = null;
		
		public GlobalViewVariable(){};
	}
}
