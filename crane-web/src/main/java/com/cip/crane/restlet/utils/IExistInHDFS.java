package com.cip.crane.restlet.utils;

import org.restlet.resource.Get;

import java.io.IOException;

/**
 * Created by mkirin on 14-8-8.
 */
public interface IExistInHDFS {
    @Get
    public String isExistInHDFS()throws IOException;
}
