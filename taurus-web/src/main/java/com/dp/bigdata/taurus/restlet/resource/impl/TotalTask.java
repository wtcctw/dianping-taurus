package com.dp.bigdata.taurus.restlet.resource.impl;

import com.dp.bigdata.taurus.generated.mapper.TaskAttemptMapper;
import com.dp.bigdata.taurus.generated.mapper.UserGroupMapper;
import com.dp.bigdata.taurus.generated.mapper.UserGroupMappingMapper;
import com.dp.bigdata.taurus.generated.mapper.UserMapper;
import com.dp.bigdata.taurus.generated.module.*;
import com.dp.bigdata.taurus.restlet.resource.ITotalTask;
import com.dp.bigdata.taurus.restlet.shared.UserDTO;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.resource.ServerResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kirinli on 14/12/11.
 */
public class TotalTask extends ServerResource implements ITotalTask {

    @Autowired
    private TaskAttemptMapper taskAttemptMapper;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserGroupMapper userGroupMapper;

    @Autowired
    private UserGroupMappingMapper userGroupMappingMapper;
    @Override
    public String retrieve() {

        UserExample example = new UserExample();
        example.or();
        List<User> users = userMapper.selectByExample(example);
        HashMap<String,String> userGroupMap = new HashMap<String, String>();
        for (User user : users) {

            UserGroupMappingExample mappingExample = new UserGroupMappingExample();
            mappingExample.or().andUseridEqualTo(user.getId());
            List<UserGroupMapping> userGroups = userGroupMappingMapper.selectByExample(mappingExample);
            if(userGroups.size() == 0){
                userGroupMap.put(user.getName(),"");
            } else {
                int groupId = userGroups.get(0).getGroupid();
                UserGroup group = userGroupMapper.selectByPrimaryKey(groupId);
                userGroupMap.put(user.getName(),group.getGroupname());
            }
        }


        JSONArray jsonData = new JSONArray();
        try {
            String start = (String) getRequestAttributes().get("starttime");
            String end = (String) getRequestAttributes().get("endtime");
            int[] successStatus = {7};
            int[] failedStatus = {8};
            int[] killStatus = {10};
            int[] timeoutStatus = {9};
            int[] congestStatus = {2};

            ArrayList<TotalTaskExample> successTasks = taskAttemptMapper.getTotalTasks(start, end, successStatus);
            ArrayList<TotalTaskExample> failedTasks = taskAttemptMapper.getTotalTasks(start, end, failedStatus);
            ArrayList<TotalTaskExample> killTasks = taskAttemptMapper.getTotalTasks(start, end, killStatus);
            ArrayList<TotalTaskExample> timeoutTasks = taskAttemptMapper.getTotalTasks(start, end,timeoutStatus);
            ArrayList<TotalTaskExample> congestTasks = taskAttemptMapper.getTotalTasks(start, end, congestStatus);

            for (TotalTaskExample task : successTasks) {

                JSONObject json = new JSONObject();
                json.put("taskName", task.getName());
                json.put("nums", task.getNum());
                json.put("status", "success");
                json.put("creator", task.getCreator());

                String group = userGroupMap.get(task.getCreator());
                if (group!= null){
                    json.put("group", group);
                }else {
                    json.put("group", "");
                }

                jsonData.put(json);


            }
            for (TotalTaskExample task : failedTasks) {
                if (task.getNum() == null){
                    break;
                }

                JSONObject json = new JSONObject();
                json.put("taskName", task.getName());
                json.put("nums", task.getNum());
                json.put("status", "failed");
                json.put("creator", task.getCreator());

                String group = userGroupMap.get(task.getCreator());
                if (group!= null){
                    json.put("group", group);
                }else {
                    json.put("group", "");
                }

                jsonData.put(json);


            }

            for (TotalTaskExample task : killTasks) {
                if (task.getNum() == null){
                    break;
                }

                JSONObject json = new JSONObject();
                json.put("taskName", task.getName());
                json.put("nums", task.getNum());
                json.put("status", "killed");
                json.put("creator", task.getCreator());

                String group = userGroupMap.get(task.getCreator());
                if (group!= null){
                    json.put("group", group);
                }else {
                    json.put("group", "");
                }

                jsonData.put(json);


            }

            for (TotalTaskExample task : timeoutTasks) {
                if (task.getNum() == null){
                    break;
                }

                JSONObject json = new JSONObject();
                json.put("taskName", task.getName());
                json.put("nums", task.getNum());
                json.put("status", "timeout");
                json.put("creator", task.getCreator());

                String group = userGroupMap.get(task.getCreator());
                if (group!= null){
                    json.put("group", group);
                }else {
                    json.put("group", "");
                }

                jsonData.put(json);


            }

            for (TotalTaskExample task : congestTasks) {
                if (task.getNum() == null){
                    break;
                }

                JSONObject json = new JSONObject();
                json.put("taskName", task.getName());
                json.put("nums", task.getNum());
                json.put("status", "congest");
                json.put("creator", task.getCreator());

                String group = userGroupMap.get(task.getCreator());
                if (group!= null){
                    json.put("group", group);
                }else {
                    json.put("group", "");
                }

                jsonData.put(json);


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(jsonData.toString());
        return jsonData.toString();
    }
}
