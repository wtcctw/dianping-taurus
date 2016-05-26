package com.cip.crane.agent.utils;

import java.util.TimerTask;

/**
 * Created by mkirin on 14-8-7.
 */
public class ClearLogsTask extends TimerTask {
    public static String logPath = "/data/app/taurus-agent/logs";
    @Override
    public void run() {
        ClearLogs.clearLogs(logPath);
    }
}
