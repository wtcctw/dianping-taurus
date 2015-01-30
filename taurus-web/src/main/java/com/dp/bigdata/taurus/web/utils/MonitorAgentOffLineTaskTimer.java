package com.dp.bigdata.taurus.web.utils;

import java.util.Timer;

/**
 * Created by kirinli on 15/1/30.
 */
public class MonitorAgentOffLineTaskTimer {
    private volatile static MonitorAgentOffLineTaskTimer loadTimerManager;
    private MonitorAgentOffLineTaskTimer(){}
    private static final long RUN_INTERVAL = 5 * 60 * 1000;
    public static MonitorAgentOffLineTaskTimer getMonitorAgentOffLineTimeManager(){
        if (loadTimerManager == null){
            synchronized (MonitorAgentOffLineTaskTimer.class){
                if (loadTimerManager == null){
                    loadTimerManager = new MonitorAgentOffLineTaskTimer();
                }
            }
        }
        return  loadTimerManager;
    }
    public  void start() {


      AlertOfflineAgentTask task = new AlertOfflineAgentTask();
        //安排指定的任务在指定的时间开始进行重复的固定延迟执行。

        //调用schedule方法执行任务
        new Timer().schedule(task,10000,RUN_INTERVAL);//过10秒执行，之后每隔RUN_INTERVAL秒执行一次

    }
}
