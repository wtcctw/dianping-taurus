package com.dp.bigdata.taurus.restlet.resource.impl;

import com.dp.bigdata.taurus.generated.mapper.HostMapper;
import com.dp.bigdata.taurus.generated.module.Host;
import com.dp.bigdata.taurus.restlet.resource.IAllHosts;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.ServerResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by kirinli on 15/1/30.
 */
public class AllHosts extends ServerResource implements IAllHosts {
        @Autowired
        private HostMapper hostMapper;

        @Override
        public String retrieve() {
            ArrayList<Host> allHosts =hostMapper.getAllOnlineHosts();
            Map<String, Object> result = new HashMap<String, Object>();

            if (allHosts == null || allHosts.size() == 0){
                result.put("hosts", "");
            }else {
                List<String> ips = new ArrayList<String>();
                for (int i = 0; i < allHosts.size(); i++){
                    Host host = allHosts.get(i);
                    ips.add(host.getIp());

                }
                Collections.shuffle(ips);
                result.put("hosts", ips);

            }
            JsonRepresentation rsp = new JsonRepresentation(result);
            try {
                JSONObject resJson =  rsp.getJsonObject();
                return  resJson.toString();
            } catch (JSONException e) {
                return "null";
            }

        }
}
