package com.dp.bigdata.taurus.restlet.resource.impl;

import com.dp.bigdata.taurus.generated.mapper.TaskAttemptMapper;
import com.dp.bigdata.taurus.generated.mapper.TaskMapper;
import com.dp.bigdata.taurus.generated.module.TaskAttempt;
import com.dp.bigdata.taurus.restlet.resource.IGetTaskLastStatus;
import org.restlet.resource.ServerResource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by kirinli on 14/12/19.
 */
public class GetTaskLastStatus  extends ServerResource implements IGetTaskLastStatus {
    @Autowired
    TaskAttemptMapper taskAttemptMapper;

    @Override
    public String retrieve() {

        String taskId = (String) getRequestAttributes().get("task_id");

        TaskAttempt taskAttempt = taskAttemptMapper.isExitRunningTask(taskId);
        if (taskAttempt == null ){
            int status = taskAttemptMapper.getTaskLastStatus(taskId);
            return "{\"status\": "+ status +"}";
        }else{
            return "{\"status\": "+ 6 +"}";
        }

    }
}
