package com.dp.bigdata.taurus.restlet.resource.impl;

import com.dp.bigdata.taurus.generated.mapper.TaskAttemptMapper;
import com.dp.bigdata.taurus.restlet.resource.ITotalTaskLoad;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.resource.ServerResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import com.google.gson.Gson;
import java.util.Map;
/**
 * Created by kirinli on 14-9-30.
 */
public class TotalTaskLoad extends ServerResource implements ITotalTaskLoad {
    @Autowired
    private TaskAttemptMapper taskAttemptMapper;

    @Override
    public String retrieve() {
        String hostInfo = "";
        JSONArray jsonData = new JSONArray();
        try {
            String start = (String) getRequestAttributes().get("starttime");
            String end = (String) getRequestAttributes().get("endtime");
            List<HashMap<String, Integer>> tasks = taskAttemptMapper.getTaskLoadHost(start, end);


            for (HashMap<String, Integer> task : tasks) {

                JSONObject json = new JSONObject();
                json.put("execHost",task.get("execHost"));
                json.put("totaltask",task.get("totaltask"));
                hostInfo += task.get("execHost") + ":" + task.get("totaltask") + ",";
                jsonData.put(json);


            }
            if (!hostInfo.isEmpty()) {
                hostInfo = hostInfo.substring(0, hostInfo.length() - 1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(jsonData.toString());
        return jsonData.toString();
    }
}
