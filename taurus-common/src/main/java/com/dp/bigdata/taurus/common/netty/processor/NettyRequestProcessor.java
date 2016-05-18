package com.dp.bigdata.taurus.common.netty.processor;

import com.dp.bigdata.taurus.common.netty.protocol.Command;
import io.netty.channel.ChannelHandlerContext;

/**
 * Author   mingdongli
 * 16/5/18  下午2:37.
 */
public interface NettyRequestProcessor {

    void processRequest(ChannelHandlerContext ctx, Command request) throws Exception;
}
