package com.dp.bigdata.taurus.client.schedule;

import com.dp.bigdata.taurus.client.ScheduleManager;
import com.dp.bigdata.taurus.client.TaskNode;
import com.dp.bigdata.taurus.client.common.util.IPUtil;
import com.dp.bigdata.taurus.client.common.util.TraceGenerator;
import com.dp.bigdata.taurus.common.netty.NettyRemotingClient;
import com.dp.bigdata.taurus.common.netty.config.NettyClientConfig;
import com.dp.bigdata.taurus.common.netty.exception.RemotingSendRequestException;
import com.dp.bigdata.taurus.common.netty.protocol.PeerTask;
import org.apache.commons.lang.StringUtils;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * 依靠quartz调度
 * User: hongbin03
 * Date: 16/1/23
 * Time: 下午7:37
 * MailTo: hongbin03@meituan.com
 */
public class QuartzScheduler {

    private static final Logger logger = LoggerFactory.getLogger(QuartzScheduler.class);
    private Map<String, NettyRemotingClient> channels = new HashMap<String, NettyRemotingClient>();
    private Scheduler scheduler;
    private final TaskNode taskNode;
    private Selector selector;


    public QuartzScheduler(TaskNode taskNode, Selector selector) {
        this.taskNode = taskNode;
        this.selector = selector;
    }

    public void init() {
        initScheduler();
        initJobInfos();
        initChannels();
    }

    public void destroy() {
        try {
            //关闭调度服务
            scheduler.shutdown();
            // 关闭channel
            Collection<NettyRemotingClient> clients = channels.values();
            for (NettyRemotingClient client : clients) {
                client.shutdown();
            }
        } catch (SchedulerException e) {
            //ignore
        }
    }

    /**
     * 初始化quartz服务
     */
    private void initScheduler() {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        try {
            scheduler = schedulerFactory.getScheduler();
            scheduler.setJobFactory(new ScheduleJobFactory());
            scheduler.start();
        } catch (Exception e) {
            logger.error("Init schedule engine failed.", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据job信息，初始化Trigger信息
     */
    private void initJobInfos() {

        Set<String> jobUnicodes = ScheduleManager.getJobs();
        for (String jobUnicode : jobUnicodes) {
            logger.info("Init cron engine for job : {}.", jobUnicode);
            String jobStatus = taskNode.getJobStatusByJobUnicode(jobUnicode);
            if(jobStatus == null || jobStatus.equalsIgnoreCase("stopped")){
                logger.info("Job {} has been stopped.", jobUnicode);
                continue;
            }
            JobKey jobKey = this.getJobKey(jobUnicode);
            JobDetail jobDetail = newJob(QuartzScheduleJob.class).withIdentity(jobKey).usingJobData("JOB_UNIQUE_CODE", jobUnicode).build();
            String cronExpression = taskNode.getCronInfosByJobUnicode(jobUnicode);
            if (cronExpression == null) {
                logger.error("{} cant found cron expression.", jobUnicode);
                continue;
            }
            CronScheduleBuilder builder = cronSchedule(cronExpression);
            //以当前时间为触发频率立刻触发一次执行,然后按照Cron频率依次执行
            builder.withMisfireHandlingInstructionFireAndProceed();

            //创建Trigger
            Trigger trigger = newTrigger().withIdentity(this.getTriggerKey(jobUnicode)).startNow().withSchedule(builder).build();
            try {
                scheduler.scheduleJob(jobDetail, trigger);
            } catch (SchedulerException e) {
                logger.error("Schedule for " + jobUnicode + " start failed.", e);
                continue;
            }
        }
    }

    /**
     * 初始化taskserver channel
     */
    public void initChannels() {
        Set<String> taskNodes = taskNode.getTaskNodes();
        for (String taskNode : taskNodes) {
            String[] ipAndPort = taskNode.split(":");
            if (ipAndPort.length != 2) {
                logger.warn("Illegal information from : {}", taskNode);
                continue;
            }
            String ip = ipAndPort[0];
            int port = Integer.parseInt(ipAndPort[1]);
            NettyClientConfig nettyClientConfig = new NettyClientConfig();
            nettyClientConfig.setConnectPort(port);
            NettyRemotingClient nettyRemotingClient = new NettyRemotingClient(nettyClientConfig);
            nettyRemotingClient.start();
            logger.info("Init client connection to : {}.", taskNode);
            channels.put(taskNode, nettyRemotingClient);
        }

    }

    private JobKey getJobKey(String jobCode) {
        String code = jobCode.substring(jobCode.lastIndexOf("_") + 1);
        String group = jobCode.substring(0, jobCode.lastIndexOf("_"));
        return new JobKey(code, group);
    }

    private TriggerKey getTriggerKey(String jobCode) {
        String code = jobCode.substring(jobCode.lastIndexOf("_") + 1);
        String group = jobCode.substring(0, jobCode.lastIndexOf("_"));
        return new TriggerKey(code, group);
    }


    private NettyRemotingClient getAndCreateChannel(String targetNode){
        NettyRemotingClient client = channels.get(targetNode);
        if (client == null) {
            logger.info("Task Node : {} cant found channel.create it now!", targetNode);
            String[] ipAndPort = targetNode.split(":");
            if (ipAndPort.length != 2) {
                logger.warn("Illegal information from : {}", targetNode);
                throw new RuntimeException("Illegal address and port information.");
            }
            int port = Integer.parseInt(ipAndPort[1]);
            NettyClientConfig nettyClientConfig = new NettyClientConfig();
            nettyClientConfig.setConnectPort(port);
            NettyRemotingClient nettyRemotingClient = new NettyRemotingClient(nettyClientConfig);
            nettyRemotingClient.start();
            logger.info("Init client connection to : {}.", taskNode);
            channels.put(targetNode, nettyRemotingClient);
            return nettyRemotingClient;
        }
        return client;
    }

    class QuartzScheduleJob implements Job {
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            //获取job数据
            JobDataMap dataMap = context.getJobDetail().getJobDataMap();
            String jobUniqueCode = dataMap.getString("JOB_UNIQUE_CODE");
            String leaderIp = StringUtils.defaultIfEmpty(IPUtil.getAddress(), IPUtil.getLocalHost());
            logger.info("jobUniqueCode:" + jobUniqueCode);
            logger.info("context.getFireTime().getTime():" + context.getFireTime().getTime());
            logger.info("context.getNextFireTime().getTime():" + context.getNextFireTime().getTime());

            PeerTask task = new PeerTask();
            task.setJobUniqueCode(jobUniqueCode);
            task.setLeaderIP(leaderIp);
            task.setTraceId(TraceGenerator.generatTraceId());
            boolean success = true;
            int retryTimes = 3;
            do {
                Set<String> taskNodes = taskNode.getTaskNodes();
                //random selection
                String targetNode = selector.select(leaderIp, taskNodes);
                NettyRemotingClient client = getAndCreateChannel(targetNode);
                success = doWork(client, targetNode, task);
            } while (!success && retryTimes-- >= 0);

            if (!success) {
                logger.warn("Send command to all target servers, but all dead.");
            }
        }

        private boolean doWork(NettyRemotingClient client, String targetNode, PeerTask task) {
            boolean success = true;
            int retryTimes = 3;
            do {
                try {
                    String[] targetNodeInfo = targetNode.split(":");
                    if (targetNodeInfo.length != 2) {
                        logger.error("Task Node information is illegal.");
                        return false;
                    }
                    client.send(targetNodeInfo[0], task);
                    logger.info("Send command : {} to taskServer : {}.", task, targetNode);
                    success = true;
                } catch (RemotingSendRequestException e) {
                    logger.info("send command failed.retry...", e);
                    success = false;
                    retryTimes--;
                }
            } while (!success && retryTimes >= 0);
            return success;
        }

    }

    class ScheduleJobFactory implements JobFactory {
        @Override
        public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler) throws SchedulerException {
            return new QuartzScheduleJob();
        }
    }
}
