package com.cip.crane.zookeeper.common.infochannel.guice;

import org.I0Itec.zkclient.ZkClient;

import com.cip.crane.zookeeper.common.infochannel.TaurusZKDeploymentInfoChannel;
import com.cip.crane.zookeeper.common.infochannel.interfaces.DeploymentInfoChannel;
import com.google.inject.AbstractModule;

public class DeploymentInfoChannelModule extends AbstractModule {
	
	@Override
	protected void configure() {
		bind(DeploymentInfoChannel.class).to(TaurusZKDeploymentInfoChannel.class);
		bindZooKeeper();
		configureOthers();
	}
	
	protected void bindZooKeeper() {
		bind(ZkClient.class).toProvider(ZooKeeperProvider.class);
	}
	
	protected void configureOthers() {}
	
}
