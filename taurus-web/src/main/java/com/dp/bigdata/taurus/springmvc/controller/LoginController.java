package com.dp.bigdata.taurus.springmvc.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
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
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.util.CookieGenerator;

import sun.misc.BASE64Encoder;

import com.dianping.lion.EnvZooKeeperConfig;
import com.dianping.lion.client.ConfigCache;
import com.dianping.lion.client.LionException;
import com.dp.bigdata.taurus.generated.module.User;
import com.dp.bigdata.taurus.restlet.resource.IUserResource;
import com.dp.bigdata.taurus.restlet.resource.IUsersResource;
import com.dp.bigdata.taurus.restlet.shared.UserDTO;

@Controller
@RequestMapping("/rest")
public class LoginController implements ServletContextAware {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	public static final String USER_NAME = "taurus-user";

	public static final String USER_GROUP = "taurus-group";

	public static final String USER_POWER = "taurus-user-power";
	
    public static  String COOKIE_USER = "";

	private String RESTLET_URL_BASE;

	private String USER_API;

	private ServletContext servletContext;
    
    @Override
	public void setServletContext(ServletContext sc) {
		this.servletContext=sc;  
	}
    
	@PostConstruct
	public void init() throws Exception{
		log.info("----------- into LoginController init ------------");
		
		try {
            RESTLET_URL_BASE = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.web.restlet.url");
        } catch (LionException e) {
            RESTLET_URL_BASE = servletContext.getInitParameter("RESTLET_SERVER");
            e.printStackTrace();
        }
		
		USER_API = RESTLET_URL_BASE + "user";
	}
	
	/**
	 * 登陆sso
	 * @param modelMap
	 * @param request
	 * @param response
	 * @param encodeRedirectUri
	 * @throws IOException 
	 */
	@RequestMapping(value = "/ssologin", method = RequestMethod.GET)
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
			String conTextPath = request.getContextPath();
			response.sendRedirect(conTextPath + (conTextPath.equals("/")?"":"/") + "/mvc/error");
			return ;
		}
		
		String userName = userInfoStr.split("\\|")[0];
		User user = setUserInfo(userName);//这个User是taurus数据库的user表，不是点评通行证的数据库
		log.info("user: "+user.getName()+" "+user.getMail());
		
		//理论上这个if条件在sso登录条件下执行不了了，请教一下原先是在什么场景下会执行？
		if (user == null) {
			ClientResource cr = new ClientResource(String.format("%s/%s", USER_API, userName));
			IUserResource resource = cr.wrap(IUserResource.class);
			boolean hasRegister = resource.hasRegister();
			if (hasRegister) {
				HttpSession session = request.getSession();
				session.setAttribute(USER_NAME, userName);
                CookieGenerator cookie = new CookieGenerator();
                cookie.setCookieDomain(".taurus.dp");//这个也要设置才能实现上面的两个网站共用
                cookie.setCookieMaxAge(1 * 24 * 60 * 60);
                BASE64Encoder base64Encoder = new BASE64Encoder();

                String cookieInfo = base64Encoder.encode(userName.getBytes());
                String cookieName ="cookie_user_jsessionid";
                cookie.setCookieName(cookieName);
                cookie.addCookie(response, cookieInfo);
                COOKIE_USER = userName;
				if(isInfoCompleted(userName)){
					response.setStatus(200);
				} else{
					response.setStatus(201);
				}
			} else {
				response.setStatus(401);
			}
		//sso登录成功之后
		} else {
			HttpSession session = request.getSession();
			session.setAttribute(USER_NAME, userName);
            CookieGenerator cookie = new CookieGenerator();
            cookie.setCookieDomain(".taurus.dp");//这个也要设置才能实现上面的两个网站共用
            cookie.setCookieMaxAge(1 * 24 * 60 * 60);
            BASE64Encoder base64Encoder = new BASE64Encoder();
            String cookieInfo = base64Encoder.encode(userName.getBytes());
            String cookieName ="cookie_user_jsessionid";
            cookie.setCookieName(cookieName);
            COOKIE_USER = userName;
            cookie.addCookie(response, cookieInfo);
            System.out.println("login success!");

			ClientResource cr = new ClientResource(USER_API);
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
	@RequestMapping(value = "/ssologout", method = RequestMethod.GET)
	public void ssologout(ModelMap modelMap, 
							HttpServletRequest request,
							HttpServletResponse response) throws IOException 
	{
		log.info("--------------init the ssologout------------");
    	
		// 只销毁了session。在线用户库里的注销工作在session的SessionDestroyedListener里完成
        request.getSession().invalidate(); 
        
        Cookie cookies[] = request.getCookies();
        if (cookies != null)
        {
            for (int i = 0; i < cookies.length; i++)
            {
                if (cookies[i].getName().equals("cookie_user_jsessionid"))
                {
                    Cookie cookie = new Cookie("cookie_user_jsessionid","");//这边得用"",不能用null
                    cookie.setPath("/");//设置成跟写入cookies一样的
                    cookie.setDomain(".taurus.dp");//设置成跟写入cookies一样的
                    response.addCookie(cookie);
                }
            }
        }
        
        COOKIE_USER = "";
        System.out.println("logout success!");
        
        String ssoLogoutUrl = null;
        String taurusUrl = null;
        
        try {
        	ssoLogoutUrl = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("cas-server-webapp.logoutUrl");
        	taurusUrl = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.web.serverName");
        } catch (LionException e) {
            e.printStackTrace();
            ssoLogoutUrl = "https://sso.51ping.com/logout";
            taurusUrl = "http://alpha.taurus.dp:8080";
        }
        
        response.sendRedirect(ssoLogoutUrl + "?service=" + URLEncoder.encode(taurusUrl, "UTF-8"));
	}
	
	/**
	 * 检查用户信息是否完整
	 * @param userName
	 * @return
	 */
	private boolean isInfoCompleted(String userName){
		ClientResource cr = new ClientResource(RESTLET_URL_BASE + "user");
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
