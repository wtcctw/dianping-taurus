package com.dp.bigdata.taurus.restlet.resource.impl;

import com.dp.bigdata.taurus.generated.mapper.TaskMapper;
import com.dp.bigdata.taurus.generated.module.Task;
import com.dp.bigdata.taurus.restlet.resource.IGetTaskNameByAttemptId;
import com.dp.bigdata.taurus.restlet.shared.TaskDTO;
import org.restlet.resource.ServerResource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by mkirin on 14-8-11.
 */
public class GetTaskNameByAttemptId extends ServerResource implements IGetTaskNameByAttemptId {
   @Autowired
   TaskMapper taskMapper;
    @Override
    public String  retrieve() {
        String attemptId = (String) getRequestAttributes().get("attempt_id");
        Task task = taskMapper.getTaskByAttemptId(attemptId);

        String taskName;
        if (task !=null){
            taskName = task.getName();
        }else{
            taskName = null;
        }

        return taskName;
    }
}
