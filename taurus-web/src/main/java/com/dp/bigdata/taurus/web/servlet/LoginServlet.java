package com.dp.bigdata.taurus.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.dianping.lion.EnvZooKeeperConfig;
import com.dianping.lion.client.ConfigCache;
import com.dianping.lion.client.LionException;
import com.dp.bigdata.taurus.web.servlet.LDAPAuthenticationService;
import org.apache.commons.lang.StringUtils;
import org.restlet.data.MediaType;
import org.restlet.resource.ClientResource;

import com.dp.bigdata.taurus.generated.module.User;
import com.dp.bigdata.taurus.restlet.resource.IUserResource;
import com.dp.bigdata.taurus.restlet.resource.IUsersResource;
import com.dp.bigdata.taurus.restlet.shared.UserDTO;
import org.springframework.web.util.CookieGenerator;

/**
 * LoginServlet
 * 
 * @author damon.zhu
 */

public class LoginServlet extends HttpServlet {

	/**
     * 
     */
	private static final long serialVersionUID = 8471117450126373174L;

	public static final String USER_NAME = "taurus-user";

	public static final String USER_GROUP = "taurus-group";

	public static final String USER_POWER = "taurus-user-power";

	private String RESTLET_URL_BASE;

	private String USER_API;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext context = getServletContext();
        try {
            RESTLET_URL_BASE = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.web.restlet.url");
        } catch (LionException e) {
            RESTLET_URL_BASE = context.getInitParameter("RESTLET_SERVER");
            e.printStackTrace();
        }
		USER_API = RESTLET_URL_BASE + "user";
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 只销毁了session。在线用户库里的注销工作在session的SessionDestroyedListener里完成
        request.getSession().invalidate(); 
        
        response.setContentType("text/html;charset=GBK");
        PrintWriter out = response.getWriter();
        Cookie cookies[] = request.getCookies();
        if (cookies != null)
        {
            for (int i = 0; i < cookies.length; i++)
            {
                if (cookies[i].getName().equals("nickname"))
                {
                    Cookie cookie = new Cookie("nickname","");//这边得用"",不能用null
                    cookie.setPath("/");//设置成跟写入cookies一样的
                    cookie.setDomain(".taurus.dp");//设置成跟写入cookies一样的
                    response.addCookie(cookie);
                }
            }
        }
        out.print("登出成功");
        out.flush();
        out.close();

	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userName = request.getParameter("username");
		String password = request.getParameter("password");

		if (StringUtils.isBlank(password)) {
			response.setStatus(401);
			return;
		}

		LDAPAuthenticationService authService = new LDAPAuthenticationService();
		User user = null;

		try {
			user = authService.authenticate(userName, password);
		} catch (Exception e) {
			e.printStackTrace();
		}

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
                cookie.setCookieName("cookie_user_jsessionid");
                cookie.addCookie(response, URLEncoder.encode(userName, "UTF-8"));
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
            CookieGenerator cookie = new CookieGenerator();
            cookie.setCookieDomain(".taurus.dp");//这个也要设置才能实现上面的两个网站共用
            cookie.setCookieMaxAge(1 * 24 * 60 * 60);
            cookie.setCookieName("cookie_user_jsessionid");
            cookie.addCookie(response, URLEncoder.encode(userName, "UTF-8"));
            System.out.println("login success!");

			ClientResource cr = new ClientResource(USER_API);
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
	}
	
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
}
