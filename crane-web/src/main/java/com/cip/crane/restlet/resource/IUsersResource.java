package com.cip.crane.restlet.resource;

import java.util.ArrayList;

import com.cip.crane.restlet.shared.UserDTO;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

/**
 * IUersResource
 * 
 * @author damon.zhu
 */
public interface IUsersResource {

    @Get
    public ArrayList<UserDTO> retrieve();
    
    @Post
    public void createIfNotExist(UserDTO user);

}
