package com.cip.crane.zookeeper.common.infochannel.guice;


import com.cip.crane.zookeeper.common.infochannel.TaurusZKScheduleInfoChannel;
import com.cip.crane.zookeeper.common.infochannel.interfaces.ScheduleInfoChannel;
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
