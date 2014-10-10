package com.dp.bigdata.taurus.restlet.resource.impl;

import com.dp.bigdata.taurus.generated.mapper.TaskAttemptMapper;
import com.dp.bigdata.taurus.restlet.resource.IUserTasks;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.resource.ServerResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

/**
 * Created by kirinli on 14-10-10.
 */
public class UserTasks extends ServerResource implements IUserTasks {
    @Autowired
    private TaskAttemptMapper taskAttemptMapper;

    @Override
    public String retrieve() {
        String userTasksInfo = "";
        JSONArray jsonData = new JSONArray();
        try {
            String user = (String) getRequestAttributes().get("username");
            String start = (String) getRequestAttributes().get("starttime");
            String end = (String) getRequestAttributes().get("endtime");
            List<HashMap<String, Integer>> tasks = taskAttemptMapper.getUserTasks(user,start, end);


            for (HashMap<String, Integer> task : tasks) {

                JSONObject json = new JSONObject();
                json.put("execHost", task.get("execHost"));
                json.put("totaltask", task.get("totaltask"));
                userTasksInfo += task.get("execHost") + ":" + task.get("totaltask") + ",";
                jsonData.put(json);


            }
            if (!userTasksInfo.isEmpty()) {
                userTasksInfo = userTasksInfo.substring(0, userTasksInfo.length() - 1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(jsonData.toString());
        return jsonData.toString();
    }
}
