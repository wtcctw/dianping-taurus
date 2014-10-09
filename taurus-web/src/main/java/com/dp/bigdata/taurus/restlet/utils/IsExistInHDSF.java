package com.dp.bigdata.taurus.restlet.utils;

import org.restlet.resource.ServerResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

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
