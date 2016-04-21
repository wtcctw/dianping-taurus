package com.dp.bigdata.taurus.core.listener;

import com.dp.bigdata.taurus.generated.module.TaskAttempt;

/**
 * Author   mingdongli
 * 16/4/18  下午08:05.
 */
public interface DependTimeoutAttemptListener extends GenericAttemptListener{

    void addDependTimeoutAttempt(TaskAttempt taskAttempt);

    void removeDependTimeoutAttempt(TaskAttempt taskAttempt);
}
