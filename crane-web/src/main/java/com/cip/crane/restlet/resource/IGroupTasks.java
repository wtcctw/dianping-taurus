package com.cip.crane.restlet.resource;

import org.restlet.resource.Get;

/**
 * Created by kirinli on 14-10-11.
 */
public interface IGroupTasks {
    @Get
    public String retrieve();
}
