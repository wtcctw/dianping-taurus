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
            List<HashMap<String, Integer>> successTasks = taskAttemptMapper.getUserTasks(user, start, end, "7");
            List<HashMap<String, Integer>> failedTasks = taskAttemptMapper.getUserTasks(user,start, end,"8");
            List<HashMap<String, Integer>> depentencyTimeOutFailedTasks = taskAttemptMapper.getUserTasks(user,start, end,"3");
            List<HashMap<String, Integer>> sumitFailedTasks = taskAttemptMapper.getUserTasks(user,start, end,"5");
            List<HashMap<String, Integer>> timeOutTasks = taskAttemptMapper.getUserTasks(user,start, end,"9");
            List<HashMap<String, Integer>> unkownTasks = taskAttemptMapper.getUserTasks(user,start, end,"11");

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
            for (HashMap<String, Integer> task : depentencyTimeOutFailedTasks) {
                if (task.get("name") == null){
                    break;
                }

                JSONObject json = new JSONObject();
                json.put("taskName", task.get("name"));
                json.put("nums", task.get("num"));
                json.put("status", "failed");
                jsonData.put(json);


            }

            for (HashMap<String, Integer> task : timeOutTasks) {
                if (task.get("name") == null){
                    break;
                }

                JSONObject json = new JSONObject();
                json.put("taskName", task.get("name"));
                json.put("nums", task.get("num"));
                json.put("status", "failed");
                jsonData.put(json);


            }

            for (HashMap<String, Integer> task : sumitFailedTasks) {
                if (task.get("name") == null){
                    break;
                }

                JSONObject json = new JSONObject();
                json.put("taskName", task.get("name"));
                json.put("nums", task.get("num"));
                json.put("status", "failed");
                jsonData.put(json);


            }

            for (HashMap<String, Integer> task : unkownTasks) {
                if (task.get("name") == null){
                    break;
                }

                JSONObject json = new JSONObject();
                json.put("taskName", task.get("name"));
                json.put("nums", task.get("num"));
                json.put("status", "failed");
                jsonData.put(json);


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(jsonData.toString());
        return jsonData.toString();
    }
}
