package com.cip.crane.restlet.resource;

import java.util.ArrayList;

import com.cip.crane.restlet.shared.UserGroupDTO;
import org.restlet.resource.Get;

/**
 * IUserGroupsResource
 * 
 * @author damon.zhu
 */
public interface IUserGroupsResource {
    
    @Get
    public ArrayList<UserGroupDTO> retrieve();

}
