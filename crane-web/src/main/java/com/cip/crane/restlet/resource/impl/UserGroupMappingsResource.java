package com.cip.crane.restlet.resource.impl;

import java.util.ArrayList;
import java.util.List;

import com.cip.crane.restlet.resource.IUserGroupMappingsResource;
import com.cip.crane.restlet.shared.UserGroupMappingDTO;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.springframework.beans.factory.annotation.Autowired;

import com.cip.crane.generated.mapper.UserGroupMappingMapper;
import com.cip.crane.generated.module.UserGroupMapping;
import com.cip.crane.generated.module.UserGroupMappingExample;

/**
 * UserGroupMappingsResource url : http://xxx/api/usergroup
 * 
 * @author renyuan.sun
 */
public class UserGroupMappingsResource extends ServerResource implements IUserGroupMappingsResource {
	@Autowired
	private UserGroupMappingMapper userGroupMappingMapper;

	@Override
	@Get
	public ArrayList<UserGroupMappingDTO> retrieve() {
		UserGroupMappingExample example = new UserGroupMappingExample();
		example.or();
		List<UserGroupMapping> userGroups = userGroupMappingMapper.selectByExample(example);
		ArrayList<UserGroupMappingDTO> userGroupDtos = new ArrayList<UserGroupMappingDTO>();
		for (UserGroupMapping userGroup : userGroups) {
			UserGroupMappingDTO userGroupDto = new UserGroupMappingDTO();
			userGroupDto.setId(userGroup.getId());
			userGroupDto.setUserId(userGroup.getUserid());
			userGroupDto.setGroupId(userGroup.getGroupid());
			userGroupDtos.add(userGroupDto);
		}
		return userGroupDtos;
	}
}
