package com.cip.crane.common;

import com.cip.crane.common.listener.AttemptListenable;
import com.cip.crane.generated.module.TaskAttempt;

import java.util.Collection;

/**
 * Author   mingdongli
 * 16/4/18  下午06:20.
 */
public interface Triggle extends AttemptListenable {
	
	void triggle();

	void triggle(Collection<TaskAttempt> taskAttempts);

}
