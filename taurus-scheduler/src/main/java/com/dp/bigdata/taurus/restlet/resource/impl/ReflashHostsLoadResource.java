package com.dp.bigdata.taurus.restlet.resource.impl;

import java.util.ArrayList;

import org.restlet.resource.ServerResource;

import com.dp.bigdata.taurus.restlet.resource.IReflashHostsLoadResource;
import com.dp.bigdata.taurus.restlet.shared.TaskDTO;
import com.dp.bigdata.taurus.restlet.utils.ReFlashHostLoadTask;

public class ReflashHostsLoadResource extends ServerResource implements IReflashHostsLoadResource {

	@Override
	public String readCachedHostsLoad() {
		return ReFlashHostLoadTask.hostLoadJsonData;
	}

	@Override
	public String reflashCachedHostsLoad() {
		return ReFlashHostLoadTask.read();
	}

	@Override
	public ArrayList<TaskDTO> reflashCachedTasks() {
		return ReFlashHostLoadTask.getTasks();
	}

	
}
