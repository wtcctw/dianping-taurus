package com.cip.crane.zookeeper.common.elect;

import com.cip.crane.common.lock.LockActionWrapper;
import com.cip.crane.common.lock.LockActionWrapperImpl;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;

/**
 * Author   mingdongli
 * 16/3/15  下午6:15.
 */
public class ZKPair {

    private ZkConnection zkConnection;

    private ZkClient zkClient;

    public LockActionWrapper lockActionWrapper = new LockActionWrapperImpl();

    public ZKPair(ZkConnection zkConnection, ZkClient zkClient) {
        this.zkConnection = zkConnection;
        this.zkClient = zkClient;
    }

    public ZkConnection getZkConnection() {
        return zkConnection;
    }

    public void setZkConnection(ZkConnection zkConnection) {
        this.zkConnection = zkConnection;
    }

    public ZkClient getZkClient() {
        return zkClient;
    }

    public void setZkClient(ZkClient zkClient) {
        this.zkClient = zkClient;
    }

    public LockActionWrapper getLockActionWrapper() {
        return lockActionWrapper;
    }
}
