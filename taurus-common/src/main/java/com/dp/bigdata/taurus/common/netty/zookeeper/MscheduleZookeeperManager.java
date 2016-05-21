package com.dp.bigdata.taurus.common.netty.zookeeper;

import com.dianping.cat.Cat;
import com.dp.bigdata.taurus.common.utils.IPUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author   mingdongli
 * 16/5/18  下午3:47.
 */
@Component
public class MscheduleZookeeperManager implements ZookeeperMananger, InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ExecutorService executor = Executors.newFixedThreadPool(1);

    private static final Object lockSchedule = new Object();

    private static final Object lockTask = new Object();

    private static final Set<String> scheduleNodes = new HashSet<String>();

    private static final Map<String, Set<String>> job2Nodes = new HashMap<String, Set<String>>();

    @Autowired
    private ZookeeperRegistryCenter zookeeperRegistryCenter;

    @Override
    public void afterPropertiesSet() throws Exception {

        logger.info("Taurus connect to ZK successfully.");
        registry();
        logger.info("Taurus registry to ZK successfully.");

        CuratorFramework curatorFramework = (CuratorFramework) zookeeperRegistryCenter.getRawClient();
        curatorFramework.getConnectionStateListenable().addListener(new ScheduleNodeStateListener(), executor);

        Map<String, Set<String>> nodes = zookeeperRegistryCenter.getJob2Nodes();
        if (nodes != null) {
            Cat.logEvent("NodesTask", String.valueOf(nodes.size()));
        }
        synchronized (lockTask) {
            job2Nodes.clear();
            job2Nodes.putAll(nodes);
        }

    }

    private void registry() {
        String address = IPUtils.getFirstNoLoopbackIP4Address();
        String localNodePath = this.zookeeperRegistryCenter.getSchedulePath() + "/" + address;
        zookeeperRegistryCenter.persistEphemeral(localNodePath, address, true);
        logger.info("Schedule Node : {} registry to ZK successfully.", address);
    }

    @Override
    public Set<String> getScheduleNodes() {
        synchronized (lockSchedule) {
            return Collections.unmodifiableSet(scheduleNodes);
        }
    }

    @Override
    public Set<String> getTaskNodes(String job) {
        synchronized (lockTask) {
            Set<String> nodes = job2Nodes.get(job);
            if (nodes != null) {
                return Collections.unmodifiableSet(nodes);
            }
            return nodes;
        }
    }

    public static class TaskNodeListenerManager extends AbstractListenerManager {

        private ZookeeperRegistryCenter zookeeperRegistryCenter;

        public TaskNodeListenerManager(ZookeeperRegistryCenter coordinatorRegistryCenter) {
            super(coordinatorRegistryCenter);
            this.zookeeperRegistryCenter = coordinatorRegistryCenter;
        }

        @Override
        public void start() {
            logger.info("TaskNodeListenerManager started.");
            listenTaskNode();
        }

        private void listenTaskNode() {
            addDataListener(new AbstractListener() {

                @Override
                protected void dataChanged(final CuratorFramework client, final TreeCacheEvent event, final String path) {

                    if (zookeeperRegistryCenter.isTaskNodePath(path)) {
                        try {
                            if (event.getType() == TreeCacheEvent.Type.NODE_ADDED) {
                                logger.info("NEW TASK node join : {}", zookeeperRegistryCenter.get(path));
                                Map<String, Set<String>> nodes = zookeeperRegistryCenter.getJob2Nodes();
                                synchronized (lockTask) {
                                    job2Nodes.clear();
                                    job2Nodes.putAll(nodes);
                                }
                            } else if (event.getType() == TreeCacheEvent.Type.NODE_REMOVED) {
                                logger.info("TASK node : {} DOWN.", path);
                                Map<String, Set<String>> nodes = zookeeperRegistryCenter.getJob2Nodes();
                                synchronized (lockTask) {
                                    job2Nodes.clear();
                                    job2Nodes.putAll(nodes);
                                }
                            }
                        } catch (Exception e) {
                            logger.error("TaskNode listener capture node change and get data failed .", e);
                        }
                    }
                }

            });
        }
    }

    public static class ScheduleNodeListenerManager extends AbstractListenerManager {

        private ZookeeperRegistryCenter zookeeperRegistryCenter;

        public ScheduleNodeListenerManager(ZookeeperRegistryCenter coordinatorRegistryCenter) {
            super(coordinatorRegistryCenter);
            this.zookeeperRegistryCenter = coordinatorRegistryCenter;
        }

        @Override
        public void start() {
            logger.info("ScheduleNodeListenerManager started.");
            listenScheduleNode();
        }

        private void listenScheduleNode() {
            addDataListener(new AbstractListener() {

                @Override
                protected void dataChanged(final CuratorFramework client, final TreeCacheEvent event, final String path) {

                    if (zookeeperRegistryCenter.isScheculePath(path)) {
                        try {
                            if (event.getType() == TreeCacheEvent.Type.NODE_ADDED) {
                                logger.info("NEW Schedule node join : {}", zookeeperRegistryCenter.get(path));

                                Set<String> nodes = zookeeperRegistryCenter.getScheduleNodes();
                                synchronized (lockSchedule) {
                                    scheduleNodes.clear();
                                    scheduleNodes.addAll(nodes);
                                }

                            } else if (event.getType() == TreeCacheEvent.Type.NODE_REMOVED) {
                                logger.info("Schedule node : {} DOWN.", path);
                                Set<String> nodes = zookeeperRegistryCenter.getScheduleNodes();
                                synchronized (lockSchedule) {
                                    scheduleNodes.clear();
                                    scheduleNodes.addAll(nodes);
                                }
                            }
                        } catch (Exception e) {
                            logger.error("ScheduleNodeListener capture data change and get data " + "failed.", e);
                        }
                    }
                }

            });
        }
    }

    class ScheduleNodeStateListener implements ConnectionStateListener {

        @Override
        public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {
            if (connectionState == ConnectionState.LOST) {
                logger.warn("Network is unreachable to ZK with a long time, stop schedule service" + ".");
                // quartzScheduler.destroy();
            } else if (connectionState == ConnectionState.SUSPENDED) {
                logger.warn("Network is unreachable to ZK. Reconnection.....");
            } else if (connectionState == ConnectionState.RECONNECTED) {
                logger.info("Network reachable to ZK,Reconnected to ZK.");
                registry();
            }
        }
    }

}
