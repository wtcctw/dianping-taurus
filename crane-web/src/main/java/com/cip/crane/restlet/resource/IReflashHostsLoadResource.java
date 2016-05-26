package com.cip.crane.restlet.resource;

import java.util.ArrayList;

import com.cip.crane.restlet.shared.TaskDTO;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;

public interface IReflashHostsLoadResource {

	@Get
	public String readCachedHostsLoad();
	
	@Post
	public String reflashCachedHostsLoad();
	
	@Put
	public ArrayList<TaskDTO> reflashCachedTasks();
}
