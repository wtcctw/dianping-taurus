package com.dp.bigdata.taurus.zookeeper.common.event;

import java.util.EventListener;

/**
 * Author   mingdongli
 * 16/3/15  下午7:50.
 */
public interface LeaderChangedListener extends EventListener {

    void onBecomingLeader(LeaderChangeEvent leaderChangeEvent);

    void onResigningAsLeader(LeaderChangeEvent leaderChangeEvent);

}
