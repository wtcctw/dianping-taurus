package com.dp.bigdata.taurus.restlet.resource.impl;

import com.dp.bigdata.taurus.core.Engine;
import com.dp.bigdata.taurus.core.MultiInstanceFilter;
import com.dp.bigdata.taurus.core.structure.MaxCapacityList;
import com.dp.bigdata.taurus.generated.mapper.TaskAttemptMapper;
import com.dp.bigdata.taurus.generated.module.TaskAttempt;
import com.dp.bigdata.taurus.restlet.resource.IClearDependencyPassTask;
import com.dp.bigdata.taurus.zookeeper.execute.helper.ExecuteStatus;
import org.restlet.resource.ServerResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by kirinli on 14/10/28.
 */
public class ClearDependencyPassTask extends ServerResource implements IClearDependencyPassTask {

    @Autowired
    private TaskAttemptMapper taskAttemptMapper;

    //不使用listener,否则core需要依赖web
    @Autowired
    private Engine engine;

    private static final int SERVICE_EXCEPTION = -1;
    private static final int TASKID_IS_NOT_FOUND = -2;
    private static final int STATUS_IS_NOT_RIGHT = -3;

    @Override
    public int retrieve() {
        int result = 0;

        try {
            String taskId = (String) getRequestAttributes().get("taskid");
            String status_str = (String) getRequestAttributes().get("status");

            if (status_str != null && !status_str.isEmpty()) {
                int status = Integer.parseInt(status_str);
                HashMap<String, String> taskIdMap = taskAttemptMapper.isExitTaskId(taskId);

                if (taskIdMap == null || taskIdMap.size() == 0) {
                    return TASKID_IS_NOT_FOUND;
                }

                if (status == ExecuteStatus.DEPENDENCY_PASS || status == ExecuteStatus.DEPENDENCY_TIMEOUT) {
                    List<TaskAttempt> taskAttemptList = taskAttemptMapper.selectDependencyTask(taskId, status);
                    if (taskAttemptList != null) {
                        if (status == ExecuteStatus.DEPENDENCY_PASS) {
                            ConcurrentMap<String, MaxCapacityList<TaskAttempt>> map = engine.getDependPassMap();
                            if(map != null){
                                MaxCapacityList<TaskAttempt> tmpTaskAttempt = map.get(taskId);
                                if (tmpTaskAttempt != null) {
                                    tmpTaskAttempt.removeAll(taskAttemptList);
                                }
                                map.put(taskId, tmpTaskAttempt);
                            }
                        } else {
                            engine.getAttemptsOfStatusDependTimeout().removeAll(taskAttemptList);
                        }
                    }
                }
                result = taskAttemptMapper.deleteDependencyPassTask(taskId, status);

                MultiInstanceFilter.jobAlert.remove(taskId.trim());

            } else {
                return STATUS_IS_NOT_RIGHT;
            }

        } catch (Exception e) {
            result = SERVICE_EXCEPTION;
        }

        return result;
    }
}
