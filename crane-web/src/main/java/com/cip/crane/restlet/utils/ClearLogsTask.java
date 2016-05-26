package com.cip.crane.restlet.utils;

import java.util.TimerTask;

/**
 * Created by mkirin on 14-8-7.
 */
public class ClearLogsTask extends TimerTask {
    public static int start = -8;
    public static int end = -1;
    @Override
    public void run() {
        ClearLogs.clearLogs(start,end);
    }
}
