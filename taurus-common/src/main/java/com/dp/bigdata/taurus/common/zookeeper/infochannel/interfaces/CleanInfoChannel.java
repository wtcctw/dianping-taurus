package com.dp.bigdata.taurus.common.zookeeper.infochannel.interfaces;

public interface CleanInfoChannel extends ClusterInfoChannel{
	
	public boolean rmrPath(String node);

}
