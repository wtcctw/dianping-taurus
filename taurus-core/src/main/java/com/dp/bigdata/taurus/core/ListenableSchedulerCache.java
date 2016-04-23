package com.dp.bigdata.taurus.core;

import com.dp.bigdata.taurus.core.listener.DependPassAttemptListener;
import com.dp.bigdata.taurus.core.listener.DependTimeoutAttemptListener;
import com.dp.bigdata.taurus.core.listener.InitializedAttemptListener;

/**
 * Author   mingdongli
 * 16/4/23  下午1:26.
 */
public interface ListenableSchedulerCache extends SchedulerCache, InitializedAttemptListener, DependTimeoutAttemptListener, DependPassAttemptListener {

}
