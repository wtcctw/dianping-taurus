package com.dp.bigdata.taurus.restlet.resource.impl;

import com.dp.bigdata.taurus.generated.mapper.TaskMapper;
import com.dp.bigdata.taurus.generated.module.Task;
import com.dp.bigdata.taurus.restlet.resource.IGetTasks;
import org.restlet.resource.ServerResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mkirin on 14-8-12.
 */
public class GetTasks extends ServerResource implements IGetTasks{
    @Autowired
    TaskMapper taskMapper;

    @Override
    public ArrayList<Task> retrieve() {
        ArrayList<Task> taskList = taskMapper.getTasks();
        return taskList;
    }
}
