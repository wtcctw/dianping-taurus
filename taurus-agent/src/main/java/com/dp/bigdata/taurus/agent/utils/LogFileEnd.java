package com.dp.bigdata.taurus.agent.utils;

import org.restlet.resource.ServerResource;

import java.io.File;

/**
 * Created by mkirin on 14-8-6.
 */
public class LogFileEnd extends ServerResource implements ILogFileEnd{
    public static String jobPath = "/data/app/taurus-agent/jobs";
    public static String running = "/running";
    public static final String FILE_SEPRATOR = File.separator;
    static boolean  isEnd(String attemptId){

        String pidFile = jobPath + running + FILE_SEPRATOR + '.' + attemptId;
        if(new File(pidFile).exists()){
            return true;
        }else{
            return false;
        }

    }

    @Override
    public String retrieve() {
        String attemptId =  (String) getRequestAttributes().get("attemptId");
        boolean isFinished = isEnd(attemptId);
        if (!isFinished){
            return "true";
        }else{
            return "false";
        }

    }

    public static void main(String[] argv){
        while(true){
            boolean isFinished = isEnd("attempt_201408042016_0001_0171_0001");
            System.out.println("***************"+isFinished);

        }
    }
}

