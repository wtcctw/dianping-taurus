package com.cip.crane.web.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class MyServletContextListener implements ServletContextListener {
	
	@Override
	public void contextInitialized(ServletContextEvent sce)
	{
		ServletContext sc = sce.getServletContext();
		//do your initialization here.
		//sc.setAttribute(.....);
		org.apache.log4j.LogManager.resetConfiguration();
		org.apache.log4j.PropertyConfigurator.configure("src/main/resources/log4j.properties");
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent sce)
	{
		ServletContext sc = sce.getServletContext();
		//do your cleanup here
	}
}