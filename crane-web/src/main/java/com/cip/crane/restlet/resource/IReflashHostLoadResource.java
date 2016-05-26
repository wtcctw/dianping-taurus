package com.cip.crane.restlet.resource;

import org.restlet.resource.Get;

public interface IReflashHostLoadResource {

	@Get
	public boolean isHostOverLoad();
	
}
