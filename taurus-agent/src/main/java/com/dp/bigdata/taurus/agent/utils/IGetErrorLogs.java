package com.dp.bigdata.taurus.agent.utils;

import org.restlet.resource.Get;

import java.io.IOException;

/**
 * Created by mkirin on 14-8-6.
 */
public interface IGetErrorLogs {
    @Get
    public String retrieve() throws IOException;
}
