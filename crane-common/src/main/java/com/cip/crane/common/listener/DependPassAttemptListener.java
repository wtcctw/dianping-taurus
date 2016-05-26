package com.cip.crane.common.listener;

import com.cip.crane.generated.module.TaskAttempt;

/**
 * Author   mingdongli
 * 16/4/18  下午08:20.
 */
public interface DependPassAttemptListener extends GenericAttemptListener{

    void addDependPassAttempt(TaskAttempt taskAttempt);

    void removeDependPassAttempt(TaskAttempt taskAttempt);
}
