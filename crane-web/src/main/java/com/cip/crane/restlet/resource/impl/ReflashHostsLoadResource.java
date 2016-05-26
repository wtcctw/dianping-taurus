package com.cip.crane.restlet.resource.impl;

import java.util.ArrayList;
import java.util.Date;

import com.cip.crane.generated.module.Task;
import com.cip.crane.restlet.shared.TaskDTO;
import com.cip.crane.restlet.utils.ReFlashHostLoadTask;
import com.cip.crane.restlet.utils.ReFlashLoad;
import org.restlet.resource.ServerResource;
import org.springframework.beans.factory.annotation.Autowired;

import com.cip.crane.generated.mapper.TaskMapper;
import com.cip.crane.restlet.resource.IReflashHostsLoadResource;

public class ReflashHostsLoadResource extends ServerResource implements IReflashHostsLoadResource {

	@Autowired
    private TaskMapper taskMapper;
	
	@Override
	public String readCachedHostsLoad() {
		return ReFlashHostLoadTask.hostLoadJsonData;
	}

	@Override
	public String reflashCachedHostsLoad() {
		long now = new Date().getTime();
		if (ReFlashHostLoadTask.lastReadDataTime != 0 && (now - ReFlashHostLoadTask.lastReadDataTime)< 60*1000){
			return  ReFlashHostLoadTask.hostLoadJsonData;
		}
		ReFlashHostLoadTask.hostLoadJsonData = ReFlashLoad.reFlashHostLoadData();
		ReFlashHostLoadTask.allTasks = getTasks();
		ReFlashHostLoadTask.lastReadDataTime = new Date().getTime();
		
		return ReFlashHostLoadTask.hostLoadJsonData;
	}

	@Override
	public ArrayList<TaskDTO> reflashCachedTasks() {
		long now = new Date().getTime();
        if (ReFlashHostLoadTask.lastReadDataTime != 0 && (now - ReFlashHostLoadTask.lastReadDataTime)< 60*1000){
            return  ReFlashHostLoadTask.allTasks;
        }
        ReFlashHostLoadTask.allTasks = getTasks();
        ReFlashHostLoadTask.lastReadDataTime = new Date().getTime();

        return ReFlashHostLoadTask.allTasks;
		
	}

	//renew tasks
    private ArrayList<TaskDTO> getTasks(){
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
