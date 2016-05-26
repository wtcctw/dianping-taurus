package com.cip.crane.restlet.resource.impl;

import com.dianping.cat.Cat;
import com.cip.crane.generated.mapper.TaskAttemptMapper;
import com.cip.crane.restlet.resource.IFailedTaskLoad;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.resource.ServerResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

/**
 * Created by kirinli on 14-9-30.
 */
public class FailedTaskLoad  extends ServerResource implements IFailedTaskLoad {
    @Autowired
    private TaskAttemptMapper taskAttemptMapper;
    @Override
    public String retrieve() {
        JSONArray jsonData = new JSONArray();
        try {
            String start = (String) getRequestAttributes().get("starttime");
            String end = (String) getRequestAttributes().get("endtime");
            List<HashMap<String, Integer>> tasks = taskAttemptMapper.getFailedTaskLoadHost(start, end);


            for (HashMap<String, Integer> task : tasks) {

                JSONObject json = new JSONObject();
                json.put("execHost",task.get("execHost"));
                json.put("totaltask",task.get("totaltask"));
                jsonData.put(json);


            }
        } catch (JSONException e) {
            Cat.logError("FailedTaskLoad JSONException", e);
        }
        return jsonData.toString();
    }
}
