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
	
	public static String ERROR_PAGE = null;
	
	public static String AGENT_PORT = null;
	
    public static String NEW_AGENT_PORT = null;
    
    public static String XSL_UPLOAD_TMP_DIR = null;
	
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
            AGENT_PORT = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.agent.restlet.port");
            NEW_AGENT_PORT = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.agent.restlet.new.port");
        } catch (LionException e) {
            RESTLET_URL_BASE = servletContext.getInitParameter("RESTLET_SERVER");
            AGENT_PORT = "8080";
            NEW_AGENT_PORT = "8088";
            Cat.logError("LionException", e);
        } catch (Exception e) {
            Cat.logError("LionException", e);
        }
		
        ERROR_PAGE = servletContext.getInitParameter("ERROR_PAGE");
        XSL_UPLOAD_TMP_DIR = servletContext.getInitParameter("XSL_UPLOAD_TMP_DIR");
	}
	
}
