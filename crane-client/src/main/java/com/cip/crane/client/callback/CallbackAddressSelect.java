package com.cip.crane.client.callback;


import com.cip.crane.common.netty.protocol.ScheduleTask;

/**
 *
 */
public interface CallbackAddressSelect {

    String select(ScheduleTask task);
}
