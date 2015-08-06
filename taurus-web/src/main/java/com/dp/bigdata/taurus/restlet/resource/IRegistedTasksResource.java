package com.dp.bigdata.taurus.restlet.resource;

import java.util.Map;

import org.restlet.resource.Get;

import com.dp.bigdata.taurus.restlet.shared.TaskDTO;

public interface IRegistedTasksResource {

	@Get
	public Map<String, TaskDTO> retrieve();
	
}
