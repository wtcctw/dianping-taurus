package com.dp.bigdata.taurus.springmvc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.dp.bigdata.taurus.generated.mapper.UserGroupMapper;
import com.dp.bigdata.taurus.springmvc.service.ITestService;

/**
 * 
 * @author chenchongze
 *
 */
@Service
public class TestService implements ITestService {

//	@Autowired
//    private UserGroupMapper userGroupMapper;

	@Override
	public Integer deleteById(Integer id) {
		
		return null;//userGroupMapper.deleteByPrimaryKey(id);
	}
	
	
}
