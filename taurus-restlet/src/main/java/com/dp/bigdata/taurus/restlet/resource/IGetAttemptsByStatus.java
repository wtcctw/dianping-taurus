package com.dp.bigdata.taurus.restlet.resource;

import com.dp.bigdata.taurus.restlet.shared.AttemptDTO;
import org.restlet.resource.Get;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mkirin on 14-8-12.
 */
public interface IGetAttemptsByStatus {
    @Get
    public ArrayList<AttemptDTO> retrieve();
}
