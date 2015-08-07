package com.dp.bigdata.taurus.restlet.resource.impl;

import java.util.List;

import net.sf.json.JSONArray;

import org.restlet.resource.ServerResource;
import org.springframework.beans.factory.annotation.Autowired;

import com.dp.bigdata.taurus.core.AttemptContext;
import com.dp.bigdata.taurus.core.Scheduler;
import com.dp.bigdata.taurus.restlet.resource.IRunningAttemptsResource;

public class RunningAttemptsResource extends ServerResource implements
		IRunningAttemptsResource {

	@Autowired
	private Scheduler scheduler;
	
	@Override
	public String retrieve() {
		String taskID = (String) getRequest().getAttributes().get("taskID");
		List<AttemptContext> runnings = scheduler.getRunningAttemptsByTaskID(taskID);
		JSONArray jsonArr = JSONArray.fromObject(runnings);
		return jsonArr.toString();
	}

}
