package com.dp.bigdata.taurus.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.Test;

import com.dianping.ops.http.HttpPoster;

public class AlarmInfoTest {

	@Test
	public void test3(){
		String dataBaseUrl = "jdbc:mysql://10.1.101.216:3306/Taurus?characterEncoding=utf-8";
		String[] s1 = dataBaseUrl.split(":");
		String[] s2 = s1[2].split("/");
		System.out.println(dataBaseUrl.split(":")[2].split("/")[2]);
		
	}
	
}
