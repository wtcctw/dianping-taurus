package com.dp.bigdata.taurus.springmvc.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jodd.util.StringUtil;

import org.codehaus.plexus.util.StringUtils;
import org.restlet.data.MediaType;
import org.restlet.resource.ClientResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.ServletContextAware;

import com.dianping.lion.EnvZooKeeperConfig;
import com.dianping.lion.client.ConfigCache;
import com.dianping.lion.client.LionException;
import com.dp.bigdata.taurus.restlet.resource.IAttemptStatusResource;
import com.dp.bigdata.taurus.restlet.resource.IGetAttemptsByStatus;
import com.dp.bigdata.taurus.restlet.resource.IGetTasks;
import com.dp.bigdata.taurus.restlet.resource.IHostResource;
import com.dp.bigdata.taurus.restlet.resource.IHostsResource;
import com.dp.bigdata.taurus.restlet.resource.IPoolsResource;
import com.dp.bigdata.taurus.restlet.resource.ITaskResource;
import com.dp.bigdata.taurus.restlet.resource.ITasksResource;
import com.dp.bigdata.taurus.restlet.resource.IUserGroupsResource;
import com.dp.bigdata.taurus.restlet.resource.IUsersResource;
import com.dp.bigdata.taurus.restlet.shared.AttemptDTO;
import com.dp.bigdata.taurus.restlet.shared.HostDTO;
import com.dp.bigdata.taurus.restlet.shared.PoolDTO;
import com.dp.bigdata.taurus.restlet.shared.StatusDTO;
import com.dp.bigdata.taurus.restlet.shared.TaskDTO;
import com.dp.bigdata.taurus.restlet.shared.UserDTO;
import com.dp.bigdata.taurus.restlet.shared.UserGroupDTO;
import com.dp.bigdata.taurus.generated.module.Task;

@Controller
public class HomeController implements ServletContextAware{

	private Logger log = LoggerFactory.getLogger(this.getClass());
	private ServletContext servletContext;
    public void setServletContext(ServletContext sc) {  
        this.servletContext=sc;  
    }
    
	/**
	 * 重构signin.jsp
	 * @param modelMap
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/signin", method = RequestMethod.GET)
	public String signin(ModelMap modelMap, HttpServletRequest request,
			HttpServletResponse response) {
		log.info("--------------init the signin------------");
		String url = (String) request.getParameter("redirect-url");
	    if (StringUtils.isBlank(url)) {
	        url = "";
	    }
	    modelMap.addAttribute("url", url);
		return "/signin.ftl";
	}
	
	/**
	 * 重构index.jsp
	 * @param modelMap
	 * @param request
	 * @param response
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap modelMap, HttpServletRequest request,
			HttpServletResponse response) throws ParseException {
		log.info("--------------init the index------------");
		
		GlobalViewVariable gvv = new GlobalViewVariable();
		
		commonnav(request,gvv);
		modelMap.addAttribute("currentUser", gvv.currentUser);
	    modelMap.addAttribute("isAdmin",gvv.isAdmin);
		
		Date time = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH");
        long hourTime = 60 * 60 * 1000;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        String stepStr = request.getParameter("step");
        String now = request.getParameter("date");
        String opStr = request.getParameter("op");
        if(StringUtil.isBlank(opStr)){
        	opStr="day";
        }
        System.out.println(stepStr + "#" + now);
        int step = -24;
        
        if (StringUtil.isBlank(now)) {
            now = df.format(time);
        }
        
        long nowLtime = df.parse(now).getTime();
        long dHour = 24 * hourTime;
        long wHour = 7 * dHour;
        long mHour = 30 * dHour;
        String nowFormat = formatter.format(new Date(nowLtime));
        
        String bf1mD = df.format(new Date(nowLtime -mHour));
        String bf1mDtip = formatter.format(new Date(nowLtime -mHour))+"~"+nowFormat;
        String bf1wD = df.format(new Date(nowLtime -wHour));
        String bf1wDtip = formatter.format(new Date(nowLtime -wHour))+"~"+nowFormat;
        String bf1dD = df.format(new Date(nowLtime -dHour));
        String bf1dDtip = formatter.format(new Date(nowLtime -dHour))+"~"+nowFormat;
        //当天
        String todayD = df.format(time);
        String todayDtip = formatter.format(new Date(time.getTime() -dHour))+
        		"~"+formatter.format(time);
        //未来1天，1周，1月
        //+1d，+1w，+1m的jsp脚本判断bug修复，现有时间粒度上加1小时进行判断,即futureNow
        //实际上变量名now和time交换一下逻辑上比较清晰,另外，request.getParameter("step")似乎没什么用。
        Calendar cal = Calendar.getInstance();
        cal.setTime(df.parse(now));
        cal.add(Calendar.HOUR, 1);
        String futureNow = df.format(cal.getTime());
        String af1dD = "";
        String af1wD = "";
        String af1mD = "";
        if(df.parse(futureNow).after(time)){
        	af1dD=af1wD=af1mD=todayD;
        }else{
        	af1dD = df.format(new Date(nowLtime + dHour));
        	af1wD = df.format(new Date(nowLtime + wHour));
        	af1mD = df.format(new Date(nowLtime + mHour));
        }
        String af1dDtip = nowFormat+"~"+formatter.format(new Date(nowLtime + dHour));
        String af1wDtip = nowFormat+"~"+formatter.format(new Date(nowLtime + wHour));
        String af1mDtip = nowFormat+"~"+formatter.format(new Date(nowLtime + mHour));
        
        modelMap.addAttribute("bf1mD", bf1mD);
        modelMap.addAttribute("bf1mDtip", bf1mDtip);
        modelMap.addAttribute("bf1wD", bf1wD);
        modelMap.addAttribute("bf1wDtip", bf1wDtip);
        modelMap.addAttribute("bf1dD", bf1dD);
        modelMap.addAttribute("bf1dDtip", bf1dDtip);
        
        modelMap.addAttribute("todayD", todayD);
        modelMap.addAttribute("todayDtip", todayDtip);
        
        modelMap.addAttribute("af1dD", af1dD);
        modelMap.addAttribute("af1dDtip", af1dDtip);
        modelMap.addAttribute("af1wD", af1wD);
        modelMap.addAttribute("af1wDtip", af1wDtip);
        modelMap.addAttribute("af1mD", af1mD);
        modelMap.addAttribute("af1mDtip", af1mDtip);
        
        String now_s = formatter.format( df.parse(now));
        modelMap.addAttribute("now_s", now_s);
        modelMap.addAttribute("step", stepStr);
        modelMap.addAttribute("op_str", opStr);
        
		return "/index.ftl";
	}
	/**
	 * 重构task_center.jsp
	 * @param modelMap
	 * @param request
	 * @param response
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/task_center", method = RequestMethod.GET)
	public String task_center(ModelMap modelMap, HttpServletRequest request,
			HttpServletResponse response) throws ParseException {
		log.info("--------------init the task_center------------");
		
		GlobalViewVariable gvv = new GlobalViewVariable();
		commonnav(request,gvv);
		modelMap.addAttribute("currentUser", gvv.currentUser);
	    modelMap.addAttribute("isAdmin",gvv.isAdmin);
		
		Date time = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH");
        long hourTime = 60 * 60 * 1000;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        String step_str = request.getParameter("step");
        String now = request.getParameter("date");
        String op_str = request.getParameter("op");
        if(op_str==null || op_str.isEmpty()){
        	op_str="day";
        }
        System.out.println(step_str + "#" + now);
        int step = -24;
        
        if (now == null || now.isEmpty()) {
            now = df.format(time);
        }
        
        long nowLtime = df.parse(now).getTime();
        long dHour = 24 * hourTime;
        long wHour = 7 * dHour;
        long mHour = 30 * dHour;
        String nowFormat = formatter.format(new Date(nowLtime));
        
        String bf1mD = df.format(new Date(nowLtime -mHour));
        String bf1mDtip = formatter.format(new Date(nowLtime - mHour)) + "~" + nowFormat;
        String bf1wD = df.format(new Date(nowLtime -wHour));
        String bf1wDtip = formatter.format(new Date(nowLtime - wHour)) + "~" + nowFormat;
        String bf1dD = df.format(new Date(nowLtime -dHour));
        String bf1dDtip = formatter.format(new Date(nowLtime - dHour)) + "~"+nowFormat;
        //当天
        String todayD = df.format(time);
        String todayDtip = formatter.format(new Date(time.getTime() -dHour))+
        		"~"+formatter.format(time);
        //未来1天，1周，1月
        //+1d，+1w，+1m的jsp脚本判断bug修复，现有时间粒度上加1小时进行判断,即futureNow
        //实际上变量名now和time交换一下逻辑上比较清晰,另外，request.getParameter("step")似乎没什么用。
        Calendar cal = Calendar.getInstance();
        cal.setTime(df.parse(now));
        cal.add(Calendar.HOUR, 1);
        String futureNow = df.format(cal.getTime());
        String af1dD;
        String af1wD;
        String af1mD;
        
        if(df.parse(futureNow).after(time)){
        	af1dD = af1wD = af1mD = todayD;
        }else{
        	af1dD = df.format(new Date(nowLtime + dHour));
        	af1wD = df.format(new Date(nowLtime + wHour));
        	af1mD = df.format(new Date(nowLtime + mHour));
        }
        String af1dDtip = nowFormat+"~"+formatter.format(new Date(nowLtime + dHour));
        String af1wDtip = nowFormat+"~"+formatter.format(new Date(nowLtime + wHour));
        String af1mDtip = nowFormat+"~"+formatter.format(new Date(nowLtime + mHour));
        
        modelMap.addAttribute("bf1mD", bf1mD);
        modelMap.addAttribute("bf1mDtip", bf1mDtip);
        modelMap.addAttribute("bf1wD", bf1wD);
        modelMap.addAttribute("bf1wDtip", bf1wDtip);
        modelMap.addAttribute("bf1dD", bf1dD);
        modelMap.addAttribute("bf1dDtip", bf1dDtip);
        
        modelMap.addAttribute("todayD", todayD);
        modelMap.addAttribute("todayDtip", todayDtip);
        
        modelMap.addAttribute("af1dD", af1dD);
        modelMap.addAttribute("af1dDtip", af1dDtip);
        modelMap.addAttribute("af1wD", af1wD);
        modelMap.addAttribute("af1wDtip", af1wDtip);
        modelMap.addAttribute("af1mD", af1mD);
        modelMap.addAttribute("af1mDtip", af1mDtip);
        
        String now_s = formatter.format( df.parse(now));
        modelMap.addAttribute("now_s", now_s);
        modelMap.addAttribute("step", step_str);
        modelMap.addAttribute("op_str", op_str);
        
		return "/task_center.ftl";
	}
	/**
	 * 重构host_center.jsp
	 * @param modelMap
	 * @param request
	 * @param response
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/host_center", method = RequestMethod.GET)
	public String host_center(ModelMap modelMap, HttpServletRequest request,
			HttpServletResponse response) throws ParseException {
		log.info("--------------init the host_center------------");
		
		GlobalViewVariable gvv = new GlobalViewVariable();
		
		commonnav(request,gvv);
		modelMap.addAttribute("currentUser", gvv.currentUser);
	    modelMap.addAttribute("isAdmin",gvv.isAdmin);
	    
		Date time = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH");
        long hourTime = 60 * 60 * 1000;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        String step_str = request.getParameter("step");
        String now = request.getParameter("date");
        String op_str = request.getParameter("op");
        if(op_str==null || op_str.isEmpty()){
        	op_str="day";
        }
        System.out.println(step_str + "#" + now);
        int step = -24;
        
        if (now == null || now.isEmpty()) {
            now = df.format(time);
        }
        
        String now_s = formatter.format( df.parse(now));
        modelMap.addAttribute("now_s", now_s);
        modelMap.addAttribute("step", step_str);
        modelMap.addAttribute("op_str", op_str);
		
		return "/host_center.ftl";
	}
	/**
	 * 重构task.jsp
	 * @param modelMap
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/task", method = RequestMethod.GET)
	public String task(ModelMap modelMap, HttpServletRequest request,
			HttpServletResponse response) {
		log.info("--------------init the task------------");
		GlobalViewVariable gvv = new GlobalViewVariable();
		commonnav(request,gvv);
		modelMap.addAttribute("currentUser", gvv.currentUser);
	    modelMap.addAttribute("isAdmin",gvv.isAdmin);
		
		gvv.cr = new ClientResource(gvv.host + "pool");
	    IPoolsResource poolResource = gvv.cr.wrap(IPoolsResource.class);
	    gvv.cr.accept(MediaType.APPLICATION_XML);
	    ArrayList<PoolDTO> pools = poolResource.retrieve();
	    int UNALLOCATED = 1;

	    gvv.cr = new ClientResource(gvv.host + "host");
	    IHostsResource hostResource = gvv.cr.wrap(IHostsResource.class);
	    gvv.cr.accept(MediaType.APPLICATION_XML);
	    ArrayList<HostDTO> hosts = hostResource.retrieve();

	    gvv.cr = new ClientResource(gvv.host + "status");
	    IAttemptStatusResource attemptResource = gvv.cr.wrap(IAttemptStatusResource.class);
	    gvv.cr.accept(MediaType.APPLICATION_XML);
	    ArrayList<StatusDTO> statuses = attemptResource.retrieve();

	    gvv.cr = new ClientResource(gvv.host + "group");
	    IUserGroupsResource groupResource = gvv.cr.wrap(IUserGroupsResource.class);
	    gvv.cr.accept(MediaType.APPLICATION_XML);
	    ArrayList<UserGroupDTO> groups = groupResource.retrieve();
	    String name = request.getParameter("appname");
	    String path = request.getParameter("path");
	    String ip = request.getParameter("ip");
	    if (name == null) {
	        name = "";
	    }
	    if (ip == null) {
	        ip = "";
	    }
		modelMap.addAttribute("ip", ip);
	    modelMap.addAttribute("hosts", hosts);
	    modelMap.addAttribute("name", name);
	    modelMap.addAttribute("path", path);
	    modelMap.addAttribute("statuses",statuses);
	    modelMap.addAttribute("users", gvv.users);
	    modelMap.addAttribute("groups",groups);
		return "/task.ftl";
	}
	/**
	 * 重构schedule.jsp
	 * @param modelMap
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/schedule", method = RequestMethod.GET)
	public String schedule(ModelMap modelMap, HttpServletRequest request,
			HttpServletResponse response) {
		log.info("--------------init the schedule------------");
		GlobalViewVariable gvv = new GlobalViewVariable();
		commonnav(request,gvv);
		modelMap.addAttribute("currentUser", gvv.currentUser);
	    modelMap.addAttribute("isAdmin",gvv.isAdmin);
	    
		return "/schedule.ftl";
	}
	/**
	 * 重构task_form.jsp
	 * @param modelMap
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/task_form", method = RequestMethod.GET)
	public String task_form(ModelMap modelMap, HttpServletRequest request,
			HttpServletResponse response) {
		log.info("--------------init the task_form------------");
		GlobalViewVariable gvv = new GlobalViewVariable();
		commonnav(request,gvv);
		modelMap.addAttribute("currentUser", gvv.currentUser);
	    modelMap.addAttribute("isAdmin",gvv.isAdmin);
	    
		String[] types = {"hadoop", "spring", "other"};

	    gvv.cr = new ClientResource(gvv.host + "status");
	    IAttemptStatusResource attemptResource = gvv.cr.wrap(IAttemptStatusResource.class);
	    gvv.cr.accept(MediaType.APPLICATION_XML);
	    ArrayList<StatusDTO> statuses = attemptResource.retrieve();

	    gvv.cr = new ClientResource(gvv.host + "group");
	    IUserGroupsResource groupResource = gvv.cr.wrap(IUserGroupsResource.class);
	    gvv.cr.accept(MediaType.APPLICATION_XML);
	    ArrayList<UserGroupDTO> groups = groupResource.retrieve();
	    String taskId = request.getParameter("task_id");
	    gvv.cr = new ClientResource(gvv.host + "task/" + taskId.trim());
	    ITaskResource taskResource = gvv.cr.wrap(ITaskResource.class);
	    gvv.cr.accept(MediaType.APPLICATION_XML);
	    TaskDTO dto = taskResource.retrieve();
	    String conditionStr = dto.getAlertRule().getConditions();
	    modelMap.addAttribute("conditionStr", conditionStr);
	    modelMap.addAttribute("dto", dto);
	    modelMap.addAttribute("statuses", statuses);
	    modelMap.addAttribute("users", gvv.users);
	    modelMap.addAttribute("groups",groups);
		return "/task_form.ftl";
	}
	/**
	 * 重构attempt.jsp
	 * @param modelMap
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/attempt", method = RequestMethod.GET)
	public String attempt(ModelMap modelMap, HttpServletRequest request,
			HttpServletResponse response) {
		log.info("--------------init the attempt------------");
		GlobalViewVariable gvv = new GlobalViewVariable();
		commonnav(request,gvv);
		modelMap.addAttribute("currentUser", gvv.currentUser);
	    modelMap.addAttribute("isAdmin",gvv.isAdmin);
	    
		return "/attempt.ftl";
	}
	/**
	 * 重构viewlog.jsp
	 * @param modelMap
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/viewlog", method = RequestMethod.GET)
	public String viewlog(ModelMap modelMap, HttpServletRequest request,
			HttpServletResponse response) {
		log.info("--------------init the viewlog------------");
		GlobalViewVariable gvv = new GlobalViewVariable();
		commonnav(request,gvv);
		modelMap.addAttribute("currentUser", gvv.currentUser);
	    modelMap.addAttribute("isAdmin",gvv.isAdmin);
	    
		return "/viewlog.ftl";
	}
	/**
	 * 重构feederror.jsp
	 * @param modelMap
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/feederror", method = RequestMethod.GET)
	public String feederror(ModelMap modelMap, HttpServletRequest request,
			HttpServletResponse response) {
		log.info("--------------init the feederror------------");
		
		GlobalViewVariable gvv = new GlobalViewVariable();
		commonnav(request,gvv);
		modelMap.addAttribute("currentUser", gvv.currentUser);
	    modelMap.addAttribute("isAdmin",gvv.isAdmin);
	    
	    String attemptId = request.getParameter("id");
	    String taskName = request.getParameter("taskName");
	    String taskId = request.getParameter("taskId");
	    String ip = request.getParameter("ip");
	    String state = request.getParameter("status");
	    String feedType = request.getParameter("feedtype");
	    String from = request.getParameter("from");
	    String logUrl = "";
	    String creator="";
	    String qq = "";
	    
	    gvv.cr = new ClientResource(gvv.host + "task/" + taskId);
        ITaskResource taskResource = gvv.cr.wrap(ITaskResource.class);
        gvv.cr.accept(MediaType.APPLICATION_XML);
        TaskDTO dto = taskResource.retrieve();
        creator = dto.getCreator();
        
        if (from!= null && from.equals("monitor")){
        	UserDTO userDTO = gvv.userMap.get(creator);
            if (userDTO != null){
                qq= userDTO.getQq();
            }else
            {
                qq = "";
            }
        }
        
        String domain = "";
        try {
            domain = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.web.deploy.weburl");
        } catch (LionException e) {

            e.printStackTrace();
            domain = "taurus.dp";
        }

        logUrl = "http://"
                + domain
                + "/mvc/viewlog?id="
                + attemptId
                + "&status="
                + state;
        
	    modelMap.addAttribute("attemptId", attemptId);
	    modelMap.addAttribute("taskName", taskName);
	    modelMap.addAttribute("taskId", taskId);
	    modelMap.addAttribute("ip", ip);
	    modelMap.addAttribute("state", state);
	    modelMap.addAttribute("feedType", feedType);
	    modelMap.addAttribute("from", from);
	    modelMap.addAttribute("creator", creator);
	    modelMap.addAttribute("logUrl", logUrl);
	    modelMap.addAttribute("qq", qq);
	    modelMap.addAttribute("users", gvv.users);
	    
		return "/feederror.ftl";
	}
	/**
	 * 重构monitor.jsp
	 * @param modelMap
	 * @param request
	 * @param response
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/monitor", method = RequestMethod.GET)
	public String monitor(ModelMap modelMap, HttpServletRequest request,
			HttpServletResponse response) throws ParseException {
		log.info("--------------init the monitor------------");
		GlobalViewVariable gvv = new GlobalViewVariable();
		commonnav(request,gvv);
		modelMap.addAttribute("currentUser", gvv.currentUser);
	    modelMap.addAttribute("isAdmin",gvv.isAdmin);
	    
	    Date time = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH");
        long hourTime = 60 * 60 * 1000;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        String stepStr = request.getParameter("step");
        String now = request.getParameter("date");
        String opStr = request.getParameter("op");
        if(StringUtil.isBlank(opStr)){
        	opStr="day";
        }
        int step = -24;
        
        if (StringUtil.isBlank(now)) {
            now = df.format(time);
        }
        
        long nowLtime = df.parse(now).getTime();
        long dHour = 24 * hourTime;
        long wHour = 7 * dHour;
        long mHour = 30 * dHour;
        String nowFormat = formatter.format(new Date(nowLtime));
        
        String bf1mD = df.format(new Date(nowLtime -mHour));
        String bf1mDtip = formatter.format(new Date(nowLtime -mHour))+"~"+nowFormat;
        String bf1wD = df.format(new Date(nowLtime -wHour));
        String bf1wDtip = formatter.format(new Date(nowLtime -wHour))+"~"+nowFormat;
        String bf1dD = df.format(new Date(nowLtime -dHour));
        String bf1dDtip = formatter.format(new Date(nowLtime -dHour))+"~"+nowFormat;
        //当天
        String todayD = df.format(time);
        String todayDtip = formatter.format(new Date(time.getTime() -dHour))+
        		"~"+formatter.format(time);
        //未来1天，1周，1月
        //+1d，+1w，+1m的jsp脚本判断bug修复，现有时间粒度上加1小时进行判断,即futureNow
        //实际上变量名now和time交换一下逻辑上比较清晰,另外，request.getParameter("step")似乎没什么用。
        Calendar cal = Calendar.getInstance();
        cal.setTime(df.parse(now));
        cal.add(Calendar.HOUR, 1);
        String futureNow = df.format(cal.getTime());
        String af1dD = "";
        String af1wD = "";
        String af1mD = "";
        if(df.parse(futureNow).after(time)){
        	af1dD = af1wD = af1mD = todayD;
        }else{
        	af1dD = df.format(new Date(nowLtime + dHour));
        	af1wD = df.format(new Date(nowLtime + wHour));
        	af1mD = df.format(new Date(nowLtime + mHour));
        }
        String af1dDtip = nowFormat+"~"+formatter.format(new Date(nowLtime + dHour));
        String af1wDtip = nowFormat+"~"+formatter.format(new Date(nowLtime + wHour));
        String af1mDtip = nowFormat+"~"+formatter.format(new Date(nowLtime + mHour));
        
        modelMap.addAttribute("bf1mD", bf1mD);
        modelMap.addAttribute("bf1mDtip", bf1mDtip);
        modelMap.addAttribute("bf1wD", bf1wD);
        modelMap.addAttribute("bf1wDtip", bf1wDtip);
        modelMap.addAttribute("bf1dD", bf1dD);
        modelMap.addAttribute("bf1dDtip", bf1dDtip);
        
        modelMap.addAttribute("todayD", todayD);
        modelMap.addAttribute("todayDtip", todayDtip);
        
        modelMap.addAttribute("af1dD", af1dD);
        modelMap.addAttribute("af1dDtip", af1dDtip);
        modelMap.addAttribute("af1wD", af1wD);
        modelMap.addAttribute("af1wDtip", af1wDtip);
        modelMap.addAttribute("af1mD", af1mD);
        modelMap.addAttribute("af1mDtip", af1mDtip);
        
        String now_s = formatter.format( df.parse(now));
        modelMap.addAttribute("now_s", now_s);
        modelMap.addAttribute("step", stepStr);
        modelMap.addAttribute("op_str", opStr);
        
		return "/monitor.ftl";
	}
	/**
	 * 重构hosts.jsp
	 * @param modelMap
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/hosts", method = RequestMethod.GET)
	public String hosts(ModelMap modelMap, HttpServletRequest request,
			HttpServletResponse response) {
		log.info("--------------init the hosts------------");
		GlobalViewVariable gvv = new GlobalViewVariable();
		commonnav(request,gvv);
		modelMap.addAttribute("currentUser", gvv.currentUser);
	    modelMap.addAttribute("isAdmin",gvv.isAdmin);

	    gvv.cr = new ClientResource(gvv.host + "host");
	    IHostsResource hostsResource = gvv.cr.wrap(IHostsResource.class);
	    gvv.cr.accept(MediaType.APPLICATION_XML);
	    ArrayList<HostDTO> hosts = hostsResource.retrieve();
	    
	    //hostList.jsp todo
	    modelMap.addAttribute("host", gvv.host);
	    modelMap.addAttribute("hosts", hosts);
	    modelMap.addAttribute("hHelper", new HomeHelper());
	    
	    String statusCode = (String) (request.getAttribute("statusCode"));
	    String hostName = request.getParameter("hostName");
	    String op = request.getParameter("op");
	    gvv.cr = new ClientResource(gvv.host + "host/" + hostName);
	    IHostResource hostResource = gvv.cr.wrap(IHostResource.class);
	    gvv.cr.accept(MediaType.APPLICATION_XML);
	    HostDTO dto = hostResource.retrieve();
	    Map<String, String> maps = new HashMap<String, String>();
	    maps.put("up", "上线");
	    //我把dowan改成down了
	    maps.put("down", "下线");
	    maps.put("restart", "重启");
	    maps.put("update", "升级");
	    String opChs = maps.get(op);
	    if (opChs == null) {
	        opChs = "操作";
	    }
	    
	    modelMap.addAttribute("statusCode", statusCode);
	    modelMap.addAttribute("hostName", hostName);
	    modelMap.addAttribute("dto", dto);
	    
	    // 任务监控标签 start
	    // 正在运行的任务RUNNING
	    ClientResource crTask = new ClientResource(gvv.host + "gettasks");
        com.dp.bigdata.taurus.restlet.resource.IGetTasks taskResource = crTask.wrap(IGetTasks.class);
        ArrayList<Task> tasks = taskResource.retrieve();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String url = gvv.host + "getattemptsbystatus/";

        Date nowTime  = new Date();
        String now = formatter.format(nowTime);
        long hourTime = 60 * 60 * 1000;
        Date taskDateTime = new Date(new Date().getTime() - 24 * hourTime);
        String taskTime = formatter.format(taskDateTime);
        
        gvv.cr = new ClientResource(url + 6);//正在运行？
        IGetAttemptsByStatus resource = gvv.cr.wrap(IGetAttemptsByStatus.class);
        ArrayList<AttemptDTO> attempts = resource.retrieve();
	    modelMap.addAttribute("attempts", attempts);
	    modelMap.addAttribute("tasks", tasks);
	    modelMap.addAttribute("nowTime", nowTime);
	    
	    
	    // 提交失败的任务 SUBMIT_FAIL
	    ClientResource submitFailCr = new ClientResource(url + 5);//提交失败？
        IGetAttemptsByStatus submitFailResource = submitFailCr.wrap(IGetAttemptsByStatus.class);
        ArrayList<AttemptDTO> submitFailAttempts = submitFailResource.retrieve();
        modelMap.addAttribute("submitFailAttempts", submitFailAttempts);
        modelMap.addAttribute("taskDateTime", taskDateTime);
        
        
        //失败的任务 FAILED
        ClientResource failCr = new ClientResource(url + 8);//失败？
        IGetAttemptsByStatus failResource = failCr.wrap(IGetAttemptsByStatus.class);
        ArrayList<AttemptDTO> failAttempts = failResource.retrieve();
        modelMap.addAttribute("failAttempts", failAttempts);
        
        //依赖超时的任务 DEPENDENCY_TIMEOUT
        ClientResource dependencyTimeOutCr = new ClientResource(url + 3);
        IGetAttemptsByStatus dependencyTimeOutResource = dependencyTimeOutCr.wrap(IGetAttemptsByStatus.class);
        ArrayList<AttemptDTO> dependencyTimeOutAttempts = dependencyTimeOutResource.retrieve();
        modelMap.addAttribute("dependencyTimeOutAttempts", dependencyTimeOutAttempts);
        
        //超时的任务 TIMEOUT
        ClientResource timeOutCr = new ClientResource(url + 9);
        IGetAttemptsByStatus timeOutResource = timeOutCr.wrap(IGetAttemptsByStatus.class);
        ArrayList<AttemptDTO> timeOutAttempts = timeOutResource.retrieve();
        modelMap.addAttribute("timeOutAttempts", timeOutAttempts);
	    // 任务监控标签 end
	    
		return "/hosts.ftl";
	}
	/**
	 * 重构host_history.jsp
	 * @param modelMap
	 * @param request
	 * @param response
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/host_history", method = RequestMethod.GET)
	public String host_history(ModelMap modelMap, HttpServletRequest request,
			HttpServletResponse response) throws ParseException {
		log.info("--------------init the host_history------------");
		GlobalViewVariable gvv = new GlobalViewVariable();
		commonnav(request,gvv);
		modelMap.addAttribute("currentUser", gvv.currentUser);
	    modelMap.addAttribute("isAdmin",gvv.isAdmin);
	    
	    String ip = request.getParameter("ip");
        java.util.Date time = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now_str = request.getParameter("date");
        if (now_str == null || now_str.isEmpty()) {
            now_str = formatter.format(time);
        }

        Date startDate = new Date(time.getTime() - 12 * 60 * 60 * 1000);
        String startTime = formatter.format(startDate);

        String endTime = formatter.format(formatter.parse(now_str));
        modelMap.addAttribute("ip", ip);
        
        gvv.cr = new ClientResource(gvv.host + "host");
        IHostsResource hostResource = gvv.cr.wrap(IHostsResource.class);
        gvv.cr.accept(MediaType.APPLICATION_XML);
        ArrayList<HostDTO> hosts = hostResource.retrieve();
        modelMap.addAttribute("hosts", hosts);
        
        modelMap.addAttribute("now_s", endTime);
		return "/host_history.ftl";
	}
	/**
	 * 重构user.jsp
	 * @param modelMap
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public String user(ModelMap modelMap, HttpServletRequest request,
			HttpServletResponse response) {
		log.info("--------------init the user------------");
		GlobalViewVariable gvv = new GlobalViewVariable();
		commonnav(request,gvv);
		modelMap.addAttribute("currentUser", gvv.currentUser);
	    modelMap.addAttribute("isAdmin",gvv.isAdmin);
	    
	    gvv.cr = new ClientResource(gvv.host + "group");
        IUserGroupsResource groupResource = gvv.cr.wrap(IUserGroupsResource.class);
        gvv.cr.accept(MediaType.APPLICATION_XML);
        ArrayList<UserGroupDTO> groups = groupResource.retrieve();
        modelMap.addAttribute("groups", groups);

        //用户分组列表显示，所以分组信息只存在与用户表，并没有分组表？
        Map<String, String> map = new HashMap<String, String>();
        for (UserDTO user : gvv.users) {
            String group = user.getGroup();
            if (group == null || group.equals("")) {
                group = "未分组";
            }
            if (map.containsKey(group)) {
            	//已有分组成员，加入分组列表，用逗号加空格分隔
                map.put(group, map.get(group) + ", " + user.getName());
            } else {
            	//创建新分组
                map.put(group, user.getName());
            }
    		//找到当前用户
            if (user.getName().equals(gvv.currentUser)) {
            	modelMap.addAttribute("user", user);
            }
        }
        modelMap.addAttribute("map", map);
        
        
        
	    modelMap.addAttribute("users", gvv.users);
	    return "/user.ftl";
	}
	/**
	 * 重构resign.jsp
	 * @param modelMap
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/resign", method = RequestMethod.GET)
	public String resign(ModelMap modelMap, HttpServletRequest request,
			HttpServletResponse response) {
		log.info("--------------init the resign------------");
		GlobalViewVariable gvv = new GlobalViewVariable();
		commonnav(request,gvv);
		modelMap.addAttribute("currentUser", gvv.currentUser);
	    modelMap.addAttribute("isAdmin",gvv.isAdmin);
	    
	    
	    gvv.cr = new ClientResource(gvv.host + "group");
	    IUserGroupsResource groupResource = gvv.cr.wrap(IUserGroupsResource.class);
	    gvv.cr.accept(MediaType.APPLICATION_XML);
	    ArrayList<UserGroupDTO> groups = groupResource.retrieve();

	    Map<String, String> map = new HashMap<String, String>();
	    for (UserDTO user : gvv.users) {
	        String group = user.getGroup();
	        if (group == null || group.equals("")) {
	            group = "未分组";
	        }
	        if (map.containsKey(group)) {
	            map.put(group, map.get(group) + ", " + user.getName());
	        } else {
	            map.put(group, user.getName());
	        }
	        if (user.getName().equals(gvv.currentUser)) {
	        	modelMap.addAttribute("user", user);
	        }  
	    } 
	    modelMap.addAttribute("map", map);
	    
	    
	    String task_api = gvv.host + "task";
        String name = request.getParameter("name");
        String path = request.getParameter("path");
        String appname = request.getParameter("appname");
        if (name != null && !name.isEmpty()) {
            task_api = task_api + "?name=" + name;
        } else if (appname != null) {
            task_api = task_api + "?appname=" + appname;
        } else if (gvv.currentUser != null) {
            task_api = task_api + "?user=" + gvv.currentUser;
        }
        if (path != null && !path.equals("")) {
    	
    	}
        gvv.cr = new ClientResource(task_api);
        ITasksResource resource = gvv.cr.wrap(ITasksResource.class);
        gvv.cr.accept(MediaType.APPLICATION_XML);
        ArrayList<TaskDTO> tasks = resource.retrieve();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        modelMap.addAttribute("tasks", tasks);
        modelMap.addAttribute("hHelper", new HomeHelper());
	    
	    modelMap.addAttribute("userId", gvv.userId);
	    return "/resign.ftl";
	}
	/**
	 * 重构dbadmin.jsp
	 * @param modelMap
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/dbadmin", method = RequestMethod.GET)
	public String dbadmin(ModelMap modelMap, HttpServletRequest request,
			HttpServletResponse response) {
		log.info("--------------init the dbadmin------------");
		GlobalViewVariable gvv = new GlobalViewVariable();
		commonnav(request,gvv);
		modelMap.addAttribute("currentUser", gvv.currentUser);
	    modelMap.addAttribute("isAdmin",gvv.isAdmin);
	    
	    
	    
	    return "/dbadmin.ftl";
	}
	/**
	 * 重构cronbuilder.jsp cron生成器
	 * @param modelMap
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/cronbuilder", method = RequestMethod.GET)
	public String cronbuilder(ModelMap modelMap, HttpServletRequest request,
			HttpServletResponse response) {
		log.info("--------------init the cronbuilder------------");
		GlobalViewVariable gvv = new GlobalViewVariable();
		commonnav(request,gvv);
		modelMap.addAttribute("currentUser", gvv.currentUser);
	    modelMap.addAttribute("isAdmin",gvv.isAdmin);
	    
	    Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        modelMap.addAttribute("year", year);
	    
	    return "/cronbuilder.ftl";
	}
	/**
	 * 重构feedback.jsp 我要反馈
	 * @param modelMap
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/feedback", method = RequestMethod.GET)
	public String feedback(ModelMap modelMap, HttpServletRequest request,
			HttpServletResponse response) {
		log.info("--------------init the feedback------------");
		GlobalViewVariable gvv = new GlobalViewVariable();
		commonnav(request,gvv);
		modelMap.addAttribute("currentUser", gvv.currentUser);
	    modelMap.addAttribute("isAdmin",gvv.isAdmin);
	    
	    
	    
	    return "/feedback.ftl";
	}
	/**
	 * 重构update.jsp 更新日志
	 * @param modelMap
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public String update(ModelMap modelMap, HttpServletRequest request,
			HttpServletResponse response) {
		log.info("--------------init the update------------");
		GlobalViewVariable gvv = new GlobalViewVariable();
		commonnav(request,gvv);
		modelMap.addAttribute("currentUser", gvv.currentUser);
	    modelMap.addAttribute("isAdmin",gvv.isAdmin);
	    
	    
	    
	    return "/update.ftl";
	}
	/**
	 * 重构about.jsp 使用帮助
	 * @param modelMap
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/about", method = RequestMethod.GET)
	public String about(ModelMap modelMap, HttpServletRequest request,
			HttpServletResponse response) {
		log.info("--------------init the about------------");
		GlobalViewVariable gvv = new GlobalViewVariable();
		commonnav(request,gvv);
		modelMap.addAttribute("currentUser", gvv.currentUser);
	    modelMap.addAttribute("isAdmin",gvv.isAdmin);
	    
	    
	    
	    return "/about.ftl";
	}
	/**
	 * 重构jsp/common-nav.jsp
	 * @param modelMap
	 * @param request
	 */
	private void commonnav(HttpServletRequest request,GlobalViewVariable gvv){
		gvv.currentUser =  (String) request.getSession().
				getAttribute(com.dp.bigdata.taurus.web.servlet.LoginServlet.USER_NAME);
	    if (gvv.currentUser != null) {
	    	
		} else {
			
		}
	    //Global variable
	    //String host;
	    gvv.userId = -1;
	    try {
	        gvv.host = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.web.restlet.url");
	    } catch (LionException e) {
	        gvv.host = servletContext.getInitParameter("RESTLET_SERVER");
	        e.printStackTrace();
	    }

	    gvv.isAdmin = false;
	    gvv.cr = new ClientResource(gvv.host + "user");
	    gvv.userResource = gvv.cr.wrap(IUsersResource.class);
	    gvv.cr.accept(MediaType.APPLICATION_XML);
	    gvv.users = gvv.userResource.retrieve();
	    gvv.userMap = new HashMap<String, UserDTO>();
	    for (UserDTO user : gvv.users) {
	        gvv.userMap.put(user.getName(),user);
	        if (user.getName().equals(gvv.currentUser)) {

	            gvv.userId = user.getId();
	            if ("admin".equals(user.getGroup()) || "monitor".equals(user.getGroup()) || "OP".equals(user.getGroup())) {
	                gvv.isAdmin = true;
	            } else {
	                gvv.isAdmin = false;
	            }

	        }
	    }
	    
	}
	/**
	 * jsp/common-nav.jsp所需公共变量
	 * @author chenchongze
	 *
	 */
	public class GlobalViewVariable{
		
		private String currentUser=null;
		private String host=null;
	    private int userId = -1;
	    private boolean isAdmin = false;
	    private ClientResource cr=null;
	    private IUsersResource userResource=null;
	    private ArrayList<UserDTO> users=null;
	    private HashMap<String, UserDTO>  userMap =null;
	    
	    public GlobalViewVariable(){};
	}

	public class HomeHelper{
		public HostDTO getDtos(String host, String dtoName){
			ClientResource cr = new ClientResource(host + "host/" + dtoName);
	        IHostResource hostResource = cr.wrap(IHostResource.class);
	        cr.accept(MediaType.APPLICATION_XML);
	        HostDTO dtos = hostResource.retrieve();
	        return dtos;
		}
		//resign任务交接页面取一个分组的用户数组
		public String[] getGroupUserList(String groupUsers){
			return groupUsers.split(",");
		}
	}
}
