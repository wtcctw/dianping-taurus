package com.dp.bigdata.taurus.web.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

/**
 * Created by kirinli on 14-10-11.
 */
public class ReFlashHostLoadTaskTimer {

    private volatile static ReFlashHostLoadTaskTimer loadTimerManager;
    private ReFlashHostLoadTaskTimer (){}
    //时间间隔

    private static final long RUN_INTERVAL = 5 * 60 * 1000;
    public static ReFlashHostLoadTaskTimer getReFlashHostLoadManager(){
        if (loadTimerManager == null){
            synchronized (ReFlashHostLoadTaskTimer.class){
                if (loadTimerManager == null){
                    loadTimerManager = new ReFlashHostLoadTaskTimer();
                }
            }
        }
        return  loadTimerManager;
    }
    public  void start() {


        ReFlashHostLoadTask task = new ReFlashHostLoadTask();
        //安排指定的任务在指定的时间开始进行重复的固定延迟执行。

        //调用schedule方法执行任务
        new Timer().schedule(task,10000,RUN_INTERVAL);//过10秒执行，之后每隔3秒执行一次

    }


}
