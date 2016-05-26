package com.cip.crane.common;

import java.util.List;

import com.cip.crane.common.parser.Operation;
import com.cip.crane.generated.module.Task;
import com.cip.crane.generated.module.TaskIDCounter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cip.crane.generated.mapper.TaskAttemptMapper;
import com.cip.crane.generated.mapper.TaskIDCounterMapper;
import com.cip.crane.generated.module.TaskAttempt;
import com.cip.crane.generated.module.TaskAttemptExample;

/**
 * DefaultAttemptStatusCheck
 * 
 * @author damon.zhu
 */
public class DefaultAttemptStatusCheck implements AttemptStatusCheck {

	private static final Log LOG = LogFactory.getLog(DefaultAttemptStatusCheck.class);

	@Autowired
	private TaskIDCounterMapper taskIdMapper;

	@Autowired
	private TaskAttemptMapper attemptMapper;

	private Scheduler scheduler;

	@Autowired
	public DefaultAttemptStatusCheck(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	public boolean isDone(Operation operation) {
		Task task;
		try {
			task = scheduler.getTaskByName(operation.getName());
		} catch (ScheduleException se) {
			return false;
		}

		if (task.getStatus() == TaskStatus.SUSPEND) {
			return false;
		}

		TaskIDCounter counter = taskIdMapper.selectByPrimaryKey(task.getTaskid());
		if (counter == null || counter.getCounter() < operation.getNumber()) {
			return false;
		}
		InstanceID instanceID = new InstanceID(TaskID.forName(task.getTaskid()), counter.getCounter()
		      - operation.getNumber() + 1);
		List<TaskAttempt> recentAttempts = getTaskAttemptByInstanceID(instanceID);
		if (recentAttempts == null || recentAttempts.size() == 0) {
			return false;
		}
		TaskAttempt history = recentAttempts.get(0);
		if (LOG.isDebugEnabled()) {
			LOG.debug("attempt : " + history.getAttemptid() + " status : " + history.getStatus());
		}
		int status = history.getStatus();

		// TODO bugs.
		if ((status == AttemptStatus.SUCCEEDED || status == AttemptStatus.FAILED 
				|| status == AttemptStatus.AUTO_KILLED || status == AttemptStatus.MAN_KILLED)) {
			if (history.getReturnvalue() != null && history.getReturnvalue() == operation.getValue()){
				return true;
			}else{
				return false;
			}
		} else {
			return false;
		}
	}

	private List<TaskAttempt> getTaskAttemptByInstanceID(InstanceID instanceID) {
		TaskAttemptExample example = new TaskAttemptExample();
		example.or().andInstanceidEqualTo(instanceID.toString());
		example.setOrderByClause("startTime desc");
		List<TaskAttempt> recentAttempts = attemptMapper.selectByExample(example);
		return recentAttempts;
	}
}
