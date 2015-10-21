package com.dp.bigdata.taurus.springmvc.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dp.bigdata.taurus.generated.module.UserGroup;
import com.dp.bigdata.taurus.generated.module.UserGroupMapping;
import com.dp.bigdata.taurus.springmvc.bean.JqGridReqBean;
import com.dp.bigdata.taurus.springmvc.bean.JqGridReqFilters;
import com.dp.bigdata.taurus.springmvc.bean.JqGridRespBean;
import com.dp.bigdata.taurus.springmvc.service.UserGroupMappingService;
import com.dp.bigdata.taurus.springmvc.service.UserGroupService;
import com.dp.bigdata.taurus.springmvc.utils.GlobalViewVariable;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author chenchongze
 *
 */
@Controller
public class SCMController extends BaseController {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	UserGroupService userGroupService;
	@Autowired
	UserGroupMappingService userGroupMappingService;
	
	//UserGroup
	@RequestMapping(value = {"/usergroups"}, method = RequestMethod.GET)
	public String userGroupIndex(ModelMap modelMap,
			HttpServletRequest request, HttpServletResponse response) {
		GlobalViewVariable globalViewVariable = new GlobalViewVariable();
		commonnav(request,globalViewVariable);
		modelMap.addAttribute("currentUser", globalViewVariable.currentUser);
		modelMap.addAttribute("isAdmin", globalViewVariable.isAdmin);
		commonAttr(modelMap);
		
		if(globalViewVariable.isAdmin == false) { 
			return "/error.ftl";
		}
		
		return "/usergroups/index.ftl";
	}
	
	@RequestMapping(value = {"/usergroups.api"}, method = RequestMethod.POST)
	public void userGroupApi(@RequestParam(value="id") String id,
			@RequestParam(value="groupname", required=false) String groupname,
			@RequestParam(value="description", required=false) String description,
			@RequestParam(value="oper") String oper,
			HttpServletRequest request, HttpServletResponse response) {
		
		if("edit".equals(oper)){
			UserGroup userGroup = new UserGroup();
			userGroup.setId(Integer.parseInt(id));
			userGroup.setDescription(description);
			userGroup.setGroupname(groupname);
			userGroupService.updateById(userGroup);
			
		}else if("del".equals(oper)){
			userGroupService.deleteByIdSplitByComma(id);
		
		}else if("add".equals(oper)){
			//TODO
		
		}
		
	}
	
	@RequestMapping(value = {"/usergroups.json"}, method = RequestMethod.POST)
	@ResponseBody
	public JqGridRespBean userGroupJson(ModelMap modelMap, JqGridReqBean jqGridReqBean,
			HttpServletRequest request, HttpServletResponse response) {
		
		JqGridReqFilters filters = null;
		
		if(StringUtils.isNotBlank(jqGridReqBean.getFilters())){
			
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				filters = objectMapper.readValue(jqGridReqBean.getFilters(), JqGridReqFilters.class);
			} catch (JsonParseException e) {
				log.error("Parse criteria error!", e);
			} catch (JsonMappingException e) {
				log.error("Parse criteria error!", e);
			} catch (IOException e) {
				log.error("Parse criteria error!", e);
			}
		}
		
		JqGridRespBean jqGridTableBean;
		
		int page = jqGridReqBean.getPage();
		int rows = jqGridReqBean.getRows();
		
		if(page > 0 && rows > 0){
			jqGridTableBean = userGroupService.retrieveByJqGrid(page, rows);
		}else{
			jqGridTableBean = userGroupService.retrieveByJqGrid(1, 10);
		}
		
		return jqGridTableBean;
	}
	
	//UserGroupMapping
	@RequestMapping(value = {"/usergroupmappings"}, method = RequestMethod.GET)
	public String userGroupMappingIndex(ModelMap modelMap,
			HttpServletRequest request, HttpServletResponse response) {
		GlobalViewVariable globalViewVariable = new GlobalViewVariable();
		commonnav(request,globalViewVariable);
		modelMap.addAttribute("currentUser", globalViewVariable.currentUser);
		modelMap.addAttribute("isAdmin", globalViewVariable.isAdmin);
		commonAttr(modelMap);
		
		if(globalViewVariable.isAdmin == false) { 
			return "/error.ftl";
		}
		
		return "/usergroupmappings/index.ftl";
	}
	
	@RequestMapping(value = {"/usergroupmappings.api"}, method = RequestMethod.POST)
	public void userGroupMappingApi(@RequestParam(value="id") String id,
			@RequestParam(value="groupid", required=false) Integer groupid,
			@RequestParam(value="userid", required=false) Integer userid,
			@RequestParam(value="oper") String oper,
			HttpServletRequest request, HttpServletResponse response) {
		
		UserGroupMapping userGroupMapping = new UserGroupMapping();
		
		Integer iId = null;
		try {
			iId = Integer.parseInt(id);
		} catch (NumberFormatException e) {
			log.error("Can't parse id!", e);
		}
		
		userGroupMapping.setId(iId);
		userGroupMapping.setGroupid(groupid);
		userGroupMapping.setUserid(userid);
		
		if("edit".equals(oper)){
			
			userGroupMappingService.updateById(userGroupMapping);
			
		}else if("del".equals(oper)){
			userGroupMappingService.deleteByIdSplitByComma(id);
		
		}else if("add".equals(oper)){
			userGroupMappingService.create(userGroupMapping);
		}
		
	}
	
	@RequestMapping(value = {"/usergroupmappings.json"}, method = RequestMethod.POST)
	@ResponseBody
	public JqGridRespBean userGroupMappingJson(ModelMap modelMap, JqGridReqBean jqGridReqBean,
			HttpServletRequest request, HttpServletResponse response) {
		
		JqGridReqFilters filters = null;
		
		if(StringUtils.isNotBlank(jqGridReqBean.getFilters())){
			
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				filters = objectMapper.readValue(jqGridReqBean.getFilters(), JqGridReqFilters.class);
			} catch (JsonParseException e) {
				log.error("Parse criteria error!", e);
			} catch (JsonMappingException e) {
				log.error("Parse criteria error!", e);
			} catch (IOException e) {
				log.error("Parse criteria error!", e);
			}
		}
		
		JqGridRespBean jqGridTableBean;
		
		int page = jqGridReqBean.getPage();
		int rows = jqGridReqBean.getRows();
		
		if(page > 0 && rows > 0){
			jqGridTableBean = userGroupMappingService.retrieveByJqGrid(page, rows);
		}else{
			jqGridTableBean = userGroupMappingService.retrieveByJqGrid(1, 10);
		}
		
		return jqGridTableBean;
	}
	
}
