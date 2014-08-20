package com.dp.bigdata.taurus.restlet.resource.impl;

import com.dp.bigdata.taurus.core.AttemptStatus;
import com.dp.bigdata.taurus.generated.mapper.TaskAttemptMapper;
import com.dp.bigdata.taurus.generated.module.TaskAttempt;
import com.dp.bigdata.taurus.restlet.resource.IGetAttemptsByStatus;
import com.dp.bigdata.taurus.restlet.shared.AttemptDTO;
import org.restlet.resource.ServerResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mkirin on 14-8-12.
 */
public class GetAttemptsByStatus extends ServerResource implements IGetAttemptsByStatus {
    @Autowired
    TaskAttemptMapper taskAttemptMapper;
    @Override
    public ArrayList<AttemptDTO> retrieve() {
        String status = (String) getRequestAttributes().get("status");
        ArrayList<AttemptDTO> attemptDTOs = new ArrayList<AttemptDTO>();
        List<TaskAttempt> taskAttempts = taskAttemptMapper.getAttemptByStatus(Integer.parseInt(status));
        if (taskAttempts!=null){
            for (TaskAttempt taskAttempt : taskAttempts){
                AttemptDTO dto = new AttemptDTO();
                dto.setAttemptID(taskAttempt.getAttemptid());

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
                attemptDTOs.add(dto);
            }
        }
        return attemptDTOs;
    }
}
