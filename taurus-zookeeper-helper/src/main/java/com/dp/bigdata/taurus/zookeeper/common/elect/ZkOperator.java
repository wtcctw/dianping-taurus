package com.dp.bigdata.taurus.zookeeper.common.elect;

/**
 * Author   mingdongli
 * 16/3/17  上午11:31.
 */
public interface ZkOperator {

    String SCHEDULE_SCHEDULING = "taurus/taskscheduling";

    boolean exists(String path);

    void createEphemeral(String path);

    void delete(String path);
}
