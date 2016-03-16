package com.dp.bigdata.taurus.zookeeper.common.elect.lock;

/**
 * Author   mingdongli
 * 16/3/15  下午2:45.
 */
public interface LockActionWrapper {

    void doAction(LockAction action);
}
