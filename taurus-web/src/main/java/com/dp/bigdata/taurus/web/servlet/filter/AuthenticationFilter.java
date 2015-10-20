package com.dp.bigdata.taurus.web.servlet.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restlet.resource.ClientResource;

import com.dp.bigdata.taurus.restlet.shared.UserDTO;
import com.dp.bigdata.taurus.restlet.utils.LionConfigUtil;
import com.dp.bigdata.taurus.springmvc.controller.InitController;

/**
 * AuthenticationFilter
 * 
 * @author damon.zhu
 */
public class AuthenticationFilter implements Filter {
	
	private Logger log = LogManager.getLogger();

	private String[] excludePages;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		if (filterConfig != null) {
			String excludePage = filterConfig.getInitParameter("excludePage");
			excludePages = excludePage.split(",");
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
	      ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String userInfoStr = req.getRemoteUser();
		
		if(StringUtils.isBlank(userInfoStr)){
			res.sendRedirect(req.getContextPath() + "/error");
			return ;
		}
		
		String dpaccount = userInfoStr.split("\\|")[0];
		HttpSession session = req.getSession(true);
		String sessionAccount = (String) session.getAttribute(InitController.USER_NAME);
		
		if(dpaccount.equalsIgnoreCase(sessionAccount)){
			log.info(dpaccount + " already logged in.");
			chain.doFilter(request, response);
			return;
		}
		
		UserDTO userDTO = setUserInfo(dpaccount);
		session.setAttribute(InitController.USER_NAME, dpaccount);
		
		ClientResource cr = new ClientResource(LionConfigUtil.RESTLET_API_BASE + "user");
		cr.post(userDTO);//createIfNotExist
		
		chain.doFilter(request, response);
		
	}

	@Override
	public void destroy() {
	}
	
	private UserDTO setUserInfo(String userName){
		UserDTO userDTO = new UserDTO();
		userDTO.setName(userName);
		userDTO.setMail(userName+"@dianping.com");
		return userDTO;
	}

}
