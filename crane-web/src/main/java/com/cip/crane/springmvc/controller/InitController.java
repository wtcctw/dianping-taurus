package com.cip.crane.springmvc.controller;

import com.dianping.cat.Cat;
import com.dianping.lion.EnvZooKeeperConfig;
import com.dianping.lion.client.ConfigCache;
import com.dianping.lion.client.LionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.ServletContextAware;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

@Controller
public class InitController implements ServletContextAware {

	private static Logger log = LoggerFactory.getLogger(InitController.class);
	
	public static ServletContext servletContext;

	public static String ERROR_PAGE = null;
	
	public static String AGENT_PORT = null;
	
    public static String NEW_AGENT_PORT = null;
    
    public static String XSL_UPLOAD_TMP_DIR = null;
	
	public static final String USER_NAME = "taurus-user";

	public static final String USER_GROUP = "taurus-group";

	public static final String USER_POWER = "taurus-user-power";
	
	public static String SWITCH_URL_ALL = null;
	
	public static String DOMAIN = null;
	
	public static String ZABBIX_SWITCH = null;
	
	public static String ADMIN_USER = null;
	
	public static String MAIL_TO = null;
	
	public static String SSO_LOGOUT_URL = null;

	public static String NON_SSO_FLAG = "is-non-sso";
	
    @Override
	public void setServletContext(ServletContext sc) {
		this.servletContext=sc;  
	}
	
	
	@PostConstruct
	public void init() {
		log.info("----------- into spring mvc init ------------");
		
		dynamicLoad();
		
		ERROR_PAGE = servletContext.getInitParameter("ERROR_PAGE");
		XSL_UPLOAD_TMP_DIR = servletContext.getInitParameter("XSL_UPLOAD_TMP_DIR");
		
		log.info("----------- 中文测试 ------------");
	}
	
	public static synchronized void dynamicLoad(){
		log.info("----------- into dynamicLoad ------------");
		try {
			AGENT_PORT = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.agent.restlet.port");
			NEW_AGENT_PORT = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.agent.restlet.new.port");
			SWITCH_URL_ALL = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.web.url.switch");
			DOMAIN = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.web.serverName");
			ZABBIX_SWITCH = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.zabbix.switch");
			ADMIN_USER = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.dbadmin.user");
			MAIL_TO = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.feedback.mail.to");
			SSO_LOGOUT_URL = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("cas-server-webapp.logoutUrl");
		} catch (LionException e) {
			log.info("LION CONGIG ERROR++++++++:"+e.getMessage());
			Cat.logError("LionException", e);
		} catch (Exception e) {
			Cat.logError("LionException", e);
		}
	}
}
