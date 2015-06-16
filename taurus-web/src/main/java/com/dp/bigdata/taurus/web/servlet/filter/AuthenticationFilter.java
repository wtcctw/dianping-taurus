package com.dp.bigdata.taurus.web.servlet.filter;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jodd.util.StringUtil;

import com.dp.bigdata.taurus.springmvc.controller.InitController;

/**
 * AuthenticationFilter
 * 
 * @author damon.zhu
 */
public class AuthenticationFilter implements Filter {

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
		System.out.println(new Date() + " [" + this.getClass().getName() + "] --------------init the doFilter------------");
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String requestURI = req.getRequestURI();
		System.out.println(new Date() + " [" + this.getClass().getName() + "] RequestURI: " + requestURI);
		
		// Filter出口1. 登录/ssologin 本机放行，cas不能放行，否则可以伪造cas认证信息；登出/ssologout 本机和cas都放行
		for (String uri : excludePages) {
			if (requestURI.toLowerCase().contains(uri)){
				System.out.println("excludePage : " + uri);
				chain.doFilter(request, response);
				return;
			}
		}
		
		if (StringUtil.isNotBlank(req.getQueryString())) {
			requestURI = requestURI + "?" + req.getQueryString();
		}
		
		HttpSession session = req.getSession(true);
		Object currentUser = session.getAttribute(InitController.USER_NAME);
		
		//未登录
		if (currentUser == null) {
			String loginUrl =  req.getContextPath() 
								+ "/rest/ssologin?redirect-url=" 
								+ URLEncoder.encode(requestURI, "UTF-8");
			req.getRequestDispatcher(loginUrl).forward(req, res);
		} else {// Filter出口2.
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
	}

}
