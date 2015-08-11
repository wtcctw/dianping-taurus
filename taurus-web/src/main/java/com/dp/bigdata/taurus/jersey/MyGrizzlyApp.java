package com.dp.bigdata.taurus.jersey;

import java.net.URI;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.mvc.freemarker.FreemarkerMvcFeature;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

public class MyGrizzlyApp {
	private static MyGrizzlyApp s_instance = new MyGrizzlyApp();
	
	private static volatile boolean s_init = false;
	
	private static final URI BASE_URI = URI.create("http://localhost:8191/api/");
	
	private static final String RS_PACK = "com.dp.bigdata.taurus.jersey.rs";
	
	private static HttpServer server;
	
	private MyGrizzlyApp(){
		
	};
	
	public static void init(){
		if (!s_init) {
			synchronized (s_instance) {
				if (!s_init) {
					server = GrizzlyHttpServerFactory.createHttpServer(BASE_URI, createApp());
					log("INFO", "Grizzly Server is initialized!");
					s_init = true;
				}
			}
		}
	}
	
	public static void init(ResourceConfig resourceConfig){
		if (!s_init) {
			synchronized (s_instance) {
				if (!s_init) {
					server = GrizzlyHttpServerFactory.createHttpServer(BASE_URI, resourceConfig);
					log("INFO", "Grizzly Server is initialized!");
					s_init = true;
				}
			}
		}
	}
	
	public static void stop() {
		if (s_init) {
			synchronized (s_instance) {
				if (s_init) {
					server.shutdownNow();
					log("INFO", "Grizzly Server is closed!");
					s_init = false;
				}
			}
		}
	}
	
	public static ResourceConfig createApp() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("contextConfigLocation", "classpath:grizzlyContext.xml");
		params.put(FreemarkerMvcFeature.TEMPLATES_BASE_PATH, "/freemarker");
		
        return new ResourceConfig()
        		.setProperties(params)
		        .register(RequestContextFilter.class)
		        .register(LoggingFilter.class)
		        .register(FreemarkerMvcFeature.class)
                .packages(RS_PACK);
    }
	
	static void log(String severity, String message) {
		MessageFormat format = new MessageFormat("[{0,date,MM-dd HH:mm:ss.sss}] [{1}] [{2}] {3}");

		System.out.println(format.format(new Object[] { new Date(), severity, "grizzly", message }));
	}
	
	public static void main(String[] args){
		init();
		System.out.println("Print 'quit' and hit ENTER to quit app!");
		Scanner sc = new Scanner(System.in);
		sc.useDelimiter("\n");
		
		while (sc.hasNext()) {
			
			if("quit".equals(sc.next())){
				stop();
				break;
			}
			
		}
	}
}
