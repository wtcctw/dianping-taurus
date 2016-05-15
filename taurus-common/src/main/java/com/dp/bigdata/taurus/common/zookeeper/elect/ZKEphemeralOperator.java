package com.dp.bigdata.taurus.common.zookeeper.elect;

import org.I0Itec.zkclient.exception.ZkException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Author   mingdongli
 * 16/3/15  下午4:29.
 */
public class ZKEphemeralOperator implements ZkOperator{

    private final Log logger = LogFactory.getLog(getClass());

    private String path;

    private String data;

    private ZooKeeper zkHandle;

    private boolean isSecure;

    private CreateCallback createCallback = new CreateCallback();

    private GetDataCallback getDataCallback = new GetDataCallback();

    private CountDownLatch latch = new CountDownLatch(1);

    KeeperException.Code result = KeeperException.Code.OK;

    public ZKEphemeralOperator(String path, String data, ZooKeeper zkHandle, boolean isSecure) {
        this.path = path;
        this.data = data;
        this.zkHandle = zkHandle;
        this.isSecure = isSecure;
    }

    public void create() {

        createEphemeral();
        KeeperException.Code result = waitUntilResolved();
        logger.info(String.format("Result of znode creation is: %s", result));
        if (KeeperException.Code.OK != result) {
            throw ZkException.create(KeeperException.create(result));
        }
    }

    private KeeperException.Code waitUntilResolved() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            logger.error(e);
        }
        return result;
    }

    private void setResult(KeeperException.Code code) {
        result = code;
        latch.countDown();
    }

    private void createEphemeral() {

        createEphemeral(path);
    }

    private List<ACL> defaultAcls(boolean isSecure) {

        if (isSecure) {
            List<ACL> list = new ArrayList<ACL>();
            list.addAll(ZooDefs.Ids.CREATOR_ALL_ACL);
            list.addAll(ZooDefs.Ids.READ_ACL_UNSAFE);
            return list;
        } else {
            return ZooDefs.Ids.OPEN_ACL_UNSAFE;
        }

    }

    @Override
    public boolean exists(String path) {
        throw new UnsupportedOperationException("not support");
    }

    @Override
    public void createEphemeral(String path) {

        ZKStringSerializer serializer = new ZKStringSerializer();
        zkHandle.create(path, serializer.serialize(data), defaultAcls(isSecure), CreateMode.EPHEMERAL, createCallback, null);
    }

    @Override
    public void createPersistent(String path) {
        throw new UnsupportedOperationException("not support");
    }

    @Override
    public void delete(String path) {

        throw new UnsupportedOperationException("not support");
    }

    private class CreateCallback implements AsyncCallback.StringCallback {

        @Override
        public void processResult(int rc, String path, Object ctx, String name) {

            KeeperException.Code code = KeeperException.Code.get(rc);

            if (KeeperException.Code.CONNECTIONLOSS == code) {
                createEphemeral();
            } else if (KeeperException.Code.NODEEXISTS == code) {
                zkHandle.getData(path, false, getDataCallback, null);
            } else {
                setResult(code);
            }

            logger.info(String.format("code is %s", code.toString()));


        }
    }

    private class GetDataCallback implements AsyncCallback.DataCallback {

        @Override
        public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {

            KeeperException.Code code = KeeperException.Code.get(rc);

            if (KeeperException.Code.NONODE == code) {
                createEphemeral();
            } else if (KeeperException.Code.OK == code) {
                if (stat.getEphemeralOwner() != zkHandle.getSessionId()) {
                    setResult(KeeperException.Code.NODEEXISTS);
                } else {
                    setResult(KeeperException.Code.OK);
                }
            } else {
                setResult(code);
            }
        }
    }

}
