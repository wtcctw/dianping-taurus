package com.dp.bigdata.taurus.restlet.resource.impl;

import java.util.Date;
import java.util.List;

import com.dianping.cat.Cat;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import groovy.json.JsonException;
import net.sf.json.JSONArray;

import org.json.JSONException;
import org.json.JSONObject;
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
        JsonArray jsonArray = new JsonArray();
        Date time = null;
        for(AttemptContext running: runnings){

            if (time == null || time.before(running.getScheduletime())){
                time = running.getScheduletime();
            }



        }
        JsonObject scheduleTimeJson = new JsonObject();
        try {
            scheduleTimeJson.addProperty("last-schedule-time", time.getTime());
        } catch (JsonException e) {

            Cat.logError("Transfer Json Error", e);

            e.printStackTrace();
        }
		return scheduleTimeJson.toString();
	}

}
