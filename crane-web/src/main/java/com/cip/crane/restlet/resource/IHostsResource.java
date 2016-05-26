package com.cip.crane.restlet.resource;

import java.util.ArrayList;

import com.cip.crane.restlet.shared.HostDTO;
import org.restlet.resource.Get;

/**
 * 
 * IHostsResource
 * @author damon.zhu
 *
 */
public interface IHostsResource {

    @Get
    public ArrayList<HostDTO> retrieve();
    
}
