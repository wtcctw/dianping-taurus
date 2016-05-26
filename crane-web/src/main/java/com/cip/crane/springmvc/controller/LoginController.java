package com.cip.crane.springmvc.controller;

import com.cip.crane.restlet.shared.UserDTO;
import com.cip.crane.restlet.utils.LionConfigUtil;
import com.cip.crane.springmvc.service.IUserService;
import com.cip.crane.springmvc.utils.GlobalViewVariable;
import com.cip.crane.web.servlet.LDAPAuthenticationService;
import com.cip.crane.restlet.resource.IUsersResource;
import org.apache.commons.lang.StringUtils;
import org.restlet.Client;
import org.restlet.data.Protocol;
import org.restlet.resource.ClientResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

@Controller
public class LoginController extends BaseController {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	public static final String USER_NAME = "taurus-user";

	public static final String USER_GROUP = "taurus-group";

	public static final String USER_POWER = "taurus-user-power";
	
    public static  String COOKIE_USER = "";

	@Autowired
	private IUserService userService;

	@RequestMapping(value = "/rocket/{dpAccount:.+}")
	public String rocketlogin(@PathVariable final String dpAccount, ModelMap modelMap,
							  HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		String sessionAccount = (String) session.getAttribute(InitController.USER_NAME);

		if(dpAccount.equals(sessionAccount) != true) {
			boolean isUserExists = userService.checkExists(dpAccount);

			if(isUserExists == false) {
				log.warn("taurus数据库中找不到用户，请至少用sso登陆一次");
				return "/error.ftl";
			}

			session.setAttribute(InitController.USER_NAME, dpAccount);
		}

		session.setAttribute(InitController.NON_SSO_FLAG, true);
		log.info("rocket login success!");

		GlobalViewVariable globalViewVariable = new GlobalViewVariable();
		commonnav(request, globalViewVariable);
		modelMap.addAttribute("currentUser", globalViewVariable.currentUser);
		modelMap.addAttribute("isAdmin", globalViewVariable.isAdmin);
		commonAttr(modelMap);

		return "/update.ftl";
	}

	/**
	 * 登陆sso
	 * @param modelMap
	 * @param request
	 * @param response
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
		HttpSession session = request.getSession(true);
		session.setAttribute(InitController.USER_NAME, userName);
        System.out.println("login success!");

		ClientResource cr = new ClientResource(LionConfigUtil.RESTLET_API_BASE + "user");
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
        
        log.info("logout success!");
        
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
			//user = authService.authenticate(userName, password);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (user == null) {
			ClientResource cr = new ClientResource(String.format("%s/%s", LionConfigUtil.RESTLET_API_BASE + "user", userName));
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

			ClientResource cr = new ClientResource(LionConfigUtil.RESTLET_API_BASE + "user");
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
		
		
		/*ClientResource cr = new ClientResource(String.format("%s/%s", LionConfigUtil.RESTLET_API_BASE + "user", userName));
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
		
		Client client = new Client(Protocol.HTTP);
		client.setConnectTimeout(1000);
		
		ClientResource cr = new ClientResource(LionConfigUtil.RESTLET_API_BASE + "user/" + userName);
		cr.setNext(client);
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
