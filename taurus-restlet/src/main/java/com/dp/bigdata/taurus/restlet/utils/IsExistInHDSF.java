package com.dp.bigdata.taurus.restlet.utils;

import com.dp.bigdata.taurus.zookeeper.common.utils.ClassLoaderUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.SecurityUtil;
import org.apache.hadoop.security.UserGroupInformation;
import org.restlet.resource.ServerResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;

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
