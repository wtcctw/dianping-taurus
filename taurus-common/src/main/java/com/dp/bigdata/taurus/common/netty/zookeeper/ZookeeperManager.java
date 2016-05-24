package com.dp.bigdata.taurus.common.netty.zookeeper;

import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.framework.state.ConnectionStateListener;

import java.util.Set;

/**
 * Author   mingdongli
 * 16/5/18  下午3:46.
 */
public interface ZookeeperManager {

    Set<String> getScheduleNodes();

    Set<String> getTaskNodes(String taskId);

    void addConnectionStateListener(ConnectionStateListener listener);

    void addDataListener(TreeCacheListener listener);

}