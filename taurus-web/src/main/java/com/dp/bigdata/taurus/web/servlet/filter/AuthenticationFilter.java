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
		String requestURI = req.getRequestURI();
		
		if(requestURI.contains("/mvc/rest/ssologin")){
			chain.doFilter(request, response);
			return;
		}
		
		//完整url请求地址,不包含QueryString
		//String requestURL = req.getRequestURL().toString();
		//System.out.println("url:"+requestURL+req.getRemoteUser());
		//解决首页显示URL层级不同JS上层目录不同的问题
		String conTextPath = req.getContextPath();
		String reqURInoConTextPath = requestURI.substring(conTextPath.length());
		// root级webapp结果为""，带项目目录webapp结果为"/"
		if(reqURInoConTextPath.equals("/") || reqURInoConTextPath.equals("")){
			requestURI = requestURI + "mvc/index";
			//requestURL = requestURL + "mvc/index";
		}
		
		if (req.getQueryString() != null) {
			requestURI = requestURI + "?" + req.getQueryString();
			//requestURL = requestURL + "?" + req.getQueryString();
		}
//		for (String uri : excludePages) {
//			if (uri.equalsIgnoreCase(req.getRequestURI().substring(req.getContextPath().length()))) {
//				System.out.println("excludePage : " + uri);
//				chain.doFilter(request, response);
//				return;
//			}
//		}
		
		HttpSession session = req.getSession(true);
		Object currentUser = session.getAttribute(LoginServlet.USER_NAME);
		if (currentUser == null) {
//			String loginUrl = conTextPath + (conTextPath.equals("/")?"":"/") + mvcLoginPage +
//					"?redirect-url=" + URLEncoder.encode(requestURL, "UTF-8");
			//System.out.println(loginUrl);
//			String loginUrl = loginPage + "?redirect-url="+URLEncoder.encode(requestURL, "UTF-8");
//			//新增过滤spring mvc页面,mvc链接临时解决方案
//			if(requestURL.toLowerCase().contains("/mvc/")){
//				loginUrl = req.getContextPath()+mvcLoginPage+
//						"?redirect-url="+URLEncoder.encode(requestURL, "UTF-8");
//			}
			
			String loginUrl =  (conTextPath.equals("/")?"":"/") +
					"mvc/rest/ssologin?redirect-url=" + URLEncoder.encode(requestURI, "UTF-8");
//			String loginUrl =  (conTextPath.equals("/")?"":"/") +
//					"ssologin.do?redirect-url=" + URLEncoder.encode(requestURI, "UTF-8");
			//res.sendRedirect(loginUrl);
			req.getRequestDispatcher(loginUrl).forward(req, res);
		
		} else if(reqURInoConTextPath.equals("/") || reqURInoConTextPath.equals("")){
			res.sendRedirect(requestURI);
		} else {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
	}

}
