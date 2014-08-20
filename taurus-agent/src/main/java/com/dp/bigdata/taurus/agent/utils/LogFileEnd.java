package com.dp.bigdata.taurus.agent.utils;

import org.restlet.resource.ServerResource;

import java.io.File;

/**
 * Created by mkirin on 14-8-6.
 */
public class LogFileEnd implements ILogFileEnd {
    public static String jobPath = "/data/app/taurus-agent/jobs";
    public static String running = "/running";
    public static final String FILE_SEPRATOR = File.separator;


    @Override
    public String isEnd(String attemptId) {
        String pidFile = jobPath + running + FILE_SEPRATOR + '.' + attemptId;
         boolean isFinished = new File(pidFile).exists();
        return isFinished ? "false":"true";
    }
}

