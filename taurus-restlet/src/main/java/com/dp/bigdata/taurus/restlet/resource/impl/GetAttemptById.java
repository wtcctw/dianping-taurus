package com.dp.bigdata.taurus.restlet.resource.impl;

import com.dp.bigdata.taurus.core.AttemptStatus;
import com.dp.bigdata.taurus.generated.mapper.TaskAttemptMapper;
import com.dp.bigdata.taurus.generated.module.TaskAttempt;
import com.dp.bigdata.taurus.restlet.resource.IGetAttemptById;
import com.dp.bigdata.taurus.restlet.shared.AttemptDTO;
import org.restlet.resource.ServerResource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by mkirin on 14-8-12.
 */
public class GetAttemptById extends ServerResource implements IGetAttemptById {
    @Autowired
    TaskAttemptMapper taskAttemptMapper;

    @Override
    public AttemptDTO retrieve() {
        String attemptId = (String) getRequestAttributes().get("attempt_id");
        TaskAttempt taskAttempt = taskAttemptMapper.getAttemptById(attemptId);
        AttemptDTO dto = new AttemptDTO();

        if (taskAttempt != null){
            dto.setAttemptID(attemptId);
            if (taskAttempt.getEndtime() != null){
                dto.setEndTime(taskAttempt.getEndtime());
            }

            if (taskAttempt.getExechost() != null){
                dto.setExecHost(taskAttempt.getExechost());
            }

            if (taskAttempt.getInstanceid() != null){
                dto.setInstanceID(taskAttempt.getInstanceid());
            }

            if (taskAttempt.getTaskid() != null){
                dto.setTaskID(taskAttempt.getTaskid());
            }
            if (taskAttempt.getScheduletime() != null){
                dto.setScheduleTime(taskAttempt.getScheduletime());
            }

            if (taskAttempt.getStarttime() != null){
                dto.setStartTime(taskAttempt.getStarttime());
            }

            if (taskAttempt.getStatus() != null){
                dto.setStatus(AttemptStatus.getInstanceRunState(taskAttempt.getStatus()));
            }

        }else {
            dto = null;
        }

        return dto;
    }
}
