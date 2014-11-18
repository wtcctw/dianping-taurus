package com.dp.bigdata.taurus.restlet.resource.impl;

import com.dp.bigdata.taurus.generated.mapper.TaskAttemptMapper;
import com.dp.bigdata.taurus.generated.mapper.TaskMapper;
import com.dp.bigdata.taurus.restlet.resource.IUpdateCreator;
import org.restlet.resource.ServerResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

/**
 * Created by kirinli on 14/11/18.
 */
public class UpdateCreator  extends ServerResource implements IUpdateCreator{
    @Autowired
    TaskMapper taskMapper;
    @Autowired
    private TaskAttemptMapper taskAttemptMapper;
    private static final int SERVICE_EXCEPTION = -1;
    private static final int TASKID_IS_NOT_FOUND = -2;
    private static final int CREATOR_IS_NOT_RIGHT = -3;
    @Override
    public int retrieve() {
        int result = 0;
        try {
            String taskName = (String) getRequestAttributes().get("taskName");
            String creator = (String) getRequestAttributes().get("creator");
            if (creator != null && !creator.isEmpty()){
                HashMap<String, String> taskIdMap = taskMapper.isExitTaskName(taskName);
                if (taskIdMap == null || taskIdMap.size() == 0){
                    return  TASKID_IS_NOT_FOUND;
                }

                result   = taskMapper.updateCreator(creator,taskName);
            }else {
                return CREATOR_IS_NOT_RIGHT;
            }

        } catch (Exception e) {
            result = SERVICE_EXCEPTION;
        }
        return result;
    }
}
