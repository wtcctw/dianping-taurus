package com.cip.crane.zookeeper.common.visit;

import com.cip.crane.zookeeper.common.elect.LeaderElector;

/**
 * Author   mingdongli
 * 16/3/16  下午2:21.
 */
public interface LeaderElectorVisitor {

    void visitLeaderElector(LeaderElector leaderElector);
}
