package com.cip.crane.restlet.resource;

import org.restlet.resource.Get;

import java.util.List;
import java.util.Map;

/**
 * Created by kirinli on 14-9-30.
 */
public interface ITotalTaskLoad {
    @Get
    public String retrieve();
}
