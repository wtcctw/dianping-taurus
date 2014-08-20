package com.dp.bigdata.taurus.zookeeper.common.infochannel.guice;

import java.io.InputStream;
import java.util.Properties;

import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.dianping.lion.EnvZooKeeperConfig;

import com.dp.bigdata.taurus.zookeeper.common.utils.ClassLoaderUtils;
import com.google.inject.Provider;
import com.dianping.lion.client.ConfigCache;

public final class ZooKeeperProvider implements Provider<ZkClient>{
    private static final Log LOG = LogFactory.getLog(ZooKeeperProvider.class);
	private static final String ZK_CONF = "zooKeeper.properties";
	
	private static final String KEY_CONNECT_STRING = "connectionString";
	private static final String KEY_SESSION_TIMEOUT = "sessionTimeout";
	private static final int CONNECTION_TIMEOUT = 30 * 1000;
	@Override
	public ZkClient get() {
		Properties props = new Properties();
		try {
			InputStream in = ClassLoaderUtils.getDefaultClassLoader().getResourceAsStream(ZK_CONF);
			props.load(in);
			in.close();
			//String connectString = props.getProperty(KEY_CONNECT_STRING);
            String connectString = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.zookeeper.connectstring");
			int sessionTimeout = Integer.parseInt(props.getProperty(KEY_SESSION_TIMEOUT));
            LOG.info("Start Connectting to zookeeper: " + connectString);
            ZkClient zk = new ZkClient(connectString, sessionTimeout,CONNECTION_TIMEOUT);
			LOG.info("Connected to "+connectString);
			return zk ;
		} catch (Exception e) {
		    LOG.error(e.getMessage(),e);
			return null;
		}
	}

	
}