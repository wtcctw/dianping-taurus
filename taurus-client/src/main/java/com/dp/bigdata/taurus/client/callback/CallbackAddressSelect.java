package com.dp.bigdata.taurus.client.callback;


import com.dp.bigdata.taurus.common.netty.protocol.ScheduleTask;

/**
 *
 */
public interface CallbackAddressSelect {

    String select(ScheduleTask task);
}
