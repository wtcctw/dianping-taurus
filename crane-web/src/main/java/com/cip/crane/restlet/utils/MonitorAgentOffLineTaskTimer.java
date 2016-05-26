package com.cip.crane.restlet.utils;

import java.util.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by kirinli on 15/1/30.
 */
public class MonitorAgentOffLineTaskTimer {
	
    private volatile static MonitorAgentOffLineTaskTimer loadTimerManager;
    
    private Logger log = LoggerFactory.getLogger(this.getClass());
    
    private Timer timer;
    
    public Timer getTimer() {
		return timer;
	}
    
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

    	log.info("start MonitorAgentOffLineTaskTimer");
    	AlertOfflineAgentTask task = new AlertOfflineAgentTask();
        //安排指定的任务在指定的时间开始进行重复的固定延迟执行。

        //调用schedule方法执行任务
    	timer = new Timer();
    	timer.schedule(task,10000,RUN_INTERVAL);//过10秒执行，之后每隔RUN_INTERVAL秒执行一次

    }
    
    public void stop(){
    	log.info("stop MonitorAgentOffLineTaskTimer");
    	
    	timer.cancel();
    	timer = null;
    }
}
