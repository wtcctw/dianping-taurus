package com.dp.bigdata.taurus.restlet.resource.impl;

import java.util.ArrayList;
import java.util.List;

import com.dp.bigdata.taurus.restlet.resource.IUsersResource;
import com.dp.bigdata.taurus.restlet.shared.UserDTO;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.springframework.beans.factory.annotation.Autowired;

import com.dp.bigdata.taurus.generated.mapper.UserGroupMapper;
import com.dp.bigdata.taurus.generated.mapper.UserGroupMappingMapper;
import com.dp.bigdata.taurus.generated.mapper.UserMapper;
import com.dp.bigdata.taurus.generated.module.User;
import com.dp.bigdata.taurus.generated.module.UserExample;
import com.dp.bigdata.taurus.generated.module.UserGroup;
import com.dp.bigdata.taurus.generated.module.UserGroupMapping;
import com.dp.bigdata.taurus.generated.module.UserGroupMappingExample;

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
            	//得到第一个分组的ID，用户多分组接入的基础，用for循环取分组逗号分隔
            	int groupId = userGroups.get(0).getGroupid();
            	UserGroup group = userGroupMapper.selectByPrimaryKey(groupId);
                userDto.setGroup(group.getGroupname());
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
