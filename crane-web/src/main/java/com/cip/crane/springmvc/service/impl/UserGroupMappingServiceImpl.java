package com.cip.crane.springmvc.service.impl;

import java.util.List;

import com.cip.crane.generated.module.UserGroupMappingExample;
import com.cip.crane.springmvc.bean.JqGridRespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cip.crane.generated.mapper.UserGroupMappingMapper;
import com.cip.crane.generated.module.UserGroup;
import com.cip.crane.generated.module.UserGroupMapping;
import com.cip.crane.springmvc.service.UserGroupMappingService;

/**
 * @author chenchongze
 *
 */
@Service
public class UserGroupMappingServiceImpl implements UserGroupMappingService {

	@Autowired
	private UserGroupMappingMapper userGroupMappingMapper;
	
	@Override
	public JqGridRespBean retrieveByJqGrid(int page, int rows) {
		JqGridRespBean jqGridTableBean = null;
		
		if(page > 0 && rows > 0){
			List<UserGroup> userGroups = userGroupMappingMapper.selectByPageAndRows((page - 1) * rows, rows);
			int totalRecords = userGroupMappingMapper.countByExample(null);
			int totalPages = (totalRecords - 1) / rows + 1;
			
			jqGridTableBean = new JqGridRespBean();
			jqGridTableBean.setData(userGroups);
			jqGridTableBean.setCurrentPage(page);
			jqGridTableBean.setTotalRecords(totalRecords);
			jqGridTableBean.setTotalPages(totalPages);
		}
		
		return jqGridTableBean;
	}

	@Override
	public int deleteByIdSplitByComma(String idsComma) {
		int sqlSucCount = 0;
		String idsArr[] = idsComma.split(",");
		
		for(String ids : idsArr){
			int id;
			int count = 0;
			
			try {
				id = Integer.parseInt(ids);
				count = userGroupMappingMapper.deleteByPrimaryKey(id);
				
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}finally{
				sqlSucCount += count;
			}
			
		}
		
		return sqlSucCount;
	}

	@Override
	public int updateById(UserGroupMapping userGroupMapping) {
		int sqlSucCount = -1;
		
		if(userGroupMapping.getId() != null) {
			sqlSucCount = userGroupMappingMapper.updateByPrimaryKeySelective(userGroupMapping);
		}
		
		return sqlSucCount;
	}

	@Override
	public int create(UserGroupMapping userGroupMapping) {
		int sqlSucCount = -1;
		
		Integer groupId = userGroupMapping.getGroupid();
		Integer userId = userGroupMapping.getUserid();
		
		if(groupId != null || userId != null) {
			
			UserGroupMappingExample example = new UserGroupMappingExample();
			example.createCriteria().andGroupidEqualTo(groupId).andUseridEqualTo(userId);
			List<UserGroupMapping> lists = userGroupMappingMapper.selectByExample(example);
			
			if(lists.size() > 0) {
				sqlSucCount = 0;
			}else {
				sqlSucCount = userGroupMappingMapper.insertSelective(userGroupMapping);
			}
		}
		
		return sqlSucCount;
	}

}
