package com.dp.bigdata.taurus.springmvc.controller;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.ServletContextAware;

import com.dianping.cat.Cat;
import com.dianping.lion.EnvZooKeeperConfig;
import com.dianping.lion.client.ConfigCache;
import com.dianping.lion.client.LionException;
import com.dp.bigdata.taurus.web.utils.ReFlashHostLoadTaskTimer;

@Controller
public class InitController implements ServletContextAware {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private ServletContext servletContext;
    
	public static String RESTLET_URL_BASE = null;
	
	public static String USER_API = null;
	
	public static final String USER_NAME = "taurus-user";

	public static final String USER_GROUP = "taurus-group";

	public static final String USER_POWER = "taurus-user-power";
	
    @Override
	public void setServletContext(ServletContext sc) {
		this.servletContext=sc;  
	}
	
	
	@PostConstruct
	public void init() {
		log.info("----------- into spring mvc init ------------");
		
		ReFlashHostLoadTaskTimer.getReFlashHostLoadManager().start();
		
		try {
            RESTLET_URL_BASE = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.web.restlet.url");
        } catch (LionException e) {
            RESTLET_URL_BASE = servletContext.getInitParameter("RESTLET_SERVER");
            Cat.logError("LionException", e);
        } catch (Exception e) {
            Cat.logError("LionException", e);
        }
		
		USER_API = RESTLET_URL_BASE + "user";
	}
	
}
