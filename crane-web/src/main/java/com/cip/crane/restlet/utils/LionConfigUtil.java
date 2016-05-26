package com.cip.crane.restlet.utils;


import com.dianping.cat.Cat;
import com.dianping.lion.EnvZooKeeperConfig;
import com.dianping.lion.client.ConfigCache;
import com.dianping.lion.client.LionException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LionConfigUtil {

    private static final Logger logger = LoggerFactory.getLogger(LionConfigUtil.class);

    public static String SERVER_MASTER_IP = null;

    public static String RESTLET_API_PORT = null;

    public static String RESTLET_API_BASE = null;

    private final static int RETRY_TIME = 3;

    private LionConfigUtil() {
    }

    public static synchronized boolean loadServerConf(final String leader) {

        int retryTime = RETRY_TIME;

        while (retryTime > 0) {

            try {

                if (StringUtils.isNotBlank(leader)) {
                    SERVER_MASTER_IP = leader;
                }else{
                    SERVER_MASTER_IP = ConfigCache.getInstance(
						EnvZooKeeperConfig.getZKAddress()).getProperty(
						"taurus.server.master.ip");
                }

                logger.info(String.format("Restlet server ip is %s", SERVER_MASTER_IP));
                RESTLET_API_PORT = ConfigCache.getInstance(
                        EnvZooKeeperConfig.getZKAddress()).getProperty(
                        "taurus.web.restlet.port");
            } catch (LionException e) {
                logger.info("LION CONGIG ERROR++++++++:" + e.getMessage());
                logger.info("Trying to reload again, retry time remain:" + (--retryTime));
                continue;
            }

            //success to load the lion config
            break;
        }

        if (retryTime == 0) {
            Cat.logEvent("SCHEDULER.NOT.START", "Please check the lion server status.");
            return false;
        }

        RESTLET_API_BASE = "http://" + SERVER_MASTER_IP + ":" + RESTLET_API_PORT + "/api/";

        return true;

    }

}
