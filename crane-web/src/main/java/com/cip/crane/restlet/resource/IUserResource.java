package com.cip.crane.restlet.resource;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import com.cip.crane.restlet.shared.UserDTO;

public interface IUserResource {
	@Get
	public UserDTO retrieve();

	@Post
	public void update(Representation re);

}
