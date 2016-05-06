package com.dp.bigdata.taurus.springmvc.service;

import com.dp.bigdata.taurus.generated.module.Task;
import com.dp.bigdata.taurus.springmvc.utils.TaurusApiException;

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
