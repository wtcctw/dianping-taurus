package com.dp.bigdata.taurus.restlet.resource;

import java.util.ArrayList;

import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;

import com.dp.bigdata.taurus.restlet.shared.TaskDTO;

public interface IReflashHostsLoadResource {

	@Get
	public String readCachedHostsLoad();
	
	@Post
	public String reflashCachedHostsLoad();
	
	@Put
	public ArrayList<TaskDTO> reflashCachedTasks();
}
