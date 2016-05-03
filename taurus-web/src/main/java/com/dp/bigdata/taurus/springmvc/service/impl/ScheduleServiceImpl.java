package com.dp.bigdata.taurus.springmvc.service.impl;

import com.dp.bigdata.taurus.generated.mapper.TaskMapper;
import com.dp.bigdata.taurus.generated.mapper.UserGroupMapper;
import com.dp.bigdata.taurus.generated.mapper.UserGroupMappingMapper;
import com.dp.bigdata.taurus.generated.mapper.UserMapper;
import com.dp.bigdata.taurus.generated.module.*;
import com.dp.bigdata.taurus.springmvc.service.IScheduleService;
import com.dp.bigdata.taurus.springmvc.utils.TaurusApiException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Author   mingdongli
 * 16/4/28  下午2:37.
 */
@Service
public class ScheduleServiceImpl implements IScheduleService {

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserGroupMapper userGroupMapper;

    @Autowired
    private UserGroupMappingMapper userGroupMappingMapper;

    @Override
    public List<Task> queryJobDetailByIds(String group, String... taskId) throws TaurusApiException {

        List<Task> result = new ArrayList<Task>();

        List<String> creators = extractCreators(group);
        List<Task> tasks = taskMapper.selectJobInfoDetailByIds(taskId, creators.toArray(new String[creators.size()]));
        result.addAll(tasks);

        return result;
    }

    @Override
    public List<Task> queryJobDetailByParam(String group, Task task) throws TaurusApiException {

        List<Task> result;

        result = taskMapper.selectJobInfoDetailByParamForApi(task);
        return result;
    }

    private List<String> extractCreators(String group){

        List<String> result = new ArrayList<String>();
        UserGroupExample groupExample = new UserGroupExample();
        groupExample.or().andGroupnameEqualTo(group);
        List<UserGroup> userGroups = userGroupMapper.selectByExample(groupExample);
        if (userGroups != null && !userGroups.isEmpty()) {
            List<Integer> groupIds = new ArrayList<Integer>();
            for (UserGroup ug : userGroups) {
                groupIds.add(ug.getId());
            }
            UserGroupMappingExample example = new UserGroupMappingExample();
            example.or().andGroupidIn(groupIds);
            List<UserGroupMapping> queryMappings = userGroupMappingMapper.selectByExample(example);
            if(queryMappings != null && !queryMappings.isEmpty()){
                List<Integer> userIds = new ArrayList<Integer>();
                for(UserGroupMapping ugm : queryMappings){
                    userIds.add(ugm.getUserid());
                }
                UserExample userExample = new UserExample();
                userExample.or().andIdIn(userIds);
                List<User> users = userMapper.selectByExample(userExample);
                if(users != null && !users.isEmpty()){
                    for(User user : users){
                        result.add(user.getName());
                    }
                }
            }

        }

        return result;
    }

}
