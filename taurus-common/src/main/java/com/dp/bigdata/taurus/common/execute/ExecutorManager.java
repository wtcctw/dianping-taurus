package com.dp.bigdata.taurus.common.execute;

/**
 * 
 * ExecutorManager
 * @author damon.zhu
 *
 */
public interface ExecutorManager {

    /**
     * execute the attempt
     * @param context
     * @throws ExecuteException
     */
    public void execute(ExecuteContext context) throws ExecuteException;

    /**
     * kill the attempt
     * @param context
     * @throws ExecuteException
     */
    public void kill(ExecuteContext context) throws ExecuteException;

    /**
     * get attempt status for the given context
     * @param context
     * @return
     * @throws ExecuteException
     */
    public ExecuteStatus getStatus(ExecuteContext context) throws ExecuteException;

    public boolean cleanAttemptNode(String ip, String attemptId);

}
