package com.dp.bigdata.taurus.zookeeper.common.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.security.auth.login.Configuration;

/**
 * Author   mingdongli
 * 16/3/15  下午5:54.
 */
public class JaasUtils {

    private static final Log LOG = LogFactory.getLog(JaasUtils.class);

    public static final String ZK_SASL_CLIENT = "zookeeper.sasl.client";
    public static final String ZK_LOGIN_CONTEXT_NAME_KEY = "zookeeper.sasl.clientconfig";

    public static boolean isZkSecurityEnabled() {

        boolean isSecurityEnabled;
        boolean zkSaslEnabled = Boolean.parseBoolean(System.getProperty(ZK_SASL_CLIENT, "true"));
        String zkLoginContextName = System.getProperty(ZK_LOGIN_CONTEXT_NAME_KEY, "Client");

        try {
            Configuration loginConf = Configuration.getConfiguration();
            isSecurityEnabled = loginConf.getAppConfigurationEntry(zkLoginContextName) != null;
        } catch (Exception e) {
            return false;
        }
        if (isSecurityEnabled && !zkSaslEnabled) {
            LOG.error("JAAS configuration is present, but system property " +
                    ZK_SASL_CLIENT + " is set to false, which disables " +
                    "SASL in the ZooKeeper client");
            return false;
        }

        return isSecurityEnabled;
    }
}
