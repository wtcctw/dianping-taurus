package com.dp.bigdata.taurus.scheduler.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jodd.util.StringUtil;

import com.dianping.lion.EnvZooKeeperConfig;
import com.dianping.lion.client.ConfigCache;

public class CoreFilter implements Filter {
	
	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		String url_base = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.web.serverName"); 
		String restlet_port = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.web.restlet.port");
		String requestURI = req.getRequestURI();
		
		if (StringUtil.isNotBlank(req.getQueryString())) {
			requestURI = requestURI + "?" + req.getQueryString();
		}
		
		String url = url_base + ":" + restlet_port + requestURI;
		System.out.println("Filter url: " + url);
		// 跨服务器不行
		req.getRequestDispatcher(url).forward(req, res);
		
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

}
