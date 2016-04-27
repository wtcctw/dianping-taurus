package com.dp.bigdata.taurus.springmvc.controller.api;

import java.util.Date;
import java.util.List;

/**
 * Author   mingdongli
 * 16/4/27  下午3:09.
 */
public class JobSimpleInfoParam extends ReWriteToStringModel{

    private Integer id;//id
    private String jobUniqueCode;//唯一编码
    private String jobCode;//任务码
    private String jobName;//任务名称
    private String jobGroup;//任务分组
    private String jobLine;//业务线
    private String expressionType;//表达式类型
    private String expression;//表达式
    private Integer maxExecuteTime;//最大执行时间
    private String taskNodes;//运行机器列表
    private String jobOwner;//任务所有者
    private Date gmtCreate;//创建时间
    private Date gmtModified;//最后修改时间
    private Integer dbSchedule;//调度信息是否入db
    private Integer subTask;//子任务标识
    private Integer startNewJob;//上次任务未完成不启动新任务
    private String myData;//用户存储的信息，任务调度时发送给用户
    private Integer port;//端口
    private Integer alarmTimeInterval;//持续报警时间间隔
    private Integer scheduleState;//启动状态
    private Integer scheduleChannel;
    private Integer pageNum;//页数
    private Integer pageSize;//每页大小
    // private JobInfoQueryParam jobInfo;//job信息
    private List<JobNotifyAlarmModel> jobNotifyAlarmList;//报警信息
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJobUniqueCode() {
        return jobUniqueCode;
    }

    public void setJobUniqueCode(String jobUniqueCode) {
        this.jobUniqueCode = jobUniqueCode;
    }

    public String getJobCode() {
        return jobCode;
    }

    public void setJobCode(String jobCode) {
        this.jobCode = jobCode;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getJobLine() {
        return jobLine;
    }

    public void setJobLine(String jobLine) {
        this.jobLine = jobLine;
    }

    public String getExpressionType() {
        return expressionType;
    }

    public void setExpressionType(String expressionType) {
        this.expressionType = expressionType;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Integer getMaxExecuteTime() {
        return maxExecuteTime;
    }

    public void setMaxExecuteTime(Integer maxExecuteTime) {
        this.maxExecuteTime = maxExecuteTime;
    }

    public String getTaskNodes() {
        return taskNodes;
    }

    public void setTaskNodes(String taskNodes) {
        this.taskNodes = taskNodes;
    }

    public String getJobOwner() {
        return jobOwner;
    }

    public void setJobOwner(String jobOwner) {
        this.jobOwner = jobOwner;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Integer getDbSchedule() {
        return dbSchedule;
    }

    public void setDbSchedule(Integer dbSchedule) {
        this.dbSchedule = dbSchedule;
    }

    public Integer getSubTask() {
        return subTask;
    }

    public void setSubTask(Integer subTask) {
        this.subTask = subTask;
    }

    public Integer getStartNewJob() {
        return startNewJob;
    }

    public void setStartNewJob(Integer startNewJob) {
        this.startNewJob = startNewJob;
    }

    public String getMyData() {
        return myData;
    }

    public void setMyData(String myData) {
        this.myData = myData;
    }

    public Integer getAlarmTimeInterval() {
        return alarmTimeInterval;
    }

    public void setAlarmTimeInterval(Integer alarmTimeInterval) {
        this.alarmTimeInterval = alarmTimeInterval;
    }

    public Integer getScheduleState() {
        return scheduleState;
    }

    public void setScheduleState(Integer scheduleState) {
        this.scheduleState = scheduleState;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public List<JobNotifyAlarmModel> getJobNotifyAlarmList() {
        return jobNotifyAlarmList;
    }

    public void setJobNotifyAlarmList(List<JobNotifyAlarmModel> jobNotifyAlarmList) {
        this.jobNotifyAlarmList = jobNotifyAlarmList;
    }

    public Integer getScheduleChannel() {
        return scheduleChannel;
    }

    public void setScheduleChannel(Integer scheduleChannel) {
        this.scheduleChannel = scheduleChannel;
    }
}
