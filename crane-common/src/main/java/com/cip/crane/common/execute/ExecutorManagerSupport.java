package com.cip.crane.common.execute;

/**
 * Author   mingdongli
 * 16/5/20  上午9:14.
 */
public abstract class ExecutorManagerSupport implements ExecutorManager{

    @Override
    public void kill(ExecuteContext context) throws ExecuteException {

    }

    @Override
    public ExecuteStatus getStatus(ExecuteContext context) throws ExecuteException {
        return null;
    }

    @Override
    public boolean cleanAttemptNode(String ip, String attemptId) {
        return true;
    }
}
