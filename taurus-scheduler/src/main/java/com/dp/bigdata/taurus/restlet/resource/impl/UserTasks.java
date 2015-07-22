package com.dp.bigdata.taurus.restlet.resource.impl;

import com.dp.bigdata.taurus.generated.mapper.TaskAttemptMapper;
import com.dp.bigdata.taurus.generated.module.GroupTaskExample;
import com.dp.bigdata.taurus.restlet.resource.IUserTasks;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.resource.ServerResource;
import org.springframework.beans.factory.annotation.Autowired;

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

            List<GroupTaskExample> successTasks = taskAttemptMapper.getUserTasks(user, start, end, successStatus);
            List<GroupTaskExample>failedTasks = taskAttemptMapper.getUserTasks(user,start, end,failedStatus);
            List<GroupTaskExample>killTasks = taskAttemptMapper.getUserTasks(user,start, end,killStatus);
            List<GroupTaskExample> timeoutTasks = taskAttemptMapper.getUserTasks(user,start, end,timeoutStatus);
            List<GroupTaskExample> congestTasks = taskAttemptMapper.getUserTasks(user,start, end,congestStatus);

            for (GroupTaskExample task : successTasks) {

                if (task.getName() == null){
                    break;
                }

                JSONObject json = new JSONObject();
                json.put("taskName", task.getName());
                json.put("taskId", task.getTaskID());
                json.put("nums", task.getNum());
                json.put("status", "success");
                jsonData.put(json);


            }
            for (GroupTaskExample task : failedTasks) {
                if (task.getName() == null){
                    break;
                }

                JSONObject json = new JSONObject();
                json.put("taskName", task.getName());
                json.put("taskId", task.getTaskID());
                json.put("nums", task.getNum());
                json.put("status", "failed");
                jsonData.put(json);


            }

            for (GroupTaskExample task : killTasks) {
                if (task.getName() == null){
                    break;
                }

                JSONObject json = new JSONObject();
                json.put("taskName", task.getName());
                json.put("taskId", task.getTaskID());
                json.put("nums", task.getNum());
                json.put("status", "killed");
                jsonData.put(json);


            }
            for (GroupTaskExample task : timeoutTasks) {
                if (task.getName() == null){
                    break;
                }

                JSONObject json = new JSONObject();
                json.put("taskName", task.getName());
                json.put("taskId", task.getTaskID());
                json.put("nums", task.getNum());
                json.put("status", "timeout");
                jsonData.put(json);


            }

            for (GroupTaskExample task : congestTasks) {
                if (task.getName() == null){
                    break;
                }

                JSONObject json = new JSONObject();
                json.put("taskName", task.getName());
                json.put("taskId", task.getTaskID());
                json.put("nums", task.getNum());
                json.put("status", "congest");
                jsonData.put(json);


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonData.toString();
    }
}
