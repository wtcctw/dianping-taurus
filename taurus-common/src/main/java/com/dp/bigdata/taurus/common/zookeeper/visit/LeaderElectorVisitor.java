package com.dp.bigdata.taurus.common.zookeeper.visit;

import com.dp.bigdata.taurus.common.zookeeper.elect.LeaderElector;

/**
 * Author   mingdongli
 * 16/3/16  下午2:21.
 */
public interface LeaderElectorVisitor {

    void visitLeaderElector(LeaderElector leaderElector);
}
