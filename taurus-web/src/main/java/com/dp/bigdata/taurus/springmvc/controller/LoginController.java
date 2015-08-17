package com.dp.bigdata.taurus.springmvc.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.restlet.resource.ClientResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.CookieGenerator;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.dp.bigdata.taurus.restlet.resource.IUserResource;
import com.dp.bigdata.taurus.restlet.resource.IUsersResource;
import com.dp.bigdata.taurus.restlet.shared.UserDTO;
import com.dp.bigdata.taurus.web.servlet.LDAPAuthenticationService;

@Controller
public class LoginController {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	public static final String USER_NAME = "taurus-user";

	public static final String USER_GROUP = "taurus-group";

	public static final String USER_POWER = "taurus-user-power";
	
    public static  String COOKIE_USER = "";
    
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
    	
		String encodedUrl = request.getParameter("redirect-url");
		
	    if (StringUtils.isBlank(encodedUrl)) { encodedUrl = "/"; }
	    
		String userInfoStr = request.getRemoteUser();
		log.info("ssoUserInfo: " + userInfoStr);
		
		if(StringUtils.isBlank(userInfoStr)){
			response.sendRedirect(request.getContextPath() + "/error");
			return ;
		}
		
		String userName = userInfoStr.split("\\|")[0];
		UserDTO userDTO = setUserInfo(userName);
		
		//sso登录成功之后
		HttpSession session = request.getSession();
		session.setAttribute(InitController.USER_NAME, userName);
        System.out.println("login success!");

		ClientResource cr = new ClientResource(InitController.RESTLET_URL_BASE + "user");
		cr.post(userDTO);//createIfNotExist
		
		if(isInfoCompleted(userName)){
			//用户信息完整
			response.setStatus(200);
		} else{
			//用户信息设置不全
			response.setStatus(201);
		}
		
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
	 * sso太慢，调试不方便，留个后门,使用的时候运行(mvn jetty:run -P dev)，并且保证数据库中有用户信息
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
		
		LDAPAuthenticationService authService = new LDAPAuthenticationService();
		UserDTO user = null;

		try {
			user = authService.authenticate(userName, password);
		} catch (Exception e) {
			e.printStackTrace();
		}

		log.info("found new ldap user: " + user.getName());
		
		if (user == null) {
			ClientResource cr = new ClientResource(String.format("%s/%s", InitController.RESTLET_URL_BASE + "user", userName));
			UserDTO userDTO = cr.get(UserDTO.class);
			
			if (userDTO != null) {
				HttpSession session = request.getSession();
				session.setAttribute(USER_NAME, userName);
                
				if(isInfoCompleted(userName)){
					response.setStatus(200);
				} else{
					response.setStatus(201);
				}
				
			} else {
				response.setStatus(401);
			}
			
		} else {
			HttpSession session = request.getSession();
			session.setAttribute(USER_NAME, userName);
            System.out.println("login success!");

			ClientResource cr = new ClientResource(InitController.RESTLET_URL_BASE + "user");
			IUsersResource resource = cr.wrap(IUsersResource.class);
			UserDTO dto = new UserDTO();
			dto.setName(userName);
			dto.setMail(user.getMail());
			resource.createIfNotExist(dto);
			
			if(isInfoCompleted(userName)){
				response.setStatus(200);
			} else{
				response.setStatus(201);
			}
			
		}
		
		
		/*ClientResource cr = new ClientResource(String.format("%s/%s", InitController.RESTLET_URL_BASE + "user", userName));
		UserDTO userDTO = cr.get(UserDTO.class);
		if (userDTO != null) {
			HttpSession session = request.getSession();
			session.setAttribute(InitController.USER_NAME, userName);
			
			if(isInfoCompleted(userName)){
				response.setStatus(200);
			} else{
				response.setStatus(201);
			}
		} else {
			response.setStatus(401);
		}*/
		
	}
	/**
	 * 检查用户信息是否完整
	 * @param userName
	 * @return
	 */
	private boolean isInfoCompleted(String userName){
		
		ClientResource cr = new ClientResource(InitController.RESTLET_URL_BASE + "user/" + userName);
		UserDTO userDTO = cr.get(UserDTO.class);
  	    
		boolean result = false;
		
		if(userDTO != null){
			
			if( StringUtils.isBlank(userDTO.getGroup())
					|| StringUtils.isBlank(userDTO.getMail())
					|| StringUtils.isBlank(userDTO.getTel()) ) {
				result = false;
			}else{
				result = true;
			}
		}
  	    
  	    return result;
	}
	
	/**
	 * 设置User点评通行证用户名和邮箱
	 * @param userName
	 * @return
	 */
	private UserDTO setUserInfo(String userName){
		UserDTO userDTO = new UserDTO();
		userDTO.setName(userName);
		userDTO.setMail(userName+"@dianping.com");
		return userDTO;
	}
}
