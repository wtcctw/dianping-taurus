package com.dp.bigdata.taurus.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import jodd.util.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.json.JSONException;
import org.junit.Test;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Uniform;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.ClientResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dp.bigdata.taurus.restlet.shared.TaskDTO;
import com.dp.bigdata.taurus.restlet.shared.UserDTO;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.integration.http.converter.SerializingHttpMessageConverter;

public class CczTest {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	/*private RestTemplate restTemplate = new RestTemplate(
			new ArrayList<HttpMessageConverter<?>>(
					Arrays.asList(
							new SerializingHttpMessageConverter())));*/
	private String RESTLET_URL_BASE = "http://alpha.taurus.dp:8192/api/";
	
	private final AtomicBoolean isInterrupt = new AtomicBoolean(true);
	
	String mailUrl = "http://web.paas.dp/mail/send";
	
	@Test
	public void testSplitNull(){
		String userInfoStr = "";
		boolean a = userInfoStr.equalsIgnoreCase(null);
		log.info(a+"");
		try {
			String dpaccount = userInfoStr.split("\\|")[0];
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("here?");
	}
	
	//@Test
	public void testEmail() {
		FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
    	formDataMultiPart.field("title", "ccz邮件测试");
    	formDataMultiPart.field("recipients", "chongze.chen@dianping.com,mingdong.li@dianping.com");
    	formDataMultiPart.field("body", "邮件正文");
    	
		try {
			String result = RestCallUtils.postRestCall(mailUrl, formDataMultiPart, String.class, 5000, 5000);
			log.info(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//@Test
	public void testboo(){
		boolean a = false;
		boolean b = false;
		while(!(a && b)){
			//wait the engine to finish last schedule
		}
		quit();
	}
	
	//@Test
	public void testRun() throws InterruptedException{
		Thread thread = new Thread(Alert.getAlert());
		thread.setName("alertThread");
		thread.start();
		
		Thread.sleep(2 * 1000);
		Alert.getAlert().isInterrupt(true);
		Thread.sleep(2 * 1000);
		Alert.getAlert().isInterrupt(false);
		Thread.sleep(2 * 1000);
		Alert.getAlert().isInterrupt(true);
		Thread.sleep(2 * 1000);
		Alert.getAlert().isInterrupt(false);
		
		while(!Alert.getAlert().isRestflag()){}
		
		quit();
	}
	
	
	
	//@Test
	public void testLatch() throws InterruptedException{
		CountDownLatch latch=new CountDownLatch(1);
		reload(latch);
    	latch.await();
    	
    	System.out.println("testLatch");
	}
	
	private void reload(CountDownLatch latch){
		TaskDTO taskDTO = null;
		int i = 3;
		
		while (i > 0) {
			try {
				//taskDTO.getCreator();
			} catch (Exception e) {
				e.printStackTrace();
				--i;
				continue;
			}
			break;
		}
		
		if(i == 0){
			System.out.println("i: " + i);
			return ;
		}
		
		latch.countDown();
	}
	
	//@Test
	public void testAtomic(){
		boolean interrupt = false;
		boolean current = isInterrupt.get();
		isInterrupt.compareAndSet(current, interrupt);
		System.out.println(isInterrupt.get());
	}
	
	//@Test
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
		
//		String url = "http://beta.taurus.dp:8192/api/host";
//		
//        RestTemplate restTemplate = new RestTemplate();
//        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
//        SerializingHttpMessageConverter myHttpMessageConverter = new SerializingHttpMessageConverter();
//        messageConverters.add(myHttpMessageConverter);
//        restTemplate.setMessageConverters(messageConverters);
//        
//        ArrayList<HostDTO> hosts = restTemplate.getForObject(url, ArrayList.class);
//        
//        for(HostDTO host: hosts){
//        	System.out.println(host.getIp());
//        }
        
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
	
	
	private void quit(){
		System.out.println("Print 'quit' and hit ENTER to quit app!");
		Scanner sc = new Scanner(System.in);
		sc.useDelimiter("\n");
		
		while (sc.hasNext()) {
			
			if("quit".equals(sc.next())){
				break;
			}
			
		}
	}
}
