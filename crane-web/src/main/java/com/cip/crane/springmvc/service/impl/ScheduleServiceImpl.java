package com.cip.crane.springmvc.service.impl;

import com.cip.crane.generated.mapper.TaskMapper;
import com.cip.crane.generated.mapper.UserGroupMapper;
import com.cip.crane.generated.mapper.UserGroupMappingMapper;
import com.cip.crane.generated.mapper.UserMapper;
import com.cip.crane.generated.module.*;
import com.cip.crane.springmvc.service.IScheduleService;
import com.cip.crane.springmvc.utils.TaurusApiException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
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

        List<String> creators = creatorsInGroup(group);
        List<Task> tasks = taskMapper.selectJobInfoDetailByIds(taskId, creators.toArray(new String[creators.size()]));
        result.addAll(tasks);

        return result;
    }

    @Override
    public List<Task> queryJobDetailByParam(String group, Task task) throws TaurusApiException {

        return taskMapper.selectJobInfoDetailByParamForApi(task);
    }

    @Override
    public boolean isTaskExist(String taskName) {

        HashMap<String, String> tasks = taskMapper.isExitTaskName(taskName);
        if (tasks != null && !tasks.isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isCreatorInGroup(String creator, String group) {

        if(StringUtils.isBlank(creator) || StringUtils.isBlank(group)){
            return false;
        }
        List<String> creators = creatorsInGroup(group);
        if(creators == null || creators.isEmpty()){
            return  false;
        }

        return creators.contains(creator);
    }

    @Override
    public List<String> creatorsInGroup(String group){

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

    @Override
    public String userConvertToId(String userName) {

        String[] users = userName.split(";");
        StringBuilder userId = new StringBuilder();

        for (int i = 0; i < users.length; i++) {
            String user = users[i];
            if (user != null & user.length() > 0) {
                UserExample example = new UserExample();

                example.or().andNameEqualTo(user);
                List<User> userList = userMapper.selectByExample(example);

                if (userList != null && userList.size() == 1) {
                    userId.append(userList.get(0).getId());
                    if (i < users.length - 1) {
                        userId.append(";");
                    }
                }
            }
        }
        return userId.toString();

    }

    @Override
    public String groupConvertToId(String groupName) {

        String[] groups = groupName.split(";");
        StringBuilder groupId = new StringBuilder();
        for (int i = 0; i < groups.length; i++) {
            String group = groups[i];

            if (group != null && group.length() > 0) {
                UserGroupExample example = new UserGroupExample();
                example.or().andGroupnameEqualTo(group);

                List<UserGroup> userGroups = userGroupMapper.selectByExample(example);

                if (userGroups != null && userGroups.size() == 1) {
                    groupId.append(userGroups.get(0).getId());

                    if (i < groups.length - 1) {
                        groupId.append(";");
                    }
                }
            }
        }
        return groupId.toString();
    }

}
