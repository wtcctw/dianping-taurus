package com.dp.bigdata.taurus.agent.utils;

import org.restlet.resource.ServerResource;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mkirin on 14-8-5.
 * 实时显示log
 */
public class GetLogs extends ServerResource implements IGetLogs{
    public static String logPath = "/data/app/taurus-agent/logs";
    public static final String FILE_SEPRATOR = File.separator;
    private final static String LOG_HEAD = "<html lang=\"en\"><head>"
                    +"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" /> "
                    +"</head><body data-spy=\"scroll\">"+"<title>Taurus作业调度平台</title>"
                    +"<style> .run-tag {font-size: 130%} .error-tag {font-size: 130%}</style>"
                    +"<script src=\"js/jquery.min.js\"></script>"
                    +"<script src=\"js/bootstrap.min.js\"></script>"
                    +"<script src=\"js/login.js\"></script>"
                    +"<link href=\"css/bootstrap.min.css\" rel=\"stylesheet\" type=\"text/css\">"
                    +"<link href=\"css/bootstrap-responsive.min.css\" rel=\"stylesheet\" type=\"text/css\">"
                    +"<link href=\"css/taurus.css\" rel=\"stylesheet\" type=\"text/css\">"
                    +"<link rel=\"Shortcut Icon\" href=\"img/icon.png\" type=\"image/x-icon\">"
                    +"<link href=\"css/index.css\" rel=\"stylesheet\" type=\"text/css\">"
                    +"<link rel=\"stylesheet\" type=\"text/css\" href=\"css/DT_bootstrap.css\">"
                    +"<div class=\"navbar navbar-inverse navbar-fixed-top\">"
                    +"<div class=\"navbar-inner\">"
                    +"<div class=\"container\">"
                    +"<button type=\"button\" class=\"btn btn-navbar\" data-toggle=\"collapse\" data-target=\".nav-collapse\">"
                    +"<span class=\"icon-bar\"></span> <span class=\"icon-bar\"></span> <span class=\"icon-bar\"></span>"
                    +"</button>"
                    +"<a class=\"brand\" href=\"./index.jsp\">Taurus</a>"
                    +"</div> </div></div>"
                    +"<ul class=\"breadcrumb\">"
                    +"<li><a href=\"./index.jsp\">首页</a> <span class=\"divider\">/</span></li>"
                    +"<li><a href=\"./schedule.jsp\">调度中心</a> <span class=\"divider\">/</span></li>"
                    +"<li><a href=\"#\" class=\"active\">查看日志</a> <span class=\"divider\">/</span></li></ul>"
                    +"<div class=\"container\">";

    private final static String LOG_STDERR = " <ul class=\"error-tag\">\n" +
            "<li><a>错误信息<span class=\"label label-important\">STDERR</span></a></li>\n"+
            "</ul> <div class=\"hero-unit\">";

    private final static String LOG_STDOUT = "</div> <ul class=\"run-tag\">\n" +
            "<li><a>日志信息<span class=\"label label-info\">STDOUT</span></a></li>\n"+
            "</ul> <div class=\"hero-unit\">";

    private final static String LOG_END = "</div></body></html>";

    private final static String HTML_LINE_SPLITTER = "</br>";
/*
    @Override
    public String retrieve() throws IOException {

        String log_path =  (String) getRequestAttributes().get("log_path");
        String[] datas = log_path.split(":");               //客户端发来的路径由date:attemptID:status构成，在这里分离
        String date = datas[0];
        String attemptID = datas[1];
        String status = datas[2];
        String logStr= "";                                  //返回的日志结果
        String logFilePath = logPath + FILE_SEPRATOR + date + FILE_SEPRATOR + attemptID + ".log";
        String errorFilePath = logPath + FILE_SEPRATOR + date + FILE_SEPRATOR + attemptID + ".error";
        String reflashHtml = "<script language=\"JavaScript\"> "     //这段js实现每十秒刷新一下请求
                +"function myrefresh(){window.location.reload();} "
                +"setTimeout('myrefresh()',10000);</script> ";

        logStr += LOG_HEAD;
        if (status.equals("RUNNING")){                               //只有RUNNING的任务才会刷新
            logStr += reflashHtml;
        }

        logStr += LOG_STDERR;

        String tmp;
        String logTmp="";
        String nullStr="";                                          //判断是否是空文件

        final RandomAccessFile errorFile= new RandomAccessFile(errorFilePath,"rw");

        while( (tmp = errorFile.readLine())!= null) {
            logTmp += tmp +"\n<br>" + HTML_LINE_SPLITTER;
            nullStr += tmp;
        }

        if (nullStr.isEmpty()){
            logStr += "找不到错误日志" + HTML_LINE_SPLITTER;
        }else{
            logStr += logTmp;
        }

        logStr += LOG_STDOUT ;
        logTmp="";
        nullStr="";                                                 //判断是否是空文件

        final RandomAccessFile logFile  = new RandomAccessFile(logFilePath,"rw");

        while( (tmp = logFile.readLine())!= null) {
            logTmp += tmp + HTML_LINE_SPLITTER;
            nullStr += tmp;
        }

        if (nullStr.isEmpty()){
            logStr += "找不到日志" + HTML_LINE_SPLITTER;
        }else{
            logStr += logTmp;
        }

        logStr += LOG_END;

        return logStr;
    }
    */

    @Override
    public String retrieve() throws IOException {
        String log_path =  (String) getRequestAttributes().get("log_path");
        String[] datas = log_path.split(":");               //客户端发来的路径由date:attemptID:status构成，在这里分离
        String date = datas[0];
        String attemptID = datas[1];
        String logStr= "";                                  //返回的日志结果
        String logFilePath = logPath + FILE_SEPRATOR + date + FILE_SEPRATOR + attemptID + ".log";
        //SimpleDateFormat df = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");

        String tmp;

        final RandomAccessFile logFile  = new RandomAccessFile(logFilePath,"rw");
        while( (tmp = logFile.readLine())!= null) {
            //String time = df.format(new Date());
            logStr +=  tmp + HTML_LINE_SPLITTER;
        }

        return logStr;
    }

    public String getErrorLog() throws IOException {
        String log_path =  (String) getRequestAttributes().get("log_path");
        String[] datas = log_path.split(":");               //客户端发来的路径由date:attemptID:status构成，在这里分离
        String date = datas[0];
        String attemptID = datas[1];
        String logStr= "";                                  //返回的日志结果
        String errorFilePath = logPath + FILE_SEPRATOR + date + FILE_SEPRATOR + attemptID + ".error";

        String tmp;
        String logTmp="";
        String nullStr="";                                          //判断是否是空文件

        final RandomAccessFile errorFile= new RandomAccessFile(errorFilePath,"rw");

        while( (tmp = errorFile.readLine())!= null) {
            logStr += tmp + HTML_LINE_SPLITTER;
            nullStr += tmp;
        }

        if (nullStr.isEmpty()){
            logStr += "找不到错误日志\n";
        }else{
            logStr += logTmp;
        }

        return logStr;
    }
}
