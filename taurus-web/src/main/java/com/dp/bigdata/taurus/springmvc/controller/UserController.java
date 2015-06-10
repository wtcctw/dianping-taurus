package com.dp.bigdata.taurus.springmvc.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/rest")
public class UserController {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());

	private int MAX_USERGROUP_NUM = 3;
	
	/**
	 * 用户设置界面更新用户分组信息
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/saveUser", method = RequestMethod.POST)
	public void saveUser(HttpServletRequest request,HttpServletResponse response) {
		log.info("--------------init the rest/saveUser------------");
		
		ClientResource cr = new ClientResource(InitController.RESTLET_URL_BASE + "user/" + request.getParameter("userName"));

		//TODO 服务端验证恶意分组或其他信息(完成)
		Form form = new Form();
		
		for (UserProperty p : UserProperty.values()) {
			String formFieldName = p.getName();
			String formFieldValue = request.getParameter(p.getName());
			
			if(formFieldName.equals(UserProperty.GROUPNAME)){
				String[] userGroups = formFieldValue.split(",");
				
				if (userGroups.length > MAX_USERGROUP_NUM) {
					Status status = Status.CLIENT_ERROR_BAD_REQUEST;
					response.setStatus(status.getCode());
					return;
				} else {
					for(String userGroup : userGroups){
						if(userGroup.equalsIgnoreCase("admin")){
							Status status = Status.CLIENT_ERROR_BAD_REQUEST;
							response.setStatus(status.getCode());
							return;
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
		response.setStatus(status.getCode());
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
}
