package com.cip.crane.restlet.resource.impl;

import java.util.ArrayList;
import java.util.List;

import com.cip.crane.generated.module.UserGroupMappingExample;
import com.cip.crane.restlet.resource.IUsersResource;
import com.cip.crane.restlet.shared.UserDTO;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.springframework.beans.factory.annotation.Autowired;

import com.cip.crane.generated.mapper.UserGroupMapper;
import com.cip.crane.generated.mapper.UserGroupMappingMapper;
import com.cip.crane.generated.mapper.UserMapper;
import com.cip.crane.generated.module.User;
import com.cip.crane.generated.module.UserExample;
import com.cip.crane.generated.module.UserGroup;
import com.cip.crane.generated.module.UserGroupMapping;

/**
 * UsersResource url : http://xxx/api/user
 * 
 * @author damon.zhu
 */
public class UsersResource extends ServerResource implements IUsersResource {

    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private UserGroupMapper userGroupMapper;
    
    @Autowired
    private UserGroupMappingMapper userGroupMappingMapper;

    @Override
    @Get
    public ArrayList<UserDTO> retrieve() {
        UserExample example = new UserExample();
        example.or();
        List<User> users = userMapper.selectByExample(example);
        ArrayList<UserDTO> userDtos = new ArrayList<UserDTO>();
        for (User user : users) {
        	UserDTO userDto = new UserDTO(user.getId(), user.getName(), user.getMail(), user.getTel(),user.getQq());
            userDtos.add(userDto);
            UserGroupMappingExample mappingExample = new UserGroupMappingExample();
            mappingExample.or().andUseridEqualTo(user.getId());
            List<UserGroupMapping> userGroups = userGroupMappingMapper.selectByExample(mappingExample);
            if(userGroups.size() == 0){
            	userDto.setGroup("");
            } else {
            	//TODO 用户多分组接入的基础，用for循环取分组逗号分隔（完成）
            	StringBuilder userGroupNames = new StringBuilder();
            	for(UserGroupMapping userGroup : userGroups){
            		int groupId = userGroup.getGroupid();
            		UserGroup group = userGroupMapper.selectByPrimaryKey(groupId);
            		userGroupNames.append(group.getGroupname() + ",");
            	}
            	userGroupNames.deleteCharAt(userGroupNames.length()-1);
            	userDto.setGroup(userGroupNames.toString());
            }
        }
        
        return userDtos;
    }

	@Override
	public void createIfNotExist(UserDTO user) {
		UserExample example = new UserExample();
		example.or().andNameEqualTo(user.getName());
		List<User> userDtos = userMapper.selectByExample(example);
		//找不到用户，新建用户（第一次使用taurus的用户）
		if(userDtos == null || userDtos.size() == 0){
			User usr = new User();
			usr.setName(user.getName());
			usr.setMail(user.getMail());
			
			userMapper.insertSelective(usr);
		}
	}

}
