package com.cip.crane.restlet.utils;

public class SystemLoad {
	
	private String last1minLoad;
	
	private String last5minLoad;
	
	private String last15minLoad;

	public String getLast1minLoad() {
		return last1minLoad;
	}

	public String getLast5minLoad() {
		return last5minLoad;
	}

	public String getLast15minLoad() {
		return last15minLoad;
	}

	public void setLast1minLoad(String last1minLoad) {
		this.last1minLoad = last1minLoad;
	}

	public void setLast5minLoad(String last5minLoad) {
		this.last5minLoad = last5minLoad;
	}

	public void setLast15minLoad(String last15minLoad) {
		this.last15minLoad = last15minLoad;
	}

}