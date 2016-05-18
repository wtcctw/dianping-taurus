package com.dp.bigdata.taurus.common.netty.protocol;

import java.util.HashSet;
import java.util.Set;

/**
 * Author   mingdongli
 * 16/5/18  上午9:23.
 */
public class ScheduleTask extends Command{

    private String taskId;

    private String attemptId;

    private Set<String> callbackAddress = new HashSet<String>();

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(String attemptId) {
        this.attemptId = attemptId;
    }

    public Set<String> getCallbackAddress() {
        return callbackAddress;
    }

    public void setCallbackAddress(Set<String> callbackAddress) {
        this.callbackAddress = callbackAddress;
    }

    @Override
    public String toString() {
        return "ScheduleTask{" +
                "taskId='" + taskId + '\'' +
                ", attemptId='" + attemptId + '\'' +
                ", callbackAddress=" + callbackAddress +
                '}';
    }
}
