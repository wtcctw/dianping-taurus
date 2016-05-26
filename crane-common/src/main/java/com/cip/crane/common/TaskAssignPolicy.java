package com.cip.crane.common;

import com.cip.crane.generated.module.Host;
import com.cip.crane.generated.module.Task;

/**
 * 
 * @author damon.zhu
 *
 */
public interface TaskAssignPolicy {
   
   /**
    * assign template a Host to execute it.
    * @param template
    * @return Host which will executes the template
    */
	public Host assignTask(Task task);
}
