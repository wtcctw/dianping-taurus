package com.cip.crane.agent.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by mkirin on 14-8-7.
 */
public class ClearLogs {
    private static List<File> getFile(String path) {
        File file = new File(path);
        File[] array = file.listFiles();
        List<File> passLogDirList = new ArrayList<File>();
        for (int i = 0; i < array.length; i++) {
            if (array[i].isDirectory()) {
                passLogDirList.add(array[i]);
            }
        }
        return passLogDirList;
    }



    public static String clearLogs(String path) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,-7);
        Date datePreSevern = cal.getTime();//这个就是7天前的Date了
        String day = format.format(datePreSevern);
        List<File> passLogDirList = getFile(path);
        String result = "sucess";
        if (passLogDirList == null){
            return result;
        }
        for (File dir: passLogDirList){
            String[] dirArray = dir.getName().split("/");

            String dirName = dirArray[dirArray.length-1];
            if (day.compareTo(dirName) > 0)
            {
                File filelist[]=dir.listFiles();
                int listlen=filelist.length;
                for(int i=0;i<listlen;i++) {

                    filelist[i].delete();
                }
                dir.delete();
            }
        }
        return result;
    }
}
