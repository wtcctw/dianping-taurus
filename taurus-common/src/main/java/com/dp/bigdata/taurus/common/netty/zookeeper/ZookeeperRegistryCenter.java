package com.dp.bigdata.taurus.common.netty.zookeeper;

import com.dp.bigdata.taurus.common.netty.config.ZookeeperConfiguration;
import com.dp.bigdata.taurus.common.netty.exception.RegExceptionHandler;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Author   mingdongli
 * 16/5/18  下午3:38.
 */
@Component
public class ZookeeperRegistryCenter implements CoordinatorRegistryCenter {

    private static final Logger log = LoggerFactory.getLogger(ZookeeperRegistryCenter.class);

    private static final String NAMESPACE = "taurus";

    private static final String NODES = NAMESPACE + "/nodes";

    private static final String EVENT_PATH = NAMESPACE + "/nodes/event";

    private static final String SCHEDULE_PATH = NAMESPACE + "/nodes/schedule";

    private static final String TASK_PATH = NAMESPACE + "/nodes/task";

    private static final String EMPTY = "";

    @Autowired
    private ZookeeperConfiguration zkConfig;

    private CuratorFramework client;

    private TreeCache cache;

    public ZookeeperRegistryCenter() {
    }

    @PostConstruct
    public void init() {
        log.debug("MSchedule : zookeeper registry center init, server lists is: {}.", zkConfig.getServerLists());
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder().connectString(zkConfig.getServerLists()).retryPolicy(new ExponentialBackoffRetry(zkConfig.getBaseSleepTimeMilliseconds(), zkConfig.getMaxRetries(), zkConfig.getMaxSleepTimeMilliseconds()));
        if (0 != zkConfig.getSessionTimeoutMilliseconds()) {
            builder.sessionTimeoutMs(zkConfig.getSessionTimeoutMilliseconds());
        }
        if (0 != zkConfig.getConnectionTimeoutMilliseconds()) {
            builder.connectionTimeoutMs(zkConfig.getConnectionTimeoutMilliseconds());
        }
        if (!Strings.isNullOrEmpty(zkConfig.getDigest())) {
            builder.authorization("digest", zkConfig.getDigest().getBytes(Charset.forName("UTF-8"))).aclProvider(new ACLProvider() {

                @Override
                public List<ACL> getDefaultAcl() {
                    return ZooDefs.Ids.CREATOR_ALL_ACL;
                }

                @Override
                public List<ACL> getAclForPath(final String path) {
                    return ZooDefs.Ids.CREATOR_ALL_ACL;
                }
            });
        }
        client = builder.build();
        client.start();
        try {
            client.blockUntilConnected();
            initZkNodes();
            cacheData();
        } catch (final Exception ex) {
            RegExceptionHandler.handleException(ex);
        }

    }

    private void initZkNodes() throws Exception {
        persist(SCHEDULE_PATH, EMPTY);
//        persist(EVENT_PATH, EMPTY);
        persist(TASK_PATH, EMPTY);
    }

    private void cacheData() throws Exception {

        cache = new TreeCache(client, NODES);
        cache.start();
    }

    @Override
    @PreDestroy
    public void close() {
        if (null != cache) {
            cache.close();
        }
        waitForCacheClose();
        CloseableUtils.closeQuietly(client);
    }

    /* TODO 等待500ms, cache先关闭再关闭client, 否则会抛异常
     * 因为异步处理, 可能会导致client先关闭而cache还未关闭结束.
     * 等待Curator新版本解决这个bug.
     * BUG地址：https://issues.apache.org/jira/browse/CURATOR-157
     */
    private void waitForCacheClose() {
        try {
            Thread.sleep(500L);
        } catch (final InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public String get(final String key) {
        if (null == cache) {
            return null;
        }
        ChildData resultIncache = cache.getCurrentData(key);
        if (null != resultIncache) {
            return null == resultIncache.getData() ? null : new String(resultIncache.getData(), Charset.forName("UTF-8"));
        }
        return null;
    }

    public Set<String> getScheduleNodes() {
        if (null == cache) {
            return null;
        }
        Set<String> returnValue = new HashSet<String>();
        Map<String, ChildData> dataMap = cache.getCurrentChildren(SCHEDULE_PATH);
        Collection<ChildData> values = dataMap.values();
        if (values == null || values.size() == 0) {
            return returnValue;
        }

        for (ChildData data : values) {
            returnValue.add(new String(data.getData(), Charset.forName("UTF-8")));
        }

        return returnValue;
    }

    @Override
    public String getDirectly(final String key) throws InterruptedException, IOException, KeeperException {

        try {
            return new String(client.getData().forPath(key), Charset.forName("UTF-8"));
        } catch (Exception k) {
            //如果是connectionlose,尝试重试三次，如果不是直接抛出异常
            RegExceptionHandler.handleException(k);
        }
        return null;
    }

    public List<String> getChildrenKeys(final String key) {
        List<String> values = new ArrayList<String>();
        try {
            values = client.getChildren().forPath(key);
        } catch (Exception k) {
            RegExceptionHandler.handleException(k);
        }
        return values;
    }


    @Override
    public boolean isExisted(final String key) {

        try {
            return client.checkExists().forPath(key) != null;
        } catch (Exception k) {
            RegExceptionHandler.handleException(k);
        }
        return false;
    }

    @Override
    public void persist(final String key, final String value) {
        try {
            if (!isExisted(key)) {
                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath
                        (key, value.getBytes(Charset.forName("utf-8")));
            } else {
                update(key, value);
            }
        } catch (Exception x) {
            RegExceptionHandler.handleException(x);
        }

    }

    @Override
    public void update(final String key, final String value) {
        try {
            client.inTransaction().check().forPath(key).and().setData().forPath(key, value
                    .getBytes(Charset.forName("UTF-8"))).and().commit();
        } catch (Exception k) {
            RegExceptionHandler.handleException(k);
        }
    }

    @Override
    public void persistEphemeral(final String key, final String value) {
        try {
            if (isExisted(key)) {
                client.delete().deletingChildrenIfNeeded().forPath(key);
            }
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(key, value.getBytes(Charset.forName("UTF-8")));
        } catch (final Exception ex) {
            RegExceptionHandler.handleException(ex);
        }
    }

    @Override
    public void persistEphemeral(String key, String value, boolean overwrite) {
        try {
            if (overwrite) {
                persistEphemeral(key, value);
            } else {
                if (!isExisted(key)) {
                    client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(key, value.getBytes(Charset.forName("UTF-8")));
                }
            }
        } catch (final Exception ex) {
            RegExceptionHandler.handleException(ex);
        }
    }

    @Override
    public void persistEphemeralSequential(final String key) {
        try {
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(key);
        } catch (final Exception ex) {
            RegExceptionHandler.handleException(ex);
        }
    }

    @Override
    public void remove(final String key) {
        try {
            client.delete().deletingChildrenIfNeeded().forPath(key);
        } catch (final Exception ex) {
            RegExceptionHandler.handleException(ex);
        }
    }

    @Override
    public long getRegistryCenterTime(final String key) {
        long result = 0L;
        try {
            String path = client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(key);
            result = client.checkExists().forPath(path).getCtime();
        } catch (final Exception ex) {
            RegExceptionHandler.handleException(ex);
        }
        Preconditions.checkState(0L != result, "Cannot get registry center time.");
        return result;
    }

    @Override
    public Object getRawClient() {
        return client;
    }

    @Override
    public Object getRawCache() {
        return cache;
    }

    public String getJobEventFullPath() {
        return EVENT_PATH;
    }

    public String getSchedulePath() {
        return SCHEDULE_PATH;
    }

    public String getTaskPath() {
        return TASK_PATH;
    }

    public boolean isJobEventPath(String path) {
        return path != null && path.contains(EVENT_PATH);
    }

    public boolean isScheculePath(String path) {
        return path != null && path.contains(SCHEDULE_PATH);
    }

    public boolean isTaskNodePath(String path) {
        return path != null && path.contains(TASK_PATH);
    }

    public Map<String, Set<String>> getJob2Nodes() {

        Map<String, ChildData> dataMap = cache.getCurrentChildren(TASK_PATH);
        // node : jobs, 首先获取task节点和job信息的对应关系
        Map<String, Set<String>> node2Jobs = new HashMap<String, Set<String>>();
        if (dataMap == null || dataMap.size() == 0) {
            return node2Jobs;
        }
        node2Jobs = Maps.transformValues(dataMap, new Function<ChildData, Set<String>>() {
            @Override
            public Set<String> apply(ChildData childData) {
                String jobunicodes = new String(childData.getData(), Charset.forName("UTF-8"));
                return Sets.newHashSet(jobunicodes.split(","));
            }
        });

        //job : nodes,获取job和节点的对应关系
        Map<String, Set<String>> job2Nodes = new HashMap<String, Set<String>>();
        for (Map.Entry<String, Set<String>> entry : node2Jobs.entrySet()) {
            String node = entry.getKey();
            Set<String> jobs = entry.getValue();
            if (jobs != null && jobs.size() > 0) {
                for (String job : jobs) {
                    if (job2Nodes.containsKey(job)) {
                        Set<String> nodes = job2Nodes.get(job);
                        nodes.add(node);
                    } else {
                        Set<String> nodes = new HashSet<String>();
                        nodes.add(node);
                        job2Nodes.put(job, nodes);
                    }
                }
            }
        }
        return job2Nodes;
    }


    public ZookeeperConfiguration getZkConfig() {
        return zkConfig;
    }

    public void setZkConfig(ZookeeperConfiguration zkConfig) {
        this.zkConfig = zkConfig;
    }

}
