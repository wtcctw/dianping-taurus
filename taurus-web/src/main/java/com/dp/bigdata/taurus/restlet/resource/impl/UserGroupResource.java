package com.dp.bigdata.taurus.restlet.resource.impl;

import org.restlet.resource.ServerResource;
import org.springframework.beans.factory.annotation.Autowired;

import com.dp.bigdata.taurus.generated.mapper.UserGroupMapper;
import com.dp.bigdata.taurus.restlet.resource.IUserGroupResource;
import com.dp.bigdata.taurus.restlet.shared.UserDTO;

public class UserGroupResource extends ServerResource implements IUserGroupResource{

	@Autowired
    private UserGroupMapper userGroupMapper;
	
	@Override
	public boolean create(UserDTO userDTO) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public UserDTO read(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(UserDTO userDTO) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteById() {
		Integer id = (Integer) getRequest().getAttributes().get("group_id");
		int affectedRowsNumber = userGroupMapper.deleteByPrimaryKey(id);
		if(affectedRowsNumber > 0)
			return true;
		return false;
	}

}
