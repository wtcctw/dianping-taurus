package com.dp.bigdata.taurus.client.common.util;

/**
 * User: hongbin03
 * Date: 16/1/23
 * Time: 下午4:45
 * MailTo: hongbin03@meituan.com
 */
public class ThreadUtils {
    public static void sleepUnInterrupted(int millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }
}
