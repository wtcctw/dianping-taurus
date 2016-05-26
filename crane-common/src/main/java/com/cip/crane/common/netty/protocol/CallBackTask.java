package com.cip.crane.common.netty.protocol;

import java.util.HashSet;
import java.util.Set;

/**
 * Author   mingdongli
 * 16/5/18  上午10:31.
 */
public class CallBackTask extends Command {

    private String traceId;

    private int subTask;

    private long endTime;

    private int runState;

    private Set<String> jobUnicodes = new HashSet<String>();

    private String node;

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public int getSubTask() {
        return subTask;
    }

    public void setSubTask(int subTask) {
        this.subTask = subTask;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getRunState() {
        return runState;
    }

    public void setRunState(int runState) {
        this.runState = runState;
    }

    public Set<String> getJobUnicodes() {
        return jobUnicodes;
    }

    public void setJobUnicodes(Set<String> jobUnicodes) {
        this.jobUnicodes = jobUnicodes;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    @Override
    public String toString() {
        return "CallBackTask{" +
                "traceId='" + traceId + '\'' +
                ", subTask=" + subTask +
                ", endTime=" + endTime +
                ", runState=" + runState +
                ", jobUnicodes=" + jobUnicodes +
                ", node='" + node + '\'' +
                '}';
    }
}
