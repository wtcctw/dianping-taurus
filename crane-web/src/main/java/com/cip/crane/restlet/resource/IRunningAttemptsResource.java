package com.cip.crane.restlet.resource;

import org.restlet.resource.Get;

public interface IRunningAttemptsResource {

	@Get
	public String retrieve();
	
}
