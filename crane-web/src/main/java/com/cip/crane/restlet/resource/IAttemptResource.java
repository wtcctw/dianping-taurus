package com.cip.crane.restlet.resource;

import com.cip.crane.restlet.shared.AttemptDTO;
import org.restlet.resource.Get;

/**
 * Created by mkirin on 14-8-12.
 */
public interface IAttemptResource {
    @Get
    public AttemptDTO retrieve();
}
