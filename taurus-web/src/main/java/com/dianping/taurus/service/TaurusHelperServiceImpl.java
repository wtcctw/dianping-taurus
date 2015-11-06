package com.dianping.taurus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.pigeon.remoting.provider.config.annotation.Service;
import com.dp.bigdata.taurus.generated.mapper.TaskMapper;
import com.dp.bigdata.taurus.generated.module.Task;
import com.dp.bigdata.taurus.generated.module.TaskExample;
import com.dp.bigdata.taurus.lion.ConfigHolder;
import com.dp.bigdata.taurus.lion.LionKeys;
import com.google.gson.JsonObject;

@Service(url="http://service.dianping.com/arch/taurus/service/TaurusHelperService_1.0.0")
public class TaurusHelperServiceImpl implements TaurusHelperService{

	@Autowired
	TaskMapper taskMapper;
	
	@Override
	public String getTaskInfoByTaskID(String taskID) {
		TaskExample example = new TaskExample();
		example.createCriteria().andTaskidEqualTo(taskID);
		List<Task> tasks = taskMapper.selectByExample(example);
		
		JsonObject jsonObj = new JsonObject();
		
		if(tasks != null && tasks.size() > 0) {
			Task task = tasks.get(0);
			String scheduleUrl = ConfigHolder.get(LionKeys.SERVER_BASE_URL) + "/schedule?name=" + task.getName();
			String historyUrl = ConfigHolder.get(LionKeys.SERVER_BASE_URL) + "/attempt?taskID=" + task.getTaskid();
			jsonObj.addProperty("scheduleUrl", scheduleUrl);
			jsonObj.addProperty("historyUrl", historyUrl);
		}
		
		return jsonObj.toString();
	}

	@Override
	public String getTaskInfoByTaskName(String taskName) {
		TaskExample example = new TaskExample();
		example.createCriteria().andNameEqualTo(taskName);
		List<Task> tasks = taskMapper.selectByExample(example);
		
		JsonObject jsonObj = new JsonObject();
		
		if(tasks != null && tasks.size() > 0) {
			Task task = tasks.get(0);
			String scheduleUrl = ConfigHolder.get(LionKeys.SERVER_BASE_URL) + "/schedule?name=" + task.getName();
			String historyUrl = ConfigHolder.get(LionKeys.SERVER_BASE_URL) + "/attempt?taskID=" + task.getTaskid();
			jsonObj.addProperty("scheduleUrl", scheduleUrl);
			jsonObj.addProperty("historyUrl", historyUrl);
		}
		
		return jsonObj.toString();
	}

	@Override
	public String getTaskInfoByAttemptID(String attemptID) {
		String[] infos = attemptID.split("_");
		String taskID = "task_" + infos[1] + "_" + infos[2];
		
		return getTaskInfoByTaskID(taskID);
	}

}
