package com.dp.bigdata.taurus.common.netty.protocol;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Author   mingdongli
 * 16/5/18  上午9:23.
 */
public class ScheduleTask extends Command{

    private String jobUniqueCode;   //taskId

    private String traceId;         // attemptId

    private int subTask;

    private List<String> taskItems = new ArrayList<String>();

    private Set<String> callbackAddress = new HashSet<String>();

    public ScheduleTask() {
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getJobUniqueCode() {
        return jobUniqueCode;
    }

    public void setJobUniqueCode(String jobUniqueCode) {
        this.jobUniqueCode = jobUniqueCode;
    }

    public int getSubTask() {
        return subTask;
    }

    public void setSubTask(int subTask) {
        this.subTask = subTask;
    }

    public List<String> getTaskItems() {
        return taskItems;
    }

    public void setTaskItems(List<String> taskItems) {
        this.taskItems = taskItems;
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
                "jobUniqueCode='" + jobUniqueCode + '\'' +
                ", traceId='" + traceId + '\'' +
                ", subTask=" + subTask +
                ", taskItems=" + taskItems +
                ", callbackAddress=" + callbackAddress +
                '}';
    }
}
