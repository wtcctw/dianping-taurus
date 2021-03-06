package com.cip.crane.restlet.resource.impl;

import com.cip.crane.generated.module.UserGroup;
import com.cip.crane.restlet.resource.IUserGroupResource;
import com.cip.crane.restlet.shared.UserGroupDTO;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.springframework.beans.factory.annotation.Autowired;

import com.cip.crane.generated.mapper.UserGroupMapper;

public class UserGroupResource extends ServerResource implements IUserGroupResource {

	@Autowired
    private UserGroupMapper userGroupMapper;
	
	@Override
	public boolean create(UserGroupDTO userGroupDTO) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@Get
	public UserGroupDTO read(int id) {
		UserGroup userGroup = userGroupMapper.selectByPrimaryKey(id);
		UserGroupDTO userGroupDTO = new UserGroupDTO(userGroup.getId(), userGroup.getGroupname());
		return userGroupDTO;
	}

	@Override
	public boolean update(UserGroupDTO userGroupDTO) {
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
