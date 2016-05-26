package com.cip.crane.restlet.resource;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;

/**
 * Created by kirinli on 15/1/30.
 */
public interface IAllHosts {
    @Get
    public String retrieve();
}
