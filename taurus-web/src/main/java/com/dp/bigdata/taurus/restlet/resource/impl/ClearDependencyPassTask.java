package com.dp.bigdata.taurus.restlet.resource.impl;

import com.dp.bigdata.taurus.core.MultiInstanceFilter;
import com.dp.bigdata.taurus.core.listener.DependPassAttemptListener;
import com.dp.bigdata.taurus.core.listener.DependTimeoutAttemptListener;
import com.dp.bigdata.taurus.core.listener.GenericAttemptListener;
import com.dp.bigdata.taurus.generated.mapper.TaskAttemptMapper;
import com.dp.bigdata.taurus.generated.module.TaskAttempt;
import com.dp.bigdata.taurus.restlet.resource.IClearDependencyPassTask;
import com.dp.bigdata.taurus.zookeeper.execute.helper.ExecuteStatus;
import org.restlet.resource.ServerResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kirinli on 14/10/28.
 */
public class ClearDependencyPassTask extends ServerResource implements IClearDependencyPassTask {

    @Autowired
    private TaskAttemptMapper taskAttemptMapper;

    private List<DependPassAttemptListener> dependPassAttemptListeners = new ArrayList<DependPassAttemptListener>();

    private List<DependTimeoutAttemptListener> dependTimeoutAttemptListeners = new ArrayList<DependTimeoutAttemptListener>();

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
                            for (DependPassAttemptListener listener : dependPassAttemptListeners) {
                                for (TaskAttempt taskAttempt : taskAttemptList) {
                                    listener.removeDependPassAttempt(taskAttempt);
                                }
                            }
                        } else {
                            for (DependTimeoutAttemptListener listener : dependTimeoutAttemptListeners) {
                                for (TaskAttempt taskAttempt : taskAttemptList) {
                                    listener.removeDependTimeoutAttempt(taskAttempt);
                                }
                            }
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

    @Override
    public void registerAttemptListener(GenericAttemptListener genericAttemptListener) {
        if (genericAttemptListener instanceof DependPassAttemptListener) {
            dependPassAttemptListeners.add((DependPassAttemptListener) genericAttemptListener);
        }
        if (genericAttemptListener instanceof DependTimeoutAttemptListener) {
            dependTimeoutAttemptListeners.add((DependTimeoutAttemptListener) genericAttemptListener);
        }
    }
}
