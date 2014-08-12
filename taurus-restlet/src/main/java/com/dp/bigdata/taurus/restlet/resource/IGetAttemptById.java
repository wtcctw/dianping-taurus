package com.dp.bigdata.taurus.restlet.resource;

import com.dp.bigdata.taurus.restlet.shared.AttemptDTO;
import org.restlet.resource.Get;

/**
 * Created by mkirin on 14-8-12.
 */
public interface IGetAttemptById {
    @Get
    public AttemptDTO retrieve();
}
