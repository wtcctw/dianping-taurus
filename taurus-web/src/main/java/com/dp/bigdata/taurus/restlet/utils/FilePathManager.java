package com.dp.bigdata.taurus.restlet.utils;

import com.dianping.lion.EnvZooKeeperConfig;
import com.dianping.lion.client.ConfigCache;
import com.dianping.lion.client.LionException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * FileUtils
 *
 * @author damon.zhu
 */
public class FilePathManager {

    private final String CONFIG_FILE = "restlet.properties";
    private final String TMP = "/data/appdata/taurus/task_files";
    private static String LOCALPATH;
    private static String NAMENODE;
    private static String WORKDIR;

    public FilePathManager() {
        Properties props = new Properties();
        try {
            InputStream in = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE);
            props.load(in);
            in.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        String localPathKeyOnLion = "taurus.web.restlet.localpath";
        String hdfsHostKeyOnLion = "taurus.web.restlet.hdfshost";
        String pathKeyOnLion = "taurus.web.restlet.path";

        try {
            LOCALPATH = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty(localPathKeyOnLion);//props.getProperty("localpath", TMP);
            NAMENODE = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty(hdfsHostKeyOnLion);//props.getProperty("host");
            WORKDIR = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty(pathKeyOnLion); //props.getProperty("path");

        } catch (LionException e) {//打lion 异常点
            LOCALPATH = props.getProperty("localpath", TMP);
            NAMENODE = props.getProperty("host");
            WORKDIR = props.getProperty("path");
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("FilePathManager EERROR++++++++:"+e.getMessage());
        }
    }

    public String getRemoteLog(String attemptID) {
        return NAMENODE + WORKDIR + "/" + "logs" + "/" + attemptID + ".html";
    }

    public String getRemoteFolder(String taskID) {
        String destPath = NAMENODE + WORKDIR + "/"
                + taskID;
        return destPath;
    }

    public String getRemotePath(String taskID, String fileName) {
        return getRemoteFolder(taskID) + "/" + fileName;
    }

    public String getLocalPath(String fileName) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = format.format(new Date());
        String fold = LOCALPATH + File.separator + dateStr;
        if (!isExist(fold)) {
            mkdir(fold);
        }
        String file = fold + File.separator + fileName;
        return file;
    }

    public String getLocalLogPath(String attemptID) {
        String fold = LOCALPATH + File.separator + "logs";
        if (!isExist(fold)) {
            mkdir(fold);
        }
        return fold + File.separator + attemptID + ".html";
    }

    public boolean isExist(String fold) {
        File file = new File(fold);
        return file.exists();
    }

    public boolean mkdir(String fold) {
        File dir = new File(fold);
        return dir.mkdirs();
    }

    public boolean rm(String path) {
        File file = new File(path);
        return file.delete();
    }
}
