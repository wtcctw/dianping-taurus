package com.dp.bigdata.taurus.restlet.resource;

import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;

import com.dp.bigdata.taurus.restlet.shared.UserDTO;

public interface IUserGroupResource {

	@Post
	public boolean create(UserDTO userDTO);  
	
	@Get
	public UserDTO read(int id);

	@Put
	public boolean update(UserDTO userDTO);
	
	@Delete
	public boolean deleteById();
	
}
