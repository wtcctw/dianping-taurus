package com.dp.bigdata.taurus.restlet.resource;

import java.util.ArrayList;

import com.dp.bigdata.taurus.restlet.shared.HostDTO;
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
