package com.dp.bigdata.taurus.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jodd.util.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.json.JSONException;
import org.junit.Test;
import org.restlet.Client;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Uniform;
import org.restlet.data.Form;
import org.restlet.data.Protocol;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.integration.http.converter.SerializingHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.dp.bigdata.taurus.restlet.shared.HostDTO;
import com.dp.bigdata.taurus.restlet.shared.TaskDTO;
import com.dp.bigdata.taurus.restlet.shared.UserDTO;
import com.dp.bigdata.taurus.springmvc.controller.InitController;

public class CczTest {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	private RestTemplate restTemplate = new RestTemplate(
			new ArrayList<HttpMessageConverter<?>>(
					Arrays.asList(
							new SerializingHttpMessageConverter())));
	private String RESTLET_URL_BASE = "http://alpha.taurus.dp:8192/api/";
	
	@Test
	public void testLocal(){
		
		String url = "http://localhost:8192/api/reflashHostLoad";
		ClientResource cr = new ClientResource(url);
		cr.setOnResponse(new Uniform() {
		    @Override
		    public void handle(Request request, Response response) {
		        int statusCode = response.getStatus().getCode();
		        System.out.println(statusCode);
		    }
		});
		String hostLoadJsonData = cr.get(String.class);
		//System.out.println(hostLoadJsonData);
		System.out.println(cr.getStatus().getCode());
//		Form form = new Form();
//		Representation re = form.getWebRepresentation();
//		re.setMediaType(MediaType.APPLICATION_XML);
		hostLoadJsonData = cr.post(null, String.class);
		System.out.println(hostLoadJsonData);
		System.out.println(cr.getStatus().getCode());
		ArrayList<TaskDTO> tasks = cr.put(null, ArrayList.class);
		System.out.println(cr.getStatus().getCode());
		
//		for(TaskDTO task: tasks){
//        	System.out.println(task.getName());
//        }
	}
	
	//@Test
	public void testRest(){
		
		/*ArrayList<UserDTO> users = restTemplate.getForObject(RESTLET_URL_BASE + "user", ArrayList.class);
		
		for(UserDTO user : users){
			System.out.println(user.getName());
		}*/
        
		UserDTO userDto = new UserDTO();
		userDto.setName("test.test");
		userDto.setMail("test@mail.com");
		//restTemplate.postForObject(RESTLET_URL_BASE + "user", userDto, void.class);
        
		
		ClientResource cr = new ClientResource(RESTLET_URL_BASE + "user" + "/chongze.chen");
		//cr.post(userDto);
		//ArrayList<UserDTO> users = cr.get(ArrayList.class);
		Boolean flag = cr.get(Boolean.class);
		System.out.println(flag);
		cr.release();
	}
	
	//@Test
	public void testRestlet(){
		
		String url = "http://beta.taurus.dp:8192/api/host";
		
        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        SerializingHttpMessageConverter myHttpMessageConverter = new SerializingHttpMessageConverter();
//        List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();
//        supportedMediaTypes.add(new MediaType("application", "x-java-serialized-object"));
//        myHttpMessageConverter.setSupportedMediaTypes(supportedMediaTypes);
        messageConverters.add(myHttpMessageConverter);
        restTemplate.setMessageConverters(messageConverters);
        
        //ResponseEntity<ArrayList> responseEntity = restTemplate.getForEntity(url, ArrayList.class);
        ArrayList<HostDTO> hosts = restTemplate.getForObject(url, ArrayList.class);
        
        for(HostDTO host: hosts){
        	System.out.println(host.getIp());
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
