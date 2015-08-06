package com.dp.bigdata.taurus.restlet.resource.impl;

import java.util.ArrayList;

import org.restlet.resource.ServerResource;
import org.springframework.beans.factory.annotation.Autowired;

import com.dp.bigdata.taurus.generated.mapper.TaskMapper;
import com.dp.bigdata.taurus.generated.module.Task;
import com.dp.bigdata.taurus.restlet.resource.IGetTasks;
import com.dp.bigdata.taurus.restlet.shared.TaskDTO;

/**
 * Created by mkirin on 14-8-12.
 */
public class GetTasks extends ServerResource implements IGetTasks {
    @Autowired
    TaskMapper taskMapper;

    @Override
    public ArrayList<TaskDTO> retrieve() {
    	ArrayList<TaskDTO> result = null;
        ArrayList<Task> taskList = taskMapper.getTasks();
        
        if(taskList.size() > 0){
        	result = new ArrayList<TaskDTO>();
        	
        	for(Task task : taskList){
        		result.add(new TaskDTO(task));
        	}
        }
        
        return result;
    }
}
