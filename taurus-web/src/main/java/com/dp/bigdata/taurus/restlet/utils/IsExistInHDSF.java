package com.dp.bigdata.taurus.restlet.utils;

import org.restlet.resource.ServerResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * Created by mkirin on 14-8-8.
 */
public class IsExistInHDSF extends ServerResource implements IExistInHDFS {
    @Autowired
    private FilePathManager pathManager;
    @Autowired
    private HdfsUtils hdfsUtils;

    @Override
    public String isExistInHDFS() {
        String attemptID = (String) getRequest().getAttributes().get("attempt_id");
        String logPath = pathManager.getRemoteLog(attemptID);

        boolean isexist;
        try {
            isexist = hdfsUtils.isExistFile(logPath);
        } catch (IOException e) {
            return "null";
        }

        return isexist ? "true" : "false";
    }
}
