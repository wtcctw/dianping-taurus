package com.cip.crane.zookeeper.common.infochannel.interfaces;

public interface CleanInfoChannel extends ClusterInfoChannel{
	
	public boolean rmrPath(String node);

}
