package com.dp.bigdata.taurus.restlet.resource;

import com.dp.bigdata.taurus.restlet.shared.TaskDTO;
import org.restlet.resource.Get;

/**
 * Created by mkirin on 14-8-11.
 */
public interface IGetTaskNameByAttemptId {
    @Get
    public String retrieve();
}
