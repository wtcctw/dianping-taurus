package com.cip.crane.restlet.resource.impl;

import com.cip.crane.generated.mapper.TaskAttemptMapper;
import com.cip.crane.generated.mapper.TaskMapper;
import com.cip.crane.restlet.resource.IUpdateCreator;
import org.restlet.resource.ServerResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

/**
 * Created by kirinli on 14/11/18.
 */
public class UpdateCreator  extends ServerResource implements IUpdateCreator {
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
            String op = (String)getRequestAttributes().get("op");
            if (creator != null && taskName != null && !creator.isEmpty() && !taskName.isEmpty()){
                if ("update".equals(op)){
                    HashMap<String, String> taskIdMap = taskMapper.isExitTaskName(taskName);
                    if (taskIdMap == null || taskIdMap.size() == 0){
                        return  TASKID_IS_NOT_FOUND;
                    }
                    result = taskMapper.updateCreator(creator,taskName);
                }else if ("resign".equals(op)){
                    String[] taskList = taskName.split(",");

                    for (int i = 0; i < taskList.length; i++ ){
                        String taskNameTmp = taskList[i];
                        if (!taskNameTmp.isEmpty()){
                            result = taskMapper.updateCreator(creator,taskNameTmp);
                        }

                    }
                }


            }else {
                return CREATOR_IS_NOT_RIGHT;
            }

        } catch (Exception e) {
            result = SERVICE_EXCEPTION;
        }
        return result;
    }
}
