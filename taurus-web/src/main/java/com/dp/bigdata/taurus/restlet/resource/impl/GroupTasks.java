package com.dp.bigdata.taurus.restlet.resource.impl;

import com.dp.bigdata.taurus.generated.mapper.TaskAttemptMapper;
import com.dp.bigdata.taurus.generated.module.GroupTaskExample;
import com.dp.bigdata.taurus.restlet.resource.IGroupTasks;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.resource.ServerResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kirinli on 14-10-11.
 */
public class GroupTasks extends ServerResource implements IGroupTasks {
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
            ArrayList<GroupTaskExample> successTasks = taskAttemptMapper.getGroupTasks(user, start, end,successStatus);
            ArrayList<GroupTaskExample> failedTasks = taskAttemptMapper.getGroupTasks(user,start, end,failedStatus);

            for (GroupTaskExample task : successTasks) {

                JSONObject json = new JSONObject();
                json.put("taskName", task.getName());
                json.put("nums", task.getNum());
                json.put("status", "success");
                json.put("creator", task.getCreator());
                jsonData.put(json);


            }
            for (GroupTaskExample task : failedTasks) {
                if (task.getNum() == null){
                    break;
                }

                JSONObject json = new JSONObject();
                json.put("taskName", task.getName());
                json.put("nums", task.getNum());
                json.put("status", "failed");
                json.put("creator", task.getCreator());
                jsonData.put(json);


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(jsonData.toString());
        return jsonData.toString();
    }
}
