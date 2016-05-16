package com.dp.bigdata.taurus.zookeeper.common.heartbeat;

import java.util.List;

/**
 * AgentHandler
 * 
 * @author damon.zhu
 */
public interface AgentHandler {
    
    public List<String> getConnectedFromDB();

    public void disConnected(String ip);

    public void connected(String ip);
}
