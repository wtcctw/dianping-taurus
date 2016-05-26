package com.cip.crane.restlet.resource.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.cip.crane.restlet.resource.IRegistedTasksResource;
import org.restlet.resource.ServerResource;
import org.springframework.beans.factory.annotation.Autowired;

import com.cip.crane.common.Scheduler;
import com.cip.crane.generated.module.Task;
import com.cip.crane.restlet.shared.TaskDTO;

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
