package com.dp.bigdata.taurus.restlet.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HostLoadUtil {

	private static Logger log = LoggerFactory.getLogger(HostLoadUtil.class);
	
	public final static int kb = 1024;
	
	private double cpuRatio; 
	
	private long freeMemory; 
	
	public double getCpuRatio() {
		return cpuRatio;
	}

	public long getFreeMemory() {
		return freeMemory;
	}

	public void setCpuRatio(double cpuRatio) {
		this.cpuRatio = cpuRatio;
	}

	public void setFreeMemory(long freeMemory) {
		this.freeMemory = freeMemory;
	}

	public static void main(String[] args){
		HostLoadUtil hlu = new HostLoadUtil();
		hlu.setFreeMemory(Runtime.getRuntime().freeMemory() / kb);
		
		System.out.println(hlu.getFreeMemory());
		
		String str = "16:34:36 up 24 days, 14:33,  1 user,  load average: 1.00, 1.03, 1.02";
		String[] sArr = str.split(":");
		String[] rArr = sArr[sArr.length-1].split(",");
		
		CpuLoad cpuLoad = new CpuLoad();
		cpuLoad.setLast1minLoad(rArr[0].trim());
		cpuLoad.setLast5minLoad(rArr[1].trim());
		cpuLoad.setLast15minLoad(rArr[2].trim());
		
		System.out.println(cpuLoad.getLast1minLoad());
		System.out.println(cpuLoad.getLast5minLoad());
		System.out.println(cpuLoad.getLast15minLoad());
		
		CommandLine cmdLine = new CommandLine("bash");
        cmdLine.addArgument("-c");
        cmdLine.addArgument("uptime", false);
        
//        try {
//        	OutputStream outAndErr = new ByteArrayOutputStream();
//			int result = execcmd(cmdLine, null, outAndErr);
//			System.out.println(outAndErr.toString());
//			outAndErr.flush();
//			outAndErr.close();
//			
//		} catch (ExecuteException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} 
        
	}

	
	public static int execcmd(CommandLine cmdLine, 
								Map<String, String> env, 
								OutputStream stdOutAndErr) 
										throws ExecuteException, IOException{
		DefaultExecutor executor = new DefaultExecutor();
		executor.setWatchdog(new ExecuteWatchdog(-1));
        log.debug("Ready to Execute. Command is "+ cmdLine);
		executor.setExitValues(null);
		PumpStreamHandler streamHandler = new PumpStreamHandler(stdOutAndErr);
		executor.setStreamHandler(streamHandler);
		return executor.execute(cmdLine, env);
	}

}
