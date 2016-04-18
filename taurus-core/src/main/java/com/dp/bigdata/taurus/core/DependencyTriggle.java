package com.dp.bigdata.taurus.core;

import com.dianping.cat.Cat;
import com.dp.bigdata.taurus.core.listener.DependPassAttemptListener;
import com.dp.bigdata.taurus.core.listener.DependTimeoutAttemptListener;
import com.dp.bigdata.taurus.core.listener.GenericAttemptListener;
import com.dp.bigdata.taurus.core.listener.InitializedAttemptListener;
import com.dp.bigdata.taurus.core.parser.DependencyParser;
import com.dp.bigdata.taurus.generated.mapper.TaskAttemptMapper;
import com.dp.bigdata.taurus.generated.module.Task;
import com.dp.bigdata.taurus.generated.module.TaskAttempt;
import com.mysql.jdbc.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.*;

/**
 * DependencyTriggle
 * 
 * @author damon.zhu
 */
public class DependencyTriggle implements Triggle {

	private static final Log LOG = LogFactory.getLog(DependencyTriggle.class);

	@Autowired
	private TaskAttemptMapper taskAttemptMapper;

	@Autowired
	private AttemptStatusCheck statusCheck;

	private final DependencyParser parser;

	private final Scheduler scheduler;

	private List<DependTimeoutAttemptListener> dependTimeoutAttemptListeners = new ArrayList<DependTimeoutAttemptListener>();

	private List<DependPassAttemptListener> dependPassAttemptListeners = new ArrayList<DependPassAttemptListener>();

	@Autowired
	public DependencyTriggle(Scheduler scheduler) {
		this.scheduler = scheduler;
		this.parser = new DependencyParser();
	}

	public static void main(String[] args) {
		Date date = new Date();
		Calendar calender = Calendar.getInstance();
		calender.setTime(date);
		calender.add(Calendar.MINUTE, -10);

		System.out.println(calender.getTime());
	}

	@Override
	public void triggle() {
		List<TaskAttempt> attempts = taskAttemptMapper.selectByGroupAndStatus();
		final Map<String, Task> tasks = scheduler.getAllRegistedTask();
		for (TaskAttempt attempt : attempts) {
			triggle(attempt, tasks.get(attempt.getTaskid()));
		}
	}

	@Override
	public void triggle(final Collection<TaskAttempt> taskAttempts) {
		Collections.sort((List<TaskAttempt>) taskAttempts, new Comparator<TaskAttempt>() {
			@Override
			public int compare(TaskAttempt t0, TaskAttempt t1) {
				return t0.getScheduletime().before(t1.getScheduletime()) == true ? -1 :
						(t0.getScheduletime().after(t1.getScheduletime()) ? 1 : 0);
			}
		});
		final Map<String, Task> tasks = scheduler.getAllRegistedTask();
		for (TaskAttempt attempt : taskAttempts) {
			triggle(attempt, tasks.get(attempt.getTaskid()));
		}
	}

	@Override
	public void registerAttemptListener(GenericAttemptListener genericAttemptListener) {

		if(genericAttemptListener instanceof DependTimeoutAttemptListener){
			dependTimeoutAttemptListeners.add((DependTimeoutAttemptListener) genericAttemptListener);
		}
		if(genericAttemptListener instanceof DependPassAttemptListener){
			dependPassAttemptListeners.add((DependPassAttemptListener) genericAttemptListener);
		}
	}

	public void triggle(TaskAttempt attempt, Task task) {
		if (task == null) {
			return;
		}

		String expression = task.getDependencyexpr();
		boolean isDepencyFinish = false;
		boolean hasDependency = true;
		if (StringUtils.isNullOrEmpty(expression)) {
			// for those tasks who have no dependencies.
			hasDependency = false;
			isDepencyFinish = true;
		} else {
			try {
				isDepencyFinish = parser.isDepdencySatisfied(expression, statusCheck);
			} catch (ParseException e) {
				Cat.logError(e);
				return;
			}
		}

		/*
		 * check whether pass the dependency-check phase
		 */
		if (isDepencyFinish) {
			if (hasDependency) {
				LOG.info("Attempt " + attempt.getAttemptid() + " has passed dependencies");
			} else {
				LOG.info("Attempt " + attempt.getAttemptid() + " has no dependencies, execute it directly");
			}
			Cat.logEvent("Depend.Fire", task.getName());

			attempt.setStatus(AttemptStatus.DEPENDENCY_PASS);
			taskAttemptMapper.updateByPrimaryKeySelective(attempt);
			for(DependPassAttemptListener listener : dependPassAttemptListeners){
				listener.addDependPassAttempt(attempt);
			}
		} else {
			/*
			 * check whether the attempt has expire the wait-time
			 */
			int timeout = task.getWaittimeout();
			Date start = attempt.getScheduletime();
			long now = System.currentTimeMillis();
			if (now > start.getTime() + 1000L * 60 * timeout && attempt.getStatus() != AttemptStatus.DEPENDENCY_TIMEOUT) {
				Cat.logEvent("Depend.Fire.Timeout", task.getName());
				LOG.info("Attempt " + attempt.getAttemptid() + " has dependency waiting timeout ");
				// I do think dependency_fail status is unnecessary.
				attempt.setStatus(AttemptStatus.DEPENDENCY_TIMEOUT);
				attempt.setEndtime(new Date());
				taskAttemptMapper.updateByPrimaryKeySelective(attempt);
				for(DependTimeoutAttemptListener listener : dependTimeoutAttemptListeners){
					listener.addDependTimeoutAttempt(attempt);
				}
			}
		}
	}
}
