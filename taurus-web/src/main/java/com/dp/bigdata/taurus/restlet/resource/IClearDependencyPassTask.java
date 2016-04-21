package com.dp.bigdata.taurus.restlet.resource;

import com.dp.bigdata.taurus.core.listener.ListenerAdder;
import org.restlet.resource.Get;

/**
 * Created by kirinli on 14/10/28.
 */
public interface IClearDependencyPassTask extends ListenerAdder {
    @Get
    public int retrieve();
}
