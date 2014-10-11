package com.dp.bigdata.taurus.web.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

/**
 * Created by kirinli on 14-10-11.
 */
public class ReFlashHostLoadTask extends TimerTask {
    public static String hostLoadJsonData;
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
       lastReadDataTime = new Date().getTime();

       return hostLoadJsonData;
   }
}
