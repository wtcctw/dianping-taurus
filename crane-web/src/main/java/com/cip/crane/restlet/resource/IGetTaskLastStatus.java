package com.cip.crane.restlet.resource;

import org.restlet.resource.Get;

/**
 * Created by kirinli on 14/12/19.
 */
public interface IGetTaskLastStatus {
    @Get
    public String retrieve();
}
