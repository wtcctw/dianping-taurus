package com.dp.bigdata.taurus.restlet.resource;

import com.dp.bigdata.taurus.generated.module.Task;
import org.restlet.resource.Get;

import java.util.ArrayList;

/**
 * Created by mkirin on 14-8-12.
 */
public interface IGetTasks {
    @Get
    public ArrayList<Task> retrieve();
}
