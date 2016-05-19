package com.dp.bigdata.taurus.client;

import com.dp.bigdata.taurus.common.netty.protocol.ScheduleTask;

/**
 * Created by yangguiliang on 14-8-18.
 */
public interface ITaskHandler {
    /**
     * 处理schedule node 下发指令
     *
     * @param task
     * @throws Exception
     */
    void handleTask(ScheduleTask task) throws Exception;
}
