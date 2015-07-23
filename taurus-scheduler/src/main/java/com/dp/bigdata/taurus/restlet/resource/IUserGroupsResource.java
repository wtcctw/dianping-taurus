package com.dp.bigdata.taurus.restlet.resource;

import java.util.ArrayList;

import com.dp.bigdata.taurus.restlet.shared.UserGroupDTO;
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
