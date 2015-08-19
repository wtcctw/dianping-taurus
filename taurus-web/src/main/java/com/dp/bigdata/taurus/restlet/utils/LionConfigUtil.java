package com.dp.bigdata.taurus.restlet.utils;


import com.dianping.cat.Cat;
import com.dianping.lion.EnvZooKeeperConfig;
import com.dianping.lion.client.ConfigCache;
import com.dianping.lion.client.LionException;

public class LionConfigUtil {

	public static String SERVER_MASTER_IP = null;
	
	public static String RESTLET_API_PORT = null;
	
	public static String RESTLET_API_BASE = null;
	
	private final static int RETRY_TIME = 3;
	
	private LionConfigUtil(){}
	
	public static synchronized boolean loadServerConf(){
		
		int retryTime = RETRY_TIME;
		
		while (retryTime > 0 ) {
			
			try {
				SERVER_MASTER_IP = ConfigCache.getInstance(
						EnvZooKeeperConfig.getZKAddress()).getProperty(
						"taurus.server.master.ip");

				RESTLET_API_PORT = ConfigCache.getInstance(
						EnvZooKeeperConfig.getZKAddress()).getProperty(
						"taurus.web.restlet.port");
			} catch (LionException e) {
				System.out.println("LION CONGIG ERROR++++++++:" + e.getMessage());
				System.out.println("Trying to reload again, retry time remain:" + (--retryTime));
				continue;
			}
			
			//success to load the lion config
			break;
		}
		
		if(retryTime == 0){
			Cat.logEvent("SCHEDULER.NOT.START", "Please check the lion server status.");
			return false;
		}
		
		RESTLET_API_BASE = "http://" + SERVER_MASTER_IP + ":" + RESTLET_API_PORT + "/api/";
		
		return true;
		
	}
	
}
