package com.dp.bigdata.taurus.zookeeper.common.elect;

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
public class ZKCheckedEphemeral {

    private final Log logger = LogFactory.getLog(getClass());

    private String path;

    private String data;

    private ZooKeeper zkHandle;

    private boolean isSecure;

    private CreateCallback createCallback = new CreateCallback();

    private GetDataCallback getDataCallback = new GetDataCallback();

    private CountDownLatch latch = new CountDownLatch(1);

    KeeperException.Code result = KeeperException.Code.OK;

    public ZKCheckedEphemeral(String path, String data, ZooKeeper zkHandle, boolean isSecure) {
        this.path = path;
        this.data = data;
        this.zkHandle = zkHandle;
        this.isSecure = isSecure;
    }

    public void create() {

//        int index = path.indexOf('/', 1) == -1 ? path.length() : path.indexOf('/', 1);
//        String prefix = path.substring(0, index);
//        String suffix = path.substring(index, path.length());
//        logger.info(String.format("Path: %s, Prefix: %s, Suffix: %s", path, prefix, suffix));
//        logger.info(String.format("Creating %s (is it secure? %b)", path, isSecure));
//        createRecursive(prefix, suffix);
        createEphemeral();
        KeeperException.Code result = waitUntilResolved();
        logger.info(String.format("Result of znode creation is: %s", result));
        if (KeeperException.Code.OK != result) {
            throw ZkException.create(KeeperException.create(result));
        }
    }

//    private void createRecursive(String prefix, String suffix) {
//
//        logger.info("Path: %s, Prefix: %s, Suffix: %s".format(path, prefix, suffix));
//        if (suffix.isEmpty()) {
//            createEphemeral();
//        } else {
//            zkHandle.create(prefix, new byte[0], defaultAcls(isSecure), CreateMode.PERSISTENT, new AsyncCallback.StringCallback() {
//
//                        @Override
//                        public void processResult(int rc, String path, Object ctx, String name) {
//
//                            KeeperException.Code code = KeeperException.Code.get(rc);
//
//                            if (KeeperException.Code.CONNECTIONLOSS == code) {
//                                String suffix = (String) ctx;
//                                createRecursive(path, suffix);
//                            } else if (KeeperException.Code.OK == code || KeeperException.Code.NODEEXISTS == code) {
//                                logger.info(String.format("code is %s", code.toString()));
//                                // do nothing
//                            } else {
//                                setResult(KeeperException.Code.get(rc));
//                                logger.info(String.format("code is %s", code.toString()));
//                            }
//                        }
//                    },
//                    suffix);
//            // Update prefix and suffix
//            int index = suffix.indexOf('/', 1) == -1 ? suffix.length() : suffix.indexOf('/', 1);
//            // Get new prefix
//            String newPrefix = prefix + suffix.substring(0, index);
//            // Get new suffix
//            String newSuffix = suffix.substring(index, suffix.length());
//            createRecursive(newPrefix, newSuffix);
//        }
//    }

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

        ZKStringSerializer serializer = new ZKStringSerializer();
        zkHandle.create(path, serializer.serialize(data), defaultAcls(isSecure), CreateMode.EPHEMERAL, createCallback, null);
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
