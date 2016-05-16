package com.dp.bigdata.taurus.common;

import com.dp.bigdata.taurus.zookeeper.common.execute.ExecuteStatus;

/**
 * @author damon.zhu
 */
public class AttemptStatus extends ExecuteStatus {

	public AttemptStatus(int status) {
		super(status);
	}

}
