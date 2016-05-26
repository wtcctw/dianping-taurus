package com.cip.crane.agent.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

/**
 * Created by mkirin on 14-8-7.
 */
public class ClearLogsTimerManager {

    private volatile static ClearLogsTimerManager clearLogsTimerManager;
    private ClearLogsTimerManager (){}
    //时间间隔

        private static final long PERIOD_DAY = 24 * 60 * 60 * 1000;
        public static ClearLogsTimerManager getClearLogsTimerManager(){
            if (clearLogsTimerManager == null){
                synchronized (ClearLogsTimerManager.class){
                    if (clearLogsTimerManager == null){
                        clearLogsTimerManager = new ClearLogsTimerManager();
                    }
                }
            }
            return  clearLogsTimerManager;
        }
        public  void start() {
            Calendar calendar = Calendar.getInstance();

            /*** 定制每日2:00执行方法 ***/

            calendar.set(Calendar.HOUR_OF_DAY, 2);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            Date date=calendar.getTime(); //第一次执行定时任务的时间

            Timer timer = new Timer();

            ClearLogsTask task = new ClearLogsTask();
            //安排指定的任务在指定的时间开始进行重复的固定延迟执行。
            timer.schedule(task,date,PERIOD_DAY);
        }


}
