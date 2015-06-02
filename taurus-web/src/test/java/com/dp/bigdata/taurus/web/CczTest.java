package com.dp.bigdata.taurus.web;

import org.junit.Test;

public class CczTest {

	@Test
	public void test1(){
		String conTextPath =  "/";
		String requestURL = "/";
		String result = requestURL.substring(conTextPath.length());
		
		System.out.println(result);
		System.out.println(result.equals(""));
	}
}
