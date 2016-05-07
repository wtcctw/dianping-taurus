package com.dp.bigdata.taurus.springmvc.controller.api;

import com.dp.bigdata.taurus.core.AttemptStatus;
import com.dp.bigdata.taurus.core.CronExpression;
import com.dp.bigdata.taurus.core.IDFactory;
import com.dp.bigdata.taurus.core.TaskStatus;
import com.dp.bigdata.taurus.core.parser.DependencyParser;
import com.dp.bigdata.taurus.generated.mapper.UserMapper;
import com.dp.bigdata.taurus.generated.module.Task;
import com.dp.bigdata.taurus.generated.module.User;
import com.dp.bigdata.taurus.generated.module.UserExample;
import com.dp.bigdata.taurus.restlet.exception.DuplicatedNameException;
import com.dp.bigdata.taurus.restlet.exception.InvalidArgumentException;
import com.dp.bigdata.taurus.restlet.resource.impl.NameResource;
import com.dp.bigdata.taurus.restlet.shared.AttemptDTO;
import com.dp.bigdata.taurus.restlet.shared.TaskApiDTO;
import com.dp.bigdata.taurus.restlet.shared.TaskDTO;
import com.dp.bigdata.taurus.restlet.utils.LionConfigUtil;
import com.dp.bigdata.taurus.restlet.utils.TaskRequestExtractor;
import com.dp.bigdata.taurus.springmvc.service.IScheduleService;
import com.dp.bigdata.taurus.springmvc.utils.TaurusApiException;
import com.dp.bigdata.taurus.utils.APIAuthorizationUtils;
import org.apache.commons.lang.StringUtils;
import org.restlet.data.Status;
import org.restlet.resource.ClientResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Author   mingdongli
 * 16/4/25  下午5:32.
 */
@API
@Controller
public class APIController {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private IScheduleService scheduleService;

    @Autowired
    private IDFactory idFactory;

    @Autowired
    private NameResource nameResource;

    @Autowired
    private UserMapper userMapper;

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

    /**
     * 用户首先将BA验证的key作为一个分组，并且加入其中
     *
     * @param taskApiDTO
     * @return
     */
    @RequestMapping(value = "/addJob", method = {RequestMethod.POST})
    @ResponseBody
    public Result addJob(@RequestBody TaskApiDTO taskApiDTO) {

        Result result;
        String taskName = taskApiDTO.getTaskName();
        String creator = taskApiDTO.getCreator();
        String userGroup = (String) request.getAttribute(APIAuthorizationUtils.BA_REQUST_ATTRIBUTE_CLIENTID);
        boolean hasAuth = scheduleService.isCreatorInGroup(creator, userGroup);
        if (!hasAuth) {
            result = Result.getInstance(false, null, ErrorCodeEnum.OPERATION_FAILED_PARAM_AUTHENTICATION.getCode(), ErrorCodeEnum.OPERATION_FAILED_PARAM_AUTHENTICATION.getField());
            return result;
        }

        boolean exist = scheduleService.isTaskExist(taskName);
        if (!exist) {
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
        addJob.put(taskDTO);

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
        String creator = taskApiDTO.getCreator();
        String userGroup = (String) request.getAttribute(APIAuthorizationUtils.BA_REQUST_ATTRIBUTE_CLIENTID);
        boolean hasAuth = scheduleService.isCreatorInGroup(creator, userGroup);
        if (!hasAuth) {
            result = Result.getInstance(false, null, ErrorCodeEnum.OPERATION_FAILED_PARAM_AUTHENTICATION.getCode(), ErrorCodeEnum.OPERATION_FAILED_PARAM_AUTHENTICATION.getField());
            return result;
        }

        boolean exist = scheduleService.isTaskExist(taskName);
        if (!exist) {
            result = Result.getInstance(false, null, ErrorCodeEnum.OPERATION_ADD_FAILED_UNIQUE_CODE_REPEAT.getCode(), ErrorCodeEnum.OPERATION_ADD_FAILED_UNIQUE_CODE_REPEAT.getField());
            return result;
        }

        TaskDTO taskDTO = new TaskDTO();
        fulfillTaskDTO(taskApiDTO, taskDTO);
        try {
            validate(taskDTO, true);
        } catch (Exception e) {
            log.error("modifyJob" + e);
            result = Result.getInstance(false, null, ErrorCodeEnum.OPERATION_FAILED.getCode(), ErrorCodeEnum.OPERATION_FAILED.getField());
            return result;

        }
        ClientResource addJob = new ClientResource("http://localhost:8080/api/" + "task");
        addJob.put(taskDTO);

        if (addJob.getStatus().getCode() == Status.SUCCESS_CREATED.getCode()) {
            result = Result.getInstance(true, null, ErrorCodeEnum.OPERATION_SUCCESS.getCode(), ErrorCodeEnum.OPERATION_SUCCESS.getField());
        } else {
            result = Result.getInstance(false, null, ErrorCodeEnum.OPERATION_FAILED.getCode(), ErrorCodeEnum.OPERATION_FAILED.getField());
        }
        return result;
    }

    /**
     * jobId查询出的task中的creator需要在BA验证的分组中，否则不通过验证
     * @param jobId
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "/startJob", method = RequestMethod.POST)
    public Result startJob(String jobId) {

        Result result;
        if (StringUtils.isBlank(jobId)) {
            result = Result.getInstance(false, null, ErrorCodeEnum.OPERATION_FAILED_PARAM_NULL.getCode(), ErrorCodeEnum.OPERATION_FAILED_PARAM_NULL.getField());
            return result;
        }
        String userGroup = (String) request.getAttribute(APIAuthorizationUtils.BA_REQUST_ATTRIBUTE_CLIENTID);
        try {
            List<Task> list = scheduleService.queryJobDetailByIds(userGroup, jobId);
            if(list == null || list.isEmpty()){
                result = Result.getInstance(true, null, ErrorCodeEnum.OPERATION_FAILED_PARAM_NOTINGROUP.getCode(), ErrorCodeEnum.OPERATION_FAILED_PARAM_NOTINGROUP.getField());
                return result;
            }
            for (Task task : list) {
                if (jobId.equals(task.getTaskid())) {
                    break;
                }
                result = Result.getInstance(true, null, ErrorCodeEnum.OPERATION_FAILED_PARAM_NOTINGROUP.getCode(), ErrorCodeEnum.OPERATION_FAILED_PARAM_NOTINGROUP.getField());
                return result;
            }
        } catch (TaurusApiException e) {
            result = Result.getInstance(false, null, e.getCode(), e.getMessage());
            return result;
        }

        ClientResource manualCr = new ClientResource(LionConfigUtil.RESTLET_API_BASE + "manualtask/" + jobId);
        log.info("APIController startJob begin taskId = " + jobId);
        manualCr.post(null);
        log.info("APIController startJob end taskId = " + jobId);

        if (manualCr.getStatus().getCode() == Status.SUCCESS_OK.getCode()) {
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

        String userGroup = (String) request.getAttribute(APIAuthorizationUtils.BA_REQUST_ATTRIBUTE_CLIENTID);
        try {
            List<Task> list = scheduleService.queryJobDetailByIds(userGroup, jobId);
            if(list == null || list.isEmpty()){
                result = Result.getInstance(true, null, ErrorCodeEnum.OPERATION_FAILED_PARAM_NOTINGROUP.getCode(), ErrorCodeEnum.OPERATION_FAILED_PARAM_NOTINGROUP.getField());
                return result;
            }
            for (Task task : list) {
                if (jobId.equals(task.getTaskid())) {
                    break;
                }
                result = Result.getInstance(true, null, ErrorCodeEnum.OPERATION_FAILED_PARAM_NOTINGROUP.getCode(), ErrorCodeEnum.OPERATION_FAILED_PARAM_NOTINGROUP.getField());
                return result;
            }
        } catch (TaurusApiException e) {
            result = Result.getInstance(false, null, e.getCode(), e.getMessage());
            return result;
        }

        ClientResource manualCr = new ClientResource(LionConfigUtil.RESTLET_API_BASE + "manualtask/" + jobId);
        log.info("APIController stopJob begin taskId = " + jobId);
        manualCr.put(null);
        log.info("APIController stopJob end taskId = " + jobId);

        if (manualCr.getStatus().getCode() == Status.SUCCESS_OK.getCode()) {
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

        String userGroup = (String) request.getAttribute(APIAuthorizationUtils.BA_REQUST_ATTRIBUTE_CLIENTID);
        try {
            List<Task> list = scheduleService.queryJobDetailByIds(userGroup, jobId);
            if(list == null || list.isEmpty()){
                result = Result.getInstance(true, null, ErrorCodeEnum.OPERATION_FAILED_PARAM_NOTINGROUP.getCode(), ErrorCodeEnum.OPERATION_FAILED_PARAM_NOTINGROUP.getField());
                return result;
            }
            for (Task task : list) {
                if (jobId.equals(task.getTaskid())) {
                    break;
                }
                result = Result.getInstance(true, null, ErrorCodeEnum.OPERATION_FAILED_PARAM_NOTINGROUP.getCode(), ErrorCodeEnum.OPERATION_FAILED_PARAM_NOTINGROUP.getField());
                return result;
            }
        } catch (TaurusApiException e) {
            result = Result.getInstance(false, null, e.getCode(), e.getMessage());
            return result;
        }

        ClientResource manualCr = new ClientResource(LionConfigUtil.RESTLET_API_BASE + "manualtask/" + jobId);
        log.info("APIController onceJob begin taskId = " + jobId);
        manualCr.get();
        log.info("APIController onceJob end taskId = " + jobId);

        if (manualCr.getStatus().getCode() == Status.SUCCESS_NO_CONTENT.getCode()) {
            result = Result.getInstance(true, null, ErrorCodeEnum.OPERATION_SUCCESS.getCode(), ErrorCodeEnum.OPERATION_SUCCESS.getField());
        } else {
            result = Result.getInstance(false, null, ErrorCodeEnum.OPERATION_FAILED.getCode(), ErrorCodeEnum.OPERATION_FAILED.getField());
        }

        return result;

    }

    @RequestMapping(value = "/getJobTrace", method = RequestMethod.GET)
    @ResponseBody
    public Result getJobTrace(String jobId) {


        Result result;
        String userGroup = (String) request.getAttribute(APIAuthorizationUtils.BA_REQUST_ATTRIBUTE_CLIENTID);
        try {
            List<Task> list = scheduleService.queryJobDetailByIds(userGroup, jobId);
            if(list == null || list.isEmpty()){
                result = Result.getInstance(true, null, ErrorCodeEnum.OPERATION_FAILED_PARAM_NOTINGROUP.getCode(), ErrorCodeEnum.OPERATION_FAILED_PARAM_NOTINGROUP.getField());
                return result;
            }
            for (Task task : list) {
                if (jobId.equals(task.getTaskid())) {
                    break;
                }
                result = Result.getInstance(true, null, ErrorCodeEnum.OPERATION_FAILED_PARAM_NOTINGROUP.getCode(), ErrorCodeEnum.OPERATION_FAILED_PARAM_NOTINGROUP.getField());
                return result;
            }
        } catch (TaurusApiException e) {
            result = Result.getInstance(false, null, e.getCode(), e.getMessage());
            return result;
        }

        try {

            ClientResource cr;
            String url = LionConfigUtil.RESTLET_API_BASE + "attempt?task_id=" + jobId;
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
            String groupId = scheduleService.groupConvertToId(alertGroup);
            taskDTO.setGroupid(groupId);
        } else {
            taskDTO.setGroupid("");
        }

        String alertUser = taskApiDTO.getAlertUser();

        if (StringUtils.isNotBlank(alertUser)) {
            String userId = scheduleService.userConvertToId(alertUser);
            taskDTO.setUserid(userId.toString());
        } else {
            taskDTO.setUserid("");
        }

        String alertType = taskApiDTO.getAlertType();
        if (StringUtils.isNotBlank(alertType)) {
            if (alertType.equalsIgnoreCase(TaskRequestExtractor.MAIL_ONLY)) {
                taskDTO.setHasmail(true);
            } else if (alertType.equalsIgnoreCase(TaskRequestExtractor.WECHAT_ONLY)) {
                taskDTO.setHassms(true);
            } else if (alertType.equalsIgnoreCase(TaskRequestExtractor.MAIL_AND_WECHAT)) {
                taskDTO.setHasmail(true);
                taskDTO.setHassms(true);
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
        taskDTO.setMainClass(taskApiDTO.getMainClass());
        taskDTO.setTaskUrl(taskApiDTO.getTaskUrl());
        taskDTO.setAutoKill(taskApiDTO.isautokill());
        taskDTO.setIskillcongexp(taskApiDTO.iskillcongexp());
        taskDTO.setIsconcurrency(taskApiDTO.isnotconcurrency());
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
        OPERATION_FAILED_PARAM_SHARDSTRATEGY_SUBTASK_NULL(410, "任务策略为分片任务时，任务分片数必须为正整数"),
        OPERATION_FAILED_PARAM_AUTHENTICATION(411, "用户名不在分组中"),
        OPERATION_FAILED_PARAM_NOTINGROUP(411, "任务不在分组中");

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

}
