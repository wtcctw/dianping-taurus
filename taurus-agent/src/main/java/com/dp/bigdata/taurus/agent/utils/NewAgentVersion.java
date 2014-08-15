package com.dp.bigdata.taurus.agent.utils;

import org.restlet.resource.ServerResource;

/**
 * Created by mkirin on 14-8-12.
 */
public class NewAgentVersion extends ServerResource implements INewAgentVersion{
    @Override
    public String retrieve() {
        return "true";
    }
}
