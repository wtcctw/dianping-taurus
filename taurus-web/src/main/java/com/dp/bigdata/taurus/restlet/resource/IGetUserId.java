package com.dp.bigdata.taurus.restlet.resource;

import com.dp.bigdata.taurus.generated.module.Task;
import org.restlet.resource.Get;

import java.util.ArrayList;

/**
 * Created by kirinli on 14/11/23.
 */
public interface IGetUserId {
    @Get
    public int retrieve();
}
