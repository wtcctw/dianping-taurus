package com.dp.bigdata.taurus.springmvc.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dp.bigdata.taurus.generated.mapper.UserGroupMapper;
import com.dp.bigdata.taurus.generated.mapper.UserGroupMappingMapper;
import com.dp.bigdata.taurus.generated.module.UserGroup;
import com.dp.bigdata.taurus.generated.module.UserGroupExample;
import com.dp.bigdata.taurus.generated.module.UserGroupMapping;
import com.dp.bigdata.taurus.generated.module.UserGroupMappingExample;
import com.dp.bigdata.taurus.springmvc.bean.JqGridRespBean;
import com.dp.bigdata.taurus.springmvc.service.UserGroupService;

/**
 * @author chenchongze
 *
 */
@Service
public class UserGroupServiceImpl implements UserGroupService {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private UserGroupMapper userGroupMapper;
	@Autowired
	private UserGroupMappingMapper userGroupMappingMapper;
	
	@Override
	public List<UserGroup> retrieveByPageAndRows(int page, int rows) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JqGridRespBean retrieveByJqGrid(int page, int rows) {
		JqGridRespBean jqGridTableBean = null;
		
		if(page > 0 && rows > 0){
			List<UserGroup> userGroups = userGroupMapper.selectByPageAndRows((page - 1) * rows, rows);
			int totalRecords = userGroupMapper.countByExample(null);
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
				count = deleteById(id);
				
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}finally{
				sqlSucCount += count;
			}
			
		}
		
		return sqlSucCount;
	}

	@Override
	public int updateById(UserGroup userGroup) {
		int sqlSucCount = -1;
		
		if(userGroup.getId() != null) {
			sqlSucCount = userGroupMapper.updateByPrimaryKeySelective(userGroup);
		}
		
		return sqlSucCount;
	}

	@Override
	public int deleteById(Integer id) {
		int sqlSucCount = 0;
		
		//check whether there is usergroupmappings
		UserGroupMappingExample userGroupMappingExample = new UserGroupMappingExample();
		userGroupMappingExample.createCriteria().andGroupidEqualTo(id);
		List<UserGroupMapping> userGroupMappings = userGroupMappingMapper.selectByExample(userGroupMappingExample);
		
		if(userGroupMappings.size() > 0) {
			log.error("can't delete while there is user belongs to this group!");
		} else {
			UserGroupExample userGroupExample = new UserGroupExample();
			userGroupExample.createCriteria().andIdEqualTo(id);
			List<UserGroup> userGroups = userGroupMapper.selectByExample(userGroupExample);
			
			if(userGroups.size() > 0) {
				log.info("begin to delete userGroup: " + userGroups.get(0).toString());
				sqlSucCount = userGroupMapper.deleteByPrimaryKey(id);
			}
		}
		
		return sqlSucCount;
	}

}
