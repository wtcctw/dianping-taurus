package com.cip.crane.common.listener;

import com.cip.crane.generated.module.TaskAttempt;

/**
 * Author   mingdongli
 * 16/4/18  下午08:05.
 */
public interface DependTimeoutAttemptListener extends GenericAttemptListener{

    void addDependTimeoutAttempt(TaskAttempt taskAttempt);
}
