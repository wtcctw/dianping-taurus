package com.dp.bigdata.taurus.restlet.resource;

import com.dp.bigdata.taurus.restlet.shared.TaskDTO;
import com.dp.bigdata.taurus.springmvc.controller.api.APIController;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;

import java.util.ArrayList;

/**
 * 
 * ITasksResource
 * @author damon.zhu
 *
 */
public interface ITasksResource {
	
	@Get
	public ArrayList<TaskDTO> retrieve();
	
	@Post
	public void create(Representation re);

	@Put
	public void createOrUpdate(APIController.TaskDTOWrapper taskDTOWrapper);

}
