package com.dp.bigdata.taurus.client.processor;

import com.dp.bigdata.taurus.client.ITaskHandler;
import com.dp.bigdata.taurus.client.ScheduleManager;
import com.dp.bigdata.taurus.client.TaskCallBacker;
import com.dp.bigdata.taurus.common.netty.exception.RemotingSendRequestException;
import com.dp.bigdata.taurus.common.netty.processor.NettyRequestProcessor;
import com.dp.bigdata.taurus.common.netty.protocol.Command;
import com.dp.bigdata.taurus.common.netty.protocol.ScheduleTask;
import com.dp.bigdata.taurus.common.utils.Pair;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 处理schedulenode下发的指令
 * User: hongbin03
 * Date: 16/1/21
 * Time: 下午4:22
 * MailTo: hongbin03@meituan.com
 */
public class ScheduleProcessor implements NettyRequestProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleProcessor.class);
    private final ExecutorService taskService;
    private final int MIN_CAPACITY = 5;
    private final ConcurrentHashMap<String, Pair<Future, ScheduleTask>> executingTasksAndResultFuture = new ConcurrentHashMap<String, Pair<Future, ScheduleTask>>();
    private final TaskCallBacker callBacker;


    public ScheduleProcessor(TaskCallBacker taskCallBacker,int serviceCapacity) {
        this.callBacker = taskCallBacker;
        taskService  = Executors.newFixedThreadPool(serviceCapacity < MIN_CAPACITY ? MIN_CAPACITY
                : serviceCapacity);
    }

    @Override
    public void processRequest(ChannelHandlerContext ctx, Command request) throws Exception {

        ScheduleTask scheduleTask = (ScheduleTask) request;
        logger.info("MSchedule-Client:messageReceived,jobUniqueCode:" + scheduleTask.getJobUniqueCode());
        ITaskHandler handler = ScheduleManager.getTaskHandler(scheduleTask.getJobUniqueCode());
        if (handler == null) {
            //未找到TaskHandler打印日志信息
            logger.error("MSchedule-Client:Cant found TaskHandler,jobUniqueCode:" + scheduleTask.getJobUniqueCode() + ",Registered TaskHandlers :" + ScheduleManager.getRegisteredJobs());
            return;
        }

        TaskWorker worker = new TaskWorker(scheduleTask, handler);
        Future executeFuture = taskService.submit(worker);
        Pair<Future, ScheduleTask> executingTaskAndResultFuture = Pair.create(executeFuture, scheduleTask);
        executingTasksAndResultFuture.put(scheduleTask.getJobUniqueCode(), executingTaskAndResultFuture);

    }

    private class TaskWorker implements Runnable {

        private ScheduleTask scheduleTask;

        private ITaskHandler handler;

        public TaskWorker(ScheduleTask task, ITaskHandler handler) {
            this.scheduleTask = task;
            this.handler = handler;
        }

        @Override
        public void run() {
            int runState = 1;
            try {
                //执行TaskHandler
                logger.info("MSchedule-Client: Execute task begin,jobUniqueCode:" + scheduleTask.getJobUniqueCode() +
                        ",traceId: " + scheduleTask.getTraceId());
                handler.handleTask(scheduleTask);
                logger.info("MSchedule-Client:Execute task successfully,jobUniqueCode:" + scheduleTask.getJobUniqueCode() + "," +
                        "traceId: " + scheduleTask.getTraceId());
            } catch (Throwable t) {
                runState = 2;
                //执行TaskHandler,失败
                logger.error("MSchedule-Client:Execute task failed,jobUniqueCode:" + scheduleTask.getJobUniqueCode() +
                        "," +
                        "traceId: " + scheduleTask.getTraceId(), t);
            }

            callBack(scheduleTask, runState);

        }

        private boolean callBack(ScheduleTask task, int runState) {
            logger.info("MSchedule-Client:callBack begin,JobUniqueCode：" + task.getJobUniqueCode() +
                    ",traceId: " + task.getTraceId());
            //加入因网络原因失败重试机制
            int count = 0;
            int retryCount = 50;
            boolean retry = false;
            do {
                try {
                    callBacker.completeTask(task, runState);
                    retry = false;
                } catch (RemotingSendRequestException e) {
                    //网络问题，尝试重试
                    logger.error("MSchedule-Client:callBack failed,JobUniqueCode：" + task.getJobUniqueCode() + ",traceId: " + task.getTraceId(), e);
                    retry = true;
                    try {
                        Thread.sleep(200L);
                    } catch (InterruptedException e1) {
                        //ignore
                    }
                } catch (Exception e){
                    logger.error("MSchedule-Client:callback failed,JobUniqueCode：" + task.getJobUniqueCode() + ",traceId: " + task.getTraceId(), e);
                    retry = false;
                }
                count++;
            } while (++count < retryCount && retry);


            logger.info("MSchedule-Client:callBack end,JobUniqueCode：" + task.getJobUniqueCode() +
                    ",traceId: " + task.getTraceId());
            return true;
        }
    }

    public ConcurrentHashMap<String, Pair<Future, ScheduleTask>> getExecutingTasksAndResultFuture() {
        return executingTasksAndResultFuture;
    }
}
