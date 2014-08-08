package com.dp.bigdata.taurus.restlet.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.restlet.resource.ServerResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

/**
 * Created by mkirin on 14-8-8.
 */
public class IsExistInHDSF extends ServerResource implements IExistInHDFS {
    @Autowired
    private FilePathManager pathManager;
    private final Configuration conf = new Configuration();

    @Override
    public String isExistInHDFS() {
        String attemptID = (String) getRequest().getAttributes().get("attempt_id");
        String logPath = pathManager.getRemoteLog(attemptID);
        String localPath = pathManager.getLocalLogPath(attemptID);
        File file = new File(localPath);


        FileSystem fs;
        FSDataInputStream hdfsInput;
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fs = FileSystem.get(URI.create(logPath), conf);
            hdfsInput = fs.open(new Path(logPath));
            if(hdfsInput == null){
                fos.close();
                fs.close();
                return "false";
            }else{
                hdfsInput.close();
                fos.close();
                fs.close();
                return "true";
            }


        } catch (IOException e) {
            return "false";
        }
    }
}
