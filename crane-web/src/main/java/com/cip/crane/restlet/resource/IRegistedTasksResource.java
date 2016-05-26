package com.cip.crane.restlet.resource;

import java.util.Map;

import org.restlet.resource.Get;

import com.cip.crane.restlet.shared.TaskDTO;

public interface IRegistedTasksResource {

	@Get
	public Map<String, TaskDTO> retrieve();
	
}
