package com.cip.crane.restlet.resource;

import org.restlet.resource.Get;

/**
 * Created by kirinli on 14/11/23.
 */
public interface IUpdateAlertRule {
    @Get
    public int retrieve();
}
