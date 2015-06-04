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
	//@Test
	public void test2(){
		Map<String, String> body = new HashMap<String, String>();
				body.put("typeObject", "Taurus");
				body.put("typeItem", "Service");
				body.put("typeAttribute", "Status");

				body.put("source", "taurus");
				body.put("domain", "10.1.1.59");
				body.put("title", "Taurus-Agent主机失联系告警服务");
				body.put(
						"content",
						"报异常了");
				body.put("url", "http://taurus.dp/hosts.jsp?hostName=10.1.1.59");
				body.put("receiver", "dpop@dianping.com");

				Map<String, String> header = new HashMap<String, String>();

				HttpPoster.postWithoutException("http://192.168.215.148/report/alarm/post", header,
						body);
	}
	//@Test
	public void test1(){
		HttpClient httpclient = new HttpClient();
        PostMethod postMethod = new PostMethod("http://192.168.215.148/report/alarm/post");
        NameValuePair[] data = {new NameValuePair("typeObject", "Taurus"),
     		   new NameValuePair("typeItem","Service"),new NameValuePair("typeAttribute","Status"),
     		   new NameValuePair("source","taurus"),new NameValuePair("domain","IP addr"),
     		   new NameValuePair("title","Taurus-Agent主机失联系告警服务"),
     		   new NameValuePair("content","报异常了")};
        postMethod.setRequestBody(data);
        try {
			int statusCode = httpclient.executeMethod(postMethod);
			System.out.println(statusCode);
			
			} catch (HttpException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}finally{
			   postMethod.releaseConnection();
			}
	}
}
