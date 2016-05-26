package com.cip.crane.restlet.resource.impl;

import com.cip.crane.generated.mapper.TaskAttemptMapper;
import com.cip.crane.generated.module.TaskAttempt;
import com.cip.crane.restlet.resource.IGetTaskLastStatus;
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
