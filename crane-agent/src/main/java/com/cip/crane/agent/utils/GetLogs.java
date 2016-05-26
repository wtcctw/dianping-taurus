package com.cip.crane.agent.utils;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by mkirin on 14-8-5.
 * 实时显示log
 */
public class GetLogs  implements IGetLogs {
    public static String logPath = "/data/app/taurus-agent/logs";
    public static String agentLogPath = "/data/app/taurus-agent/agent-logs";
    public static final String FILE_SEPRATOR = File.separator;

    @Override
    public String getLogs(String date, String attemptID, String fileOffset,String flag , String queryType) throws IOException {

        String logStr = "";                                  //返回的日志结果
        String logFilePath;

        long lastTimeFileSize = Long.parseLong(fileOffset);

        if (queryType.equals("log")) {
            logFilePath = logPath + FILE_SEPRATOR + date + FILE_SEPRATOR + attemptID + ".log";
        } else if(queryType.equals("errorlog")) {
            logFilePath = logPath + FILE_SEPRATOR + date + FILE_SEPRATOR + attemptID + ".error";
        }else {
            logFilePath = agentLogPath + FILE_SEPRATOR +  "all.log";
        }



        String tmp;
        if (flag.equals("INC")) {
            final RandomAccessFile logFile = new RandomAccessFile(logFilePath, "rw");
            logFile.seek(lastTimeFileSize);
            StringBuffer tmpLogStr = new StringBuffer();
            while ((tmp = logFile.readLine()) != null) {
                tmpLogStr.append(tmp);
                tmpLogStr.append("\n");
            }
            logStr = tmpLogStr.toString();
            logFile.close();
        } else {

           /* final RandomAccessFile logFile = new RandomAccessFile(logFilePath, "rw");
            long fileLength = logFile.length();
            double fileSize = fileLength / 1024L / 1024L;
            if (fileSize > 1) {
                logFile.seek(fileLength - 1024 * 1024);              //如果文件大于1MB 则只显示文件最后的1MB数据
            }
            while ((tmp = logFile.readLine()) != null) {
                logStr += tmp + "\n";
            }*/


            final int BUFFER_SIZE = 0x100000;// 缓冲区大小为3M

            File logFile = new File(logFilePath);

            long maxSize =  1024L*1024L;
            long fileLen = logFile.length();
            long startPos =  0;
            if (fileLen > maxSize){
                startPos =  logFile.length() - maxSize;
                fileLen = maxSize;
            }
            MappedByteBuffer inputBuffer = new RandomAccessFile(logFile, "r")
                    .getChannel().map(FileChannel.MapMode.READ_ONLY,
                            startPos , fileLen );

            byte[] dst = new byte[BUFFER_SIZE];// 每次读出3M的内容

            for (int offset = 0; offset < inputBuffer.capacity(); offset += BUFFER_SIZE) {

                if (inputBuffer.capacity() - offset >= BUFFER_SIZE) {

                    for (int i = 0; i < BUFFER_SIZE; i++)

                        dst[i] = inputBuffer.get(offset + i);

                } else {

                    for (int i = 0; i < inputBuffer.capacity() - offset; i++)

                        dst[i] = inputBuffer.get(offset + i);

                }

                int length = (inputBuffer.capacity() % BUFFER_SIZE == 0) ? BUFFER_SIZE
                        : inputBuffer.capacity() % BUFFER_SIZE;

                logStr += new String(dst, 0, length);

            }



        }

        return logStr;
    }
}
