package com.dp.bigdata.taurus.springmvc.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.restlet.data.MediaType;
import org.restlet.resource.ClientResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.CookieGenerator;

import com.dp.bigdata.taurus.generated.module.User;
import com.dp.bigdata.taurus.restlet.resource.IUserResource;
import com.dp.bigdata.taurus.restlet.resource.IUsersResource;
import com.dp.bigdata.taurus.restlet.shared.UserDTO;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

@Controller
public class LoginController {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 登陆sso
	 * @param modelMap
	 * @param request
	 * @param response
	 * @param encodeRedirectUri
	 * @throws IOException 
	 */
	@RequestMapping(value = "/rest/ssologin", method = RequestMethod.GET)
	public void ssologin(ModelMap modelMap, 
							HttpServletRequest request,
							HttpServletResponse response) throws IOException 
	{
		log.info("--------------init the ssologin------------");
    	
		String encodedUrl = (String) request.getParameter("redirect-url");
		
	    if (StringUtils.isBlank(encodedUrl)) {
	    	encodedUrl = "/";
	    }
	    
	    log.info("encodedUrl: "+encodedUrl);
		String userInfoStr = request.getRemoteUser();//有没有可能绕过sso，直接访问这个链接，伪造各种用户信息？
		log.info("ssoUserInfo: " + userInfoStr);
		
		if(userInfoStr == null){
			response.sendRedirect(request.getContextPath() + "/error");
			return ;
		}
		
		String userName = userInfoStr.split("\\|")[0];
		User user = setUserInfo(userName);//这个User是taurus数据库的user表，不是点评通行证的数据库
		log.info("user: "+user.getName()+" "+user.getMail());
		
		//sso登录成功之后
		HttpSession session = request.getSession();
		session.setAttribute(InitController.USER_NAME, userName);
        System.out.println("login success!");

		ClientResource cr = new ClientResource(InitController.RESTLET_URL_BASE + "user");
		IUsersResource resource = cr.wrap(IUsersResource.class);
		UserDTO dto = new UserDTO();
		dto.setName(userName);
		dto.setMail(user.getMail());
		resource.createIfNotExist(dto);
		
		if(isInfoCompleted(userName)){
			//用户信息完整
			response.setStatus(200);
		} else{
			//用户信息设置不全
			response.setStatus(201);
		}
		
		log.info("decode:"+URLDecoder.decode(encodedUrl, "UTF-8"));
		response.sendRedirect(URLDecoder.decode(encodedUrl, "UTF-8"));
		
	}
	
	/**
	 * 登出sso
	 * @param modelMap
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/rest/ssologout", method = RequestMethod.GET)
	public void ssologout(ModelMap modelMap, 
							HttpServletRequest request,
							HttpServletResponse response) throws IOException 
	{
		log.info("--------------init the ssologout------------");
    	
		// 只销毁了session。在线用户库里的注销工作在session的SessionDestroyedListener里完成
        request.getSession().invalidate(); 
        
        System.out.println("logout success!");
        
        String ssoLogoutUrl = InitController.SSO_LOGOUT_URL;
        String taurusUrl = InitController.DOMAIN;
        
        response.sendRedirect(ssoLogoutUrl + "?service=" + URLEncoder.encode(taurusUrl, "UTF-8"));
	}
	
	/**
	 * sso太慢，调试不方便，留个后门,使用的时候去掉sso的filter，并且保证数据库中有用户信息
	 * @param modelMap
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	public void oldlogin(ModelMap modelMap, 
							HttpServletRequest request,
							HttpServletResponse response) throws IOException 
	{
		log.info("--------------init the oldlogin------------");
		String userName = request.getParameter("username");
		String password = request.getParameter("password");

		if (StringUtils.isBlank(password)) {
			response.setStatus(401);
			return;
		}
		
		ClientResource cr = new ClientResource(String.format("%s/%s", InitController.RESTLET_URL_BASE + "user", userName));
		IUserResource resource = cr.wrap(IUserResource.class);
		boolean hasRegister = resource.hasRegister();
		if (hasRegister) {
			HttpSession session = request.getSession();
			session.setAttribute(InitController.USER_NAME, userName);
			
			if(isInfoCompleted(userName)){
				response.setStatus(200);
			} else{
				response.setStatus(201);
			}
		} else {
			response.setStatus(401);
		}
		
	}
	/**
	 * 检查用户信息是否完整
	 * @param userName
	 * @return
	 */
	private boolean isInfoCompleted(String userName){
		ClientResource cr = new ClientResource(InitController.RESTLET_URL_BASE + "user");
  	    IUsersResource userResource = cr.wrap(IUsersResource.class);
  	    cr.accept(MediaType.APPLICATION_XML);
  	    ArrayList<UserDTO> users = userResource.retrieve();
  	    
  	    for(UserDTO user:users){
  	    	if(userName.equals(user.getName())){
  	    		if(user.getGroup() == null || user.getMail() == null || user.getTel() == null 
  						|| user.getGroup().equals("") || user.getMail().equals("") || user.getTel().equals("") ){
  	    			return false;
  	    		} else{
  	    			return true;
  	    		}
  	    	}
  	    }
  	    
  	    return false;
	}
	
	/**
	 * 设置User点评通行证用户名和邮箱
	 * @param userName
	 * @return
	 */
	private User setUserInfo(String userName){
		User user = new User();
		user.setName(userName);
		user.setMail(userName+"@dianping.com");
		return user;
	}
}
