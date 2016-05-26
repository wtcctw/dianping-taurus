package com.cip.crane.restlet.resource;

import com.cip.crane.restlet.shared.TaskDTO;

import org.restlet.resource.Get;

import java.util.ArrayList;

/**
 * Created by mkirin on 14-8-12.
 */
public interface IGetTasks {
    @Get
    public ArrayList<TaskDTO> retrieve();
}
