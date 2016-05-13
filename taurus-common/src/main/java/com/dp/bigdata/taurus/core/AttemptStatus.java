package com.dp.bigdata.taurus.core;

import com.dp.bigdata.taurus.zookeeper.helper.execute.ExecuteStatus;

/**
 * @author damon.zhu
 */
public class AttemptStatus extends ExecuteStatus {

	public AttemptStatus(int status) {
		super(status);
	}

}
