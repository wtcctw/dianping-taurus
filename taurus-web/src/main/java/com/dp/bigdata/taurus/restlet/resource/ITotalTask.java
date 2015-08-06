package com.dp.bigdata.taurus.restlet.resource;

import org.restlet.resource.Get;

/**
 * Created by kirinli on 14/12/11.
 */
public interface ITotalTask {
    @Get
    public String retrieve();
}
