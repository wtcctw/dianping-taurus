package com.dp.bigdata.taurus.common.listener;

import com.dp.bigdata.taurus.generated.module.TaskAttempt;

/**
 * Author   mingdongli
 * 16/4/18  下午08:02.
 */
public interface InitializedAttemptListener extends GenericAttemptListener{

    void addInitializedAttempt(TaskAttempt taskAttempt);
}
