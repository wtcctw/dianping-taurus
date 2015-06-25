package com.dp.bigdata.taurus.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jodd.util.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.json.JSONException;
import org.junit.Test;
import org.restlet.ext.json.JsonRepresentation;

public class CczTest {

	@Test
	public void test1(){
		String jsonStr = "{\"hosts\":[\"192.168.215.117\",\"192.168.222.191\",\"192.168.222.71\"]}";
		
		String jsonNull = "{\"hosts\":\"\"}";
		
		String jsonOne = null;
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<String> ips = new ArrayList<String>();
		ips.add("192.168.215.117");
		result.put("hosts", ips);
		JsonRepresentation rsp = new JsonRepresentation(result);
		try {
			org.json.JSONObject resJson =  rsp.getJsonObject();
			jsonOne = resJson.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JSONObject jsonObj = JSONObject.fromObject(jsonStr);
		Object obj = jsonObj.get("hosts");
		if(StringUtil.isNotBlank(obj.toString())){
			JSONArray jsonArr = jsonObj.getJSONArray("hosts");
			
			Object[] hostLists = jsonArr.toArray();
			
			for(Object host : hostLists){
				System.out.println(host.toString());
			}
		}
		
		
	}
}
