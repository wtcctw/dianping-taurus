package com.dianping.taurus.service;

public interface TaurusHelperService {

	public String getTaskInfoByTaskID(String taskID);
	
	public String getTaskInfoByTaskName(String taskName);
	
	public String getTaskInfoByAttemptID(String attemptID);
}