package com.dp.bigdata.taurus.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dp.bigdata.taurus.client.common.constants.ZkPathConstants;
import com.dp.bigdata.taurus.client.common.util.DelayQ;
import com.dp.bigdata.taurus.client.common.util.IPUtil;
import com.dp.bigdata.taurus.client.election.LeaderExecutionCallback;
import com.dp.bigdata.taurus.client.schedule.DefaultSelector;
import com.dp.bigdata.taurus.client.schedule.QuartzScheduler;
import com.dp.bigdata.taurus.common.lion.AbstractLionPropertyInitializer;
import com.dp.bigdata.taurus.common.lion.LionDynamicConfig;
import com.dp.bigdata.taurus.common.netty.config.ZookeeperConfiguration;
import com.dp.bigdata.taurus.common.netty.zookeeper.AbstractListener;
import com.dp.bigdata.taurus.common.netty.zookeeper.AbstractListenerManager;
import com.dp.bigdata.taurus.common.netty.zookeeper.ZookeeperRegistryCenter;
import com.dp.bigdata.taurus.common.structure.BooleanConverter;
import com.dp.bigdata.taurus.common.structure.Converter;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * User: hongbin03
 * Date: 16/1/3
 * Time: 下午3:19
 * MailTo: hongbin03@meituan.com
 */
public class TaskNode extends AbstractLionPropertyInitializer<Boolean>{
    private static final Logger logger = LoggerFactory.getLogger(TaskNode.class);
    private static final String SELF_SCHEDULE_SWITCH = "taurus.client.selfschedule";
    private static final Set<String> groups = ScheduleManager.getGroup2Jobs().keySet();
    private static final ExecutorService executor = Executors.newFixedThreadPool(1);
    private static final DelayQ delayQ = new DelayQ();
    private static final int DELAY_ITEM = 1;
    private static final int sessionTimeout = 4000;
    private volatile boolean isLeader = false;
    private final ZookeeperRegistryCenter zookeeperRegistryCenter = new ZookeeperRegistryCenter();
    private final AtomicReference<LeaderLatch> latchAtomicReference = new AtomicReference<LeaderLatch>();
    private final AtomicReference<QuartzScheduler> quartzSchedulerAtomicReference = new AtomicReference<QuartzScheduler>();
    private static final Object lockSchedule = new Object();
    private static final Set<String> scheduleNodes = new HashSet<String>();
    private ZookeeperConfiguration zookeeperConfiguration;
    private int taskAcceptorPort;

    public TaskNode(ZookeeperConfiguration zookeeperConfiguration, int taskAcceptPort) {
        this.zookeeperConfiguration = zookeeperConfiguration;
        this.taskAcceptorPort = taskAcceptPort;
    }

    public void init() {
        try {
            lionDynamicConfig = new LionDynamicConfig();
            super.afterPropertiesSet();
        } catch (Exception e) {
            lionValue = getDefaultValue();
        }
        logger.info("TaskNode init zookeeper registry center begin.");
        zookeeperRegistryCenter.setZkConfig(zookeeperConfiguration);
        zookeeperRegistryCenter.init();

        CuratorFramework curatorFramework = (CuratorFramework) zookeeperRegistryCenter.getRawClient();
        curatorFramework.getConnectionStateListenable().addListener(new ScheduleNodeStateListener(), executor);

        ScheduleNodeListenerManager scheduleNodeListenerManager = new ScheduleNodeListenerManager(zookeeperRegistryCenter);
        scheduleNodeListenerManager.start();
        prepareElection();
        logger.info("TaskNode init zookeeper registry center successfully.");
        registry();
        Set<String> scheduleNodes = getScheduleNodesFromRegistry();
        logger.info("Schedule nodes  is : {}.", scheduleNodes);
        resetScheduleNodes(scheduleNodes);
        logger.info("MSchedule registry to ZK successfully.");
    }


    class OnLeader implements LeaderExecutionCallback {

        @Override
        public void execute() {
            logger.info("Becoming Leader...");
            isLeader = true;
            if (lionValue && isAllScheduleCrashed()) {
                logger.info("Fair Schedule service start.....");
                QuartzScheduler quartzScheduler = new QuartzScheduler(TaskNode.this, new DefaultSelector());
                quartzScheduler.init();
                quartzSchedulerAtomicReference.set(quartzScheduler);
            }
        }

    }

    @Override
    protected String getKey() {
        return SELF_SCHEDULE_SWITCH;
    }

    @Override
    protected Boolean getDefaultValue() {
        return false;
    }

    @Override
    protected Converter<Boolean> getConvert() {
        return new BooleanConverter();
    }

    private boolean isAllScheduleCrashed() {
        return getScheduleNodes().size() == 0 ? true : false;
    }

    public void destroy() {
        zookeeperRegistryCenter.close();
        executor.shutdown();
        //释放调度权限
        checkAndReleaseScheduleRight();
    }

    public Set<String> getScheduleNodes() {
        synchronized (lockSchedule) {
            return scheduleNodes;
        }
    }

    class ScheduleNodeListenerManager extends AbstractListenerManager {

        private final ZookeeperRegistryCenter zookeeperRegistryCenter;

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
                                String scheduleNode = zookeeperRegistryCenter.get(path);
                                logger.info("NEW Schedule node join : {}", scheduleNode);
                                checkAndReleaseScheduleRight();
                                Set<String> nodes = getScheduleNodesFromRegistry();
                                resetScheduleNodes(nodes);
                            } else if (event.getType() == TreeCacheEvent.Type.NODE_REMOVED) {
                                logger.info("Schedule node : {} DOWN.", path);
                                Set<String> nodes = getScheduleNodesFromRegistry();
                                resetScheduleNodes(nodes);
                                checkAndFireAllScheduleNodesCrashedEvent(nodes);
                            }
                        } catch (Exception e) {
                            logger.error("ScheduleNodeListener capture data change and get data " + "failed.", e);
                        }
                    }
                }

            });
        }


    }

    /**
     * 当所有的schedulenodes crashed，则向延迟队列写入事件来标明该事件
     *
     * @param nodes
     */
    private void checkAndFireAllScheduleNodesCrashedEvent(Set<String> nodes) {
        if (nodes.isEmpty()) {
            logger.warn("Schedule nodes all crashed!");
            delayQ.addDelayedItem(DELAY_ITEM, sessionTimeout, TimeUnit.MILLISECONDS, new AfterLeaderElection());
        }
    }

    private Set<String> getScheduleNodesFromRegistry() {
        return zookeeperRegistryCenter.getScheduleNodes();
    }

    private void resetScheduleNodes(Set<String> nodes) {
        synchronized (lockSchedule) {
            scheduleNodes.clear();
            scheduleNodes.addAll(nodes);
        }
    }


    private void checkAndReleaseScheduleRight() {
        delayQ.removeItem(DELAY_ITEM);
        QuartzScheduler quartzScheduler;
        if ((quartzScheduler = quartzSchedulerAtomicReference.getAndSet(null)) != null) {
            logger.info("Fair Task Server give up the right to schedule.");
            quartzScheduler.destroy();
        }
    }


    class AfterLeaderElection implements DelayQ.Cleaner {

        @Override
        public void doSomething() throws InterruptedException {
            logger.info("All schedule nodes crashed with session timeout : {}.", sessionTimeout);
            if (lionValue && isLeader()) {
                logger.info("Fair Schedule service start.....");
                QuartzScheduler quartzScheduler = new QuartzScheduler(TaskNode.this, new DefaultSelector());
                quartzScheduler.init();
                quartzSchedulerAtomicReference.set(quartzScheduler);
            }
        }
    }

    /**
     * @return
     */
    public boolean isLeader() {
        return isLeader;
    }

    class ScheduleNodeStateListener implements ConnectionStateListener {


        @Override
        public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {
            if (connectionState == ConnectionState.LOST) {
                logger.warn("Network is unreachable to ZK with a long time, connection state LOST");
                isLeader = false;
            } else if (connectionState == ConnectionState.SUSPENDED) {
                logger.warn("Network is unreachable to ZK. Reconnection.....");
            } else if (connectionState == ConnectionState.RECONNECTED) {
                logger.info("Network reachable to ZK,Reconnected to ZK.");
                registry();
                executeInLeader();
            }
        }
    }

    /**
     * 选举Leader节点，Leader当选后会回调OnLeader.execute函数，如果没有当选则会阻塞
     */
    public void executeInLeader() {

        String group = (String) groups.toArray()[0];
        String electionLatchPath = String.format(ZkPathConstants.JOB_LEADER_ELECTION_LATCH, group);

        //当出现网络故障时，避免假死后该节点再次注册时阻塞，需要先close掉老的latch！
        LeaderLatch oldLatch = latchAtomicReference.get();
        if (oldLatch != null) {
            try {
                oldLatch.close();
            } catch (IOException e) {
                logger.warn("LeaderLatch close encounter error.", e);
            }
        }
        //clear
        latchAtomicReference.set(null);

        try {
            LeaderLatch latch = new LeaderLatch((CuratorFramework) zookeeperRegistryCenter.getRawClient(), electionLatchPath);
            //reset
            latchAtomicReference.set(latch);
            latch.start();
            latch.await();
            new OnLeader().execute();
        } catch (final Exception ex) {
            if (ex instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            } else {
                throw new RuntimeException(ex);
            }
        }
    }


    private void registry() {
        String address = StringUtils.defaultIfEmpty(IPUtil.getAddress(), IPUtil.getLocalHost());
        String addressAndPort = address + ":" + taskAcceptorPort;
        createNodePathUnderJobUnicode(addressAndPort);
        checkAndCreateJobNodePath(addressAndPort);
        logger.info("Task Node : {} registry to ZK successfully.", address);
    }


    /**
     * 注意是检查zk节点是否存在，如果不存在则去创建
     */
    private void prepareElection() {
        checkAndCreateJobInfoPath();
        checkAndCreateLeaderElectionLatchPath();
    }

    /**
     * 创建 /jobs/{groupName}/static 节点，该目录下是属于该组的 job，以jobunicode表示
     */
    private void checkAndCreateJobInfoPath() {
        for (String group : groups) {
            logger.info("Check and create Job info path : {}.", group);
            String jobInfoPath = String.format(ZkPathConstants.JOB_CRON_ROOT_PATH, group);
            zookeeperRegistryCenter.persist(jobInfoPath, "");
        }
    }

    /**
     * 创建  /jobs/{groupName}/election/latch 用于组内Leader选举
     * 注意⚠:
     * 即使存在多组，Leader选举时，我们只取其中一个组作为Leader选举的Latch
     */
    private void checkAndCreateLeaderElectionLatchPath() {
        List<String> randomGroups = new ArrayList<String>();
        randomGroups.addAll(groups);
        String group = randomGroups.get(0);
        logger.info("Check and create leader election latch path : {}.", group);
        String electionLatchPath = String.format(ZkPathConstants.JOB_LEADER_ELECTION_LATCH, group);
        zookeeperRegistryCenter.persist(electionLatchPath, "");
    }

    /**
     * 创建 /jobs/{groupName}/node/{addressAndPort} 瞬时节点  内容 {addressAndPort}
     *
     * @param addressAndPort
     */
    private void checkAndCreateJobNodePath(String addressAndPort) {
        for (String group : groups) {
            logger.info("Check and create job node path {}.", group);
            String node = String.format(ZkPathConstants.JOB_NODE_INFO_PATH, group, addressAndPort);
            zookeeperRegistryCenter.persistEphemeral(node, addressAndPort, true);
        }
    }

    /**
     * 创建 /nodes/task/{addressAndPort} 瞬时节点，内容 {registeredJobs}
     *
     * @param addressAndPort
     */
    private void createNodePathUnderJobUnicode(String addressAndPort) {
        String localNodePath = zookeeperRegistryCenter.getTaskPath() + "/" + addressAndPort;
        String registeredJobs = ScheduleManager.getRegisteredJobs();
        zookeeperRegistryCenter.persistEphemeral(localNodePath, registeredJobs, true);
    }

    /**
     * cron 信息目录：/job/{groupName}/static/{jobUnicode}
     * 值为cron信息和下发数据:{"cron":"xxxxx","data":"xxxx"}
     * <p/>
     * 注意！ 该函数返回值允许为null，调用时需要做好非空判断
     *
     * @param jobUnicode
     * @return
     */
    public String getCronInfosByJobUnicode(final String jobUnicode) {
        String groupName = jobUnicode.substring(0, jobUnicode.lastIndexOf("_"));
        String jobInfoPath = String.format(ZkPathConstants.JOB_CRON_INTO_PATH, groupName, jobUnicode);
        String cronData = zookeeperRegistryCenter.get(jobInfoPath);
        if (cronData == null) {
            logger.warn("Cron information is empty for path : {}", jobInfoPath);
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(cronData);
        return (String) jsonObject.get("cron");
    }


    public String getJobStatusByJobUnicode(final String jobUnicode) {
        String groupName = jobUnicode.substring(0, jobUnicode.lastIndexOf("_"));
        String jobInfoPath = String.format(ZkPathConstants.JOB_CRON_INTO_PATH, groupName, jobUnicode);
        String cronData = zookeeperRegistryCenter.get(jobInfoPath);
        if (cronData == null) {
            logger.warn("Cron information is empty for path : {}", jobInfoPath);
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(cronData);
        return (String) jsonObject.get("status");
    }

    /**
     * 获取task节点信息
     *
     * @return
     */
    public Set<String> getTaskNodes() {
        Set<String> taskNodes = new HashSet<String>();
        for (String group : groups) {
            String node = String.format(ZkPathConstants.JOB_NODE_PATH, group);
            taskNodes.addAll(zookeeperRegistryCenter.getChildrenKeys(node));
        }
        return taskNodes;
    }

}
