package com.dp.bigdata.taurus.common.zookeeper.execute;

/**
 * 
 * ExecuteStatus
 * @author damon.zhu
 *
 */
public class ExecuteStatus {

    public static final int INITIALIZED = 1;
    public static final int DEPENDENCY_PASS = 2;
    public static final int DEPENDENCY_TIMEOUT = 3;//依赖任务等待时间超时
    public static final int SUBMIT_SUCCESS = 4;
    public static final int SUBMIT_FAIL = 5;
    public static final int RUNNING = 6;
    public static final int SUCCEEDED = 7;
    public static final int FAILED = 8;
    public static final int TIMEOUT = 9;//执行时间超时
    public static final int AUTO_KILLED = 10;//执行时间超时被自动杀死
    public static final int UNKNOWN = 11;
    public static final int EXPIRED = 12;//拥塞调度过期
    public static final int MAN_KILLED = 13;//人工杀死调度
    public static final int DEPENDENCY_EXPIRED = 14;//TODO 依赖任务等待时间超时后过期
    public static final int CONGESTION_SKIPED = 15;//TODO 依赖任务等待时间超时后过期

    private static final String[] runStates = { "INITIALIZED", "DEPENDENCY_PASS", "DEPENDENCY_TIMEOUT", "SUBMIT_SUCCESS",
            "SUBMIT_FAIL", "RUNNING", "SUCCEEDED", "FAILED", "TIMEOUT", "KILLED", "UNKNOWN" , "EXPIRED", "MAN_KILLED",
            "DEPENDENCY_EXPIRED", "CONGESTION_SKIPED"};

    private int status;
    private int returnCode;

    public ExecuteStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    /**
     * Helper method to get human-readable state of the job.
     * 
     * @param state job state
     * @return human-readable state of the job
     */
    public static String getInstanceRunState(int state) {
        if (state < 1 || state > runStates.length) {
            return "UNKNOWN";
        }
        return runStates[state - 1];
    }
    
    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }
}