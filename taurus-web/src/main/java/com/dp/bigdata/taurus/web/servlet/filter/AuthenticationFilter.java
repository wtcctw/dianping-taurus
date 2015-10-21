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
import org.restlet.resource.ClientResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dp.bigdata.taurus.restlet.shared.UserDTO;
import com.dp.bigdata.taurus.restlet.utils.LionConfigUtil;
import com.dp.bigdata.taurus.springmvc.controller.InitController;

/**
 * AuthenticationFilter
 * 
 * @author damon.zhu
 */
public class AuthenticationFilter implements Filter {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
	      ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String userInfoStr = req.getRemoteUser();
		
		String dpaccount = null;
		try {
			dpaccount = userInfoStr.split("\\|")[0];
		} catch (Exception e) {
			log.error("get remote user error!", e);
		}
		
		HttpSession session = req.getSession(true);
		String sessionAccount = (String) session.getAttribute(InitController.USER_NAME);
		
		if(StringUtils.isNotBlank(sessionAccount)){
			log.info(sessionAccount + " already logged in.");
			chain.doFilter(request, response);
			return;
		}
		
		if(StringUtils.isBlank(sessionAccount)) {
			
			if(StringUtils.isBlank(dpaccount)){
				res.sendRedirect(req.getContextPath() + "/error");
				return ;
			}
			
			UserDTO userDTO = setUserInfo(dpaccount);
			session.setAttribute(InitController.USER_NAME, dpaccount);
			
			ClientResource cr = new ClientResource(LionConfigUtil.RESTLET_API_BASE + "user");
			cr.post(userDTO);//createIfNotExist
			
			chain.doFilter(request, response);
			return;
		}
		
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
