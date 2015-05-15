package com.dp.bigdata.taurus.springmvc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dianping.cat.Cat;
import com.dianping.lion.EnvZooKeeperConfig;
import com.dianping.lion.client.ConfigCache;
import com.dianping.lion.client.LionException;
import com.dp.bigdata.taurus.springmvc.bean.WebResult;

@Controller
public class HomeController {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	//signin
	@RequestMapping(value = "/signin", method = RequestMethod.GET)
	public String signin(ModelMap modelMap, HttpServletRequest request,
			HttpServletResponse response) {
		log.info("--------------init the signin------------");
		String url = (String) request.getParameter("redirect-url");
	    if (url == null) {
	        url = "";
	    }
	    modelMap.addAttribute("url", url);
		return "/signin.ftl";
	}
	
	
	//lion test
	@RequestMapping(value = "/liontest/{user}", method = RequestMethod.GET)
	@ResponseBody
	public WebResult liontest(HttpServletRequest request,
			HttpServletResponse response, @PathVariable String user) {
		log.info("--------------init the liontest------------");
		WebResult result = new WebResult(request);
		response.setCharacterEncoding("UTF-8");
    	response.setHeader("Content-type", "text/html;charset=UTF-8");
    	
		String hiBase = "";
        try {
            hiBase = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("tena.say.hello");
        } catch (LionException e) {
            hiBase = "你好，%s 调用lion 异常了";
            //hiBasei1 = "你好，lison 调用lion 异常了";            
            Cat.logError("BpiAction init LionException", e);
        } catch (Exception e) {
            Cat.logError("BpiAction init Exception", e);
        }
        
//        log.info(hiBase);
//	        JSONObject jsonObject = new JSONObject();
        String hiStr = String.format(hiBase,user);
//	        try {
//	        	
//	            jsonObject.put("message", hiStr);
//	        } catch (JSONException e) {
//	            e.printStackTrace();
//	        }
        result.setStatus(201);
        result.setErrorMsg("测试数据");
        result.addAttr("testy", hiStr);
		return result;
	}
}
