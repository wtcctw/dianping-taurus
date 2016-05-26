package com.cip.crane.client.common.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenzhencheng on 15/12/2.
 */
public class EnvConstants {
    //环境变量
    public final static Map<String,String> ENVCONSTANTMAP=new HashMap<String, String>();
    public final static String ENV_DEV_PATH="/zookeeper-dev.conf";
    public final static String ENV_BETA_PATH="/zookeeper-beta.conf";
    public final static String ENV_ONLINE_PATH="/zookeeper-online.conf";

    public final static String ENV_ONLINE="online";
    public final static String ENV_DEV="dev";
    public final static String ENV_BETA="beta";

    static {
        ENVCONSTANTMAP.put(ENV_DEV,ENV_DEV_PATH);
        ENVCONSTANTMAP.put(ENV_BETA,ENV_BETA_PATH);
        ENVCONSTANTMAP.put(ENV_ONLINE,ENV_ONLINE_PATH);
    }


}
