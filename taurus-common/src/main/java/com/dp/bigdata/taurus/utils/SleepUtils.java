package com.dp.bigdata.taurus.utils;

/**
 * Author   mingdongli
 * 16/3/17  上午11:44.
 */
public class SleepUtils {

    public static void sleepHalfSecond() {
        sleep(500L);
    }

    public static void sleep(final long millis) {
        try {
            Thread.sleep(millis);
        } catch (final InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
