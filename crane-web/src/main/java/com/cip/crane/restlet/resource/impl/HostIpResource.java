package com.cip.crane.restlet.resource.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cip.crane.generated.module.Host;
import com.cip.crane.restlet.resource.IHostIpResource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.springframework.beans.factory.annotation.Autowired;

import com.cip.crane.generated.mapper.HostMapper;
import com.cip.crane.generated.module.HostExample;

/**
 * Resource url : http://xxx.xxx/api/hostip
 * 
 * @author renyuan.sun
 */
public class HostIpResource extends ServerResource implements IHostIpResource {
	@SuppressWarnings("unused")
	private static final Log LOGGER = LogFactory.getLog(HostIpResource.class);

	@Autowired
	private HostMapper hostMapper;

	@Override
	@Get
	public Representation retrieve() {
		HostExample example = new HostExample();
		Map<String,Object> result = new HashMap<String,Object>();
		example.createCriteria().andIsonlineEqualTo(true);
		List<Host> hosts = hostMapper.selectByExample(example);
		List<String> ips = new ArrayList<String>();
		for (Host host : hosts) {
			ips.add(host.getIp());
		}
		result.put("hosts",ips);
		return new JsonRepresentation(result);

	}
}
