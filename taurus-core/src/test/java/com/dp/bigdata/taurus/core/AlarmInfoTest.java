package com.dp.bigdata.taurus.core;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.Test;

import com.dianping.ops.http.HttpPoster;
import com.dianping.ops.http.HttpResult;

public class AlarmInfoTest {

	@Test
	public void testOpsAlarmHelper(){
		String dataBaseUrl = "jdbc:mysql://10.1.101.216:3306/Taurus?characterEncoding=utf-8";
		OpsAlarmHelper oaHelper = new OpsAlarmHelper();
		
		HttpResult result = oaHelper.buildTypeObject("Taurus")
									.buildTypeItem("Service")
									.buildTypeAttribute("Status")
									.buildSource("taurus")
									.buildDomain(dataBaseUrl.split(":")[2].split("/")[2])
									.buildTitle("本地单元测试告警接口")
									.buildContent("本地单元测试告警接口")
									.buildUrl(dataBaseUrl)
									.buildReceiver("dpop@dianping.com")
									.sendAlarmPost("http://192.168.215.148/report/alarm/post");
		System.out.println(result.isSuccess);
	}
	
	//@Test
	public void test3(){
		String dataBaseUrl = "jdbc:mysql://10.1.101.216:3306/Taurus?characterEncoding=utf-8";
		Map<String, String> header = new HashMap<String, String>();
        Map<String, String> body = new HashMap<String, String>();
        
        body.put("typeObject", "Taurus");
        body.put("typeItem", "Service");
        body.put("typeAttribute", "Status");
        body.put("source", "taurus");
        // 摘出数据库ip给运维报警
        body.put("domain", dataBaseUrl.split(":")[2].split("/")[2]);
        body.put("title", "本地单元测试线上接口");
        body.put("content","本地单元测试线上接口");
        //此处动态修改
        body.put("url", dataBaseUrl);
        body.put("receiver", "dpop@dianping.com");
			
        HttpResult result = HttpPoster.postWithoutException("http://pulse.dp/report/alarm/post", header, body);
        System.out.println(result.isSuccess);
        System.out.println("test");
	}
	
}
