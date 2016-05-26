package com.cip.crane.springmvc.service;

import com.cip.crane.generated.module.Task;
import com.cip.crane.springmvc.utils.TaurusApiException;

import java.util.List;

/**
 * Author   mingdongli
 * 16/4/28  下午1:56.
 */
public interface IScheduleService {

    List<Task> queryJobDetailByIds(String group, String ... taskId) throws TaurusApiException;

    List<Task> queryJobDetailByParam(String group, Task task) throws TaurusApiException;

    boolean isTaskExist(String taskName);

    boolean isCreatorInGroup(String creator, String group);

    List<String> creatorsInGroup(String group);

    String userConvertToId(String userName);

    String groupConvertToId(String groupName);

}
