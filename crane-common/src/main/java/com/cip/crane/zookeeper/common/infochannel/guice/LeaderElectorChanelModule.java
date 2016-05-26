package com.cip.crane.zookeeper.common.infochannel.guice;

import com.cip.crane.zookeeper.common.elect.LeaderElector;
import com.cip.crane.zookeeper.common.elect.ZKPair;
import com.cip.crane.zookeeper.common.elect.TaurusZKLeaderElector;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

/**
 * Author   mingdongli
 * 16/3/15  下午2:14.
 */
public class LeaderElectorChanelModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(LeaderElector.class).to(TaurusZKLeaderElector.class).in(Scopes.SINGLETON);
        bindZooKeeper();
    }

    protected void bindZooKeeper() {
        bind(ZKPair.class).toProvider(ZKPairProvider.class).in(Scopes.SINGLETON);
    }

}
