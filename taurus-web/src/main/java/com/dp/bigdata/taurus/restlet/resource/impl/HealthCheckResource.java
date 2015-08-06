package com.dp.bigdata.taurus.restlet.resource.impl;

import org.restlet.resource.ServerResource;

import com.dp.bigdata.taurus.restlet.resource.IHealthCheckResource;

public class HealthCheckResource extends ServerResource implements IHealthCheckResource {

	@Override
	public String healthCheck() {
		return "ok";
	}
	
}
