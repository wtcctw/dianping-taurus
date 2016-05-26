package com.cip.crane.springmvc.service.impl;

import com.cip.crane.springmvc.service.ITestService;
import org.springframework.stereotype.Service;

//import com.cip.crane.generated.mapper.UserGroupMapper;


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
