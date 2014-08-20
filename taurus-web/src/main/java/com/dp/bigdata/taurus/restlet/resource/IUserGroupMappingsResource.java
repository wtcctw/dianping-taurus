package com.dp.bigdata.taurus.restlet.resource;

import java.util.ArrayList;

import com.dp.bigdata.taurus.restlet.shared.UserGroupMappingDTO;
import org.restlet.resource.Get;

public interface IUserGroupMappingsResource {

    @Get
    public ArrayList<UserGroupMappingDTO> retrieve();
}
