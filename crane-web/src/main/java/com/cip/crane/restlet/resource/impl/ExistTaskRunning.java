package com.cip.crane.restlet.resource.impl;

import com.cip.crane.generated.mapper.TaskAttemptMapper;
import com.cip.crane.restlet.resource.IExistTaskRunning;
import com.cip.crane.generated.module.TaskAttempt;
import org.restlet.resource.ServerResource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by kirinli on 14/12/8.
 */
public class ExistTaskRunning extends ServerResource implements IExistTaskRunning {

    @Autowired
    private TaskAttemptMapper taskAttemptMapper;

    @Override
    public String retrieve() {
        String taskId = (String) getRequestAttributes().get("task_id");

        TaskAttempt taskAttempt = taskAttemptMapper.isExitRunningTask(taskId);
        if (taskAttempt == null ){
            return  "false";
        }else{
            return "true";
        }
    }
}
