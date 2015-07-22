package com.dp.bigdata.taurus.restlet.resource.impl;

import com.dp.bigdata.taurus.generated.mapper.HostMapper;
import com.dp.bigdata.taurus.generated.module.Host;
import com.dp.bigdata.taurus.restlet.resource.IExceptionHosts;
import org.restlet.resource.ServerResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

/**
 * Created by kirinli on 15/1/30.
 */
public class ExcpetionHosts extends ServerResource implements IExceptionHosts {
    @Autowired
    private HostMapper hostMapper;

    @Override
    public String retrieve() {
        ArrayList<Host> exceptionHosts =hostMapper.getAllExceptionHosts();
        if (exceptionHosts == null || exceptionHosts.size() == 0){
            return "";
        }else {
            StringBuffer hostsArray = new StringBuffer();
            for (int i = 0; i < exceptionHosts.size(); i++){
                Host host = exceptionHosts.get(i);
                if (i == exceptionHosts.size() - 1){
                    hostsArray.append(host.getIp());
                }else {
                    hostsArray.append(host.getIp());
                    hostsArray.append(",");
                }

            }
            return  hostsArray.toString();
        }
    }
}
