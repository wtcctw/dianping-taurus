package com.cip.crane.zookeeper.common.heartbeat;


/**
 * AgentStatusMonitor
 * 
 * @author damon.zhu
 */
public interface AgentMonitor {
	
	public void interruptMonitor(boolean interrupt);

    public void agentMonitor(AgentHandler handler);

}
