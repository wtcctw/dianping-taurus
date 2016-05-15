package com.dp.bigdata.taurus.common.zookeeper.elect;

/**
 * Author   mingdongli
 * 16/3/17  上午11:31.
 */
public interface ZkOperator {

    boolean exists(String path);

    void createEphemeral(String path);

    void createPersistent(String path);

    void delete(String path);
}
