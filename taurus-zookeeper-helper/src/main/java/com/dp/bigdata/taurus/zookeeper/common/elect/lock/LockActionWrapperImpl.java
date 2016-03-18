package com.dp.bigdata.taurus.zookeeper.common.elect.lock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Author   mingdongli
 * 16/3/15  下午2:49.
 */
public class LockActionWrapperImpl implements LockActionWrapper{

    protected ReentrantLock lock = new ReentrantLock();

    @Override
    public void doAction(LockAction action){

        lock.lock();
        try {
            action.doAction();
        } finally {
            lock.unlock();
        }
    }
}
