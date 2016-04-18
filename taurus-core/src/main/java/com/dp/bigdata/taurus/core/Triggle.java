package com.dp.bigdata.taurus.core;

import com.dp.bigdata.taurus.core.listener.GenericAttemptListener;
import com.dp.bigdata.taurus.generated.module.TaskAttempt;

import java.util.Collection;

/**
 * 
 * @author damon.zhu
 *
 */
public interface Triggle {
	
	void triggle();

	void triggle(Collection<TaskAttempt> taskAttempts);

	void registerAttemptListener(GenericAttemptListener genericAttemptListener);
	
}
