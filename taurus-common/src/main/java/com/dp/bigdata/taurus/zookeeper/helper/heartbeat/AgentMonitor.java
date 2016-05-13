package com.dp.bigdata.taurus.zookeeper.helper.heartbeat;


/**
 * AgentStatusMonitor
 * 
 * @author damon.zhu
 */
public interface AgentMonitor {
	
	public void interruptMonitor(boolean interrupt);

    public void agentMonitor(AgentHandler handler);

}
