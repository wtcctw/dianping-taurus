package com.dp.bigdata.taurus.restlet.resource;

import java.util.ArrayList;

import com.dp.bigdata.taurus.restlet.shared.StatusDTO;
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
