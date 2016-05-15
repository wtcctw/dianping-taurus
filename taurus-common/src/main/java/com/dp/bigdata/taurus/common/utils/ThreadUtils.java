package com.dp.bigdata.taurus.common.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

/**
 * Author   mingdongli
 * 16/4/15  下午5:21.
 */
public class ThreadUtils {

    public static ExecutorService newSingleThreadExecutor(String processName) {
        return Executors.newSingleThreadExecutor(newThreadFactory(processName));
    }

    public static ExecutorService newFixedThreadPool(int qty, String processName) {
        return Executors.newFixedThreadPool(qty, newThreadFactory(processName));
    }

    public static ScheduledExecutorService newSingleThreadScheduledExecutor(String processName) {
        return Executors.newSingleThreadScheduledExecutor(newThreadFactory(processName));
    }

    public static ScheduledExecutorService newFixedThreadScheduledPool(int qty, String processName) {
        return Executors.newScheduledThreadPool(qty, newThreadFactory(processName));
    }

    public static ThreadFactory newThreadFactory(String processName) {
        return new ThreadFactoryBuilder()
                .setNameFormat(processName + "-%d")
                .setDaemon(true)
                .build();
    }

    public static String getProcessName(Class<?> clazz) {
        if ( clazz.isAnonymousClass() )
        {
            return getProcessName(clazz.getEnclosingClass());
        }
        return clazz.getSimpleName();
    }
}
