package com.dp.bigdata.taurus.common;

/**
 * Author   mingdongli
 * 16/4/23  上午11:57.
 */
public interface SchedulerConfig {

    void setMaxConcurrency(int maxConcurrency);

    boolean isTriggleThreadRestFlag();

}
