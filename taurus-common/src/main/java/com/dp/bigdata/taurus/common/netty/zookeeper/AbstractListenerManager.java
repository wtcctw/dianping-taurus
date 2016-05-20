package com.dp.bigdata.taurus.common.netty.zookeeper;

import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author   mingdongli
 * 16/5/18  下午4:01.
 */
public abstract class AbstractListenerManager {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private ZookeeperClient zookeeperClient;

    public AbstractListenerManager(ZookeeperRegistryCenter zookeeperRegistryCenter) {
        zookeeperClient = new ZookeeperClient();
        zookeeperClient.setZookeeperRegistryCenter(zookeeperRegistryCenter);
    }

    public abstract void start();

    protected void addDataListener(final TreeCacheListener listener) {
        zookeeperClient.addDataListener(listener);
    }

    protected void addConnectionStateListener(final ConnectionStateListener listener) {
        zookeeperClient.addConnectionStateListener(listener);
    }

}
