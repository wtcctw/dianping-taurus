package com.dp.bigdata.taurus.client.common.util;

import java.util.UUID;

/**
 * User: hongbin03
 * Date: 16/1/25
 * Time: 下午3:12
 * MailTo: hongbin03@meituan.com
 */
public class TraceGenerator {

    /**
     * UUID生成器
     * @return
     */
    public static String generatTraceId(){
       return UUID.randomUUID().toString();
    }
}
