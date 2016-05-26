package com.cip.crane.restlet.shared;

/**
 * Author   mingdongli
 * 16/5/2  下午7:52.
 */
public class TaskApiDTO {

    private String taskName;

    private String taskType;

    private String taskCommand;

    private String crontab;

    private String dependency = "";

    private String proxyUser = "";

    private int maxExecutionTime = 60;

    private int maxWaitTime = 60;

    private int retryTimes = 0;

    private String creator = "";

    private String description = "";

    private String alertCondition = "";

    private String alertType = "4";

    private String alertGroup = "";

    private String alertUser = "";

    private String taskUrl = "";

    private String mainClass = "";

    private String appName = "";

    private boolean iskillcongexp = false;

    private boolean isautokill = true;

    private boolean isnotconcurrency = true;

    private String hostName;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTaskCommand() {
        return taskCommand;
    }

    public void setTaskCommand(String taskCommand) {
        this.taskCommand = taskCommand;
    }

    public String getCrontab() {
        return crontab;
    }

    public void setCrontab(String crontab) {
        this.crontab = crontab;
    }

    public String getDependency() {
        return dependency;
    }

    public void setDependency(String dependency) {
        this.dependency = dependency;
    }

    public String getProxyUser() {
        return proxyUser;
    }

    public void setProxyUser(String proxyUser) {
        this.proxyUser = proxyUser;
    }

    public int getMaxExecutionTime() {
        return maxExecutionTime;
    }

    public void setMaxExecutionTime(int maxExecutionTime) {
        this.maxExecutionTime = maxExecutionTime;
    }

    public int getMaxWaitTime() {
        return maxWaitTime;
    }

    public void setMaxWaitTime(int maxWaitTime) {
        this.maxWaitTime = maxWaitTime;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAlertCondition() {
        return alertCondition;
    }

    public void setAlertCondition(String alertCondition) {
        this.alertCondition = alertCondition;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public String getAlertGroup() {
        return alertGroup;
    }

    public void setAlertGroup(String alertGroup) {
        this.alertGroup = alertGroup;
    }

    public String getAlertUser() {
        return alertUser;
    }

    public void setAlertUser(String alertUser) {
        this.alertUser = alertUser;
    }

    public String getTaskUrl() {
        return taskUrl;
    }

    public void setTaskUrl(String taskUrl) {
        this.taskUrl = taskUrl;
    }

    public String getMainClass() {
        return mainClass;
    }

    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public boolean iskillcongexp() {
        return iskillcongexp;
    }

    public void setIskillcongexp(boolean iskillcongexp) {
        this.iskillcongexp = iskillcongexp;
    }

    public boolean isnotconcurrency() {
        return isnotconcurrency;
    }

    public void setIsnotconcurrency(boolean isnotconcurrency) {
        this.isnotconcurrency = isnotconcurrency;
    }

    public boolean isautokill() {
        return isautokill;
    }

    public void setIsautokill(boolean isautokill) {
        this.isautokill = isautokill;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
}
