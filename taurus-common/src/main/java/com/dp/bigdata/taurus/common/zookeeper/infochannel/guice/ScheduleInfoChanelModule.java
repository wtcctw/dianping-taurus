package com.dp.bigdata.taurus.common.zookeeper.infochannel.guice;


import com.dp.bigdata.taurus.common.zookeeper.infochannel.TaurusZKScheduleInfoChannel;
import com.dp.bigdata.taurus.common.zookeeper.infochannel.interfaces.ScheduleInfoChannel;
import org.I0Itec.zkclient.ZkClient;

import com.google.inject.AbstractModule;

public class ScheduleInfoChanelModule extends AbstractModule{

	@Override
	protected void configure() {
		bind(ScheduleInfoChannel.class).to(TaurusZKScheduleInfoChannel.class);
		bindZooKeeper();
	}
	
	protected void bindZooKeeper() {
		bind(ZkClient.class).toProvider(ZooKeeperProvider.class);
	}
	
}
