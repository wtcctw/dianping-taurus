package com.dp.bigdata.taurus.restlet.resource;

import com.dp.bigdata.taurus.generated.module.Task;
import org.restlet.resource.Get;

import java.util.ArrayList;

/**
 * Created by kirinli on 14/12/19.
 */
public interface IGetTaskLastStatus {
    @Get
    public String retrieve();
}
