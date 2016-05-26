package com.cip.crane.client;

import com.cip.crane.common.netty.NettyRemotingClient;
import com.cip.crane.common.netty.exception.RemotingSendRequestException;
import com.cip.crane.common.netty.protocol.CallBackTask;
import com.cip.crane.common.netty.protocol.ScheduleTask;
import com.cip.crane.client.callback.CallbackAddressSelect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by yangguiliang on 14-8-18.
 */
public class TaskCallBacker {
    private int port;

    private CallbackAddressSelect selector;

    private NettyRemotingClient client;

    private Logger log = LoggerFactory.getLogger(this.getClass());

    public void completeTask(ScheduleTask task, int runState) throws Exception {
        log.info("MSchedule-Client:completeTask begin ,JobUniqueCode：" + task.getJobUniqueCode() +
                "," +
                "traceId: " + task.getTraceId() + ",runState:" + runState);
        CallBackTask callBackTask = this.getCallBackTaskInstance(task, runState);
        String hostAddress = selector.select(task);

        try {
            client.send(hostAddress, callBackTask);
        } catch (RemotingSendRequestException ex) {
            log.error("MSchedule-Client:completeTask error,JobUniqueCode：" + task.getJobUniqueCode() +
                    ",traceId: " + task.getTraceId() + ",runState:" + runState + ",hostAddress:" + hostAddress);
            throw ex;
        }

        log.info("MSchedule-Client:completeTask successfully,JobUniqueCode：" + task.getJobUniqueCode() + ",traceId: " + task.getTraceId() + ",runState:" + runState + ",hostAddress:" + hostAddress);
    }


    private CallBackTask getCallBackTaskInstance(ScheduleTask task, int runState) {
        CallBackTask callBackTask = new CallBackTask();
        callBackTask.setTraceId(task.getTraceId());
        callBackTask.setEndTime(System.currentTimeMillis());
        callBackTask.setRunState(runState);
        callBackTask.setSubTask(task.getSubTask());
        return callBackTask;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public CallbackAddressSelect getSelector() {
        return selector;
    }

    public void setSelector(CallbackAddressSelect selector) {
        this.selector = selector;
    }

    public NettyRemotingClient getClient() {
        return client;
    }

    public void setClient(NettyRemotingClient client) {
        this.client = client;
    }
}
