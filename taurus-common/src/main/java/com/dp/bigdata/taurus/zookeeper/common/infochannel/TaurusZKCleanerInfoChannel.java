package com.dp.bigdata.taurus.zookeeper.common.infochannel;

import com.google.inject.Singleton;
import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import com.dp.bigdata.taurus.zookeeper.common.infochannel.interfaces.CleanInfoChannel;
import com.google.inject.Inject;

@Singleton
public class TaurusZKCleanerInfoChannel extends TaurusZKInfoChannel implements CleanInfoChannel{

	private final Log LOGGER = LogFactory.getLog(TaurusZKCleanerInfoChannel.class);
	
	@Inject
	TaurusZKCleanerInfoChannel(ZkClient zk) {
        super(zk);
    }

	@Override
	public boolean rmrPath(String path) {
	    LOGGER.debug("Start removing path: " + path);
		return zk.deleteRecursive(path);
	} 


}
