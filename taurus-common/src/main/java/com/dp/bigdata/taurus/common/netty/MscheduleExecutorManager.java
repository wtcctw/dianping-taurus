package com.dp.bigdata.taurus.common.netty;

import com.dp.bigdata.taurus.common.netty.exception.RemotingSendRequestException;
import com.dp.bigdata.taurus.common.netty.protocol.ScheduleTask;
import com.dp.bigdata.taurus.common.utils.IPUtils;
import com.dp.bigdata.taurus.common.utils.SleepUtils;
import com.dp.bigdata.taurus.zookeeper.common.execute.ExecuteContext;
import com.dp.bigdata.taurus.zookeeper.common.execute.ExecuteException;
import com.dp.bigdata.taurus.zookeeper.common.execute.ExecuteStatus;
import com.dp.bigdata.taurus.zookeeper.common.execute.ExecutorManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Author   mingdongli
 * 16/5/17  下午10:48.
 */

@Component
@Qualifier("netty")
public class MscheduleExecutorManager implements ExecutorManager{

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final String MSCHEDULE_TYPE = "mschedule";

    @Autowired
    private NettyRemotingClient nettyRemotingClient;

    @Override
    public void execute(ExecuteContext context) throws ExecuteException {

        executeAttempt(context);
    }

    @Override
    public void kill(ExecuteContext context) throws ExecuteException {

    }

    @Override
    public ExecuteStatus getStatus(ExecuteContext context) throws ExecuteException {
        return null;
    }

    @Override
    public boolean updateStatus(ExecuteContext context) throws ExecuteException {
        return true;
    }

    @Override
    public boolean cleanAttemptNode(String ip, String attemptId) {
        return false;
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
