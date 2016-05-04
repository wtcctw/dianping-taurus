package com.dp.bigdata.taurus.springmvc.controller.api;

import com.dp.bigdata.taurus.core.*;
import com.dp.bigdata.taurus.core.parser.DependencyParser;
import com.dp.bigdata.taurus.generated.mapper.AlertRuleMapper;
import com.dp.bigdata.taurus.generated.mapper.TaskMapper;
import com.dp.bigdata.taurus.generated.mapper.UserGroupMapper;
import com.dp.bigdata.taurus.generated.mapper.UserMapper;
import com.dp.bigdata.taurus.generated.module.*;
import com.dp.bigdata.taurus.restlet.exception.DuplicatedNameException;
import com.dp.bigdata.taurus.restlet.exception.InvalidArgumentException;
import com.dp.bigdata.taurus.restlet.resource.impl.NameResource;
import com.dp.bigdata.taurus.restlet.shared.AttemptDTO;
import com.dp.bigdata.taurus.restlet.shared.TaskApiDTO;
import com.dp.bigdata.taurus.restlet.shared.TaskDTO;
import com.dp.bigdata.taurus.restlet.utils.LionConfigUtil;
import com.dp.bigdata.taurus.restlet.utils.PoolManager;
import com.dp.bigdata.taurus.springmvc.service.IScheduleService;
import com.dp.bigdata.taurus.springmvc.utils.TaurusApiException;
import com.dp.bigdata.taurus.utils.APIAuthorizationUtils;
import org.apache.commons.lang.StringUtils;
import org.restlet.data.Status;
import org.restlet.resource.ClientResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.*;


/**
 * Author   mingdongli
 * 16/4/25  下午5:32.
 */
@API
@Controller
public class APIController {

    private Logger log = LoggerFactory.getLogger(getClass());

    private static final String MAIL_ONLY = "1";

    private static final String WECHAT_ONLY = "2";

    private static final String DAXIANG_ONLY = "3";

    private static final String ALL = "4";

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private IScheduleService scheduleService;

    @Autowired
    private IDFactory idFactory;

    @Autowired
    private PoolManager poolManager;

    @Autowired
    private NameResource nameResource;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private UserGroupMapper userGroupMapper;

    @Autowired
    private AlertRuleMapper alertRuleMapper;

    @Autowired
    private Scheduler scheduler;

    @RequestMapping(value = "/jobList", method = {RequestMethod.GET})
    @ResponseBody
    public Result getJobList(String jobIds) {
        Result result;
        String userGroup = (String) request.getAttribute(APIAuthorizationUtils.BA_REQUST_ATTRIBUTE_CLIENTID);
        //校验参数是否为空
        log.info("APIController  jobList begin  jobIds:" + jobIds + " ,clientId:" + userGroup);
        if (StringUtils.isBlank(jobIds)) {
            result = Result.getInstance(false, null, ErrorCodeEnum.OPERATION_FAILED_PARAM_NULL.getCode(), ErrorCodeEnum.OPERATION_FAILED_PARAM_NULL.getField());
            return result;
        }

        try {
            List<Task> list = scheduleService.queryJobDetailByIds(userGroup, jobIds.split(","));
            result = Result.getInstance(true, list, ErrorCodeEnum.OPERATION_SUCCESS.getCode(), ErrorCodeEnum.OPERATION_SUCCESS.getField());

        } catch (TaurusApiException e) {
            log.error("getJobList jobDetailAPIList ", e);
            result = Result.getInstance(false, null, e.getCode(), e.getMessage());

        } catch (Exception e) {
            log.error("getJobList queryParam error ", e);
            result = Result.getInstance(false, null, ErrorCodeEnum.OPERATION_FAILED_PARAM_TYPE_ERROR.getCode(), ErrorCodeEnum.OPERATION_FAILED_PARAM_TYPE_ERROR.getField());
            return result;
        }
        log.info("ScheduleAPIController  jobList end  jobIds:" + jobIds + " ,clientId:" + jobIds);
        return result;

    }

    @RequestMapping(value = "/quaryJobList", method = {RequestMethod.GET})
    @ResponseBody
    public Result quaryJobList(Task task) {

        Result result;
        String userGroup = (String) request.getAttribute(APIAuthorizationUtils.BA_REQUST_ATTRIBUTE_CLIENTID);
        try {
            List<Task> list = scheduleService.queryJobDetailByParam(userGroup, task);
            result = Result.getInstance(true, list, ErrorCodeEnum.OPERATION_SUCCESS.getCode(), ErrorCodeEnum.OPERATION_SUCCESS.getField());
        } catch (TaurusApiException e) {
            log.error("quaryJobList jobDetailAPIList ", e);
            result = Result.getInstance(false, null, e.getCode(), e.getMessage());
            return result;
        }
        return result;
    }

    @RequestMapping(value = "/addJob", method = {RequestMethod.POST})
    @ResponseBody
    public Result addJob(@RequestBody TaskApiDTO taskApiDTO) {

        Result result;
        String taskName = taskApiDTO.getTaskName();
        HashMap<String, String> tasks = taskMapper.isExitTaskName(taskName);
        if (tasks != null && !tasks.isEmpty()) {
            result = Result.getInstance(false, null, ErrorCodeEnum.OPERATION_ADD_FAILED_UNIQUE_CODE_REPEAT.getCode(), ErrorCodeEnum.OPERATION_ADD_FAILED_UNIQUE_CODE_REPEAT.getField());
            return result;
        }

        TaskDTO taskDTO = initTaskDTO();
        fulfillTaskDTO(taskApiDTO, taskDTO);
        try {
            validate(taskDTO, false);
        } catch (Exception e) {
            log.error("addJob" + e);
            result = Result.getInstance(false, null, ErrorCodeEnum.OPERATION_FAILED.getCode(), ErrorCodeEnum.OPERATION_FAILED.getField());
            return result;

        }

        ClientResource addJob = new ClientResource(LionConfigUtil.RESTLET_API_BASE + "task");
        addJob.put(new TaskDTOWrapper(taskDTO, false));

        if (addJob.getStatus().getCode() == Status.SUCCESS_CREATED.getCode()) {
            result = Result.getInstance(true, null, ErrorCodeEnum.OPERATION_SUCCESS.getCode(), ErrorCodeEnum.OPERATION_SUCCESS.getField());
        } else {
            result = Result.getInstance(false, null, ErrorCodeEnum.OPERATION_FAILED.getCode(), ErrorCodeEnum.OPERATION_FAILED.getField());
        }

        return result;

    }

    @RequestMapping(value = "/modifyJob", method = {RequestMethod.POST})
    @ResponseBody
    public Result modifyJob(@RequestBody TaskApiDTO taskApiDTO) {

        Result result;
        String taskName = taskApiDTO.getTaskName();
        HashMap<String, String> tasks = taskMapper.isExitTaskName(taskName);
        if (tasks == null || tasks.isEmpty()) {
            result = Result.getInstance(false, null, ErrorCodeEnum.OPERATION_ADD_FAILED_NO_UNIQUE_CODE.getCode(), ErrorCodeEnum.OPERATION_ADD_FAILED_NO_UNIQUE_CODE.getField());
            return result;
        }


        TaskDTO taskDTO = new TaskDTO();
        fulfillTaskDTO(taskApiDTO, taskDTO);
        try {
            TaskExample taskExample = new TaskExample();
            taskExample.createCriteria().andNameEqualTo(taskName);
            List<Task> taskList = taskMapper.selectByExample(taskExample);
            Task task = taskList.get(0);
            taskDTO.setTaskid(task.getTaskid());
            validate(taskDTO, true);
        } catch (Exception e) {
            log.error("modifyJob" + e);
            result = Result.getInstance(false, null, ErrorCodeEnum.OPERATION_FAILED.getCode(), ErrorCodeEnum.OPERATION_FAILED.getField());
            return result;

        }
        ClientResource addJob = new ClientResource(LionConfigUtil.RESTLET_API_BASE + "task");
        addJob.put(new TaskDTOWrapper(taskDTO, false));

        if (addJob.getStatus().getCode() == Status.SUCCESS_CREATED.getCode()) {
            result = Result.getInstance(true, null, ErrorCodeEnum.OPERATION_SUCCESS.getCode(), ErrorCodeEnum.OPERATION_SUCCESS.getField());
        } else {
            result = Result.getInstance(false, null, ErrorCodeEnum.OPERATION_FAILED.getCode(), ErrorCodeEnum.OPERATION_FAILED.getField());
        }
        return result;
    }


    @ResponseBody
    @RequestMapping(value = "/startJob", method = RequestMethod.POST)
    public Result startJob(String jobId) {

        Result result;
        if (StringUtils.isBlank(jobId)) {
            result = Result.getInstance(false, null, ErrorCodeEnum.OPERATION_FAILED_PARAM_NULL.getCode(), ErrorCodeEnum.OPERATION_FAILED_PARAM_NULL.getField());
            return result;
        }
        ClientResource manualCr = new ClientResource(LionConfigUtil.RESTLET_API_BASE + "manualtask/" + jobId);
        log.info("APIController startJob begin taskId = " + jobId);
        manualCr.post(null);
        log.info("APIController startJob end taskId = " + jobId);

        if (manualCr.getStatus().getCode() == Status.SUCCESS_CREATED.getCode()) {
            result = Result.getInstance(true, null, ErrorCodeEnum.OPERATION_SUCCESS.getCode(), ErrorCodeEnum.OPERATION_SUCCESS.getField());
        } else {
            result = Result.getInstance(false, null, ErrorCodeEnum.OPERATION_FAILED.getCode(), ErrorCodeEnum.OPERATION_FAILED.getField());
        }

        return result;

    }

    @RequestMapping(value = "/stopJob", method = RequestMethod.POST)
    @ResponseBody
    public Result stopJob(String jobId) {

        Result result;
        if (StringUtils.isBlank(jobId)) {
            result = Result.getInstance(false, null, ErrorCodeEnum.OPERATION_FAILED_PARAM_NULL.getCode(), ErrorCodeEnum.OPERATION_FAILED_PARAM_NULL.getField());
            return result;
        }
        ClientResource manualCr = new ClientResource(LionConfigUtil.RESTLET_API_BASE + "manualtask/" + jobId);
        log.info("APIController stopJob begin taskId = " + jobId);
        manualCr.put(null);
        log.info("APIController stopJob end taskId = " + jobId);

        if (manualCr.getStatus().getCode() == Status.SUCCESS_CREATED.getCode()) {
            result = Result.getInstance(true, null, ErrorCodeEnum.OPERATION_SUCCESS.getCode(), ErrorCodeEnum.OPERATION_SUCCESS.getField());
        } else {
            result = Result.getInstance(false, null, ErrorCodeEnum.OPERATION_FAILED.getCode(), ErrorCodeEnum.OPERATION_FAILED.getField());
        }

        return result;
    }

    @RequestMapping(value = "/onceJob", method = RequestMethod.POST)
    @ResponseBody
    public Result onceJob(String jobId) {

        Result result;
        if (StringUtils.isBlank(jobId)) {
            result = Result.getInstance(false, null, ErrorCodeEnum.OPERATION_FAILED_PARAM_NULL.getCode(), ErrorCodeEnum.OPERATION_FAILED_PARAM_NULL.getField());
            return result;
        }
        ClientResource manualCr = new ClientResource(LionConfigUtil.RESTLET_API_BASE + "manualtask/" + jobId);
        log.info("APIController onceJob begin taskId = " + jobId);
        manualCr.get();
        log.info("APIController onceJob end taskId = " + jobId);

        if (manualCr.getStatus().getCode() == Status.SUCCESS_CREATED.getCode()) {
            result = Result.getInstance(true, null, ErrorCodeEnum.OPERATION_SUCCESS.getCode(), ErrorCodeEnum.OPERATION_SUCCESS.getField());
        } else {
            result = Result.getInstance(false, null, ErrorCodeEnum.OPERATION_FAILED.getCode(), ErrorCodeEnum.OPERATION_FAILED.getField());
        }

        return result;

    }

    @RequestMapping(value = "/getJobTrace", method = RequestMethod.GET)
    @ResponseBody
    public Result getJobTrace(String jobCodes) {


        Result result;
        try {

            ClientResource cr;
            String url = LionConfigUtil.RESTLET_API_BASE + "attempt?task_id=" + jobCodes;
            cr = new ClientResource(url);
            cr.setRequestEntityBuffering(true);
            ArrayList<AttemptDTO> attempts = cr.get(ArrayList.class);
            result = Result.getInstance(true, attempts, ErrorCodeEnum.OPERATION_SUCCESS.getCode(), ErrorCodeEnum.OPERATION_SUCCESS.getField());
        } catch (Exception e) {
            log.error("getJobTrace  error", e);
            result = Result.getInstance(true, null, ErrorCodeEnum.OPERATION_FAILED.getCode(), ErrorCodeEnum.OPERATION_FAILED.getField());
        }

        return result;
    }

    private TaskDTO initTaskDTO() {

        TaskDTO taskDTO = new TaskDTO();
        Date current = new Date();
        taskDTO.setAddtime(current);
        taskDTO.setLastscheduletime(current);
        String id = idFactory.newTaskID();
        taskDTO.setTaskid(id);
        taskDTO.setStatus(TaskStatus.getTaskRunState(TaskStatus.RUNNING));

        return taskDTO;
    }

    private void fulfillTaskDTO(TaskApiDTO taskApiDTO, TaskDTO taskDTO) {

        taskDTO.setUpdatetime(new Date());
        String condition = taskApiDTO.getAlertCondition();
        if (StringUtils.isBlank(condition)) {
            taskDTO.setConditions(AttemptStatus.getInstanceRunState(AttemptStatus.FAILED));
        }

        String alertGroup = taskApiDTO.getAlertGroup();
        if (StringUtils.isNotBlank(alertGroup)) {
            String[] groups = alertGroup.split(";");
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
            taskDTO.setGroupid(groupId.toString());
        } else {
            taskDTO.setGroupid("");
        }

        String alertUser = taskApiDTO.getAlertUser();
        if (StringUtils.isNotBlank(alertUser)) {
            String[] users = alertUser.split(";");
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

            taskDTO.setUserid(userId.toString());
        } else {
            taskDTO.setUserid("");
        }

        String alertType = taskApiDTO.getAlertType();
        if (StringUtils.isNotBlank(alertType)) {
            if (alertType.equalsIgnoreCase(MAIL_ONLY)) {
                taskDTO.setHasmail(true);
            } else if (alertType.equalsIgnoreCase(WECHAT_ONLY)) {
                taskDTO.setHassms(true);
            } else if (alertType.equalsIgnoreCase(DAXIANG_ONLY)) {
                taskDTO.setHasdaxiang(true);
            } else if (alertType.equalsIgnoreCase(ALL)) {
                taskDTO.setHasmail(true);
                taskDTO.setHassms(true);
                taskDTO.setHasdaxiang(true);
            } else {
                taskDTO.setHasmail(true);
            }
        }

        taskDTO.setAppName(taskApiDTO.getAppName());
        taskDTO.setName(taskApiDTO.getTaskName());
        taskDTO.setType(taskApiDTO.getTaskType());
        taskDTO.setPoolid(1); //以后可让用户设置
        taskDTO.setCommand(taskApiDTO.getTaskCommand());
        taskDTO.setCrontab(taskApiDTO.getCrontab());
        taskDTO.setDependencyexpr(taskApiDTO.getDependency());
        taskDTO.setProxyuser(taskApiDTO.getProxyUser());
        taskDTO.setCreator(taskApiDTO.getCreator());
        taskDTO.setExecutiontimeout(taskApiDTO.getMaxExecutionTime());
        taskDTO.setWaittimeout(taskApiDTO.getMaxWaitTime());
        taskDTO.setDescription(taskApiDTO.getDescription());
        taskDTO.setAutoKill(taskApiDTO.iskillcongexp());
        taskDTO.setMainClass(taskApiDTO.getMainClass());
        taskDTO.setTaskUrl(taskApiDTO.getTaskUrl());
        taskDTO.setIskillcongexp(taskApiDTO.iskillcongexp());
        taskDTO.setHostname(taskApiDTO.getHostName());
        int retryTimes = taskApiDTO.getRetryTimes();
        taskDTO.setRetrytimes(retryTimes);
        if (retryTimes > 0) {
            taskDTO.setIsautoretry(true);
        } else {
            taskDTO.setIsautoretry(false);
        }
    }

    private void validate(TaskDTO task, boolean isUpdateAction) throws Exception {
        if (StringUtils.isBlank(task.getCreator())) {
            throw new InvalidArgumentException("Cannot get creator name from request");
        }

        if (StringUtils.isBlank(task.getUserid())) {
            String name = task.getCreator();
            UserExample example = new UserExample();
            example.or().andNameEqualTo(name);
            List<User> user = userMapper.selectByExample(example);
            if (user == null || user.size() != 1) {
                throw new InvalidArgumentException("Cannot get mail user from request");
            } else {
                User u = user.get(0);
                task.setUserid(u.getId().toString());
            }
        }

        if (StringUtils.isBlank(task.getProxyuser())) {
            throw new InvalidArgumentException("Cannot get proxy user from request");
        }

        if (StringUtils.isNotBlank(task.getDependencyexpr())) {
            if (!DependencyParser.isValidateExpression(task.getDependencyexpr())) {
                throw new InvalidArgumentException("Invalid dependency expression : " + task.getDependencyexpr());
            }
        }

        if (StringUtils.isBlank(task.getName())) {
            throw new InvalidArgumentException("Cannot get task name from request");
        }

        if (!isUpdateAction && nameResource.isExistTaskName(task.getName())) {
            throw new DuplicatedNameException("Duplicated Name : " + task.getName());
        }

        try {
            new CronExpression(task.getCrontab());
        } catch (Exception e) {
            throw e;
        }
    }

    private static class Result<T> {

        private boolean ret = true;

        private Integer errCode;

        private String errMsg;

        private T data;

        public static <T> Result getSuccessInstance(T data) {
            return getInstance(true, data, null, null);
        }

        public static Result getFailedInstance(Integer errCode, String errMsg) {
            return getInstance(false, null, errCode, errMsg);
        }

        public static <T> Result getInstance(boolean ret, T data, Integer errCode, String errMsg) {
            Result<T> result = new Result<T>();
            result.setRet(ret);
            result.setData(data);
            result.setErrCode(errCode);
            result.setErrMsg(errMsg);
            return result;
        }

        public boolean isRet() {
            return ret;
        }

        public void setRet(boolean ret) {
            this.ret = ret;
        }

        public Integer getErrCode() {
            return errCode;
        }

        public void setErrCode(Integer errCode) {
            this.errCode = errCode;
        }

        public String getErrMsg() {
            return errMsg;
        }

        public void setErrMsg(String errMsg) {
            this.errMsg = errMsg;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }
    }

    public static class BAInfoModel implements Serializable {

        private String clientId;

        private String jobLine;

        private String jobGroup;

        public String getClientId() {
            return clientId;
        }

        public String getJobLine() {
            return jobLine;
        }

        public String getJobGroup() {
            return jobGroup;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public void setJobLine(String jobLine) {
            this.jobLine = jobLine;
        }

        public void setJobGroup(String jobGroup) {
            this.jobGroup = jobGroup;
        }

    }

    protected enum ErrorCodeEnum {

        //define error code
        OPERATION_SUCCESS(0, "操作成功"),
        OPERATION_FAILED(1, "操作失败"),

        OPERATION_ADD_FAILED(101, "添加失败"),
        OPERATION_ADD_FAILED_UNIQUE_CODE_REPEAT(102, "job唯一码已存在"),
        OPERATION_ADD_FAILED_CRON_NOT_VALID(103, "cron表达式配置错误"),
        OPERATION_ADD_FAILED_PORT_NOT_VALID(104, "通信端口必须在8410-8430,默认8383"),
        OPERATION_ADD_FAILED_NO_UNIQUE_CODE(105, "job唯一码不存在"),

        OPERATION_MODIFY_FAILED(201, "修改失败"),
        OPERATION_MODIFY_FAILED_UNIQUE_CODE_REPEAT(202, "job唯一码已存在"),
        OPERATION_MODIFY_FAILED_CRON_NOT_VALID(203, "cron表达式配置错误"),
        OPERATION_MODIFY_FAILED_JOB_IS_START(204, "您本次修改了job的关键信息，请先停掉再编辑"),

        OPERATION_DELETE_FAILED(301, "删除失败"),
        OPERATION_DELETE_FAILED_JOB_IS_START(302, "删除失败,处于启动状态的job不允许删除"),
        OPERATION_DELETE_FAILED_NOT_JOB_OWNER(303, "删除失败，您不是任务所有者不能删除该任务"),

        OPERATION_FAILED_PARAM_NULL(401, "必填参数为空"),
        OPERATION_FAILED_PARAM_EXPRESSIONTYPE(402, "expressiontype必须为 cron "),
        OPERATION_FAILED_PARAM_BAERROR(403, "jobLine和jobGroup的组合信息与clientId不符"),
        OPERATION_FAILED_PARAM_TYPE_ERROR(404, "参数类型错误"),
        OPERATION_FAILED_PARAM_JOB_NOFOUND(405, "未找到相应的jobInfo信息"),
        OPERATION_FAILED_PARAM_MIS_NOFOUND(406, "未找到相应的mis信息"),
        OPERATION_FAILED_PARAM_PORT_ERROR(407, "taskAcceptorPort must in 8410~8430,include 8410 and " +
                "8430,default 8383"),
        OPERATION_FAILED_PARAM_IP_ERROR(408, "指定机器IP地址有误"),
        OPERATION_FAILED_PARAM_IP_TASKNODE_NULL(409, "指定IP模式，任务节点需要配置"),
        OPERATION_FAILED_PARAM_SHARDSTRATEGY_SUBTASK_NULL(410, "任务策略为分片任务时，任务分片数必须为正整数");

        private Integer code;

        private String field;

        ErrorCodeEnum(Integer code, String field) {
            this.code = code;
            this.field = field;
        }

        public Integer getCode() {
            return code;
        }

        public String getField() {
            return field;
        }
    }

    private static class JobInfoOutModel {

        private String id;//id

        private String jobUniqueCode;//唯一编码

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getJobUniqueCode() {
            return jobUniqueCode;
        }

        public void setJobUniqueCode(String jobUniqueCode) {
            this.jobUniqueCode = jobUniqueCode;
        }
    }

    public static class TaskDTOWrapper{

        private TaskDTO taskDTO;

        private boolean update;

        public TaskDTOWrapper(TaskDTO taskDTO, boolean update) {
            this.taskDTO = taskDTO;
            this.update = update;
        }

        public TaskDTO getTaskDTO() {
            return taskDTO;
        }

        public boolean isUpdate() {
            return update;
        }
    }

}
