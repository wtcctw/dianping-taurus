package com.dp.bigdata.taurus.restlet.resource.impl;

import com.dp.bigdata.taurus.generated.mapper.TaskAttemptMapper;
import com.dp.bigdata.taurus.restlet.resource.IClearDependencyPassTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.resource.ServerResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kirinli on 14/10/28.
 */
public class ClearDependencyPassTask extends ServerResource implements IClearDependencyPassTask {

    @Autowired
    private TaskAttemptMapper taskAttemptMapper;

    @Override
    public int retrieve() {
        int result = -1;
        try {
            String taskId = (String) getRequestAttributes().get("taskid");
            String status_str = (String) getRequestAttributes().get("status");
            if (status_str != null && !status_str.isEmpty()){
                int status = Integer.parseInt(status_str);
                HashMap<String, String> taskIdMap = taskAttemptMapper.isExitTaskId(taskId);
                if (taskIdMap == null || taskIdMap.size() == 0){
                    return  -2;
                }

                result   = taskAttemptMapper.deleteDependencyPassTask(taskId,status);
            }else {
                return -3;
            }




        } catch (Exception e) {
            result = -1;
        }
        return result;
    }
}
