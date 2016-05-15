package com.dp.bigdata.taurus.common.zookeeper.infochannel.guice;

import com.dp.bigdata.taurus.common.zookeeper.elect.LeaderElector;
import com.dp.bigdata.taurus.common.zookeeper.elect.TaurusZKLeaderElector;
import com.dp.bigdata.taurus.common.zookeeper.elect.ZKPair;
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
