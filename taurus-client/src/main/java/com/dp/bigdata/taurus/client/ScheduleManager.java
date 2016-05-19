package com.dp.bigdata.taurus.client;

import com.dp.bigdata.taurus.client.callback.CallbackAddressSelect;
import com.dp.bigdata.taurus.client.callback.DirectAddressSelector;
import com.dp.bigdata.taurus.client.callback.ZKSelector;
import com.dp.bigdata.taurus.client.processor.ScheduleProcessor;
import com.dp.bigdata.taurus.client.processor.TaskProcessor;
import com.dp.bigdata.taurus.common.netty.NettyRemotingClient;
import com.dp.bigdata.taurus.common.netty.NettyRemotingServer;
import com.dp.bigdata.taurus.common.netty.config.NettyClientConfig;
import com.dp.bigdata.taurus.common.netty.config.NettyServerConfig;
import com.dp.bigdata.taurus.common.netty.config.ZookeeperConfiguration;
import com.dp.bigdata.taurus.common.netty.processor.NettyRequestProcessor;
import com.dp.bigdata.taurus.common.netty.protocol.CommandType;
import com.dp.bigdata.taurus.common.netty.protocol.ScheduleTask;
import com.dp.bigdata.taurus.common.utils.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by yangguiliang on 14-8-18.
 */
public class ScheduleManager {

    private int maxAcceptorPort = 8430;

    private int minAcceptorPort = 8410;

    private int defaultAcceptorPort = 8383;

    private int maxTaskThread = 30;

    private int sessionTimeOut = 60000;

    private int zkConnectionTimeoutMs = 3000;

    private int maxRetryTimes = 3;

    private int baseSleepMs = 3000;

    private int detectInterval = 60;

    private String namespace = "taurus";

    private int taskCallBackPort = 8383;

    private int taskAcceptorPort = 8383;

    //接收指令请求
    private NettyRemotingServer server;

    //反馈执行结果
    private NettyRemotingClient client;

    private TaskCallBacker taskCallBacker;

    private CallbackAddressSelect selector;

    private TaskNode taskNode;

    private Map<String, ITaskHandler> initedJobs;

    //默认使用zk来发现服务节点
    private int useZkDetectNodes = 0;

    private static ConcurrentHashMap<String, ITaskHandler> jobs = new ConcurrentHashMap<String, ITaskHandler>();
    //任务分组对应的job
    private static ConcurrentHashMap<String, TreeSet<String>> group2Jobs = new ConcurrentHashMap<String, TreeSet<String>>();

    private static Logger log = LoggerFactory.getLogger(ScheduleManager.class);

    public static void register(String jobUniqueCode, ITaskHandler handler) {
        jobs.putIfAbsent(jobUniqueCode, handler);
    }

    public static void unregister(String jobUniqueCode) {
        jobs.remove(jobUniqueCode);
    }

    public static ITaskHandler getTaskHandler(String jobUniqueCode) {
        if (!jobs.containsKey(jobUniqueCode)) {
            log.error("mschedule-client:" + jobUniqueCode + "未注册服务");
            return null;
        }
        return jobs.get(jobUniqueCode);
    }

    public static String getRegisteredJobs() {
        StringBuilder jobUniqueCodes = new StringBuilder();
        Iterator<String> iterator = jobs.keySet().iterator();
        if (iterator.hasNext()) {
            jobUniqueCodes.append(iterator.next());
        }
        while (iterator.hasNext()) {
            jobUniqueCodes.append(",").append(iterator.next());
        }
        return jobUniqueCodes.toString();
    }

    public static Set<String> getJobs() {
        return jobs.keySet();
    }

    public void init() throws Exception {
        //注册job
        if (initedJobs != null) {
            jobs.putAll(initedJobs);
        }
        //分组
        Set<String> jobUnicodes = jobs.keySet();
        for (String jobUnicode : jobUnicodes) {
            String group = jobUnicode;
            if (group2Jobs.containsKey(group)) {
                group2Jobs.get(group).add(jobUnicode);
            } else {
                TreeSet<String> jobus = new TreeSet<String>();
                jobus.add(jobUnicode);
                group2Jobs.put(group.trim(), jobus);
            }
        }

        //0表示使用zk去获取节点方式
        if (isZKMode()) {
            log.info("Use Zookeeper to detect Schedule nodes.");
            //初始化TaskNode
            ZookeeperConfiguration zookeeperConfiguration = new ZookeeperConfiguration();
            zookeeperConfiguration.afterPropertiesSet();
            zookeeperConfiguration.setBaseSleepTimeMilliseconds(this.getBaseSleepMs());
            zookeeperConfiguration.setConnectionTimeoutMilliseconds(this.getZkConnectionTimeoutMs());
            zookeeperConfiguration.setMaxRetries(this.getMaxRetryTimes());
            zookeeperConfiguration.setNamespace(this.getNamespace());
            zookeeperConfiguration.setSessionTimeoutMilliseconds(this.getSessionTimeOut());
            this.taskNode = new TaskNode(zookeeperConfiguration, taskAcceptorPort);
            this.taskNode.init();
            this.selector = new ZKSelector(taskNode);
        } else {
            log.info("Use direct address to callback");
            this.selector = new DirectAddressSelector();
        }

        //server
        NettyServerConfig serverConfig = new NettyServerConfig();
        serverConfig.setListenPort(taskAcceptorPort);
        server = new NettyRemotingServer(serverConfig);

        //client
        NettyClientConfig clientConfig = new NettyClientConfig();
        clientConfig.setConnectPort(taskCallBackPort);
        client = new NettyRemotingClient(clientConfig);
        this.taskCallBacker = new TaskCallBacker();
        this.taskCallBacker.setPort(this.getTaskCallBackPort());
        this.taskCallBacker.setSelector(selector);
        this.taskCallBacker.setClient(client);

        ScheduleProcessor scheduleProcessor = new ScheduleProcessor(taskCallBacker, maxTaskThread >> 1);
        TaskProcessor taskAcceptor = new TaskProcessor(maxTaskThread >> 1);

        ExecutorService scheduleService = Executors.newFixedThreadPool(maxTaskThread >> 1);
        server.registerProcessor(CommandType.ScheduleSendTask, scheduleProcessor, scheduleService);
        server.registerProcessor(CommandType.TaskSendResult, taskAcceptor, scheduleService);

        client.start();
        server.start();

        //构造callback,在task节点jvm退出时，会回调schedule
        Runtime.getRuntime().addShutdownHook(new JVMQuitCallbackThread());

        //服务启动ok，选主
        if (isZKMode()) {
            selectLeader();
        }
    }


    private void selectLeader(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                taskNode.executeInLeader();
            }
        }).start();
    }

    private boolean isZKMode() {
        return useZkDetectNodes == 0;
    }

    public void destroy() {
        log.info("Destroy task node resources: zk and threadpool.");
        if (isZKMode()) {
            this.taskNode.destroy();
        }
    }

    public int getMaxTaskThread() {
        return maxTaskThread;
    }

    public void setMaxTaskThread(int maxTaskThread) {
        this.maxTaskThread = maxTaskThread;
    }

    public int getSessionTimeOut() {
        return sessionTimeOut;
    }

    public void setSessionTimeOut(int sessionTimeOut) {
        this.sessionTimeOut = sessionTimeOut;
    }

    public int getDetectInterval() {
        return detectInterval;
    }

    public void setDetectInterval(int detectInterval) {
        this.detectInterval = detectInterval;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public int getTaskCallBackPort() {
        return taskCallBackPort;
    }

    public void setTaskCallBackPort(int taskCallBackPort) {
        this.taskCallBackPort = taskCallBackPort;
    }

    public int getTaskAcceptorPort() {
        return taskAcceptorPort;
    }

    public void setTaskAcceptorPort(int taskAcceptorPort) {
        if ((taskAcceptorPort >= minAcceptorPort && taskAcceptorPort <= maxAcceptorPort) || taskAcceptorPort == defaultAcceptorPort) {
        } else {
            throw new IllegalArgumentException("taskAcceptorPort must in 8410~8430,include 8410 and 8430,default " + "8383");
        }
        this.taskAcceptorPort = taskAcceptorPort;
    }

    public TaskCallBacker getTaskCallBacker() {
        return taskCallBacker;
    }

    public void setTaskCallBacker(TaskCallBacker taskCallBacker) {
        this.taskCallBacker = taskCallBacker;
    }

    public TaskNode getTaskNode() {
        return taskNode;
    }

    public void setTaskNode(TaskNode taskNode) {
        this.taskNode = taskNode;
    }

    public Map<String, ITaskHandler> getInitedJobs() {
        return initedJobs;
    }

    public void setInitedJobs(Map<String, ITaskHandler> initedJobs) {
        this.initedJobs = initedJobs;
    }

    public int getZkConnectionTimeoutMs() {
        return zkConnectionTimeoutMs;
    }

    public void setZkConnectionTimeoutMs(int zkConnectionTimeoutMs) {
        this.zkConnectionTimeoutMs = zkConnectionTimeoutMs;
    }

    public int getMaxRetryTimes() {
        return maxRetryTimes;
    }

    public void setMaxRetryTimes(int maxRetryTimes) {
        this.maxRetryTimes = maxRetryTimes;
    }

    public int getBaseSleepMs() {
        return baseSleepMs;
    }

    public void setBaseSleepMs(int baseSleepMs) {
        this.baseSleepMs = baseSleepMs;
    }

    public int getUseZkDetectNodes() {
        return useZkDetectNodes;
    }

    public void setUseZkDetectNodes(int useZkDetectNodes) {
        this.useZkDetectNodes = useZkDetectNodes;
    }


    public static ConcurrentHashMap<String, TreeSet<String>> getGroup2Jobs() {
        return group2Jobs;
    }

    class JVMQuitCallbackThread extends Thread {

        @Override
        public void run() {
            log.info("Before taskNode quit,check executing task and callback result.");
            Pair<NettyRequestProcessor, ExecutorService> procesor = server.getProcessorByCommandType(CommandType.ScheduleSendTask);
            ScheduleProcessor scheduleProcessor = (ScheduleProcessor) procesor.getFirst();
            ConcurrentHashMap<String, Pair<Future, ScheduleTask>> executingTasks = scheduleProcessor.getExecutingTasksAndResultFuture();
            Collection<Pair<Future, ScheduleTask>> entrys = executingTasks.values();
            for (Pair<Future, ScheduleTask> entry : entrys) {
                if (!entry.getFirst().isDone()) {
                    log.warn("Task: {} in executing, callback to schedule manager with status " +
                            "code UNKNOWN" +
                            ".", entry.getSecond());
                    try {
                        taskCallBacker.completeTask(entry.getSecond(), 4);
                    } catch (Exception e) {
                        log.warn("Task callback encounter error.", e);
                    }
                }
            }
            server.shutdown();
            client.shutdown();
        }
    }
}
