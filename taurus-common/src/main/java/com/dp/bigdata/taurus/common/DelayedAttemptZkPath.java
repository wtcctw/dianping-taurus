package com.dp.bigdata.taurus.common;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * Author   mingdongli
 * 16/5/9  下午6:33.
 */
public class DelayedAttemptZkPath implements Delayed{

    private static final long DEFAULT_DELAY = 30000;

    private String ip;

    private String attemptId;

    private long startTime;

    public DelayedAttemptZkPath(String ip, String attemptId) {
        this(ip, attemptId, DEFAULT_DELAY);
    }

    public DelayedAttemptZkPath(String ip, String attemptId, long delay) {
        this.ip = ip;
        this.attemptId = attemptId;
        this.startTime = System.currentTimeMillis() + delay;
    }

    public String getIp() {
        return ip;
    }

    public String getAttemptId() {
        return attemptId;
    }

    @Override
    public long getDelay(TimeUnit unit) {

        long diff = startTime - System.currentTimeMillis();
        return unit.convert(diff, TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        if (this.startTime < ((DelayedAttemptZkPath) o).startTime) {
            return -1;
        }
        if (this.startTime > ((DelayedAttemptZkPath) o).startTime) {
            return 1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "DelayedAttemptZkPath{" +
                "ip='" + ip + '\'' +
                ", attemptId='" + attemptId + '\'' +
                ", startTime=" + startTime +
                '}';
    }
}
