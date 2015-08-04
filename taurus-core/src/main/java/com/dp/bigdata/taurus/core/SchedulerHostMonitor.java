package com.dp.bigdata.taurus.core;

public class SchedulerHostMonitor implements HostMonitor {

	public String hostLoadJsonData;
	
	@Override
	public void heartbeatMonitor() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hostLoadMonitor() {
		hostLoadJsonData = reFlashHostLoadData();
	}
	
	private String reFlashHostLoadData(){
		String result = null;
		
		return result;
	}

}
