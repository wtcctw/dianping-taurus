package com.dp.bigdata.taurus.agent.common;

import java.io.File;

import com.dp.bigdata.taurus.agent.utils.AgentEnvValue;
import com.dp.bigdata.taurus.agent.utils.TaskHelper;

/**
 * 设置taurus agent需要的大部分环境变量
 * 
 * @author renyuan.sun
 */
public abstract class BaseEnvManager implements Runnable {
    public final static String clearZombie = "kill -9 `ps -ef | grep taurus-agent | awk '$3 == 1 {print $2}' `";
    public static String agentRoot = "/data/app/taurus-agent";
    public static String jobPath = "/data/app/taurus-agent/jobs";
    public static String logPath = "/data/app/taurus-agent/logs";
    public static String hadoopAuthority = "/script/hadoop-authority.sh";
    public static String logFileUpload = "/script/log-upload.sh";
    public static String killJob = "/script/kill-tree.sh";
    public static String env = "/script/agent-env.sh";
    public static String krb5Conf="/krb5.conf";
    public static String running = "/running";
    public static String hadoop = "/hadoop";
    public static String homeDir = "/home";
    public static boolean needSudoAuthority = true;

    public static final boolean ON_WINDOWS = System.getProperty("os.name").toLowerCase().startsWith("windows");
    public static final String USER = System.getProperty("user.name");
    public static final String AGENT_VERSION = AgentEnvValue.getVersion();
    public static final String CONFIGS = AgentEnvValue.getConfigs();
    public static TaskHelper taskHelper = new TaskHelper();

    public static final String FILE_SEPRATOR = File.separator;
    static{
    	update();
    }
    private static String getRootPath() {
        String result = BaseEnvManager.class.getResource("BaseEnvManager.class").toString();
        int index = result.indexOf("WEB-INF");
        result = result.substring(0, index);
        if (result.startsWith("jar")) {
            result = result.substring(10);
        } else if (result.startsWith("file")) {
            result = result.substring(6);
        }
        if (result.endsWith("/")) result = result.substring(0, result.length() - 1);//不包含最后的"/"
        return "/" + result;

    }
    public static void update(){
    	agentRoot = AgentEnvValue.getValue(AgentEnvValue.AGENT_ROOT_PATH, agentRoot);
        String rootPath =  getRootPath();
        rootPath += "/WEB-INF/classes";

        jobPath = AgentEnvValue.getValue(AgentEnvValue.JOB_PATH, jobPath);
        logPath = AgentEnvValue.getValue(AgentEnvValue.LOG_PATH, logPath);
        hadoopAuthority = rootPath + hadoopAuthority;
        logFileUpload = agentRoot + logFileUpload;
        killJob = rootPath + killJob;
        krb5Conf = rootPath + krb5Conf;
        running = jobPath + running;
        hadoop = jobPath + hadoop;
        env = rootPath + env;
        homeDir = AgentEnvValue.getValue(AgentEnvValue.HOME_PATH, homeDir);
        needSudoAuthority = new Boolean(AgentEnvValue.getValue(AgentEnvValue.NEED_SUDO_AUTHORITY,
        		new Boolean(needSudoAuthority).toString()));
    }
}
