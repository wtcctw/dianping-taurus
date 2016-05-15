package com.dp.bigdata.taurus.common;

import com.dp.bigdata.taurus.generated.module.Task;
import com.dp.bigdata.taurus.generated.module.TaskAttempt;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * Author   mingdongli
 * 16/4/23  下午12:32.
 */
public interface SchedulerCache extends SchedulerConfig{

    void clearCache();

    void initCache();

    List<AttemptContext> getAllRunningAttempt();

    List<AttemptContext> getRunningAttemptsByTaskID(String taskID);

    Map<String, Task> getAllRegistedTask();

    ConcurrentMap<String, Date> getPreviousFireTimeMap();

    Task getTaskByName(String name) throws ScheduleException;

    boolean isRuningAttempt(String attemptID);

    CronExpression getCronExpression(String taskId);

    void addOrUpdateCronCache(Task task);

    List<TaskAttempt> getAttemptsOfStatusDependTimeout();

}
