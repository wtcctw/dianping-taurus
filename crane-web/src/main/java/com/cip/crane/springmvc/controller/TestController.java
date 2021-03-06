package com.cip.crane.springmvc.controller;

import java.util.ArrayList;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cip.crane.restlet.utils.LionConfigUtil;
import com.cip.crane.springmvc.bean.WebResult;
import com.cip.crane.springmvc.service.ITestService;
import com.cip.crane.springmvc.utils.GlobalViewVariable;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cip.crane.restlet.shared.UserGroupDTO;


@Controller
@RequestMapping("/test")
public class TestController extends BaseController {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private ITestService testService;
	
	private int MAX_USERGROUP_NUM = 3;

	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap modelMap, 
						HttpServletRequest request,
						HttpServletResponse response) 
	{
		log.info("--------------init the test/index------------");
		
		return "/test/index.ftl";
	}
	
	@RequestMapping(value = "/updateLionConfig", method = RequestMethod.POST)
	@ResponseBody
	public WebResult signin(HttpServletRequest request, HttpServletResponse response) {
		log.info("--------------init the test/updateLionConfig------------");
		WebResult result = new WebResult(request);
		
		String user = (String) request.getSession().getAttribute("taurus-user");
		String adminUser = InitController.ADMIN_USER;
		
		if (adminUser.contains(user)) {
			log.info("Start reload lion config parameters...");
			InitController.dynamicLoad();
			result.setMessage("Update lion config successfully!");
		} else {
			result.setMessage("No authority!");
		}
		
		return result;
	}
	
	@RequestMapping(value = "/saveUser", method = RequestMethod.POST)
	@ResponseBody
	public WebResult saveUser(HttpServletRequest request,HttpServletResponse response) {
		log.info("--------------init the test/saveUser------------");
		
		WebResult result = new WebResult(request);
		ClientResource cr = new ClientResource(LionConfigUtil.RESTLET_API_BASE + "user/" + request.getParameter("userName"));

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
						// admin组不能再加别的组
						if(userGroup.equals("admin")){
							formFieldValue = userGroup;
							break;
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

	@RequestMapping(value = "/showUsers", method = RequestMethod.GET)
	@ResponseBody
	public WebResult showUsers(HttpServletRequest request, HttpServletResponse response) {
		log.info("--------------init the showUsers------------");
		
		WebResult result = new WebResult(request);
		GlobalViewVariable globalViewVariable = new GlobalViewVariable();
		commonnav(request,globalViewVariable);
		result.addAttr("users", globalViewVariable.users);
		
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
		ArrayList<UserGroupDTO> groups = globalViewVariable.cr.get(ArrayList.class);
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
	
	@RequestMapping(value = "/testget", method = RequestMethod.GET)
	@ResponseBody
	public WebResult testget(HttpServletRequest request, 
									HttpServletResponse response) 
	{
		log.info("--------------init the testget------------");
		
		WebResult result = new WebResult(request);

		// 检测正常Agent 发现异常 告警
		ClientResource cr = new ClientResource(LionConfigUtil.RESTLET_API_BASE + "allhosts");
        String onlineHosts = cr.get(String.class);
        
        
        JSONObject jsonObj = JSONObject.fromObject(onlineHosts);
		
		JSONArray jsonArr = jsonObj.getJSONArray("hosts");
		
		Object[] hostLists = jsonArr.toArray();
		
		for(int i=0;i<hostLists.length;++i){
			result.addAttr("host"+i, hostLists[i].toString());
		}
        
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
}
