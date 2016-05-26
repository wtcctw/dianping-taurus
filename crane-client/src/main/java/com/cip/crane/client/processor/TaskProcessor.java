package com.cip.crane.client.processor;

import com.cip.crane.client.ITaskHandler;
import com.cip.crane.client.ScheduleManager;
import com.cip.crane.common.netty.processor.NettyRequestProcessor;
import com.cip.crane.common.netty.protocol.Command;
import com.cip.crane.common.netty.protocol.PeerTask;
import com.cip.crane.common.netty.protocol.ScheduleTask;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 处理TaskNode下发的指令
 * User: hongbin03
 * Date: 16/1/21
 * Time: 下午4:12
 * MailTo: hongbin03@meituan.com
 */
public class TaskProcessor implements NettyRequestProcessor {
    private static final Logger logger = LoggerFactory.getLogger(TaskProcessor.class);

    private final ExecutorService taskService;
    private final int MIN_CAPACITY = 5;

    public TaskProcessor(int serviceCapacity) {
        taskService = Executors.newFixedThreadPool(serviceCapacity < MIN_CAPACITY ? MIN_CAPACITY : serviceCapacity);
    }

    @Override
    public void processRequest(ChannelHandlerContext ctx, Command request) throws Exception {
        PeerTask peerTask = (PeerTask) request;
        logger.info("TaskProcessor received command : {}", peerTask);
        logger.info("MSchedule-Client:messageReceived,jobUniqueCode:" + peerTask.getJobUniqueCode());
        ITaskHandler handler = ScheduleManager.getTaskHandler(peerTask.getJobUniqueCode());
        if (handler == null) {
            //未找到TaskHandler打印日志信息
            logger.error("MSchedule-Client:Cant found TaskHandler,jobUniqueCode:" + peerTask.getJobUniqueCode() + ",Registered TaskHandlers :" + ScheduleManager.getRegisteredJobs());
            return;
        }
        ScheduleTask scheduleTask = new ScheduleTask();
        scheduleTask.setJobUniqueCode(peerTask.getJobUniqueCode());
        scheduleTask.setTraceId(peerTask.getTraceId());
        scheduleTask.setTaskItems(peerTask.getTaskItems());
        TaskWorker worker = new TaskWorker(scheduleTask, handler);
        taskService.submit(worker);

    }


    private class TaskWorker implements Runnable {
        private ScheduleTask peerTask;
        private ITaskHandler handler;

        public TaskWorker(ScheduleTask task, ITaskHandler handler) {
            this.peerTask = task;
            this.handler = handler;
        }

        @Override
        public void run() {
            try {
                //执行TaskHandler
                logger.info("MSchedule-Client: Execute task begin,jobUniqueCode:" + peerTask.getJobUniqueCode() +
                        ",traceId: " + peerTask.getTraceId());
                handler.handleTask(peerTask);
                logger.info("MSchedule-Client:Execute task successfully,jobUniqueCode:" + peerTask.getJobUniqueCode() + "," +
                        "traceId: " + peerTask.getTraceId());
            } catch (Throwable t) {
                //执行TaskHandler,失败
                logger.error("MSchedule-Client:Execute task failed,jobUniqueCode:" + peerTask.getJobUniqueCode() +
                        "," +
                        "traceId: " + peerTask.getTraceId(), t);
            }
        }

    }
}
