package com.cip.crane.common.netty.remote;

import com.cip.crane.common.netty.exception.RemotingSendRequestException;
import com.cip.crane.common.netty.protocol.Command;

/**
 * Author   mingdongli
 * 16/5/18  上午10:14.
 */
public interface RemotingClient extends RemotingService{

    boolean send(String address, Command command) throws RemotingSendRequestException;
}
