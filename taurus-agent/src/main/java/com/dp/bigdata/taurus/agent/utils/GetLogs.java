package com.dp.bigdata.taurus.agent.utils;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.engine.adapter.HttpRequest;
import org.restlet.resource.ServerResource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;

/**
 * Created by mkirin on 14-8-5.
 * 实时显示log
 */
public class GetLogs extends ServerResource implements IGetLogs {
    public static String logPath = "/data/app/taurus-agent/logs";
    public static final String FILE_SEPRATOR = File.separator;

    @Override
    public String retrieve() throws IOException {

        String logStr = "";                                  //返回的日志结果
        String logFilePath;

        String date = (String) getRequestAttributes().get("date");               //日志日期
        String attemptID = (String) getRequestAttributes().get("attemptId");
        String fileOffset = (String) getRequestAttributes().get("file_offset");  //文件偏移量
        String flag = (String) getRequestAttributes().get("flag");               //这个主要是标志当web刷新时，任务还在执行，但是等请求的时候，任务已经结束的时间点
        String queryType = (String) getRequestAttributes().get("query_type");    //区分是log，还是error log

        long lastTimeFileSize = Long.parseLong(fileOffset);

        if (queryType.equals("log")){
            logFilePath = logPath + FILE_SEPRATOR + date + FILE_SEPRATOR + attemptID + ".log";
        }else{
            logFilePath = logPath + FILE_SEPRATOR + date + FILE_SEPRATOR + attemptID + ".error";
        }


        String tmp;
       if (flag.equals("INC")){
            final RandomAccessFile logFile = new RandomAccessFile(logFilePath, "rw");
            logFile.seek(lastTimeFileSize);

            while ((tmp = logFile.readLine()) != null) {
                logStr += tmp +"\n";
            }
        } else {

            final RandomAccessFile logFile = new RandomAccessFile(logFilePath, "rw");
            long fileLength = logFile.length();
            double fileSize = (double)fileLength / 1024 / 1024;
            if (fileSize > 1)
            {
                logFile.seek(fileLength- 1024 * 1024);              //如果文件大于1MB 则只显示文件最后的1MB数据
            }
            while ((tmp = logFile.readLine()) != null) {
                logStr += tmp + "\n";
            }

        }

        return logStr;
    }
}
