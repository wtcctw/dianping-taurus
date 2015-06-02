package com.dp.bigdata.taurus.web.servlet.filter;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mortbay.log.Log;

import com.dp.bigdata.taurus.web.servlet.LoginServlet;

/**
 * AuthenticationFilter
 * 
 * @author damon.zhu
 */
public class AuthenticationFilter implements Filter {

	private String loginPage;
	
	private String mvcLoginPage;

	private String[] excludePages;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		if (filterConfig != null) {
			loginPage = filterConfig.getInitParameter("loginPage");
			mvcLoginPage = filterConfig.getInitParameter("mvcLoginPage");
			String excludePage = filterConfig.getInitParameter("excludePage");
			excludePages = excludePage.split(",");
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
	      ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String requestURL = req.getRequestURI();
		
		//解决首页显示URL层级不同JS上层目录不同的问题
		String conTextPath = req.getContextPath();
		String reqURInoConTextPath = requestURL.substring(conTextPath.length());
		// root级webapp结果为""，带项目目录webapp结果为"/"
		if(reqURInoConTextPath.equals("/") || reqURInoConTextPath.equals("")){
			requestURL = requestURL + "mvc/index";
		}
		
		if (req.getQueryString() != null) {
			requestURL = requestURL + "?" + req.getQueryString();
		}
		for (String uri : excludePages) {
			if (uri.equalsIgnoreCase(req.getRequestURI().substring(req.getContextPath().length()))) {
				System.out.println("excludePage : " + uri);
				chain.doFilter(request, response);
				return;
			}
		}

		HttpSession session = req.getSession(true);
		Object currentUser = session.getAttribute(LoginServlet.USER_NAME);
		if (currentUser == null) {
			String loginUrl = conTextPath + (conTextPath.equals("/")?"":"/") + mvcLoginPage +
					"?redirect-url=" + URLEncoder.encode(requestURL, "UTF-8");
			//System.out.println(loginUrl);
//			String loginUrl = loginPage + "?redirect-url="+URLEncoder.encode(requestURL, "UTF-8");
//			//新增过滤spring mvc页面,mvc链接临时解决方案
//			if(requestURL.toLowerCase().contains("/mvc/")){
//				loginUrl = req.getContextPath()+mvcLoginPage+
//						"?redirect-url="+URLEncoder.encode(requestURL, "UTF-8");
//			}
			res.sendRedirect(loginUrl);
		
		} else if(reqURInoConTextPath.equals("/") || reqURInoConTextPath.equals("")){
			res.sendRedirect(requestURL);
		} else {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
	}

}
