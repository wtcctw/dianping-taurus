package com.dp.bigdata.taurus.agent.utils;

import org.restlet.resource.ServerResource;

import java.io.File;

/**
 * Created by mkirin on 14-8-6.
 */
public class LogFileEnd extends ServerResource implements ILogFileEnd {
    public static String jobPath = "/data/app/taurus-agent/jobs";
    public static String running = "/running";
    public static final String FILE_SEPRATOR = File.separator;

    static boolean isEnd(String attemptId) {
        String pidFile = jobPath + running + FILE_SEPRATOR + '.' + attemptId;
        return new File(pidFile).exists();
    }

    @Override
    public String retrieve() {
        String attemptId = (String) getRequestAttributes().get("attemptId");
        boolean isFinished = isEnd(attemptId);
        return isFinished ? "false":"true";
    }
}

