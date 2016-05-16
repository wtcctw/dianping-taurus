package com.dp.bigdata.taurus.zookeeper.common.infochannel.guice;

import com.dianping.lion.EnvZooKeeperConfig;
import com.dianping.lion.client.ConfigCache;
import com.dp.bigdata.taurus.common.zookeeper.elect.ZKPair;
import com.dp.bigdata.taurus.common.zookeeper.elect.ZKStringSerializer;
import com.dp.bigdata.taurus.common.utils.ClassLoaderUtils;
import com.google.inject.Provider;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.InputStream;
import java.util.Properties;

/**
 * Author   mingdongli
 * 16/3/15  下午6:12.
 */
public class ZKPairProvider implements Provider<ZKPair> {
    private static final Log LOG = LogFactory.getLog(ZooKeeperProvider.class);
    private static final String ZK_CONF = "zooKeeper.properties";

    private static final String KEY_CONNECT_STRING = "connectionString";
    private static final String KEY_SESSION_TIMEOUT = "sessionTimeout";
    private static final String KEY_CONNECTION_TIMEOUT = "connectionTimeout";
    @Override
    public ZKPair get() {
        Properties props = new Properties();
        try {
            InputStream in = ClassLoaderUtils.getDefaultClassLoader().getResourceAsStream(ZK_CONF);
            props.load(in);
            in.close();
            String connectString = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.zookeeper.connectstring");
            int sessionTimeout = Integer.parseInt(props.getProperty(KEY_SESSION_TIMEOUT));
            int connectionTimeout = Integer.parseInt(props.getProperty(KEY_CONNECTION_TIMEOUT));
            LOG.info("Start Connectting to zookeeper: " + connectString);
            ZkConnection zkConnection = new ZkConnection(connectString, sessionTimeout);
            ZkClient zkClient = new ZkClient(zkConnection, connectionTimeout, new ZKStringSerializer());
            LOG.info("Connected to "+connectString);
            return new ZKPair(zkConnection, zkClient);
        } catch (Exception e) {
            LOG.error(e.getMessage(),e);
            System.out.println("ZooKeeperProvider EERROR++++++++:"+e.getMessage());
            return null;
        }
    }

}
