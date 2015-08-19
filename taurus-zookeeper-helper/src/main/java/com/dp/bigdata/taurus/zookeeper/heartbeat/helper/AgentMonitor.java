package com.dp.bigdata.taurus.zookeeper.heartbeat.helper;


/**
 * AgentStatusMonitor
 * 
 * @author damon.zhu
 */
public interface AgentMonitor {
	
	public void interruptMonitor(boolean interrupt);

    public void agentMonitor(AgentHandler handler);

}
