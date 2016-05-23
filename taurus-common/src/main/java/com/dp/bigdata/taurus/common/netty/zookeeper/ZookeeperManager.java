package com.dp.bigdata.taurus.common.netty.zookeeper;

import java.util.Set;

/**
 * Author   mingdongli
 * 16/5/18  下午3:46.
 */
public interface ZookeeperManager {

    Set<String> getScheduleNodes();

    Set<String> getTaskNodes(String taskId);
}
