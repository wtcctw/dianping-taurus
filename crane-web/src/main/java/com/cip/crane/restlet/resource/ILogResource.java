package com.cip.crane.restlet.resource;

import org.restlet.representation.FileRepresentation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;

/**
 * IAttemptResource DELETE is to kill a attempt.
 * 
 * @author damon.zhu
 */
public interface ILogResource {


    @Delete
    public void kill();


}
