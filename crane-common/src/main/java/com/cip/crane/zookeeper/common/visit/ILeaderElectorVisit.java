package com.cip.crane.zookeeper.common.visit;

/**
 * Author   mingdongli
 * 16/3/16  下午2:20.
 */
public interface ILeaderElectorVisit {

    void accept(LeaderElectorVisitor electorVisitor);
}
