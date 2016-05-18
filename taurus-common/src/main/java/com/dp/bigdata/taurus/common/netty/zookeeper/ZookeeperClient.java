package com.dp.bigdata.taurus.common.netty.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Author   mingdongli
 * 16/5/18  下午4:02.
 */
public final class ZookeeperClient {

    private static Logger log = LoggerFactory.getLogger(ZookeeperClient.class);

    private  ZookeeperRegistryCenter zookeeperRegistryCenter;

    public ZookeeperClient() {
    }

    public void addConnectionStateListener(final ConnectionStateListener listener) {
        getClient().getConnectionStateListenable().addListener(listener);
    }

    private CuratorFramework getClient() {
        return (CuratorFramework) zookeeperRegistryCenter.getRawClient();
    }

    /**
     * 注册数据监听器.
     */
    public void addDataListener(final TreeCacheListener listener) {
        TreeCache cache = (TreeCache) zookeeperRegistryCenter.getRawCache();
        cache.getListenable().addListener(listener);
    }


    public ZookeeperRegistryCenter getZookeeperRegistryCenter() {
        return zookeeperRegistryCenter;
    }

    public void setZookeeperRegistryCenter(ZookeeperRegistryCenter zookeeperRegistryCenter) {
        this.zookeeperRegistryCenter = zookeeperRegistryCenter;
    }


    private void  setNodeData(String path, String value) throws InterruptedException, IOException, KeeperException {
        zookeeperRegistryCenter.update(path, value);
    }

    public void setManagerEvent(String value) throws Exception{
        setNodeData(zookeeperRegistryCenter.getJobEventFullPath(), value);
    }

}
