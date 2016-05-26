package com.cip.crane.jersey.rs;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cip.crane.generated.mapper.HostMapper;
import com.cip.crane.generated.module.Host;
import com.cip.crane.generated.module.HostExample;

@Component
@Path("/")
public class HelloResource {
	
	@Autowired
	private HostMapper hostMapper;
	
	@GET
	@Path("host")
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
		HostExample example = new HostExample();
		example.createCriteria().andIpEqualTo("192.168.78.42");
		
		List<Host> list = hostMapper.selectByExample(example);
		
		for(Host host : list){
			System.out.println("Host: " + host.getIp());
		}
		
        return list.get(0).getIp();
    }
	
}