package com.dp.bigdata.taurus.restlet.resource.impl;

import com.dianping.cat.Cat;
import com.dp.bigdata.taurus.generated.mapper.TaskAttemptMapper;
import com.dp.bigdata.taurus.restlet.resource.IRunTaskLoad;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kirinli on 14-9-30.
 */
public class RunTaskLoad extends ServerResource implements IRunTaskLoad {
    @Autowired
    private TaskAttemptMapper taskAttemptMapper;

    @Override
    @Get
    public String retrieve() {
        JSONArray jsonData = new JSONArray();
        try {
            List<HashMap<String, Integer>> tasks = taskAttemptMapper.getRunningTaskLoadHost();
            if (tasks !=null){
                for (HashMap<String, Integer> task : tasks) {

                    JSONObject json = new JSONObject();

                    json.put("execHost", task.get("execHost"));

                    json.put("totaltask", task.get("totaltask"));
                    jsonData.put(json);


                }
            }

        } catch (JSONException e) {
            Cat.logError("RunTaskLoad JSONException", e);
        }

        return jsonData.toString();
    }
}
