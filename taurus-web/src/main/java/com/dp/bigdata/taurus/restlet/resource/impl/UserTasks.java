package com.dp.bigdata.taurus.restlet.resource.impl;

import com.dp.bigdata.taurus.generated.mapper.TaskAttemptMapper;
import com.dp.bigdata.taurus.generated.module.GroupTaskExample;
import com.dp.bigdata.taurus.restlet.resource.IUserTasks;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.resource.ServerResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
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
        JSONArray jsonData = new JSONArray();
        try {
            String user = (String) getRequestAttributes().get("username");
            String start = (String) getRequestAttributes().get("starttime");
            String end = (String) getRequestAttributes().get("endtime");

            int[] successStatus = {7};
            int[] failedStatus = {8};
            int[] killStatus = {10};
            int[] timeoutStatus = {9};
            int[] congestStatus = {2};

            List<HashMap<String, Integer>> successTasks = taskAttemptMapper.getUserTasks(user, start, end, successStatus);
            List<HashMap<String, Integer>> failedTasks = taskAttemptMapper.getUserTasks(user,start, end,failedStatus);
            List<HashMap<String, Integer>> killTasks = taskAttemptMapper.getUserTasks(user,start, end,killStatus);
            List<HashMap<String, Integer>> timeoutTasks = taskAttemptMapper.getUserTasks(user,start, end,timeoutStatus);
            List<HashMap<String, Integer>> congestTasks = taskAttemptMapper.getUserTasks(user,start, end,congestStatus);

            for (HashMap<String, Integer> task : successTasks) {

                JSONObject json = new JSONObject();
                json.put("taskName", task.get("name"));
                json.put("nums", task.get("num"));
                json.put("status", "success");
                jsonData.put(json);


            }
            for (HashMap<String, Integer> task : failedTasks) {
                if (task.get("name") == null){
                    break;
                }

                JSONObject json = new JSONObject();
                json.put("taskName", task.get("name"));
                json.put("nums", task.get("num"));
                json.put("status", "failed");
                jsonData.put(json);


            }

            for (HashMap<String, Integer> task : killTasks) {
                if (task.get("name") == null){
                    break;
                }

                JSONObject json = new JSONObject();
                json.put("taskName", task.get("name"));
                json.put("nums", task.get("num"));
                json.put("status", "killed");
                jsonData.put(json);


            }
            for (HashMap<String, Integer> task : timeoutTasks) {
                if (task.get("name") == null){
                    break;
                }

                JSONObject json = new JSONObject();
                json.put("taskName", task.get("name"));
                json.put("nums", task.get("num"));
                json.put("status", "timeout");
                jsonData.put(json);


            }

            for (HashMap<String, Integer> task : congestTasks) {
                if (task.get("name") == null){
                    break;
                }

                JSONObject json = new JSONObject();
                json.put("taskName", task.get("name"));
                json.put("nums", task.get("num"));
                json.put("status", "congest");
                jsonData.put(json);


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonData.toString();
    }
}
