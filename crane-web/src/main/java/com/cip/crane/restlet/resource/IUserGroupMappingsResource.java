package com.cip.crane.restlet.resource;

import java.util.ArrayList;

import com.cip.crane.restlet.shared.UserGroupMappingDTO;
import org.restlet.resource.Get;

public interface IUserGroupMappingsResource {

    @Get
    public ArrayList<UserGroupMappingDTO> retrieve();
}
