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

}
