package com.dp.bigdata.taurus.common.netty.zookeeper;

import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Author   mingdongli
 * 16/5/18  下午4:01.
 */
public abstract class AbstractListenerManager implements InitializingBean{

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ZookeeperClient zookeeperClient;

    public AbstractListenerManager() {
    }

    public AbstractListenerManager(ZookeeperRegistryCenter zookeeperRegistryCenter) {
        zookeeperClient = new ZookeeperClient();
        zookeeperClient.setZookeeperRegistryCenter(zookeeperRegistryCenter);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        start();
    }

    public abstract void start();

    protected void addDataListener(final TreeCacheListener listener) {
        zookeeperClient.addDataListener(listener);
    }

    protected void addConnectionStateListener(final ConnectionStateListener listener) {
        zookeeperClient.addConnectionStateListener(listener);
    }

}
