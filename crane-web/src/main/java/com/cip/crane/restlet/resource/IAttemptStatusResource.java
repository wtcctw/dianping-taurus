package com.cip.crane.restlet.resource;

import java.util.ArrayList;

import com.cip.crane.restlet.shared.StatusDTO;
import org.restlet.resource.Get;

/**
 * IAttemptStatusResource
 * 
 * @author damon.zhu
 */
public interface IAttemptStatusResource {

    @Get
    public ArrayList<StatusDTO> retrieve();
}
