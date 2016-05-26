package com.dp.bigdata.taurus.common.netty;

import com.dp.bigdata.taurus.common.execute.ExecuteContext;
import com.dp.bigdata.taurus.common.execute.ExecuteException;
import com.dp.bigdata.taurus.common.execute.ExecutorManager;
import com.dp.bigdata.taurus.common.execute.ExecutorManagerSupport;
import com.dp.bigdata.taurus.common.netty.exception.RemotingSendRequestException;
import com.dp.bigdata.taurus.common.netty.processor.CallbackProcessor;
import com.dp.bigdata.taurus.common.netty.protocol.CommandType;
import com.dp.bigdata.taurus.common.netty.protocol.ScheduleTask;
import com.dp.bigdata.taurus.common.utils.IPUtils;
import com.dp.bigdata.taurus.common.utils.SleepUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author   mingdongli
 * 16/5/17  下午10:48.
 */

public class MscheduleExecutorManager extends ExecutorManagerSupport implements ExecutorManager{

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final String MSCHEDULE_TYPE = "mschedule";

    @Autowired
    private NettyRemotingClient nettyRemotingClient;

    @Autowired
    private NettyRemotingServer nettyRemotingServer;

    @Autowired
    private CallbackProcessor callbackProcessor;

    @PostConstruct
    public void registerProcessor(){
        ExecutorService scheduleService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() << 1);
        nettyRemotingServer.registerProcessor(CommandType.ScheduleReceiveResult, callbackProcessor, scheduleService);
    }

    @Override
    public void execute(ExecuteContext context) throws ExecuteException {

        executeAttempt(context);
    }

    private ScheduleTask buildScheduleTask(ExecuteContext executeContext) {

        ScheduleTask scheduleTask = new ScheduleTask();
        scheduleTask.setJobUniqueCode(executeContext.getTaskID());
        scheduleTask.setTraceId(executeContext.getAttemptID());
        String schedAddress = IPUtils.getFirstNoLoopbackIP4Address();
        Set<String> callbackAddress = new HashSet<String>();
        callbackAddress.add(schedAddress);
        scheduleTask.setCallbackAddress(callbackAddress);
        return scheduleTask;
    }

    private void executeAttempt(ExecuteContext executeContext) throws ExecuteException{

        ScheduleTask scheduleTask = buildScheduleTask(executeContext);
        String agentIps = executeContext.getAgentIP();
        String[] ipAndPort = agentIps.split(":");
        if(ipAndPort.length != 2){
            logger.error("Address : {} illegal.", agentIps);
            throw new RuntimeException("Address illegal.");
        }

        int retryCount = 3;
        boolean success = false;
        do {
            try {
                nettyRemotingClient.send(ipAndPort[0], Integer.parseInt(ipAndPort[1]), scheduleTask);
                success = true;
            } catch (RemotingSendRequestException e) {
                logger.error(String.format("send ScheduleTask to %s:%s error", ipAndPort[0], ipAndPort[1]));
                retryCount--;
                SleepUtils.sleep(100);
            }
        } while (retryCount >= 0 && !success);

        if(!success){
            throw new ExecuteException("send ScheduleTask error");
        }
    }
}
