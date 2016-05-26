package com.cip.crane.restlet.resource.impl;

import com.cip.crane.restlet.resource.IHealthCheckResource;
import org.restlet.resource.ServerResource;

public class HealthCheckResource extends ServerResource implements IHealthCheckResource {

	@Override
	public String healthCheck() {
		return "ok";
	}
	
}
