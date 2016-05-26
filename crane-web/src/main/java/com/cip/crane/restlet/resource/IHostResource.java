package com.cip.crane.restlet.resource;

import com.cip.crane.restlet.shared.HostDTO;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;

/**
 * 
 * IHostResource
 * 
 * @author damon.zhu
 * 
 */
public interface IHostResource {

	@Get
	public HostDTO retrieve();

	@Put
	public void update(HostDTO host);

	@Post
	public void operate(String op);
}
