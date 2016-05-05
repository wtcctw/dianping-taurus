package com.dp.bigdata.taurus.core;

import com.dp.bigdata.taurus.core.structure.BoundedList;
import com.dp.bigdata.taurus.generated.module.TaskAttempt;
import org.apache.commons.lang.StringUtils;

/**
 * Author   mingdongli
 * 16/4/23  下午1:31.
 */
public abstract class ListenableCachedScheduler extends CachedScheduler implements ListenableSchedulerCache{

    public ListenableCachedScheduler() {
        super();
    }

    @Override
    public synchronized void addDependPassAttempt(TaskAttempt taskAttempt) {
        attemptsOfStatusInitialized.remove(taskAttempt);
        attemptsOfStatusDependTimeout.remove(taskAttempt);
        addLastTaskAttempt(taskAttempt);
    }

    @Override
    public synchronized void removeDependPassAttempt(TaskAttempt taskAttempt) {
        String taskId = taskAttempt.getTaskid();
        if (StringUtils.isNotBlank(taskId)) {
            BoundedList<TaskAttempt> tmpTask = dependPassMap.get(taskId);
            if (tmpTask != null) {
                tmpTask.remove(taskAttempt);
            }
        }
    }

    @Override
    public synchronized void addDependTimeoutAttempt(TaskAttempt taskAttempt) {
        removeDependPassAttempt(taskAttempt);
        attemptsOfStatusInitialized.remove(taskAttempt);
        attemptsOfStatusDependTimeout.add(taskAttempt);
    }

    @Override
    public synchronized void addInitializedAttempt(TaskAttempt taskAttempt) {
        attemptsOfStatusInitialized.add(taskAttempt);
    }
}
