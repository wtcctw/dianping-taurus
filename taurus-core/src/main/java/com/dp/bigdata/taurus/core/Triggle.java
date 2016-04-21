package com.dp.bigdata.taurus.core;

import com.dp.bigdata.taurus.core.listener.ListenerAdder;
import com.dp.bigdata.taurus.generated.module.TaskAttempt;

import java.util.Collection;

/**
 * Author   mingdongli
 * 16/4/18  下午06:20.
 */
public interface Triggle extends ListenerAdder {
	
	void triggle();

	void triggle(Collection<TaskAttempt> taskAttempts);

}
