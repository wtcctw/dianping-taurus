package com.dp.bigdata.taurus.common;

import com.dp.bigdata.taurus.common.listener.DependPassAttemptListener;
import com.dp.bigdata.taurus.common.listener.DependTimeoutAttemptListener;
import com.dp.bigdata.taurus.common.listener.InitializedAttemptListener;

/**
 * Author   mingdongli
 * 16/4/23  下午1:26.
 */
public interface ListenableSchedulerCache extends SchedulerCache, InitializedAttemptListener, DependTimeoutAttemptListener, DependPassAttemptListener {

}
