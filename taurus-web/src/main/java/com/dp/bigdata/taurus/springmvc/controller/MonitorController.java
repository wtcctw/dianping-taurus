package com.dp.bigdata.taurus.springmvc.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.restlet.resource.ClientResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dp.bigdata.taurus.restlet.shared.AttemptDTO;
import com.dp.bigdata.taurus.restlet.shared.TaskDTO;
import com.dp.bigdata.taurus.zookeeper.execute.helper.ExecuteStatus;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Controller
@RequestMapping("/monitor")
public class MonitorController {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	//这里要不要考虑同步的问题？
    private static ArrayList<AttemptDTO> attempts;
    private static ArrayList<TaskDTO> tasks;
    

    @RequestMapping(value = "/jobdetail", method = RequestMethod.POST)
	public void jobdetail(ModelMap modelMap, 
							HttpServletRequest request,
							HttpServletResponse response) throws IOException 
    {
		log.info("--------------init the jobdetail------------");
        
        String start = request.getParameter("start");
        String end = request.getParameter("end");

        ClientResource cr = new ClientResource(InitController.RESTLET_URL_BASE + "jobdetail/" + "/" + start + "/" + end);
        String jsonString = cr.get(String.class);
        
        OutputStream output = response.getOutputStream();
        output.write(jsonString.getBytes());
        output.close();
	}
    
    /**
     * 每次加载任务监控monitor.ftl都刷新作业们的运行历史
     * @param modelMap
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/reflash_attempts", method = RequestMethod.POST)
	public void monitor(ModelMap modelMap, 
						HttpServletRequest request,
						HttpServletResponse response) throws IOException 
    {
		log.info("--------------init the reflash_attempts------------");
    	
        String taskTime = request.getParameter("start");
        String url = InitController.RESTLET_URL_BASE + "getattemptsbystatus/";
        ClientResource cr = new ClientResource(url + taskTime);
        attempts = cr.get(ArrayList.class);

        cr = new ClientResource(InitController.RESTLET_URL_BASE + "reflashHostLoad");
        tasks = cr.put(null, ArrayList.class);
        
        if (tasks == null) {
            ClientResource crTask = new ClientResource(InitController.RESTLET_URL_BASE + "gettasks");
            tasks = crTask.get(ArrayList.class);
        }
        
        OutputStream output = response.getOutputStream();
        output.write("success".getBytes());
        output.close();
	}
	
    @RequestMapping(value = "/runningtasks", method = RequestMethod.GET)
	public String runningtasks(ModelMap modelMap, 
								HttpServletRequest request,
								HttpServletResponse response) 
    {
		log.info("--------------init the runningtasks------------");
    	
        modelMap.addAttribute("attempts", attempts);
        modelMap.addAttribute("tasks", tasks);
        modelMap.addAttribute("mHelper", new MonitorHelper());
        
        return "/monitor/runningtasks.ftl";
	}
    
    
    @RequestMapping(value = "/submitfail", method = RequestMethod.POST)
	public String submitfail(ModelMap modelMap, 
								HttpServletRequest request,
								HttpServletResponse response) 
    {
		log.info("--------------init the submitfail------------");
    	
        modelMap.addAttribute("attempts", attempts);
        modelMap.addAttribute("tasks", tasks);
        modelMap.addAttribute("mHelper", new MonitorHelper());
        
        return "/monitor/submitfail.ftl";
	}
    
    @RequestMapping(value = "/dependencypass", method = RequestMethod.POST)
	public String dependencypass(ModelMap modelMap, 
									HttpServletRequest request,
									HttpServletResponse response) 
    {
		log.info("--------------init the dependencypass------------");
    	
        modelMap.addAttribute("attempts", attempts);
        modelMap.addAttribute("tasks", tasks);
        
        return "/monitor/dependencypass.ftl";
	}
    
    @RequestMapping(value = "/failedtasks", method = RequestMethod.POST)
	public String failedtasks(ModelMap modelMap, 
								HttpServletRequest request,
								HttpServletResponse response) 
    {
		log.info("--------------init the failedtasks------------");
    	
        modelMap.addAttribute("attempts", attempts);
        modelMap.addAttribute("tasks", tasks);
        modelMap.addAttribute("mHelper", new MonitorHelper());
        
        return "/monitor/failedtasks.ftl";
	}
    
    @RequestMapping(value = "/dependencytimeout", method = RequestMethod.POST)
	public String dependencytimeout(ModelMap modelMap, 
									HttpServletRequest request,
									HttpServletResponse response) 
    {
		log.info("--------------init the dependencytimeout------------");
    	
        modelMap.addAttribute("attempts", attempts);
        modelMap.addAttribute("tasks", tasks);
        modelMap.addAttribute("mHelper", new MonitorHelper());
        
        return "/monitor/dependencytimeout.ftl";
	}
    
    @RequestMapping(value = "/timeout", method = RequestMethod.POST)
	public String timeout(ModelMap modelMap, 
							HttpServletRequest request,
							HttpServletResponse response) 
    {
		log.info("--------------init the timeout------------");
    	
        modelMap.addAttribute("attempts", attempts);
        modelMap.addAttribute("tasks", tasks);
        modelMap.addAttribute("mHelper", new MonitorHelper());
        
        return "/monitor/timeout.ftl";
	}
    
    public class MonitorHelper{
    	
    	/**
    	 * runningtasks.ftl辅助方法
    	 * @param ip
    	 * @return
    	 */
    	public boolean isViewLog(String ip){
    		boolean result = false;

            if (InitController.ZABBIX_SWITCH.equals("false")) {
            	result = false;
            } else {
            	result = AttemptProxyController.isHostOverLoad(ip);
            }
            
            return result;
    	}
    	
    	/**
    	 * submitfail.ftl等辅助方法
    	 * @param taskID
    	 * @return
    	 */
    	public String getLastTaskStatus(String taskID){
    		String status_api = InitController.RESTLET_URL_BASE + "getlaststatus";
            String status = null;
            
            try {
            	ClientResource cr = new ClientResource(status_api + "/" + taskID);
                status = cr.get(String.class);
            } catch (Exception e) {
                status = null;
            }

            String lastTaskStatus = null;
            int taskState = -1;
            
            if (status != null) {
                try {
                    JsonParser parser = new JsonParser();
                    JsonElement statusElement = parser.parse(status);
                    JsonObject statusObject = statusElement.getAsJsonObject();
                    JsonElement statusValue = statusObject.get("status");

                    taskState = statusValue.getAsInt();

                    lastTaskStatus = ExecuteStatus.getInstanceRunState(taskState);
                } catch (Exception e) {
                    lastTaskStatus = "NULL";
                }
            }
            
            return lastTaskStatus;
    	}
    }
}
