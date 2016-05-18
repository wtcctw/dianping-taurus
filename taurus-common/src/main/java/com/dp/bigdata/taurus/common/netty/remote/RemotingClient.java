package com.dp.bigdata.taurus.common.netty.remote;

import com.dp.bigdata.taurus.common.netty.exception.RemotingSendRequestException;
import com.dp.bigdata.taurus.common.netty.protocol.Command;

/**
 * Author   mingdongli
 * 16/5/18  上午10:14.
 */
public interface RemotingClient extends RemotingService{

    boolean send(String address, Command command) throws RemotingSendRequestException;
}
