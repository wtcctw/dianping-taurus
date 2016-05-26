package com.cip.crane.common.netty.remote;

import io.netty.channel.Channel;

import java.net.SocketAddress;

/**
 * Author   mingdongli
 * 16/5/18  上午10:34.
 */
public class RemotingHelper {

    public static String parseChannelRemoteAddr(final Channel channel) {
        if (null == channel) {
            return "";
        }
        final SocketAddress remote = channel.remoteAddress();
        final String addr = remote != null ? remote.toString() : "";

        if (addr.length() > 0) {
            int index = addr.lastIndexOf("/");
            if (index >= 0) {
                return addr.substring(index + 1);
            }

            return addr;
        }
        return "";
    }

}
