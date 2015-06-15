package com.dp.bigdata.taurus.springmvc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/dynamic/js")
public class JsController {

	@RequestMapping(value = "/login.js", method = RequestMethod.GET)
	public String login(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
		return "/js/login.ftl";
	}
	
	@RequestMapping(value = "/monitor_center.js", method = RequestMethod.GET)
	public String monitor_center(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
		return "/js/monitor_center.ftl";
	}
	
}
