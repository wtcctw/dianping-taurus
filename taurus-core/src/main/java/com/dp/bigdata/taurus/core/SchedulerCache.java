package com.dp.bigdata.taurus.core;

import com.dp.bigdata.taurus.generated.module.Task;
import com.dp.bigdata.taurus.generated.module.TaskAttempt;

import java.util.List;
import java.util.Map;

/**
 * Author   mingdongli
 * 16/4/23  下午12:32.
 */
public interface SchedulerCache extends SchedulerConfig{

    List<AttemptContext> getAllRunningAttempt();

    List<AttemptContext> getRunningAttemptsByTaskID(String taskID);

    Map<String, Task> getAllRegistedTask();

    Task getTaskByName(String name) throws ScheduleException;

    boolean isRuningAttempt(String attemptID);

    CronExpression getCronExpression(String taskId);

    void addOrUpdateCronCache(Task task);

    List<TaskAttempt> getAttemptsOfStatusDependTimeout();

}
