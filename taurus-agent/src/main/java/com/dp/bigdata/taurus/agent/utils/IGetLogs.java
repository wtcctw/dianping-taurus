package com.dp.bigdata.taurus.agent.utils;

import org.restlet.resource.Get;

import java.io.IOException;

/**
 * Created by mkirin on 14-8-5.
 */
public interface IGetLogs {
    public  String getLogs(String date, String attemptID, String fileOffset,String flag , String queryType) throws IOException;


}
