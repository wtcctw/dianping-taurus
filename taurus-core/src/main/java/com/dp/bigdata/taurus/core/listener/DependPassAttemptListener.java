package com.dp.bigdata.taurus.core.listener;

import com.dp.bigdata.taurus.generated.module.TaskAttempt;

/**
 * Author   mingdongli
 * 16/4/18  下午11:20.
 */
public interface DependPassAttemptListener extends GenericAttemptListener{

    void addDependPassAttempt(TaskAttempt taskAttempt);

    void removeDependPassAttempt(TaskAttempt taskAttempt);
}
