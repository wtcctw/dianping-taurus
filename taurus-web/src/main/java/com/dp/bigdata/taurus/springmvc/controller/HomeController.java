package com.dp.bigdata.taurus.springmvc.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.plexus.util.StringUtils;
import org.restlet.data.MediaType;
import org.restlet.resource.ClientResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;

import com.dianping.cat.Cat;
import com.dianping.lion.EnvZooKeeperConfig;
import com.dianping.lion.client.ConfigCache;
import com.dianping.lion.client.LionException;
import com.dp.bigdata.taurus.restlet.resource.IAttemptStatusResource;
import com.dp.bigdata.taurus.restlet.resource.IHostsResource;
import com.dp.bigdata.taurus.restlet.resource.IPoolsResource;
import com.dp.bigdata.taurus.restlet.resource.ITaskResource;
import com.dp.bigdata.taurus.restlet.resource.IUserGroupsResource;
import com.dp.bigdata.taurus.restlet.resource.IUsersResource;
import com.dp.bigdata.taurus.restlet.shared.HostDTO;
import com.dp.bigdata.taurus.restlet.shared.PoolDTO;
import com.dp.bigdata.taurus.restlet.shared.StatusDTO;
import com.dp.bigdata.taurus.restlet.shared.TaskDTO;
import com.dp.bigdata.taurus.restlet.shared.UserDTO;
import com.dp.bigdata.taurus.restlet.shared.UserGroupDTO;
import com.dp.bigdata.taurus.springmvc.bean.WebResult;

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
	    //log.info(servletContext.getInitParameter("RESTLET_SERVER"));
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
		
		commonnav(modelMap,request,gvv);
		modelMap.addAttribute("currentUser", gvv.currentUser);
	    modelMap.addAttribute("isAdmin",gvv.isAdmin);
		
		Date time = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH");
        long hourTime = 60 * 60 * 1000;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        String stepStr = request.getParameter("step");
        String now = request.getParameter("date");
        String opStr = request.getParameter("op");
        if(opStr==null || opStr.isEmpty()){
        	opStr="day";
        }
        System.out.println(stepStr + "#" + now);
        int step = -24;
        
        if (now == null || now.isEmpty()) {
            now = df.format(time);
        }
        
        long nowLtime = df.parse(now).getTime();
        long dHour = 24*hourTime;
        long wHour = 7*dHour;
        long mHour = 30*dHour;
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
		commonnav(modelMap,request,gvv);
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
		
		commonnav(modelMap,request,gvv);
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
		commonnav(modelMap,request,gvv);
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
		commonnav(modelMap,request,gvv);
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
		commonnav(modelMap,request,gvv);
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
		commonnav(modelMap,request,gvv);
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
		commonnav(modelMap,request,gvv);
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
		commonnav(modelMap,request,gvv);
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
	 * 重构jsp/common-nav.jsp
	 * @param modelMap
	 * @param request
	 */
	private void commonnav(ModelMap modelMap, HttpServletRequest request,GlobalViewVariable gvv){
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

	//lion test
//	@RequestMapping(value = "/liontest/{user}", method = RequestMethod.GET)
//	@ResponseBody
//	public WebResult liontest(HttpServletRequest request,
//			HttpServletResponse response, @PathVariable String user) {
//		log.info("--------------init the liontest------------");
//		WebResult result = new WebResult(request);
//		response.setCharacterEncoding("UTF-8");
//    	response.setHeader("Content-type", "text/html;charset=UTF-8");
//    	
//		String hiBase = "";
//        try {
//            hiBase = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("tena.say.hello");
//        } catch (LionException e) {
//            hiBase = "你好，%s 调用lion 异常了";
//            Cat.logError("BpiAction init LionException", e);
//        } catch (Exception e) {
//            Cat.logError("BpiAction init Exception", e);
//        }
//        
//        String hiStr = String.format(hiBase,user);
//        result.setStatus(201);
//        result.setErrorMsg("测试数据");
//        result.addAttr("testy", hiStr);
//		return result;
//	}
}
