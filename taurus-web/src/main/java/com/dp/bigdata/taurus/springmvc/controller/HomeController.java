package com.dp.bigdata.taurus.springmvc.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jodd.util.StringUtil;

import org.codehaus.plexus.util.StringUtils;
import org.restlet.resource.ClientResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dianping.lion.EnvZooKeeperConfig;
import com.dianping.lion.client.ConfigCache;
import com.dp.bigdata.taurus.lion.ConfigHolder;
import com.dp.bigdata.taurus.lion.LionKeys;
import com.dp.bigdata.taurus.restlet.shared.AttemptDTO;
import com.dp.bigdata.taurus.restlet.shared.HostDTO;
import com.dp.bigdata.taurus.restlet.shared.PoolDTO;
import com.dp.bigdata.taurus.restlet.shared.StatusDTO;
import com.dp.bigdata.taurus.restlet.shared.TaskDTO;
import com.dp.bigdata.taurus.restlet.shared.UserDTO;
import com.dp.bigdata.taurus.restlet.shared.UserGroupDTO;
import com.dp.bigdata.taurus.springmvc.utils.GlobalViewVariable;

@Controller
public class HomeController extends BaseController {

	private Logger log = LoggerFactory.getLogger(this.getClass());
    
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
		
		String switchUrlAll = InitController.SWITCH_URL_ALL;
		modelMap.addAttribute("switchUrls", switchUrlAll.split(","));
		
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
	@RequestMapping(value = {"/","/index"}, method = RequestMethod.GET)
	public String index(ModelMap modelMap, 
						HttpServletRequest request,
						HttpServletResponse response) throws ParseException 
	{

		GlobalViewVariable globalViewVariable = new GlobalViewVariable();
		commonnav(request,globalViewVariable);
		modelMap.addAttribute("currentUser", globalViewVariable.currentUser);
		modelMap.addAttribute("isAdmin", globalViewVariable.isAdmin);
		commonAttr(modelMap);
		
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
		String nowDateTip = tipDateFormat.format(nowDate);
        
		Long dateHour = baseDate.getTime();
		Long hourTime = 60 * 60 * 1000L;
		Long dayHour = 24 * hourTime;
		Long weekHour = 7 * dayHour;
		Long monthHour = 30 * dayHour;
		
		String oneMonthEarlierTime = urlDateFormat.format(new Date(dateHour - monthHour));
		String oneMonthEarlierTip = tipDateFormat.format(new Date(dateHour - monthHour)) + "~" + baseDateTip;
		String oneMonthEarlierHistoryTip = tipDateFormat.format(new Date(dateHour - monthHour)) + "~" + nowDateTip;
		
		String oneWeekEarlierTime = urlDateFormat.format(new Date(dateHour - weekHour));
		String oneWeekEarlierTip = tipDateFormat.format(new Date(dateHour - weekHour)) + "~" + baseDateTip;
		String oneWeekEarlierHistoryTip = tipDateFormat.format(new Date(dateHour - weekHour)) + "~" + nowDateTip;
		
		String oneDayEarlierTime = urlDateFormat.format(new Date(dateHour - dayHour));
		String oneDayEarlierTip = tipDateFormat.format(new Date(dateHour - dayHour)) + "~" + baseDateTip;
		String oneDayEarlierHistoryTip = tipDateFormat.format(new Date(dateHour - dayHour)) + "~" + nowDateTip;
		
		String todayTime = urlDateFormat.format(nowDate);
		String todayTip = tipDateFormat.format(new Date(nowDate.getTime() -dayHour)) + "~" + nowDateTip;
		
		/**
		 * 未来1天，1周，1月
		 * +1d，+1w，+1m在现有时间粒度上加1小时进行判断,即OneHourLaterTime
		 */
		Calendar cal = Calendar.getInstance();
		cal.setTime(baseDate);
		cal.add(Calendar.HOUR, 1);
		
		String oneHourLaterTime = urlDateFormat.format(cal.getTime());
		
		String oneDayLaterTime = null;
		String oneWeekLaterTime = null;
		String oneMonthLaterTime = null;
		
		if (urlDateFormat.parse(oneHourLaterTime).after(nowDate)) {
			oneDayLaterTime = oneWeekLaterTime = oneMonthLaterTime = todayTime;
		} else {
			oneDayLaterTime = urlDateFormat.format(new Date(dateHour + dayHour));
			oneWeekLaterTime = urlDateFormat.format(new Date(dateHour + weekHour));
			oneMonthLaterTime = urlDateFormat.format(new Date(dateHour + monthHour));
		}
		
		String oneDayLaterTip = baseDateTip + "~" + tipDateFormat.format(new Date(dateHour + dayHour));
		String oneWeekLaterTip = baseDateTip + "~" + tipDateFormat.format(new Date(dateHour + weekHour));
		String oneMonthLaterTip = baseDateTip + "~" + tipDateFormat.format(new Date(dateHour + monthHour));
		
		String oneDayLaterHistoryTip = tipDateFormat.format(new Date(dateHour + dayHour)) + "~" + nowDateTip;
		String oneWeekLaterHistoryTip = tipDateFormat.format(new Date(dateHour + weekHour)) + "~" + nowDateTip;
		String oneMonthLaterHistoryTip = tipDateFormat.format(new Date(dateHour + monthHour)) + "~" + nowDateTip;
		
		
		// op = day
		modelMap.addAttribute("bf1mD", oneMonthEarlierTime);
		modelMap.addAttribute("bf1mDtip", oneMonthEarlierTip);
		modelMap.addAttribute("bf1wD", oneWeekEarlierTime);
		modelMap.addAttribute("bf1wDtip", oneWeekEarlierTip);
		modelMap.addAttribute("bf1dD", oneDayEarlierTime);
		modelMap.addAttribute("bf1dDtip", oneDayEarlierTip);
		
		modelMap.addAttribute("todayD", todayTime);
		modelMap.addAttribute("todayDtip", todayTip);
		
		modelMap.addAttribute("af1dD", oneDayLaterTime);
		modelMap.addAttribute("af1dDtip", oneDayLaterTip);
		modelMap.addAttribute("af1wD", oneWeekLaterTime);
		modelMap.addAttribute("af1wDtip", oneWeekLaterTip);
		modelMap.addAttribute("af1mD", oneMonthLaterTime);
		modelMap.addAttribute("af1mDtip", oneMonthLaterTip);
		
		// op = history
		modelMap.addAttribute("bf1mHtip", oneMonthEarlierHistoryTip);
		modelMap.addAttribute("bf1wHtip", oneWeekEarlierHistoryTip);
		modelMap.addAttribute("bf1dHtip", oneDayEarlierHistoryTip);
		modelMap.addAttribute("af1dHtip", oneDayLaterHistoryTip);
		modelMap.addAttribute("af1wHtip", oneWeekLaterHistoryTip);
		modelMap.addAttribute("af1mHtip", oneMonthLaterHistoryTip);
		
		modelMap.addAttribute("now_s", baseDateTip);
		modelMap.addAttribute("step", stepStr);
		modelMap.addAttribute("op_str", opStr);
		
		String switchUrlAll = InitController.SWITCH_URL_ALL;
		modelMap.addAttribute("switchUrls", switchUrlAll.split(","));
		
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
		commonAttr(modelMap);
		
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
		String nowDateTip = tipDateFormat.format(nowDate);
        
		Long dateHour = baseDate.getTime();
		Long hourTime = 60 * 60 * 1000L;
		Long dayHour = 24 * hourTime;
		Long weekHour = 7 * dayHour;
		Long monthHour = 30 * dayHour;
		
		String OneMonthEarlierTime = urlDateFormat.format(new Date(dateHour - monthHour));
		String OneMonthEarlierTip = tipDateFormat.format(new Date(dateHour - monthHour)) + "~" + baseDateTip;
		String OneMonthEarlierHistoryTip = tipDateFormat.format(new Date(dateHour - monthHour)) + "~" + nowDateTip;
		
		String OneWeekEarlierTime = urlDateFormat.format(new Date(dateHour - weekHour));
		String OneWeekEarlierTip = tipDateFormat.format(new Date(dateHour - weekHour)) + "~" + baseDateTip;
		String OneWeekEarlierHistoryTip = tipDateFormat.format(new Date(dateHour - weekHour)) + "~" + nowDateTip;
		
		String OneDayEarlierTime = urlDateFormat.format(new Date(dateHour - dayHour));
		String OneDayEarlierTip = tipDateFormat.format(new Date(dateHour - dayHour)) + "~" + baseDateTip;
		String OneDayEarlierHistoryTip = tipDateFormat.format(new Date(dateHour - dayHour)) + "~" + nowDateTip;
		
		String todayTime = urlDateFormat.format(nowDate);
		String todayTip = tipDateFormat.format(new Date(nowDate.getTime() -dayHour)) + "~" + nowDateTip;
		
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
		
		String OneDayLaterHistoryTip = tipDateFormat.format(new Date(dateHour + dayHour)) + "~" + nowDateTip;
		String OneWeekLaterHistoryTip = tipDateFormat.format(new Date(dateHour + weekHour)) + "~" + nowDateTip;
		String OneMonthLaterHistoryTip = tipDateFormat.format(new Date(dateHour + monthHour)) + "~" + nowDateTip;
		
		
		// op = day
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
		
		// op = history
		modelMap.addAttribute("bf1mHtip", OneMonthEarlierHistoryTip);
		modelMap.addAttribute("bf1wHtip", OneWeekEarlierHistoryTip);
		modelMap.addAttribute("bf1dHtip", OneDayEarlierHistoryTip);
		modelMap.addAttribute("af1dHtip", OneDayLaterHistoryTip);
		modelMap.addAttribute("af1wHtip", OneWeekLaterHistoryTip);
		modelMap.addAttribute("af1mHtip", OneMonthLaterHistoryTip);
		
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
		commonAttr(modelMap);
	    
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
		commonAttr(modelMap);
		
		String name = request.getParameter("appname");
		String path = request.getParameter("path");
		String ip = request.getParameter("ip");
		
		if (name == null) { name = ""; }
		
		if (ip == null) { ip = ""; }
		
		modelMap.addAttribute("name", name);
		modelMap.addAttribute("path", path);
		modelMap.addAttribute("ip", ip);
		
		globalViewVariable.cr = new ClientResource(globalViewVariable.host + "pool");
		ArrayList<PoolDTO> pools = globalViewVariable.cr.get(ArrayList.class);
		int UNALLOCATED = 1;
		
		globalViewVariable.cr = new ClientResource(globalViewVariable.host + "host");
		ArrayList<HostDTO> hosts = globalViewVariable.cr.get(ArrayList.class);
		modelMap.addAttribute("hosts", hosts);
		
		globalViewVariable.cr = new ClientResource(globalViewVariable.host + "status");
		ArrayList<StatusDTO> statuses = globalViewVariable.cr.get(ArrayList.class);
		modelMap.addAttribute("statuses",statuses);
		
		globalViewVariable.cr = new ClientResource(globalViewVariable.host + "group");
		ArrayList<UserGroupDTO> groups = globalViewVariable.cr.get(ArrayList.class);
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
		commonAttr(modelMap);
	    
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
		commonAttr(modelMap);
		
		String[] types = {"hadoop", "spring", "other"};

		globalViewVariable.cr = new ClientResource(globalViewVariable.host + "status");
	    ArrayList<StatusDTO> statuses = globalViewVariable.cr.get(ArrayList.class);
	    modelMap.addAttribute("statuses", statuses);
	    
	    globalViewVariable.cr = new ClientResource(globalViewVariable.host + "group");
	    ArrayList<UserGroupDTO> groups = globalViewVariable.cr.get(ArrayList.class);
	    modelMap.addAttribute("groups",groups);
	    
	    String taskId = request.getParameter("task_id");
	    globalViewVariable.cr = new ClientResource(globalViewVariable.host + "task/" + taskId.trim());
	    TaskDTO dto = globalViewVariable.cr.get(TaskDTO.class);
	    String conditionStr = dto.getConditions();
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
		commonAttr(modelMap);
	    
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
		commonAttr(modelMap);
	    
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
		commonAttr(modelMap);
		
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
		TaskDTO dto = globalViewVariable.cr.get(TaskDTO.class);
		creator = dto.getCreator();
		
		if (StringUtil.isNotBlank(from) && from.equals("monitor")) {
			UserDTO userDTO = globalViewVariable.userMap.get(creator);
			
			if (userDTO != null) {
				qq= userDTO.getQq();
			} else {
				qq = "";
			}
		}
		
		String domain = InitController.DOMAIN;
		
		logUrl = domain
		        + "/viewlog?id="
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
		
		String alert_admin = ConfigHolder.get(LionKeys.ADMIN_USER);
		modelMap.addAttribute("alert_admin", alert_admin);
		
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
		commonAttr(modelMap);
	    
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
		commonAttr(modelMap);

		globalViewVariable.cr = new ClientResource(globalViewVariable.host + "host");
	    ArrayList<HostDTO> hosts = globalViewVariable.cr.get(ArrayList.class);
	    
	    //hostList.jsp todo
	    modelMap.addAttribute("host", globalViewVariable.host);
	    modelMap.addAttribute("hosts", hosts);
	    modelMap.addAttribute("hHelper", new HomeHelper());
	    
	    String statusCode = (String) (request.getAttribute("statusCode"));
	    String hostName = request.getParameter("hostName");
	    String op = request.getParameter("op");
	    globalViewVariable.cr = new ClientResource(globalViewVariable.host + "host/" + hostName);
	    HostDTO dto = globalViewVariable.cr.get(HostDTO.class);
	    
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
	    
		//终端开关控制
		boolean isWebtermEnabled = false;
		if(StringUtil.isNotBlank(hostName)){
			isWebtermEnabled = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getBooleanProperty("taurus.webterm.enabled");
		}
		modelMap.addAttribute("isWebtermEnabled", isWebtermEnabled);
		
	    // 任务监控标签 start
	    
	    // 正在运行的任务RUNNING
		globalViewVariable.cr = new ClientResource(globalViewVariable.host + "gettasks");
        ArrayList<TaskDTO> tasks = globalViewVariable.cr.get(ArrayList.class);

        String url = globalViewVariable.host + "getattemptsbystatus/";

        Date nowDate  = new Date();
        Long hourTime = 60 * 60 * 1000L;
        Date taskDateTime = new Date(nowDate.getTime() - 24 * hourTime);
        
        globalViewVariable.cr = new ClientResource(url + 6);//正在运行？
        ArrayList<AttemptDTO> attempts = globalViewVariable.cr.get(ArrayList.class);
	    modelMap.addAttribute("attempts", attempts);
	    modelMap.addAttribute("tasks", tasks);
	    modelMap.addAttribute("nowTime", nowDate);
	    
	    // 提交失败的任务 SUBMIT_FAIL
	    globalViewVariable.cr = new ClientResource(url + 5);//提交失败？
        ArrayList<AttemptDTO> submitFailAttempts = globalViewVariable.cr.get(ArrayList.class);
        modelMap.addAttribute("submitFailAttempts", submitFailAttempts);
        modelMap.addAttribute("taskDateTime", taskDateTime);
        
        // 失败的任务 FAILED
        globalViewVariable.cr = new ClientResource(url + 8);//失败？
        ArrayList<AttemptDTO> failAttempts = globalViewVariable.cr.get(ArrayList.class);
        modelMap.addAttribute("failAttempts", failAttempts);
        
        // 依赖超时的任务 DEPENDENCY_TIMEOUT
        globalViewVariable.cr = new ClientResource(url + 3);//依赖超时？
        ArrayList<AttemptDTO> dependencyTimeOutAttempts = globalViewVariable.cr.get(ArrayList.class);
        modelMap.addAttribute("dependencyTimeOutAttempts", dependencyTimeOutAttempts);
        
        // 超时的任务 TIMEOUT
        globalViewVariable.cr = new ClientResource(url + 9);//超时？
        ArrayList<AttemptDTO> timeOutAttempts = globalViewVariable.cr.get(ArrayList.class);
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
		commonAttr(modelMap);
		
		String ip = request.getParameter("ip");
		modelMap.addAttribute("ip", ip);
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowTimeFormat = formatter.format(new Date());
		modelMap.addAttribute("now_s", nowTimeFormat);
		
		globalViewVariable.cr = new ClientResource(globalViewVariable.host + "host");
		ArrayList<HostDTO> hosts = globalViewVariable.cr.get(ArrayList.class);
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
		commonAttr(modelMap);
		
		globalViewVariable.cr = new ClientResource(globalViewVariable.host + "group");
		ArrayList<UserGroupDTO> groups = globalViewVariable.cr.get(ArrayList.class);
		modelMap.addAttribute("groups", groups);
		
		//用户分组列表显示
		Map<String, String> map = new HashMap<String, String>();
		
		for (UserDTO user : globalViewVariable.users) {
			//找到当前用户
		    if (user.getName().equals(globalViewVariable.currentUser)) {
		    	modelMap.addAttribute("user", user);
		    }
		    
		    // 多分组重新改写(完成)
		    String groupNamesWithComma = user.getGroup();
		    if (StringUtil.isBlank(groupNamesWithComma)) { groupNamesWithComma = "未分组"; }
		    
		    String[] groupNames = groupNamesWithComma.split(",");
		    for(String groupName : groupNames){
		    	if (map.containsKey(groupName)) {
			    	//已有分组成员，加入分组列表，用逗号加空格分隔
			        map.put(groupName, map.get(groupName) + ", " + user.getName());
			    } else {
			    	//创建新分组
			        map.put(groupName, user.getName());
			    }
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
		commonAttr(modelMap);
		
		Map<String, String> map = new HashMap<String, String>();
		
		for (UserDTO user : globalViewVariable.users) {
			//找到当前用户
		    if (user.getName().equals(globalViewVariable.currentUser)) {
		    	modelMap.addAttribute("user", user);
		    }
		    
		    // 多分组重新改写(完成)
		    String groupNamesWithComma = user.getGroup();
		    if (StringUtil.isBlank(groupNamesWithComma)) { groupNamesWithComma = "未分组"; }
		    
		    String[] groupNames = groupNamesWithComma.split(",");
		    for(String groupName : groupNames){
		    	if (map.containsKey(groupName)) {
			    	//已有分组成员，加入分组列表，用逗号加空格分隔
			        map.put(groupName, map.get(groupName) + ", " + user.getName());
			    } else {
			    	//创建新分组
			        map.put(groupName, user.getName());
			    }
		    }
		} 
		modelMap.addAttribute("map", map);
		
		String task_api = globalViewVariable.host + "task";
		String name = request.getParameter("name");
		String path = request.getParameter("path");
		String appname = request.getParameter("appname");
		
		if (StringUtil.isNotBlank(name)) {
		    task_api = task_api + "?name=" + name;
		} else if (StringUtil.isNotBlank(appname)) {
		    task_api = task_api + "?appname=" + appname;
		} else if (StringUtil.isNotBlank(globalViewVariable.currentUser)) {
		    task_api = task_api + "?user=" + globalViewVariable.currentUser;
		}
		
		globalViewVariable.cr = new ClientResource(task_api);
		ArrayList<TaskDTO> tasks = globalViewVariable.cr.get(ArrayList.class);
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
		commonAttr(modelMap);
		
		if(globalViewVariable.isAdmin == false) { 
			return "/error.ftl";
		}
	    
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
		commonAttr(modelMap);
	    
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
		commonAttr(modelMap);
		
		String alert_admin = ConfigHolder.get(LionKeys.ADMIN_USER);
		modelMap.addAttribute("alert_admin", alert_admin);
	    
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
		commonAttr(modelMap);
	    
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
		commonAttr(modelMap);
	    
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
		commonAttr(modelMap);
	    
	    return "/error.ftl";
	}
	
	/**
	 * freemarker帮助类
	 * @author chenchongze
	 *
	 */
	public class HomeHelper{
		
		public HostDTO getDtos(String host, String dtoName){
			
			ClientResource cr = new ClientResource(host + "host/" + dtoName);
			HostDTO dtos = cr.get(HostDTO.class);
			
			return dtos;
		}
		
		//resign任务交接页面取一个分组的用户数组
		public String[] getGroupUserList(String groupUsers){
			
			return groupUsers.split(",");
		}
	}
}
