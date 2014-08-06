package com.dp.bigdata.taurus.agent.utils;

import org.restlet.resource.ServerResource;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mkirin on 14-8-6.
 */
public class GetErrorLogs extends ServerResource implements IGetErrorLogs {
    public static String logPath = "/data/app/taurus-agent/logs";
    public static final String FILE_SEPRATOR = File.separator;
    private final static String HTML_LINE_SPLITTER = "</br>";
    @Override
    public String retrieve() throws IOException {
        String log_path =  (String) getRequestAttributes().get("log_path");
        String[] datas = log_path.split(":");               //客户端发来的路径由date:attemptID:status构成，在这里分离
        String date = datas[0];
        String attemptID = datas[1];
        String logStr= "";                                  //返回的日志结果
        String errorFilePath = logPath + FILE_SEPRATOR + date + FILE_SEPRATOR + attemptID + ".error";
        SimpleDateFormat df = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");

        String tmp;

        final RandomAccessFile errorFile= new RandomAccessFile(errorFilePath,"rw");
        while( (tmp = errorFile.readLine())!= null) {
            String time = df.format(new Date());
            logStr +=  tmp + HTML_LINE_SPLITTER ;
        }

        return logStr;
    }
}
