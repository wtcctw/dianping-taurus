package com.dp.bigdata.taurus.common.netty.protocol;

import java.util.ArrayList;
import java.util.List;

/**
 * Author   mingdongli
 * 16/5/18  上午10:32.
 */
public class PeerTask extends Command{

    private String jobUniqueCode;

    private String traceId;

    private String leaderIP;

    private List<String> taskItems = new ArrayList<String>();

    public PeerTask() {
    }

    public String getJobUniqueCode() {
        return jobUniqueCode;
    }

    public void setJobUniqueCode(String jobUniqueCode) {
        this.jobUniqueCode = jobUniqueCode;
    }

    public List<String> getTaskItems() {
        return taskItems;
    }

    public void setTaskItems(List<String> taskItems) {
        this.taskItems = taskItems;
    }

    public String getLeaderIP() {
        return leaderIP;
    }

    public void setLeaderIP(String leaderIP) {
        this.leaderIP = leaderIP;
    }


    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    @Override
    public String toString() {
        return "PeerTask{" +
                "jobUniqueCode='" + jobUniqueCode + '\'' +
                ", leaderIP='" + leaderIP + '\'' +
                ", taskItems=" + taskItems +
                '}';
    }
}
