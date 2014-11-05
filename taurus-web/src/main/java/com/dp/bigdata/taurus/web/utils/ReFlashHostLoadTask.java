package com.dp.bigdata.taurus.web.utils;

import com.dp.bigdata.taurus.generated.module.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimerTask;

/**
 * Created by kirinli on 14-10-11.
 */
public class ReFlashHostLoadTask extends TimerTask {
    public static String hostLoadJsonData;
    public static ArrayList<Task> allTasks ;
    public static long lastReadDataTime;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public void run() {
        hostLoadJsonData = ReFlashLoad.reFlashHostLoadData();
    }

   public static String read(){
       long now = new Date().getTime();
       if (lastReadDataTime != 0 && (now - lastReadDataTime)< 60*1000){
           return  hostLoadJsonData;
       }
       hostLoadJsonData = ReFlashLoad.reFlashHostLoadData();
       allTasks = ReFlashLoad.getTasks();
       lastReadDataTime = new Date().getTime();

       return hostLoadJsonData;
   }

    public static ArrayList<Task> getTasks(){
        long now = new Date().getTime();
        if (lastReadDataTime != 0 && (now - lastReadDataTime)< 60*1000){
            return  allTasks;
        }
        allTasks = ReFlashLoad.getTasks();
        lastReadDataTime = new Date().getTime();

        return allTasks;
    }
}
