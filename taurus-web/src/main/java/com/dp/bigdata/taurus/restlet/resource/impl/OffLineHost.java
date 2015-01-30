package com.dp.bigdata.taurus.restlet.resource.impl;

import com.dp.bigdata.taurus.generated.mapper.HostMapper;
import com.dp.bigdata.taurus.generated.mapper.TaskMapper;
import com.dp.bigdata.taurus.generated.module.Host;
import com.dp.bigdata.taurus.restlet.resource.IOffLineHost;
import com.google.gson.JsonObject;
import org.restlet.data.Status;
import org.restlet.resource.ServerResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

/**
 * Created by kirinli on 15/1/30.
 */
public class OffLineHost   extends ServerResource implements IOffLineHost {
    @Autowired
    private HostMapper hostMapper;

    @Autowired
    private TaskMapper taskMapper;
    @Override
    public String retrieve() {
        String hostname = (String) getRequestAttributes().get("hostname");
        Host host = hostMapper.selectByPrimaryKey(hostname);
        if (host == null) {
            JsonObject respJson = new JsonObject();
            respJson.addProperty("state","404");
            respJson.addProperty("message",hostname+" is not found!");
            return respJson.toString();
        }
        ArrayList<Host> tasks = taskMapper.getRealTasksByHost(hostname);
        if(tasks == null || tasks.size() == 0){
            host.setIp(hostname);
            host.setIsonline(false);
            hostMapper.updateByPrimaryKey(host);
            JsonObject respJson = new JsonObject();
            respJson.addProperty("state","200");
            respJson.addProperty("message",hostname+" is offline success!");
            return respJson.toString();
        }else {
            JsonObject respJson = new JsonObject();
            respJson.addProperty("state","400");
            respJson.addProperty("message",hostname+" has many jobs on taurus!");
            return respJson.toString();
        }


    }
}
