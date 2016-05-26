package com.cip.crane.common;

/**
 * 
 * @author damon.zhu
 *
 */
public class JarID  extends ID{
	
	private String url;

	public JarID(String url){
		this.url = url;
	}
	
	public String getID(){
		return url.hashCode() + "";
	}
	
	public static String getID(String url){
		return url.hashCode() + "";
	}
}
