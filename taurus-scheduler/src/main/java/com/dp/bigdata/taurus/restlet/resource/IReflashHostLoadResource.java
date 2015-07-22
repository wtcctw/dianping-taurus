package com.dp.bigdata.taurus.restlet.resource;

import org.restlet.resource.Get;

public interface IReflashHostLoadResource {

	@Get
	public boolean isHostOverLoad();
	
}
