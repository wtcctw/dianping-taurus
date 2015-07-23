package com.dp.bigdata.taurus.restlet.resource;

import org.restlet.resource.Get;

/**
 * Created by kirinli on 14/12/8.
 */
public interface IExistTaskRunning {
    @Get
    public String retrieve();
}
