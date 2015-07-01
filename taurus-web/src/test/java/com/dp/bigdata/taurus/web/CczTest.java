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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.integration.http.converter.SerializingHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.dp.bigdata.taurus.generated.module.Task;

public class CczTest {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());

	//@Test
	public void testRestlet(){
		
		String url = "http://localhost:8192/api/gettasks";
		
        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        SerializingHttpMessageConverter myHttpMessageConverter = new SerializingHttpMessageConverter();
//        List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();
//        supportedMediaTypes.add(new MediaType("application", "x-java-serialized-object"));
//        myHttpMessageConverter.setSupportedMediaTypes(supportedMediaTypes);
        messageConverters.add(myHttpMessageConverter);
        restTemplate.setMessageConverters(messageConverters);
        
        //ResponseEntity<ArrayList> responseEntity = restTemplate.getForEntity(url, ArrayList.class);
        ArrayList<Task> results = restTemplate.getForObject(url, ArrayList.class);
        
        for(Task result: results){
        	log.info("test: "+result.getName());
        }
        
	}
	
	
	//@Test
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
