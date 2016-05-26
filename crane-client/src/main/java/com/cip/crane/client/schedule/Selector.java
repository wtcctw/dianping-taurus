package com.cip.crane.client.schedule;

import java.util.Set;

/**
 * User: hongbin03
 * Date: 16/1/25
 * Time: 下午1:29
 * MailTo: hongbin03@meituan.com
 */
public interface Selector {

    String select(String leaderIp, Set<String> source);
}
