package com.dp.bigdata.taurus.common.alert;

import java.util.HashMap;
import java.util.Map;

import com.dianping.ops.http.HttpPoster;
import com.dianping.ops.http.HttpResult;

/**
 * 运维告警帮助类
 * @author chenchongze
 *
 */
public class OpsAlarmHelper {
	
	private Map<String, String> header;
	
	private Map<String, String> body;
	
	private final static int CONFIGNUM = 9;
	
	public OpsAlarmHelper(){
		header = new HashMap<String, String>();
		body = new HashMap<String, String>();
	}
	
	public HttpResult sendAlarmPost(String opsAlarmUrl){
		HttpResult result = null;
		
		try{
			
			if(body.size() == CONFIGNUM){
				result = HttpPoster.postWithoutException(opsAlarmUrl, header, body);
			}else{
				throw new OpsAlarmException("Lack of config, check the number of config!");
			}
			
		} catch(OpsAlarmException oe){
			oe.printStackTrace();
			result = null;
		}
		
		return result;
		
	}
	
	public OpsAlarmHelper buildTypeObject(String typeObject){
		body.put("typeObject", typeObject);
		return this;
	}
	
	public OpsAlarmHelper buildTypeItem(String typeItem){
		body.put("typeItem", typeItem);
		return this;
	}
	
	public OpsAlarmHelper buildTypeAttribute(String typeAttribute){
		body.put("typeAttribute", typeAttribute);
		return this;
	}
	
	public OpsAlarmHelper buildSource(String source){
		body.put("source", source);
		return this;
	}
	
	public OpsAlarmHelper buildDomain(String domain){
		body.put("domain", domain);
		return this;
	}
	
	public OpsAlarmHelper buildTitle(String title){
		body.put("title", title);
		return this;
	}
	
	public OpsAlarmHelper buildContent(String content){
		body.put("content", content);
		return this;
	}
	
	public OpsAlarmHelper buildUrl(String url){
		body.put("url", url);
		return this;
	}
	
	public OpsAlarmHelper buildReceiver(String receiver){
		body.put("receiver", receiver);
		return this;
	}
}
