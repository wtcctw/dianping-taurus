package com.dp.bigdata.taurus.common.zookeeper.event;

import java.util.EventObject;

/**
 * Author   mingdongli
 * 16/3/15  下午7:46.
 */
public class LeaderChangeEvent extends EventObject {

    public LeaderChangeEvent(Object source) {
        super(source);
    }
}
