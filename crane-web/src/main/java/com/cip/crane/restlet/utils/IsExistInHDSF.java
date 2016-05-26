package com.cip.crane.restlet.utils;

import org.restlet.resource.ServerResource;

/**
 * Created by mkirin on 14-8-8.
 */
public class IsExistInHDSF extends ServerResource implements IExistInHDFS {

    @Override
    public String isExistInHDFS() {

        boolean isexist =true;

        return isexist ? "true" : "false";
    }
}
