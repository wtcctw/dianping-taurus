package com.dp.bigdata.taurus.restlet.resource;

import org.restlet.resource.Get;

public interface IRunningAttemptsResource {

	@Get
	public String retrieve();
	
}
