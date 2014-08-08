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
        String logFilePath = "";
        String log_path = (String) getRequestAttributes().get("log_path");
        String[] datas = log_path.split(":");               //客户端发来的路径由date:attemptID:status构成，在这里分离
        String date = datas[0];
        String attemptID = datas[1];
        String posSize = datas[2];
        String flag = datas[3];                 //这个主要是标志当web刷新时，任务还在执行，但是等请求的时候，任务已经结束的时间点
        String queryType = datas[4];
        long lastTimeFileSize = Long.parseLong(posSize);
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

            while ((tmp = logFile.readLine()) != null) {
                logStr += tmp + "\n";
            }

        }

        return logStr;
    }
}
