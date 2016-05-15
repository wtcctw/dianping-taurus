package com.dp.bigdata.taurus.common.zookeeper.heartbeat;


/**
 * AgentStatusMonitor
 * 
 * @author damon.zhu
 */
public interface AgentMonitor {
	
	public void interruptMonitor(boolean interrupt);

    public void agentMonitor(AgentHandler handler);

}
