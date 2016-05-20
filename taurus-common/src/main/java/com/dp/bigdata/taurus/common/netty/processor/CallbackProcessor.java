package com.dp.bigdata.taurus.common.netty.processor;

import com.dianping.cat.Cat;
import com.dp.bigdata.taurus.common.Scheduler;
import com.dp.bigdata.taurus.common.netty.protocol.CallBackTask;
import com.dp.bigdata.taurus.common.netty.protocol.Command;
import com.dp.bigdata.taurus.generated.mapper.TaskAttemptMapper;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 更新状态，结束一个调用过程
 * Author   mingdongli
 * 16/5/18  下午4:56.
 */
@Component
public class CallbackProcessor implements NettyRequestProcessor {

    private static final Logger logger = LoggerFactory.getLogger(CallbackProcessor.class);

    @Autowired
    private TaskAttemptMapper taskAttemptMapper;

    @Autowired
    private Scheduler scheduler;

    @Override
    public void processRequest(ChannelHandlerContext ctx, Command request) throws Exception {
        CallBackTask callBackTask = (CallBackTask) request;
        logger.info("Schedule node received callback : {}.", callBackTask);
        try {
            callBack(callBackTask);
        }catch (Exception e){
            logger.error("update staus error", e);
            Cat.logError("CallbackProcessor", e);
            callBack(callBackTask);
        }
        logger.info("Schedule node handle callback {} successfully.", callBackTask);
    }

    /**
     * Task运行完后，通过回调方式记录任务运行情况
     *
     * @param callBackTask
     */
    public void callBack(CallBackTask callBackTask) throws Exception {


        String attemptId = callBackTask.getTraceId();

        if (RunState.SUCCESS.value == callBackTask.getRunState()) {
            scheduler.attemptSucceed(attemptId);
        } else if (RunState.FAIL.value == callBackTask.getRunState()) {
            scheduler.attemptFailed(attemptId);
        } else if (RunState.UNKNOWN.value == callBackTask.getRunState()) {
            scheduler.attemptUnKnown(attemptId);
        }
    }


    public enum RunState {
        //运行中
        DOING(0),
        //成功
        SUCCESS(1),
        //失败
        FAIL(2),
        //调度失败
        SCHEDULE_FAIL(3),
        //未知问题,task节点重新发布，导致无法判断该任务是否执行完成
        UNKNOWN(4);

        public final int value;

        RunState(int value) {
            this.value = value;
        }

    }
}
