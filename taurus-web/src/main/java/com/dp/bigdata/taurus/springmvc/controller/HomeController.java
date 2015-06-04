package com.dp.bigdata.taurus.springmvc.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
import com.dp.bigdata.taurus.generated.module.Task;
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
import com.dp.bigdata.taurus.web.servlet.LoginServlet;

@Controller
public class HomeController implements ServletContextAware{

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private ServletContext servletContext;
	
	public void setServletContext(ServletContext sc) { this.servletContext=sc; }
    
	/**
	 * 重构signin.jsp
	 * @param modelMap
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/signin", method = RequestMethod.GET)
	public String signin(ModelMap modelMap, 
						HttpServletRequest request,
						HttpServletResponse response) 
	{
		log.info("--------------init the signin------------");
		
		String url = (String) request.getParameter("redirect-url");
		
		if (StringUtils.isBlank(url)) { url = ""; }
		
		modelMap.addAttribute("url", url);
		
		String[] switchUrls = null;
		try {
			String keys = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.web.deploy.weburl");
			switchUrls = keys.split(",");
		} catch (LionException e) {
			e.printStackTrace();
			switchUrls = "http://alpha.taurus.dp:8080,http://beta.taurus.dp,http://ppe.taurus.dp,http://taurus.dp".split(",");
		}
		modelMap.addAttribute("switchUrls", switchUrls);
		
		return "/signin.ftl";
	}
	
	/**
	 * 重构index.jsp,主体代码task_center,monitor可以复用
	 * @param modelMap
	 * @param request
	 * @param response
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap modelMap, 
						HttpServletRequest request,
						HttpServletResponse response) throws ParseException 
	{
		log.info("--------------init the index------------");
		
		GlobalViewVariable globalViewVariable = new GlobalViewVariable();
		commonnav(request,globalViewVariable);
		modelMap.addAttribute("currentUser", globalViewVariable.currentUser);
		modelMap.addAttribute("isAdmin", globalViewVariable.isAdmin);
		
		String stepStr = request.getParameter("step");//查询间隔时间段的“步数”
		String baseDateStr = request.getParameter("date");//查询所基于的时间
		String opStr = request.getParameter("op");
		
		Date nowDate = new Date();
		SimpleDateFormat urlDateFormat = new SimpleDateFormat("yyyyMMddHH");//url请求参数中的时间格式
		SimpleDateFormat tipDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");//前端tooltip显示的时间格式

		if (StringUtil.isBlank(opStr)) { opStr = "day"; }//opStr缺省值
		
		if (StringUtil.isBlank(baseDateStr)) { baseDateStr = urlDateFormat.format(nowDate); }//baseDateStr缺省值
		
		Date baseDate = urlDateFormat.parse(baseDateStr);
		String baseDateTip = tipDateFormat.format(baseDate);
        
		Long dateHour = baseDate.getTime();
		Long hourTime = 60 * 60 * 1000L;
		Long dayHour = 24 * hourTime;
		Long weekHour = 7 * dayHour;
		Long monthHour = 30 * dayHour;
		
		String OneMonthEarlierTime = urlDateFormat.format(new Date(dateHour - monthHour));
		String OneMonthEarlierTip = tipDateFormat.format(new Date(dateHour - monthHour)) + "~" + baseDateTip;
		
		String OneWeekEarlierTime = urlDateFormat.format(new Date(dateHour - weekHour));
		String OneWeekEarlierTip = tipDateFormat.format(new Date(dateHour - weekHour)) + "~" + baseDateTip;
		
		String OneDayEarlierTime = urlDateFormat.format(new Date(dateHour - dayHour));
		String OneDayEarlierTip = tipDateFormat.format(new Date(dateHour - dayHour)) + "~" + baseDateTip;
		
		String todayTime = urlDateFormat.format(nowDate);
		String todayTip = tipDateFormat.format(new Date(nowDate.getTime() -dayHour)) + "~" + tipDateFormat.format(nowDate);
		
		/**
		 * 未来1天，1周，1月
		 * +1d，+1w，+1m在现有时间粒度上加1小时进行判断,即OneHourLaterTime
		 */
		Calendar cal = Calendar.getInstance();
		cal.setTime(baseDate);
		cal.add(Calendar.HOUR, 1);
		
		String OneHourLaterTime = urlDateFormat.format(cal.getTime());
		
		String OneDayLaterTime = null;
		String OneWeekLaterTime = null;
		String OneMonthLaterTime = null;
		
		if (urlDateFormat.parse(OneHourLaterTime).after(nowDate)) {
			OneDayLaterTime = OneWeekLaterTime = OneMonthLaterTime = todayTime;
		} else {
			OneDayLaterTime = urlDateFormat.format(new Date(dateHour + dayHour));
			OneWeekLaterTime = urlDateFormat.format(new Date(dateHour + weekHour));
			OneMonthLaterTime = urlDateFormat.format(new Date(dateHour + monthHour));
		}
		
		String OneDayLaterTip = baseDateTip + "~" + tipDateFormat.format(new Date(dateHour + dayHour));
		String OneWeekLaterTip = baseDateTip + "~" + tipDateFormat.format(new Date(dateHour + weekHour));
		String OneMonthLaterTip = baseDateTip + "~" + tipDateFormat.format(new Date(dateHour + monthHour));
		
		modelMap.addAttribute("bf1mD", OneMonthEarlierTime);
		modelMap.addAttribute("bf1mDtip", OneMonthEarlierTip);
		modelMap.addAttribute("bf1wD", OneWeekEarlierTime);
		modelMap.addAttribute("bf1wDtip", OneWeekEarlierTip);
		modelMap.addAttribute("bf1dD", OneDayEarlierTime);
		modelMap.addAttribute("bf1dDtip", OneDayEarlierTip);
		
		modelMap.addAttribute("todayD", todayTime);
		modelMap.addAttribute("todayDtip", todayTip);
		
		modelMap.addAttribute("af1dD", OneDayLaterTime);
		modelMap.addAttribute("af1dDtip", OneDayLaterTip);
		modelMap.addAttribute("af1wD", OneWeekLaterTime);
		modelMap.addAttribute("af1wDtip", OneWeekLaterTip);
		modelMap.addAttribute("af1mD", OneMonthLaterTime);
		modelMap.addAttribute("af1mDtip", OneMonthLaterTip);
		
		modelMap.addAttribute("now_s", baseDateTip);
		modelMap.addAttribute("step", stepStr);
		modelMap.addAttribute("op_str", opStr);
		
		String[] switchUrls = null;
		try {
			String keys = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.web.url.switch");
			switchUrls = keys.split(",");
		} catch (LionException e) {
			e.printStackTrace();
			switchUrls = "http://alpha.taurus.dp:8080,http://beta.taurus.dp,http://ppe.taurus.dp,http://taurus.dp".split(",");
		}
		modelMap.addAttribute("switchUrls", switchUrls);
		
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
	public String task_center(ModelMap modelMap, 
								HttpServletRequest request,
								HttpServletResponse response) throws ParseException 
	{
		log.info("--------------init the task_center------------");
		
		GlobalViewVariable globalViewVariable = new GlobalViewVariable();
		commonnav(request,globalViewVariable);
		modelMap.addAttribute("currentUser", globalViewVariable.currentUser);
		modelMap.addAttribute("isAdmin", globalViewVariable.isAdmin);
		
		String stepStr = request.getParameter("step");//查询间隔时间段的“步数”
		String baseDateStr = request.getParameter("date");//查询所基于的时间
		String opStr = request.getParameter("op");
		
		Date nowDate = new Date();
		SimpleDateFormat urlDateFormat = new SimpleDateFormat("yyyyMMddHH");//url请求参数中的时间格式
		SimpleDateFormat tipDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");//前端tooltip显示的时间格式

		if (StringUtil.isBlank(opStr)) { opStr = "day"; }//opStr缺省值
		
		if (StringUtil.isBlank(baseDateStr)) { baseDateStr = urlDateFormat.format(nowDate); }//baseDateStr缺省值
		
		Date baseDate = urlDateFormat.parse(baseDateStr);
		String baseDateTip = tipDateFormat.format(baseDate);
        
		Long dateHour = baseDate.getTime();
		Long hourTime = 60 * 60 * 1000L;
		Long dayHour = 24 * hourTime;
		Long weekHour = 7 * dayHour;
		Long monthHour = 30 * dayHour;
		
		String OneMonthEarlierTime = urlDateFormat.format(new Date(dateHour - monthHour));
		String OneMonthEarlierTip = tipDateFormat.format(new Date(dateHour - monthHour)) + "~" + baseDateTip;
		
		String OneWeekEarlierTime = urlDateFormat.format(new Date(dateHour - weekHour));
		String OneWeekEarlierTip = tipDateFormat.format(new Date(dateHour - weekHour)) + "~" + baseDateTip;
		
		String OneDayEarlierTime = urlDateFormat.format(new Date(dateHour - dayHour));
		String OneDayEarlierTip = tipDateFormat.format(new Date(dateHour - dayHour)) + "~" + baseDateTip;
		
		String todayTime = urlDateFormat.format(nowDate);
		String todayTip = tipDateFormat.format(new Date(nowDate.getTime() -dayHour)) + "~" + tipDateFormat.format(nowDate);
		
		/**
		 * 未来1天，1周，1月
		 * +1d，+1w，+1m在现有时间粒度上加1小时进行判断,即OneHourLaterTime
		 */
		Calendar cal = Calendar.getInstance();
		cal.setTime(baseDate);
		cal.add(Calendar.HOUR, 1);
		
		String OneHourLaterTime = urlDateFormat.format(cal.getTime());
		
		String OneDayLaterTime = null;
		String OneWeekLaterTime = null;
		String OneMonthLaterTime = null;
		
		if (urlDateFormat.parse(OneHourLaterTime).after(nowDate)) {
			OneDayLaterTime = OneWeekLaterTime = OneMonthLaterTime = todayTime;
		} else {
			OneDayLaterTime = urlDateFormat.format(new Date(dateHour + dayHour));
			OneWeekLaterTime = urlDateFormat.format(new Date(dateHour + weekHour));
			OneMonthLaterTime = urlDateFormat.format(new Date(dateHour + monthHour));
		}
		
		String OneDayLaterTip = baseDateTip + "~" + tipDateFormat.format(new Date(dateHour + dayHour));
		String OneWeekLaterTip = baseDateTip + "~" + tipDateFormat.format(new Date(dateHour + weekHour));
		String OneMonthLaterTip = baseDateTip + "~" + tipDateFormat.format(new Date(dateHour + monthHour));
		
		modelMap.addAttribute("bf1mD", OneMonthEarlierTime);
		modelMap.addAttribute("bf1mDtip", OneMonthEarlierTip);
		modelMap.addAttribute("bf1wD", OneWeekEarlierTime);
		modelMap.addAttribute("bf1wDtip", OneWeekEarlierTip);
		modelMap.addAttribute("bf1dD", OneDayEarlierTime);
		modelMap.addAttribute("bf1dDtip", OneDayEarlierTip);
		
		modelMap.addAttribute("todayD", todayTime);
		modelMap.addAttribute("todayDtip", todayTip);
		
		modelMap.addAttribute("af1dD", OneDayLaterTime);
		modelMap.addAttribute("af1dDtip", OneDayLaterTip);
		modelMap.addAttribute("af1wD", OneWeekLaterTime);
		modelMap.addAttribute("af1wDtip", OneWeekLaterTip);
		modelMap.addAttribute("af1mD", OneMonthLaterTime);
		modelMap.addAttribute("af1mDtip", OneMonthLaterTip);
		
		modelMap.addAttribute("now_s", baseDateTip);
		modelMap.addAttribute("step", stepStr);
		modelMap.addAttribute("op_str", opStr);
        
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
	public String host_center(ModelMap modelMap, 
								HttpServletRequest request,
								HttpServletResponse response) throws ParseException 
	{
		log.info("--------------init the host_center------------");
		
		GlobalViewVariable globalViewVariable = new GlobalViewVariable();
		commonnav(request,globalViewVariable);
		modelMap.addAttribute("currentUser", globalViewVariable.currentUser);
		modelMap.addAttribute("isAdmin", globalViewVariable.isAdmin);
	    
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        String stepStr = request.getParameter("step");
        String dateStr = request.getParameter("date");
        String opStr = request.getParameter("op");
        
        if (StringUtil.isBlank(opStr)) { opStr="day"; }

        if (StringUtil.isBlank(dateStr)) { dateStr = df.format(new Date()); }
        
        String now_s = formatter.format(df.parse(dateStr));
        modelMap.addAttribute("now_s", now_s);
        modelMap.addAttribute("step", stepStr);
        modelMap.addAttribute("op_str", opStr);
		
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
	public String task(ModelMap modelMap, 
						HttpServletRequest request,
						HttpServletResponse response) 
	{
		log.info("--------------init the task------------");
		
		GlobalViewVariable globalViewVariable = new GlobalViewVariable();
		commonnav(request,globalViewVariable);
		modelMap.addAttribute("currentUser", globalViewVariable.currentUser);
		modelMap.addAttribute("isAdmin", globalViewVariable.isAdmin);
		modelMap.addAttribute("users", globalViewVariable.users);
		
		String name = request.getParameter("appname");
		String path = request.getParameter("path");
		String ip = request.getParameter("ip");
		
		if (name == null) { name = ""; }
		
		if (ip == null) { ip = ""; }
		
		modelMap.addAttribute("name", name);
		modelMap.addAttribute("path", path);
		modelMap.addAttribute("ip", ip);
		
		globalViewVariable.cr = new ClientResource(globalViewVariable.host + "pool");
		IPoolsResource poolResource = globalViewVariable.cr.wrap(IPoolsResource.class);
		globalViewVariable.cr.accept(MediaType.APPLICATION_XML);
		ArrayList<PoolDTO> pools = poolResource.retrieve();
		int UNALLOCATED = 1;
		
		globalViewVariable.cr = new ClientResource(globalViewVariable.host + "host");
		IHostsResource hostResource = globalViewVariable.cr.wrap(IHostsResource.class);
		globalViewVariable.cr.accept(MediaType.APPLICATION_XML);
		ArrayList<HostDTO> hosts = hostResource.retrieve();
		modelMap.addAttribute("hosts", hosts);
		
		globalViewVariable.cr = new ClientResource(globalViewVariable.host + "status");
		IAttemptStatusResource attemptResource = globalViewVariable.cr.wrap(IAttemptStatusResource.class);
		globalViewVariable.cr.accept(MediaType.APPLICATION_XML);
		ArrayList<StatusDTO> statuses = attemptResource.retrieve();
		modelMap.addAttribute("statuses",statuses);
		
		globalViewVariable.cr = new ClientResource(globalViewVariable.host + "group");
		IUserGroupsResource groupResource = globalViewVariable.cr.wrap(IUserGroupsResource.class);
		globalViewVariable.cr.accept(MediaType.APPLICATION_XML);
		ArrayList<UserGroupDTO> groups = groupResource.retrieve();
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
	public String schedule(ModelMap modelMap, 
							HttpServletRequest request,
							HttpServletResponse response) 
	{
		log.info("--------------init the schedule------------");
		
		GlobalViewVariable globalViewVariable = new GlobalViewVariable();
		commonnav(request,globalViewVariable);
		modelMap.addAttribute("currentUser", globalViewVariable.currentUser);
		modelMap.addAttribute("isAdmin", globalViewVariable.isAdmin);
	    
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
	public String task_form(ModelMap modelMap, 
							HttpServletRequest request,
							HttpServletResponse response) 
	{
		log.info("--------------init the task_form------------");
		
		GlobalViewVariable globalViewVariable = new GlobalViewVariable();
		commonnav(request,globalViewVariable);
		modelMap.addAttribute("currentUser", globalViewVariable.currentUser);
		modelMap.addAttribute("isAdmin", globalViewVariable.isAdmin);
		modelMap.addAttribute("users", globalViewVariable.users);
		
		String[] types = {"hadoop", "spring", "other"};

		globalViewVariable.cr = new ClientResource(globalViewVariable.host + "status");
	    IAttemptStatusResource attemptResource = globalViewVariable.cr.wrap(IAttemptStatusResource.class);
	    globalViewVariable.cr.accept(MediaType.APPLICATION_XML);
	    ArrayList<StatusDTO> statuses = attemptResource.retrieve();
	    modelMap.addAttribute("statuses", statuses);
	    
	    globalViewVariable.cr = new ClientResource(globalViewVariable.host + "group");
	    IUserGroupsResource groupResource = globalViewVariable.cr.wrap(IUserGroupsResource.class);
	    globalViewVariable.cr.accept(MediaType.APPLICATION_XML);
	    ArrayList<UserGroupDTO> groups = groupResource.retrieve();
	    modelMap.addAttribute("groups",groups);
	    
	    String taskId = request.getParameter("task_id");
	    globalViewVariable.cr = new ClientResource(globalViewVariable.host + "task/" + taskId.trim());
	    ITaskResource taskResource = globalViewVariable.cr.wrap(ITaskResource.class);
	    globalViewVariable.cr.accept(MediaType.APPLICATION_XML);
	    TaskDTO dto = taskResource.retrieve();
	    String conditionStr = dto.getAlertRule().getConditions();
	    modelMap.addAttribute("dto", dto);
	    modelMap.addAttribute("conditionStr", conditionStr);
	    
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
	public String attempt(ModelMap modelMap, 
							HttpServletRequest request,
							HttpServletResponse response) 
	{
		log.info("--------------init the attempt------------");
		
		GlobalViewVariable globalViewVariable = new GlobalViewVariable();
		commonnav(request,globalViewVariable);
		modelMap.addAttribute("currentUser", globalViewVariable.currentUser);
		modelMap.addAttribute("isAdmin", globalViewVariable.isAdmin);
	    
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
	public String viewlog(ModelMap modelMap, 
							HttpServletRequest request,
							HttpServletResponse response) 
	{
		log.info("--------------init the viewlog------------");
		
		GlobalViewVariable globalViewVariable = new GlobalViewVariable();
		commonnav(request,globalViewVariable);
		modelMap.addAttribute("currentUser", globalViewVariable.currentUser);
		modelMap.addAttribute("isAdmin", globalViewVariable.isAdmin);
	    
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
	public String feederror(ModelMap modelMap, 
							HttpServletRequest request,
							HttpServletResponse response) 
	{
		log.info("--------------init the feederror------------");
		
		GlobalViewVariable globalViewVariable = new GlobalViewVariable();
		commonnav(request,globalViewVariable);
		modelMap.addAttribute("currentUser", globalViewVariable.currentUser);
		modelMap.addAttribute("isAdmin", globalViewVariable.isAdmin);
		modelMap.addAttribute("users", globalViewVariable.users);
		
		String attemptId = request.getParameter("id");
		String taskName = request.getParameter("taskName");
		String taskId = request.getParameter("taskId");
		String ip = request.getParameter("ip");
		String state = request.getParameter("status");
		String feedType = request.getParameter("feedtype");
		String from = request.getParameter("from");
		String logUrl = null;
		String creator = null;
		String qq = null;
		
		globalViewVariable.cr = new ClientResource(globalViewVariable.host + "task/" + taskId);
		ITaskResource taskResource = globalViewVariable.cr.wrap(ITaskResource.class);
		globalViewVariable.cr.accept(MediaType.APPLICATION_XML);
		TaskDTO dto = taskResource.retrieve();
		creator = dto.getCreator();
		
		if (StringUtil.isBlank(from) == false && from.equals("monitor")) {
			UserDTO userDTO = globalViewVariable.userMap.get(creator);
			
			if (userDTO != null) {
				qq= userDTO.getQq();
			} else {
				qq = "";
			}
		}
		
		String domain = null;
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
		
		GlobalViewVariable globalViewVariable = new GlobalViewVariable();
		commonnav(request,globalViewVariable);
		modelMap.addAttribute("currentUser", globalViewVariable.currentUser);
		modelMap.addAttribute("isAdmin", globalViewVariable.isAdmin);
	    
		String stepStr = request.getParameter("step");//查询间隔时间段的“步数”
		String baseDateStr = request.getParameter("date");//查询所基于的时间
		String opStr = request.getParameter("op");
		
		Date nowDate = new Date();
		SimpleDateFormat urlDateFormat = new SimpleDateFormat("yyyyMMddHH");//url请求参数中的时间格式
		SimpleDateFormat tipDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");//前端tooltip显示的时间格式

		if (StringUtil.isBlank(opStr)) { opStr = "day"; }//opStr缺省值
		
		if (StringUtil.isBlank(baseDateStr)) { baseDateStr = urlDateFormat.format(nowDate); }//baseDateStr缺省值
		
		Date baseDate = urlDateFormat.parse(baseDateStr);
		String baseDateTip = tipDateFormat.format(baseDate);
        
		Long dateHour = baseDate.getTime();
		Long hourTime = 60 * 60 * 1000L;
		Long dayHour = 24 * hourTime;
		Long weekHour = 7 * dayHour;
		Long monthHour = 30 * dayHour;
		
		String OneMonthEarlierTime = urlDateFormat.format(new Date(dateHour - monthHour));
		String OneMonthEarlierTip = tipDateFormat.format(new Date(dateHour - monthHour)) + "~" + baseDateTip;
		
		String OneWeekEarlierTime = urlDateFormat.format(new Date(dateHour - weekHour));
		String OneWeekEarlierTip = tipDateFormat.format(new Date(dateHour - weekHour)) + "~" + baseDateTip;
		
		String OneDayEarlierTime = urlDateFormat.format(new Date(dateHour - dayHour));
		String OneDayEarlierTip = tipDateFormat.format(new Date(dateHour - dayHour)) + "~" + baseDateTip;
		
		String todayTime = urlDateFormat.format(nowDate);
		String todayTip = tipDateFormat.format(new Date(nowDate.getTime() -dayHour)) + "~" + tipDateFormat.format(nowDate);
		
		/**
		 * 未来1天，1周，1月
		 * +1d，+1w，+1m在现有时间粒度上加1小时进行判断,即OneHourLaterTime
		 */
		Calendar cal = Calendar.getInstance();
		cal.setTime(baseDate);
		cal.add(Calendar.HOUR, 1);
		
		String OneHourLaterTime = urlDateFormat.format(cal.getTime());
		
		String OneDayLaterTime = null;
		String OneWeekLaterTime = null;
		String OneMonthLaterTime = null;
		
		if (urlDateFormat.parse(OneHourLaterTime).after(nowDate)) {
			OneDayLaterTime = OneWeekLaterTime = OneMonthLaterTime = todayTime;
		} else {
			OneDayLaterTime = urlDateFormat.format(new Date(dateHour + dayHour));
			OneWeekLaterTime = urlDateFormat.format(new Date(dateHour + weekHour));
			OneMonthLaterTime = urlDateFormat.format(new Date(dateHour + monthHour));
		}
		
		String OneDayLaterTip = baseDateTip + "~" + tipDateFormat.format(new Date(dateHour + dayHour));
		String OneWeekLaterTip = baseDateTip + "~" + tipDateFormat.format(new Date(dateHour + weekHour));
		String OneMonthLaterTip = baseDateTip + "~" + tipDateFormat.format(new Date(dateHour + monthHour));
		
		modelMap.addAttribute("bf1mD", OneMonthEarlierTime);
		modelMap.addAttribute("bf1mDtip", OneMonthEarlierTip);
		modelMap.addAttribute("bf1wD", OneWeekEarlierTime);
		modelMap.addAttribute("bf1wDtip", OneWeekEarlierTip);
		modelMap.addAttribute("bf1dD", OneDayEarlierTime);
		modelMap.addAttribute("bf1dDtip", OneDayEarlierTip);
		
		modelMap.addAttribute("todayD", todayTime);
		modelMap.addAttribute("todayDtip", todayTip);
		
		modelMap.addAttribute("af1dD", OneDayLaterTime);
		modelMap.addAttribute("af1dDtip", OneDayLaterTip);
		modelMap.addAttribute("af1wD", OneWeekLaterTime);
		modelMap.addAttribute("af1wDtip", OneWeekLaterTip);
		modelMap.addAttribute("af1mD", OneMonthLaterTime);
		modelMap.addAttribute("af1mDtip", OneMonthLaterTip);
		
		modelMap.addAttribute("now_s", baseDateTip);
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
		
		GlobalViewVariable globalViewVariable = new GlobalViewVariable();
		commonnav(request,globalViewVariable);
		modelMap.addAttribute("currentUser", globalViewVariable.currentUser);
		modelMap.addAttribute("isAdmin", globalViewVariable.isAdmin);

		globalViewVariable.cr = new ClientResource(globalViewVariable.host + "host");
	    IHostsResource hostsResource = globalViewVariable.cr.wrap(IHostsResource.class);
	    globalViewVariable.cr.accept(MediaType.APPLICATION_XML);
	    ArrayList<HostDTO> hosts = hostsResource.retrieve();
	    
	    //hostList.jsp todo
	    modelMap.addAttribute("host", globalViewVariable.host);
	    modelMap.addAttribute("hosts", hosts);
	    modelMap.addAttribute("hHelper", new HomeHelper());
	    
	    String statusCode = (String) (request.getAttribute("statusCode"));
	    String hostName = request.getParameter("hostName");
	    String op = request.getParameter("op");
	    globalViewVariable.cr = new ClientResource(globalViewVariable.host + "host/" + hostName);
	    IHostResource hostResource = globalViewVariable.cr.wrap(IHostResource.class);
	    globalViewVariable.cr.accept(MediaType.APPLICATION_XML);
	    HostDTO dto = hostResource.retrieve();
	    
	    Map<String, String> maps = new HashMap<String, String>();
	    maps.put("up", "上线");
	    maps.put("down", "下线");//我把dowan改成down了
	    maps.put("restart", "重启");
	    maps.put("update", "升级");
	    String opChs = maps.get(op);
	    if (opChs == null) { opChs = "操作"; }
	    
	    modelMap.addAttribute("statusCode", statusCode);
	    modelMap.addAttribute("hostName", hostName);
	    modelMap.addAttribute("dto", dto);
	    
	    // 任务监控标签 start
	    
	    // 正在运行的任务RUNNING
	    ClientResource crTask = new ClientResource(globalViewVariable.host + "gettasks");
        com.dp.bigdata.taurus.restlet.resource.IGetTasks taskResource = crTask.wrap(IGetTasks.class);
        ArrayList<Task> tasks = taskResource.retrieve();

        String url = globalViewVariable.host + "getattemptsbystatus/";

        Date nowDate  = new Date();
        Long hourTime = 60 * 60 * 1000L;
        Date taskDateTime = new Date(nowDate.getTime() - 24 * hourTime);
        
        globalViewVariable.cr = new ClientResource(url + 6);//正在运行？
        IGetAttemptsByStatus resource = globalViewVariable.cr.wrap(IGetAttemptsByStatus.class);
        ArrayList<AttemptDTO> attempts = resource.retrieve();
	    modelMap.addAttribute("attempts", attempts);
	    modelMap.addAttribute("tasks", tasks);
	    modelMap.addAttribute("nowTime", nowDate);
	    
	    // 提交失败的任务 SUBMIT_FAIL
	    ClientResource submitFailCr = new ClientResource(url + 5);//提交失败？
        IGetAttemptsByStatus submitFailResource = submitFailCr.wrap(IGetAttemptsByStatus.class);
        ArrayList<AttemptDTO> submitFailAttempts = submitFailResource.retrieve();
        modelMap.addAttribute("submitFailAttempts", submitFailAttempts);
        modelMap.addAttribute("taskDateTime", taskDateTime);
        
        // 失败的任务 FAILED
        ClientResource failCr = new ClientResource(url + 8);//失败？
        IGetAttemptsByStatus failResource = failCr.wrap(IGetAttemptsByStatus.class);
        ArrayList<AttemptDTO> failAttempts = failResource.retrieve();
        modelMap.addAttribute("failAttempts", failAttempts);
        
        // 依赖超时的任务 DEPENDENCY_TIMEOUT
        ClientResource dependencyTimeOutCr = new ClientResource(url + 3);//依赖超时？
        IGetAttemptsByStatus dependencyTimeOutResource = dependencyTimeOutCr.wrap(IGetAttemptsByStatus.class);
        ArrayList<AttemptDTO> dependencyTimeOutAttempts = dependencyTimeOutResource.retrieve();
        modelMap.addAttribute("dependencyTimeOutAttempts", dependencyTimeOutAttempts);
        
        // 超时的任务 TIMEOUT
        ClientResource timeOutCr = new ClientResource(url + 9);//超时？
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
	public String host_history(ModelMap modelMap, 
								HttpServletRequest request,
								HttpServletResponse response) throws ParseException 
	{
		log.info("--------------init the host_history------------");
		
		GlobalViewVariable globalViewVariable = new GlobalViewVariable();
		commonnav(request,globalViewVariable);
		modelMap.addAttribute("currentUser", globalViewVariable.currentUser);
		modelMap.addAttribute("isAdmin", globalViewVariable.isAdmin);
		
		String ip = request.getParameter("ip");
		modelMap.addAttribute("ip", ip);
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowTimeFormat = formatter.format(new Date());
		modelMap.addAttribute("now_s", nowTimeFormat);
		
		globalViewVariable.cr = new ClientResource(globalViewVariable.host + "host");
		IHostsResource hostResource = globalViewVariable.cr.wrap(IHostsResource.class);
		globalViewVariable.cr.accept(MediaType.APPLICATION_XML);
		ArrayList<HostDTO> hosts = hostResource.retrieve();
		modelMap.addAttribute("hosts", hosts);
		
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
	public String user(ModelMap modelMap, 
						HttpServletRequest request,
						HttpServletResponse response) 
	{
		log.info("--------------init the user------------");
		
		GlobalViewVariable globalViewVariable = new GlobalViewVariable();
		commonnav(request,globalViewVariable);
		modelMap.addAttribute("currentUser", globalViewVariable.currentUser);
		modelMap.addAttribute("isAdmin", globalViewVariable.isAdmin);
		modelMap.addAttribute("users", globalViewVariable.users);
		
		globalViewVariable.cr = new ClientResource(globalViewVariable.host + "group");
		IUserGroupsResource groupResource = globalViewVariable.cr.wrap(IUserGroupsResource.class);
		globalViewVariable.cr.accept(MediaType.APPLICATION_XML);
		ArrayList<UserGroupDTO> groups = groupResource.retrieve();
		modelMap.addAttribute("groups", groups);
		
		//用户分组列表显示
		Map<String, String> map = new HashMap<String, String>();
		
		for (UserDTO user : globalViewVariable.users) {
		    String group = user.getGroup();
		    
		    if (StringUtil.isBlank(group)) { group = "未分组"; }
		    
		    if (map.containsKey(group)) {
		    	//已有分组成员，加入分组列表，用逗号加空格分隔
		        map.put(group, map.get(group) + ", " + user.getName());
		    } else {
		    	//创建新分组
		        map.put(group, user.getName());
		    }
		    
			//找到当前用户
		    if (user.getName().equals(globalViewVariable.currentUser)) {
		    	modelMap.addAttribute("user", user);
		    }
		}
		
		modelMap.addAttribute("map", map);
		
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
	public String resign(ModelMap modelMap, 
						HttpServletRequest request,
						HttpServletResponse response) 
	{
		log.info("--------------init the resign------------");
		
		GlobalViewVariable globalViewVariable = new GlobalViewVariable();
		commonnav(request,globalViewVariable);
		modelMap.addAttribute("currentUser", globalViewVariable.currentUser);
		modelMap.addAttribute("isAdmin", globalViewVariable.isAdmin);
		modelMap.addAttribute("userId", globalViewVariable.userId);
		
		globalViewVariable.cr = new ClientResource(globalViewVariable.host + "group");
		IUserGroupsResource groupResource = globalViewVariable.cr.wrap(IUserGroupsResource.class);
		globalViewVariable.cr.accept(MediaType.APPLICATION_XML);
		ArrayList<UserGroupDTO> groups = groupResource.retrieve();
		
		Map<String, String> map = new HashMap<String, String>();
		
		for (UserDTO user : globalViewVariable.users) {
			
			String group = user.getGroup();
			
			if (StringUtil.isBlank(group)) { group = "未分组"; }
			
			if (map.containsKey(group)) {
			    map.put(group, map.get(group) + ", " + user.getName());
			} else {
			    map.put(group, user.getName());
			}
			
			if (user.getName().equals(globalViewVariable.currentUser)) {
				modelMap.addAttribute("user", user);
			}
		} 
		modelMap.addAttribute("map", map);
		
		String task_api = globalViewVariable.host + "task";
		String name = request.getParameter("name");
		String path = request.getParameter("path");
		String appname = request.getParameter("appname");
		
		if (StringUtil.isBlank(name) == false) {
		    task_api = task_api + "?name=" + name;
		} else if (StringUtil.isBlank(appname) == false) {
		    task_api = task_api + "?appname=" + appname;
		} else if (StringUtil.isBlank(globalViewVariable.currentUser) == false) {
		    task_api = task_api + "?user=" + globalViewVariable.currentUser;
		}
		
		globalViewVariable.cr = new ClientResource(task_api);
		ITasksResource resource = globalViewVariable.cr.wrap(ITasksResource.class);
		globalViewVariable.cr.accept(MediaType.APPLICATION_XML);
		ArrayList<TaskDTO> tasks = resource.retrieve();
		modelMap.addAttribute("tasks", tasks);
		
		modelMap.addAttribute("hHelper", new HomeHelper());
		
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
	public String dbadmin(ModelMap modelMap, 
							HttpServletRequest request,
							HttpServletResponse response) 
	{
		log.info("--------------init the dbadmin------------");
		
		GlobalViewVariable globalViewVariable = new GlobalViewVariable();
		commonnav(request,globalViewVariable);
		modelMap.addAttribute("currentUser", globalViewVariable.currentUser);
		modelMap.addAttribute("isAdmin", globalViewVariable.isAdmin);
	    
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
	public String cronbuilder(ModelMap modelMap, 
								HttpServletRequest request,
								HttpServletResponse response) 
	{
		log.info("--------------init the cronbuilder------------");
		
		GlobalViewVariable globalViewVariable = new GlobalViewVariable();
		commonnav(request,globalViewVariable);
		modelMap.addAttribute("currentUser", globalViewVariable.currentUser);
		modelMap.addAttribute("isAdmin", globalViewVariable.isAdmin);
	    
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
	public String feedback(ModelMap modelMap, 
							HttpServletRequest request,
							HttpServletResponse response) 
	{
		log.info("--------------init the feedback------------");
		
		GlobalViewVariable globalViewVariable = new GlobalViewVariable();
		commonnav(request,globalViewVariable);
		modelMap.addAttribute("currentUser", globalViewVariable.currentUser);
		modelMap.addAttribute("isAdmin", globalViewVariable.isAdmin);
	    
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
	public String update(ModelMap modelMap, 
							HttpServletRequest request,
							HttpServletResponse response) 
	{
		log.info("--------------init the update------------");
		
		GlobalViewVariable globalViewVariable = new GlobalViewVariable();
		commonnav(request,globalViewVariable);
		modelMap.addAttribute("currentUser", globalViewVariable.currentUser);
		modelMap.addAttribute("isAdmin", globalViewVariable.isAdmin);
	    
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
	public String about(ModelMap modelMap, 
							HttpServletRequest request,
							HttpServletResponse response) 
	{
		log.info("--------------init the about------------");
		
		GlobalViewVariable globalViewVariable = new GlobalViewVariable();
		commonnav(request,globalViewVariable);
		modelMap.addAttribute("currentUser", globalViewVariable.currentUser);
		modelMap.addAttribute("isAdmin", globalViewVariable.isAdmin);
	    
	    return "/about.ftl";
	}
	
	/**
	 * 重构error.jsp
	 * @param modelMap
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/error", method = {RequestMethod.GET,RequestMethod.POST})
	public String error(ModelMap modelMap, 
							HttpServletRequest request,
							HttpServletResponse response) 
	{
		log.info("--------------init the error------------");
		
		GlobalViewVariable globalViewVariable = new GlobalViewVariable();
		commonnav(request,globalViewVariable);
		modelMap.addAttribute("currentUser", globalViewVariable.currentUser);
		modelMap.addAttribute("isAdmin", globalViewVariable.isAdmin);
	    
	    return "/error.ftl";
	}
	
	/**
	 * 重构jsp/common-nav.jsp
	 * @param modelMap
	 * @param request
	 */
	private void commonnav(HttpServletRequest request,GlobalViewVariable globalViewVariable){
		
		globalViewVariable.currentUser = (String) request.getSession().getAttribute(LoginServlet.USER_NAME);
		globalViewVariable.userId = -1;
		
		try {
			globalViewVariable.host = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.web.restlet.url");
		} catch (LionException e) {
			globalViewVariable.host = servletContext.getInitParameter("RESTLET_SERVER");
		    e.printStackTrace();
		}
		
		globalViewVariable.isAdmin = false;
		globalViewVariable.cr = new ClientResource(globalViewVariable.host + "user");
		globalViewVariable.userResource = globalViewVariable.cr.wrap(IUsersResource.class);
		globalViewVariable.cr.accept(MediaType.APPLICATION_XML);
		globalViewVariable.users = globalViewVariable.userResource.retrieve();
		globalViewVariable.userMap = new HashMap<String, UserDTO>();
		
		for (UserDTO user : globalViewVariable.users) {
			globalViewVariable.userMap.put(user.getName(),user);
			if (user.getName().equals(globalViewVariable.currentUser)) {
				
				globalViewVariable.userId = user.getId();
				
				if ("admin".equals(user.getGroup()) || "monitor".equals(user.getGroup()) || "OP".equals(user.getGroup())) {
					globalViewVariable.isAdmin = true;
				} else {
					globalViewVariable.isAdmin = false;
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
		
		private String currentUser = null;
		private String host = null;
		private int userId = -1;
		private boolean isAdmin = false;
		private ClientResource cr = null;
		private IUsersResource userResource = null;
		private ArrayList<UserDTO> users = null;
		private HashMap<String, UserDTO> userMap = null;
		
		public GlobalViewVariable(){};
	}

	/**
	 * freemarker帮助类
	 * @author chenchongze
	 *
	 */
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
