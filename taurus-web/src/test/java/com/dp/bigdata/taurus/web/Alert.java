package com.dp.bigdata.taurus.web;

import java.util.concurrent.atomic.AtomicBoolean;

public class Alert implements Runnable {
	
	private static volatile Alert alert;
	
	private static final int INTERVAL = 1 * 1000;
	
	private final AtomicBoolean isInterrupt = new AtomicBoolean(false);
	
	private volatile boolean restflag = false;
	
	private Alert(){}
	
	public static Alert getAlert(){
		
		if(alert == null){
			synchronized(Alert.class){
				if(alert == null){
					alert = new Alert();
				}
			}
		}
		
		return alert;
	}

	@Override
	public void run() {
		
		while (true) {
			
			while(isInterrupt.get()){ restflag = true; }
			restflag = false;
			
			try {
				System.out.println("into alert run");
				Thread.sleep(INTERVAL);
			} catch (Throwable e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	public void isInterrupt(boolean interrupt) {
		boolean current = isInterrupt.get();
		isInterrupt.compareAndSet(current, interrupt);
	}

	public boolean isRestflag() {
		return restflag;
	}

}
