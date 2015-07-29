package com.dp.bigdata.taurus.restlet.resource;

import org.restlet.resource.Get;

public interface IHealthCheckResource {

	@Get
	public String healthCheck();
	
}
