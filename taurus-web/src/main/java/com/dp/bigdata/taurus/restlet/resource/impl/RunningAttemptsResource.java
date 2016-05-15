package com.dp.bigdata.taurus.restlet.resource.impl;

import java.util.Date;
import java.util.List;

import com.dianping.cat.Cat;
import com.dianping.lion.Environment;

import com.google.gson.JsonObject;
import groovy.json.JsonException;

import org.restlet.resource.ServerResource;
import org.springframework.beans.factory.annotation.Autowired;

import com.dp.bigdata.taurus.common.AttemptContext;
import com.dp.bigdata.taurus.common.Scheduler;
import com.dp.bigdata.taurus.restlet.resource.IRunningAttemptsResource;

public class RunningAttemptsResource extends ServerResource implements
		IRunningAttemptsResource {

	@Autowired
	private Scheduler scheduler;
	
	@Override
	public String retrieve() {
		String taskID = (String) getRequest().getAttributes().get("taskID");
		List<AttemptContext> runnings = scheduler.getRunningAttemptsByTaskID(taskID);
        Date time = null;

        if (runnings != null){
            for(AttemptContext running: runnings){

                if (time == null || time.before(running.getScheduletime())){
                    time = running.getScheduletime();
                }

            }


        }
        
        JsonObject scheduleTimeJson = new JsonObject();

        try {
            if (time != null) {
                scheduleTimeJson.addProperty("last-schedule-time", time.getTime());
            }else{
                scheduleTimeJson.addProperty("last-schedule-time", -1);
            }

            String env = Environment.getEnv();
            scheduleTimeJson.addProperty("env", env);
        } catch (JsonException e) {

            Cat.logError("Transfer Json Error", e);

            e.printStackTrace();
        }
        return scheduleTimeJson.toString();

    }

}
