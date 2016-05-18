package com.dp.bigdata.taurus.common.netty;

import com.dp.bigdata.taurus.zookeeper.common.execute.ExecuteContext;
import com.dp.bigdata.taurus.zookeeper.common.execute.ExecuteException;
import com.dp.bigdata.taurus.zookeeper.common.execute.ExecuteStatus;
import com.dp.bigdata.taurus.zookeeper.common.execute.ExecutorManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author   mingdongli
 * 16/5/18  上午10:06.
 */
public abstract class AbstractExecutorManager implements ExecutorManager{

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void execute(ExecuteContext context) throws ExecuteException {

    }

    @Override
    public void kill(ExecuteContext context) throws ExecuteException {

    }

    @Override
    public ExecuteStatus getStatus(ExecuteContext context) throws ExecuteException {
        return null;
    }

    @Override
    public boolean updateStatus(ExecuteContext context) throws ExecuteException {
        return true;
    }

    @Override
    public boolean cleanAttemptNode(String ip, String attemptId) {
        return true;
    }

}
