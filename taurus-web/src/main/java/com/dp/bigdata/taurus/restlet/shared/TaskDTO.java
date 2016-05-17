package com.dp.bigdata.taurus.restlet.shared;

import com.dp.bigdata.taurus.common.TaskStatus;
import com.dp.bigdata.taurus.generated.module.AlertRule;
import com.dp.bigdata.taurus.generated.module.Task;

import java.io.Serializable;
import java.util.Date;

public class TaskDTO implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 482732054965365244L;

	private String taskid;

	private String name;
	
	private String appName;

	private String creator;

	private String dependencyexpr;

	private Date addtime;

	private Date lastscheduletime;

	private Date updatetime;

	private String crontab;

	private String status;

	private boolean isAutoKill;

	private String proxyuser;

	private Integer waittimeout;

	private Integer executiontimeout;

	private Boolean isautoretry;

	private String filename;

	private Integer retrytimes;

	private String command;

	private Integer poolid;

	private String hostname;

	private String type;

	private String description;

	private int ruleID;

	private boolean hassms;

	private boolean hasmail;

	private boolean hasdaxiang;

	private String userid;

	private String groupid;

	private String conditions;

	private String mainClass;

	private String taskUrl;

	private String hadoopName;
	
	private Boolean iskillcongexp;

	private Boolean isnotconcurrency;

	public TaskDTO(){ }
	
	public TaskDTO(Task task){
		//TODO 不知道跟spring类型的作业有什么需要注意的事项
		this.setAddtime(task.getAddtime());
		this.setAppName(task.getAppname());
		this.setCommand(task.getCommand());
		this.setCreator(task.getCreator());
		this.setCrontab(task.getCrontab());
		this.setDependencyexpr(task.getDependencyexpr());
		this.setDescription(task.getDescription());
		this.setExecutiontimeout(task.getExecutiontimeout());
		this.setFilename(task.getFilename());
		this.setHadoopName(task.getHadoopname());
		this.setHostname(task.getHostname());
		this.setAutoKill(task.getIsautokill());
		this.setIsautoretry(task.getIsautoretry());
		this.setIskillcongexp(task.getIskillcongexp());
		this.setIsconcurrency(task.getIsnotconcurrency());
		this.setLastscheduletime(task.getLastscheduletime());
		this.setName(task.getName());
		this.setPoolid(task.getPoolid());
		this.setProxyuser(task.getProxyuser());
		this.setRetrytimes(task.getRetrytimes());
		this.setStatus(TaskStatus.getTaskRunState(task.getStatus()));
		this.setTaskid(task.getTaskid());
		this.setType(task.getType());
		this.setUpdatetime(task.getUpdatetime());
		this.setWaittimeout(task.getWaittimeout());
	}

	public Date getAddtime() {
		return addtime;
	}

	public AlertRule getAlertRule() {
		AlertRule rule = new AlertRule();
		rule.setConditions(conditions);
		rule.setGroupid(groupid);
		rule.setHasmail(hasmail);
		rule.setHassms(hassms);
		rule.setHasdaxiang(hasdaxiang);
		rule.setId(ruleID);
		rule.setJobid(taskid);
		rule.setUserid(userid);
		return rule;
	}

	public String getCommand() {
		return command;
	}

	public String getConditions() {
		return conditions;
	}

	public String getCreator() {
		return creator;
	}

	public String getCrontab() {
		return crontab;
	}

	public String getDependencyexpr() {
		return dependencyexpr;
	}

	public String getDescription() {
		return description;
	}

	public Integer getExecutiontimeout() {
		return executiontimeout;
	}

	public String getFilename() {
		return filename;
	}

	public String getGroupid() {
		return groupid;
	}

	public String getHadoopName() {
		return hadoopName;
	}

	public String getHostname() {
		return hostname;
	}

	public String getHtmlCommand() {
		return command.replaceAll("\"", "&quot;");
	}

	public Boolean getIsautoretry() {
		return isautoretry;
	}

	public Date getLastscheduletime() {
		return lastscheduletime;
	}

	public String getMainClass() {
		return mainClass;
	}

	public String getName() {
		return name;
	}

	public Integer getPoolid() {
		return poolid;
	}

	public String getProxyuser() {
		return proxyuser;
	}

	public Integer getRetrytimes() {
		return retrytimes;
	}

	public int getRuleID() {
		return ruleID;
	}

	public String getStatus() {
		return status;
	}

	public Task getTask() {
		Task task = new Task();
		task.setAddtime(addtime);
		if (getType() != null && getType().equalsIgnoreCase("spring")) {
			String springCommand;
			if (mainClass != null) {
				springCommand = mainClass + " " + command;
			} else {
				springCommand = command;
			}
			task.setCommand(springCommand);
			task.setFilename(taskUrl);
		} else {
			task.setCommand(command);
			task.setFilename(filename);
		}
		task.setCreator(creator);
		task.setCrontab(crontab);
		task.setDependencyexpr(dependencyexpr);
		task.setDescription(description);
		task.setExecutiontimeout(executiontimeout);
		task.setHostname(hostname);
		task.setIsautoretry(isautoretry);
		task.setIsautokill(isAutoKill);
		task.setName(name);
		task.setLastscheduletime(lastscheduletime);
		task.setPoolid(poolid);
		task.setProxyuser(proxyuser);
		task.setRetrytimes(retrytimes);
		task.setStatus(TaskStatus.getTaskRunState(status));
		task.setTaskid(taskid);
		task.setType(type);
		task.setUpdatetime(updatetime);
		task.setWaittimeout(waittimeout);
		task.setHadoopname(hadoopName);
		task.setAppname(appName);
		task.setIskillcongexp(iskillcongexp);
		task.setIsnotconcurrency(isnotconcurrency);
		return task;
	}

	public String getTaskid() {
		return taskid;
	}

	public String getTaskUrl() {
		return taskUrl;
	}

	public String getType() {
		return type;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public String getUserid() {
		return userid;
	}

	public Integer getWaittimeout() {
		return waittimeout;
	}

	public boolean isAutoKill() {
		return isAutoKill;
	}

	public boolean isHasmail() {
		return hasmail;
	}

	public boolean isHassms() {
		return hassms;
	}

	public boolean isHasdaxiang() {
		return hasdaxiang;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public void setAutoKill(boolean isAutoKill) {
		this.isAutoKill = isAutoKill;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public void setConditions(String conditions) {
		this.conditions = conditions;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public void setCrontab(String crontab) {
		this.crontab = crontab;
	}

	public void setDependencyexpr(String dependencyexpr) {
		this.dependencyexpr = dependencyexpr;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setExecutiontimeout(Integer executiontimeout) {
		this.executiontimeout = executiontimeout;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public void setHadoopName(String hadoopName) {
		this.hadoopName = hadoopName;
	}

	public void setHasmail(boolean hasmail) {
		this.hasmail = hasmail;
	}

	public void setHasdaxiang(boolean hasdaxiang) {
		this.hasdaxiang = hasdaxiang;
	}

	public void setHassms(boolean hassms) {
		this.hassms = hassms;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public void setIsautoretry(Boolean isautoretry) {
		this.isautoretry = isautoretry;
	}

	public void setLastscheduletime(Date lastscheduletime) {
		this.lastscheduletime = lastscheduletime;
	}

	public void setMainClass(String mainClass) {
		this.mainClass = mainClass;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPoolid(Integer poolid) {
		this.poolid = poolid;
	}

	public void setProxyuser(String proxyuser) {
		this.proxyuser = proxyuser;
	}

	public void setRetrytimes(Integer retrytimes) {
		this.retrytimes = retrytimes;
	}

	public void setRuleID(int ruleID) {
		this.ruleID = ruleID;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public void setTaskUrl(String taskUrl) {
		this.taskUrl = taskUrl;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public void setWaittimeout(Integer waittimeout) {
		this.waittimeout = waittimeout;
	}

	public String getAppName() {
	   return appName;
   }

	public void setAppName(String appName) {
	   this.appName = appName;
   }

    public Boolean getIskillcongexp() {
		return iskillcongexp;
	}

	public void setIskillcongexp(Boolean iskillcongexp) {
		this.iskillcongexp = iskillcongexp;
	}

	public Boolean getIsnotconcurrency() {
		return isnotconcurrency;
	}

	public void setIsconcurrency(Boolean isnotconcurrency) {
		this.isnotconcurrency = isnotconcurrency;
	}

	@Override
	public String toString() {
		return "TaskDTO{" +
				"taskid='" + taskid + '\'' +
				", name='" + name + '\'' +
				", appName='" + appName + '\'' +
				", creator='" + creator + '\'' +
				", dependencyexpr='" + dependencyexpr + '\'' +
				", addtime=" + addtime +
				", lastscheduletime=" + lastscheduletime +
				", updatetime=" + updatetime +
				", crontab='" + crontab + '\'' +
				", status='" + status + '\'' +
				", isAutoKill=" + isAutoKill +
				", proxyuser='" + proxyuser + '\'' +
				", waittimeout=" + waittimeout +
				", executiontimeout=" + executiontimeout +
				", isautoretry=" + isautoretry +
				", filename='" + filename + '\'' +
				", retrytimes=" + retrytimes +
				", command='" + command + '\'' +
				", poolid=" + poolid +
				", hostname='" + hostname + '\'' +
				", type='" + type + '\'' +
				", description='" + description + '\'' +
				", ruleID=" + ruleID +
				", hassms=" + hassms +
				", hasmail=" + hasmail +
				", hasdaxiang=" + hasdaxiang +
				", userid='" + userid + '\'' +
				", groupid='" + groupid + '\'' +
				", conditions='" + conditions + '\'' +
				", mainClass='" + mainClass + '\'' +
				", taskUrl='" + taskUrl + '\'' +
				", hadoopName='" + hadoopName + '\'' +
				", iskillcongexp=" + iskillcongexp +
				", isnotconcurrency=" + isnotconcurrency +
				'}';
	}
}
