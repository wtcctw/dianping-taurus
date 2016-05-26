package com.cip.crane.restlet.resource;

import org.restlet.resource.Get;

/**
 * Created by kirinli on 14-10-10.
 */
public interface IUserTasks {
    @Get
    public String retrieve();
}
