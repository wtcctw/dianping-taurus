package com.cip.crane.springmvc.utils;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

public class MyFreeMarkerView extends FreeMarkerView {
	
	//private final static String SPRINGMVC_SERVLET_ROOTPATH_KEY = "mvc";
	 
    @Override
    protected void exposeHelpers(Map<String, Object> model, HttpServletRequest request) throws Exception {
    	
    	//model.put(SPRINGMVC_SERVLET_ROOTPATH_KEY,InitController.SPRINGMVC_SERVLET_ROOTPATH);
    	super.exposeHelpers(model, request);
    }

}
