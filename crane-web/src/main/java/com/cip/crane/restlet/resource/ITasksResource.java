package com.cip.crane.restlet.resource;

import com.cip.crane.restlet.shared.TaskDTO;
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
	public void createOrUpdate(TaskDTO taskDTO);

}
