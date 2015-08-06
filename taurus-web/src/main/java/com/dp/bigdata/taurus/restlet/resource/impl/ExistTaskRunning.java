package com.dp.bigdata.taurus.restlet.resource.impl;

import com.dp.bigdata.taurus.generated.mapper.TaskAttemptMapper;
import com.dp.bigdata.taurus.generated.module.TaskAttempt;
import com.dp.bigdata.taurus.restlet.resource.IExistTaskRunning;
import org.restlet.resource.ServerResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

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
