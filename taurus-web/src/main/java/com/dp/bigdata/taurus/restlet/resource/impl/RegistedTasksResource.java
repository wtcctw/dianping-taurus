package com.dp.bigdata.taurus.restlet.resource.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.restlet.resource.ServerResource;
import org.springframework.beans.factory.annotation.Autowired;

import com.dp.bigdata.taurus.common.Scheduler;
import com.dp.bigdata.taurus.generated.module.Task;
import com.dp.bigdata.taurus.restlet.resource.IRegistedTasksResource;
import com.dp.bigdata.taurus.restlet.shared.TaskDTO;

public class RegistedTasksResource extends ServerResource implements IRegistedTasksResource {

	@Autowired
    private Scheduler scheduler;
	
	@Override
	public Map<String, TaskDTO> retrieve() {
		
		Map<String, TaskDTO> result = null;
		Map<String, Task> registedTasks = scheduler.getAllRegistedTask();
		
		if(registedTasks.size() > 0){
			result = new HashMap<String, TaskDTO>();
			
			for(String key : registedTasks.keySet()){
				result.put(key, new TaskDTO(registedTasks.get(key)));
			}
		}
		
		return Collections.unmodifiableMap(result);
	}

}
