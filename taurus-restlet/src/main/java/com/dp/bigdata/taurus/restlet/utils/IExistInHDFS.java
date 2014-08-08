package com.dp.bigdata.taurus.restlet.utils;

import org.restlet.resource.Get;

/**
 * Created by mkirin on 14-8-8.
 */
public interface IExistInHDFS {
    @Get
    public String isExistInHDFS();
}
