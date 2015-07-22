package com.dp.bigdata.taurus.restlet.resource.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jodd.util.StringUtil;

import com.dp.bigdata.taurus.restlet.resource.IUserResource;
import com.dp.bigdata.taurus.restlet.shared.UserDTO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import org.springframework.beans.factory.annotation.Autowired;

import com.dp.bigdata.taurus.generated.mapper.UserGroupMapper;
import com.dp.bigdata.taurus.generated.mapper.UserGroupMappingMapper;
import com.dp.bigdata.taurus.generated.mapper.UserMapper;
import com.dp.bigdata.taurus.generated.module.User;
import com.dp.bigdata.taurus.generated.module.UserExample;
import com.dp.bigdata.taurus.generated.module.UserGroup;
import com.dp.bigdata.taurus.generated.module.UserGroupExample;
import com.dp.bigdata.taurus.generated.module.UserGroupMapping;
import com.dp.bigdata.taurus.generated.module.UserGroupMappingExample;

/**
 * UsersResource url : http://xxx/api/user/{user_name}
 * 
 * @author damon.zhu
 */
public class UserResource extends ServerResource implements IUserResource {
	
    private static final Log LOG = LogFactory.getLog(TaskResource.class);

	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private UserGroupMapper groupMapper;
	
	@Autowired
	private UserGroupMappingMapper mappingMapper;

	private static final String USER_ID="id";
	private static final String TEL="tel";
	private static final String EMAIL="email";
	private static final String GROUP="groupName";
	private static final String NAME="userName";
    private static final String QQ="qq";
    
    private final static int MAX_USERGROUP_NUM = 3;
	
	@Get
	@Override
	public UserDTO retrieve() {
		String userName = (String) getRequest().getAttributes().get("user_name");
		UserExample example = new UserExample();
		example.or().andNameEqualTo(userName);
		List<User> users = userMapper.selectByExample(example);
		
		UserDTO result = null;
		
		if(users.size() == 1){
			
			result = new UserDTO(users.get(0));
			UserGroupMappingExample mappingExample = new UserGroupMappingExample();
            mappingExample.or().andUseridEqualTo(result.getId());
            List<UserGroupMapping> userGroups = mappingMapper.selectByExample(mappingExample);
            
            if(userGroups.size() == 0){
            	result.setGroup("");
            } else {
            	StringBuilder userGroupNames = new StringBuilder();
            	for(UserGroupMapping userGroup : userGroups){
            		int groupId = userGroup.getGroupid();
            		UserGroup group = groupMapper.selectByPrimaryKey(groupId);
            		userGroupNames.append(group.getGroupname() + ",");
            	}
            	userGroupNames.deleteCharAt(userGroupNames.length()-1);
            	result.setGroup(userGroupNames.toString());
            }
            
		}
		
		return result;
		
	}

	@Post
	@Override
	public void update(Representation re) {
		try{ 
			updateInternal(re);
		} catch(Exception e){
			LOG.error(e,e);
			getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
		}
		
	}
	
	private void updateInternal(Representation re){
		
		Form form = new Form(re);
		Map<String, String> formMap = new HashMap<String, String>(form.getValuesMap());
		UserDTO user = new UserDTO();
		String groupName = "";
		
		if (re == null) {
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			return;
		}
		
		for (Entry<String, String> entry : formMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue() == null ? "" : entry.getValue().trim();
			if(key.equals(USER_ID)){
				user.setId(Integer.parseInt(value));
			} else if(key.equals(EMAIL)){
				user.setMail(value);
			} else if(key.equals(TEL)){
				user.setTel(value);
			} else if(key.equals(GROUP)){
				user.setGroup(value);
				groupName = value;
			} else if(key.equals(NAME)){
				user.setName(value);
			}else if(key.equals(QQ)){
                user.setQq(value);
            }

		}
		
		userMapper.updateByPrimaryKey(user.getUser());
		if(StringUtil.isNotBlank(groupName)){
			// TODO 用户多分组保存用户表，分组表，用户分组映射表(完成)
			String[] userGroups = groupName.split(",");
			List<Integer> groupIDs = new ArrayList<Integer>();
			for(String userGroup : userGroups){
				int groupID = addGroup(userGroup);
				groupIDs.add(groupID);
				//addUserGroupMapping(groupID,user.getId());
			}
			updateMultiUserGroupMapping(groupIDs, user.getId());
		}
	}
	//更新维护用户分组映射表（至多3个）
	private synchronized void updateMultiUserGroupMapping(List<Integer> groupIDs, int userID) {
		UserGroupMappingExample example = new UserGroupMappingExample();
		example.or().andUseridEqualTo(userID);
		List<UserGroupMapping> mappings = mappingMapper.selectByExample(example);
		int mappingSize = mappings.size();
		int groupIdSize = groupIDs.size();
		if(mappingSize <= MAX_USERGROUP_NUM && groupIdSize <= MAX_USERGROUP_NUM){
			if (mappingSize <= groupIdSize) {//数据库旧记录少于新记录
				int i = 0;
				for( ; i < mappingSize; ++i){//更新旧记录
					UserGroupMapping userGroup = new UserGroupMapping();
					userGroup.setId(mappings.get(i).getId());
					userGroup.setGroupid(groupIDs.get(i));
					userGroup.setUserid(userID);
					mappingMapper.updateByPrimaryKey(userGroup);
				}
				for( ; i < groupIdSize; ++i){//插入新记录
					UserGroupMapping userGroup = new UserGroupMapping();
					userGroup.setGroupid(groupIDs.get(i));
					userGroup.setUserid(userID);
					mappingMapper.insert(userGroup);
				}
			} else {//数据库旧记录多于新记录
				int i = 0;
				for( ; i < groupIdSize; ++i){//更新旧记录
					UserGroupMapping userGroup = new UserGroupMapping();
					userGroup.setId(mappings.get(i).getId());
					userGroup.setGroupid(groupIDs.get(i));
					userGroup.setUserid(userID);
					mappingMapper.updateByPrimaryKey(userGroup);
				}
				for( ; i < mappingSize; ++i){//删除多余记录
					mappingMapper.deleteByPrimaryKey(mappings.get(i).getId());
				}
			}
			
		} else {
			throw new RuntimeException("Found user " + userID + " belongs to more than " + MAX_USERGROUP_NUM + " groups");
		} 
	}
	
	// 原先只允许一个分组时用的方法
	@Deprecated
	private synchronized void addUserGroupMapping(int groupID, int userID) {
		UserGroupMappingExample example = new UserGroupMappingExample();
		example.or().andUseridEqualTo(userID);
		List<UserGroupMapping> mappings = mappingMapper.selectByExample(example);
		int size = mappings.size();
		if(size == 0){
			UserGroupMapping userGroup = new UserGroupMapping();
			userGroup.setGroupid(groupID);
			userGroup.setUserid(userID);
			mappingMapper.insert(userGroup);
		} else if(size == 1){
			UserGroupMapping userGroup = new UserGroupMapping();
			userGroup.setId(mappings.get(0).getId());
			userGroup.setGroupid(groupID);
			userGroup.setUserid(userID);
			mappingMapper.updateByPrimaryKey(userGroup);
		} else {
			throw new RuntimeException("Found user " + userID + " belongs to diffrent groups");
		} 
	}

	private synchronized int addGroup(String groupName){
		UserGroupExample groupExample = new UserGroupExample();
		groupExample.or().andGroupnameEqualTo(groupName);
		List<UserGroup> groups = groupMapper.selectByExample(groupExample);
		int size = groups.size();
		if(size == 0){
			UserGroup group = new UserGroup();
			group.setGroupname(groupName);
			groupMapper.insert(group);
			groups = groupMapper.selectByExample(groupExample);
			return groups.get(0).getId();
		} else if(size == 1){
			return groups.get(0).getId();
		} else {
			throw new RuntimeException("Found duplicated group name in table TaurusUserGroup");
		}
	}

}
