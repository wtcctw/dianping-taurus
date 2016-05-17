package com.dp.bigdata.taurus.common;

import com.dp.bigdata.taurus.generated.module.Task;
import com.dp.bigdata.taurus.generated.module.TaskAttempt;

/**
 * 
 * Scheduler is the core of the engine.
 * 
 * @author damon.zhu
 * 
 */
public interface Scheduler extends ListenableSchedulerCache{

	/**
	 * Submit a new task into Engine, waiting to be scheduled.
	 * 
	 * @param task
	 * @throws ScheduleException
	 */
	void registerTask(Task task) throws ScheduleException;

	/**
	 * Remove a existing Task in the Engine.
	 * 
	 * @param taskID
	 * @throws ScheduleException
	 */
	void unRegisterTask(String taskID) throws ScheduleException;

	/**
	 * Update a existing Task in the Engine.
	 * 
	 * @param task
	 * @throws ScheduleException
	 */
	void updateTask(Task task) throws ScheduleException;

	/**
	 * Execute the Task in the timeout period.
	 * 
	 * @param job
	 * @param timeout
	 * @throws ScheduleException
	 */
	void executeTask(String taskID, long timeout) throws ScheduleException;
	
	/**
	 * Suspend the task, the suspended task will never be scheduled.
	 * @param taskID
	 * @throws ScheduleException
	 */
	void suspendTask(String taskID) throws ScheduleException;

    /**
     * Resume the task, the resumed task will be scheduled.
     * 
     * @param taskID
     * @throws ScheduleException
     */
	void resumeTask(String taskID) throws ScheduleException;

	/**
	 * Notify the scheduler to kill a attempt.
	 * 自动杀死一个指定调度
	 * @param attemptID
	 * @throws ScheduleException
	 */
	void killAttempt(String attemptID) throws ScheduleException;
	
	/**
	 * 手动杀死一个指定调度
	 * @param attemptID
	 * @throws ScheduleException
	 */
	void killAttemptManual(String attemptID) throws ScheduleException;
	
	/**
	 * Notify the scheduler that a attempt has been finished.
	 * @param attemptID
	 */
	void attemptSucceed(String attemptID);
	
	/**
	 * Notify the scheduler that a attempt has been expired.
	 * @param attemptID
	 */
	void attemptExpired(String attemptID);
	
	/**
	 * Notify the scheduler that a attempt has been failed.
	 * @param attemptID
	 */
	void attemptFailed(String attemptID);
	
	/**
	 * Notify the scheduler that a attempt has been finished with a unknown status.
	 * @param attemptID
	 */
	void attemptUnKnown(String attemptID);
	
	/**
	 * get attempt status to the given attemptID
	 * @param attemptID
	 * @return AttemptStatus
	 */
	AttemptStatus getAttemptStatus(String attemptID);
	
	TaskAttempt getRecentFiredAttemptByTaskID(String taskID);
	 
	 /**
	  * 设置拥塞的后续预期调度为过期状态，注意和运行超时TIMEOUT状态区分
	  * @param attemptID
	  */
	 void expireCongestionAttempt(String attemptID);

}
