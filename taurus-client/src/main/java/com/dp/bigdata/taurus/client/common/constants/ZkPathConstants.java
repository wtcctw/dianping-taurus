package com.dp.bigdata.taurus.client.common.constants;

/**
 * 纪录应用zk的相关常量
 * User: hongbin03
 * Date: 16/1/27
 * Time: 上午11:52
 * MailTo: hongbin03@meituan.com
 */
public final class ZkPathConstants {

    public static final String JOB_NODE_PATH = "/jobs/%s/nodes";

    public static final String JOB_NODE_INFO_PATH = "/jobs/%s/nodes/%s";
    public static final String JOB_CRON_INTO_PATH = "/jobs/%s/static/%s";

    public static final String JOB_CRON_ROOT_PATH = "/jobs/%s/static";
    public static final String JOB_LEADER_ELECTION_LATCH = "/jobs/%s/election/latch";
}
