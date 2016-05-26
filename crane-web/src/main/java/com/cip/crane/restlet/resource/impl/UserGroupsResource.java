package com.cip.crane.restlet.resource.impl;

import java.util.ArrayList;
import java.util.List;

import com.cip.crane.generated.module.UserGroup;
import com.cip.crane.restlet.resource.IUserGroupsResource;
import com.cip.crane.restlet.shared.UserGroupDTO;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.springframework.beans.factory.annotation.Autowired;

import com.cip.crane.generated.mapper.UserGroupMapper;
import com.cip.crane.generated.module.UserGroupExample;

/**
 * UserGroupsResource url : http://xxx/api/group
 * 
 * @author damon.zhu
 */
public class UserGroupsResource extends ServerResource implements IUserGroupsResource {

    @Autowired
    private UserGroupMapper userGroupMapper;

    @Override
    @Get
    public ArrayList<UserGroupDTO> retrieve() {
        UserGroupExample example = new UserGroupExample();
        example.or();
        List<UserGroup> groups = userGroupMapper.selectByExample(example);
        ArrayList<UserGroupDTO> groupDtos = new ArrayList<UserGroupDTO>();
        for (UserGroup group : groups) {
            groupDtos.add(new UserGroupDTO(group.getId(), group.getGroupname()));
        }
        return groupDtos;
    }

}
