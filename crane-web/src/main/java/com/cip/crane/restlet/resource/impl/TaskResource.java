package com.cip.crane.restlet.resource.impl;

import java.util.List;
import java.util.Map;

import com.cip.crane.restlet.resource.ITaskResource;
import com.cip.crane.restlet.shared.TaskDTO;
import com.cip.crane.restlet.utils.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.restlet.Request;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ServerResource;
import org.springframework.beans.factory.annotation.Autowired;

import com.cip.crane.common.ScheduleException;
import com.cip.crane.common.Scheduler;
import com.cip.crane.common.TaskID;
import com.cip.crane.generated.mapper.AlertRuleMapper;
import com.cip.crane.generated.mapper.UserGroupMapper;
import com.cip.crane.generated.mapper.UserMapper;
import com.cip.crane.generated.module.AlertRule;
import com.cip.crane.generated.module.AlertRuleExample;
import com.cip.crane.generated.module.Task;
import com.cip.crane.generated.module.User;
import com.cip.crane.generated.module.UserExample;
import com.cip.crane.generated.module.UserGroup;
import com.cip.crane.generated.module.UserGroupExample;


/**
 * Resource url : http://xxx.xxx/api/task/{task_id}
 * 
 * @author damon.zhu
 */
public class TaskResource extends ServerResource implements ITaskResource {

    private static final Log LOG = LogFactory.getLog(TaskResource.class);

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private AlertRuleMapper alertRuleMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserGroupMapper userGroupMapper;


    @Autowired
    private AgentDeploymentUtils agentDeployUtils;

    @Autowired
    private RequestExtrator<TaskDTO> requestExtractor;

    @Autowired
    private FilePathManager filePathManager;

    @Override
    public TaskDTO retrieve() {
        String taskID = (String) getRequestAttributes().get("task_id");
        TaskDTO dto = new TaskDTO();
        try {
            TaskID.forName(taskID);
        } catch (IllegalArgumentException e) {
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            LOG.error(e.getMessage());
            return dto;
        }
        Map<String ,Task> map = scheduler.getAllRegistedTask();
        Task task = map.get(taskID);
        if (task == null) {
            setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            LOG.info("Cannot find the task by taskID = " + taskID);
        } else {
            dto = TaskConverter.toDto(task);
            AlertRuleExample example = new AlertRuleExample();
            example.or().andJobidEqualTo(taskID);
            List<AlertRule> rules = alertRuleMapper.selectByExample(example);
            if (rules != null && rules.size() == 1) {
                AlertRule rule = rules.get(0);
                dto.setHasmail(rule.getHasmail());
                dto.setHassms(rule.getHassms());
                dto.setHasdaxiang(rule.getHasdaxiang());
                dto.setConditions(rule.getConditions().toUpperCase());
                String userID = rule.getUserid();
                if (StringUtils.isNotBlank(userID)) {
                    String[] users = userID.split(";");
                    StringBuilder userName = new StringBuilder();
                    for (int i = 0; i < users.length; i++) {
                        String user = users[i];
                        UserExample userExample = new UserExample();
                        userExample.or().andIdEqualTo(Integer.parseInt(user));
                        List<User> userList = userMapper.selectByExample(userExample);
                        if (userList != null && userList.size() == 1) {
                            userName.append(userList.get(0).getName());
                        }
                        if (i < users.length - 1) {
                            userName.append(";");
                        }
                    }
                    dto.setUserid(userName.toString());
                }
                String groudID = rule.getGroupid();
                if (StringUtils.isNotBlank(groudID)) {
                    String[] groups = groudID.split(";");
                    StringBuilder groupName = new StringBuilder();
                    for (int i = 0; i < groups.length; i++) {
                        String group = groups[i];
                        UserGroupExample groupExample = new UserGroupExample();
                        groupExample.or().andIdEqualTo(Integer.parseInt(group));
                        List<UserGroup> userGroups = userGroupMapper.selectByExample(groupExample);
                        if (userGroups != null && userGroups.size() == 1) {
                            groupName.append(userGroups.get(0).getGroupname());
                        }
                        if (i < groups.length - 1) {
                            groupName.append(";");
                        }
                    }
                    dto.setGroupid(groupName.toString());
                }
            }
        }
        return dto;
    }

    @Override
    public void update(Representation re) {
        if (re == null) {
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            return;
        }

        final TaskDTO task;
        Request req = getRequest();
        try {
            task = requestExtractor.extractTask(req, true);
            String taskID = (String) getRequestAttributes().get("task_id");
            task.setTaskid(taskID);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            return;
        }

        if (MediaType.MULTIPART_FORM_DATA.equals(re.getMediaType(), true) && !StringUtils.isBlank(task.getFilename())) {
            final String srcPath = filePathManager.getLocalPath(task.getFilename());
            final String destPath = filePathManager.getRemotePath(task.getTaskid(), task.getFilename());
            try {
                agentDeployUtils.notifyAllAgent(task.getTask(), DeployOptions.UNDEPLOY);
                agentDeployUtils.notifyAllAgent(task.getTask(), DeployOptions.DEPLOY);
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
                setStatus(Status.SERVER_ERROR_INTERNAL);
                return;
            }
        }
        try {
            scheduler.updateTask(task.getTask());
            AlertRuleExample example = new AlertRuleExample();
            example.or().andJobidEqualTo(task.getTaskid());
            AlertRule updatedRule = task.getAlertRule();
            updatedRule.setId(null);
            alertRuleMapper.updateByExampleSelective(updatedRule, example);
            setStatus(Status.SUCCESS_CREATED);
        } catch (ScheduleException e) {
            LOG.error(e.getMessage(), e);
            setStatus(Status.SERVER_ERROR_INTERNAL);
        }
    }

    @Override
    public void remove() {
        String taskID = (String) getRequest().getAttributes().get("task_id");

        try {
            TaskID.forName(taskID);
        } catch (IllegalArgumentException e) {
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            return;
        }

        try {
            Task task = scheduler.getAllRegistedTask().get(taskID);
            if (task == null) {
                setStatus(Status.CLIENT_ERROR_NOT_FOUND);
                return;
            }

            scheduler.unRegisterTask(taskID);
            AlertRuleExample example = new AlertRuleExample();
            example.or().andJobidEqualTo(taskID);
            alertRuleMapper.deleteByExample(example);
        } catch (ScheduleException e) {
            LOG.error(e.getMessage(), e);
            setStatus(Status.SERVER_ERROR_INTERNAL);
        }
    }
}
