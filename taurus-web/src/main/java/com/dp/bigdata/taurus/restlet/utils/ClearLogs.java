package com.dp.bigdata.taurus.restlet.utils;

import com.dp.bigdata.taurus.zookeeper.common.infochannel.ZooKeeperCleaner;

/**
 * Created by mkirin on 14-8-7.
 */
public class ClearLogs {

    public static String clearLogs(int start,int end) {
        try {
            ZooKeeperCleaner.clearNodes(start,end);
            return "success";
        }catch (Exception e){
            return "failed";
        }

    }
}
