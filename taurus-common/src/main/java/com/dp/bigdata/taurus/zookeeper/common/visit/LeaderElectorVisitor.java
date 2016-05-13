package com.dp.bigdata.taurus.zookeeper.common.visit;

import com.dp.bigdata.taurus.zookeeper.common.elect.LeaderElector;

/**
 * Author   mingdongli
 * 16/3/16  下午2:21.
 */
public interface LeaderElectorVisitor {

    void visitLeaderElector(LeaderElector leaderElector);
}
