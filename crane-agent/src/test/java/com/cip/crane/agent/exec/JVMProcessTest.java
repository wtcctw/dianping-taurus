package com.cip.crane.agent.exec;

import org.apache.commons.exec.CommandLine;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by chenchongze on 15/12/9.
 */
public class JVMProcessTest {

    public static void main(String[] args) {
        TaurusExecutor taurusExecutor = new TaurusExecutor();
        String cmd = "echo nobody; echo $$ > /Users/chenchongze/Downloads/testpid; sh /Users/chenchongze/Downloads/test1.sh; echo $? > /Users/chenchongze/Downloads/testrid; rm -f /Users/chenchongze/Downloads/testpid";
        String cmd1 = "echo nobody; echo $$ > /Users/chenchongze/Downloads/testpid; java -jar /Users/chenchongze/Downloads/test1.sh; echo $? > /Users/chenchongze/Downloads/beta-db-transfer-job-dev-1.0.0.jar; echo $? > /Users/chenchongze/Downloads/testrid; rm -f /Users/chenchongze/Downloads/testpid";
        String std = "/Users/chenchongze/Downloads/teststd";
        String err = "/Users/chenchongze/Downloads/testerr";
        FileOutputStream logFileStream = null;
        FileOutputStream errorFileStream = null;
        try {
            logFileStream = new FileOutputStream(std);
            errorFileStream = new FileOutputStream(err);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        CommandLine cmdLine = new CommandLine("bash");
        cmdLine.addArgument("-c");
        cmdLine.addArgument(cmd, false);

        try {
            taurusExecutor.execute("test011", 0, null, cmdLine, logFileStream, errorFileStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
