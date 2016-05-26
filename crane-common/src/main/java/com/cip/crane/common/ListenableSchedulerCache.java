package com.cip.crane.common;

import com.cip.crane.common.listener.DependPassAttemptListener;
import com.cip.crane.common.listener.DependTimeoutAttemptListener;
import com.cip.crane.common.listener.InitializedAttemptListener;

/**
 * Author   mingdongli
 * 16/4/23  下午1:26.
 */
public interface ListenableSchedulerCache extends SchedulerCache, InitializedAttemptListener, DependTimeoutAttemptListener, DependPassAttemptListener {

}
