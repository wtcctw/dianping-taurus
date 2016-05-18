package com.dp.bigdata.taurus.common.netty.codec;

import com.dp.bigdata.taurus.common.netty.protocol.Command;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * Author   mingdongli
 * 16/5/18  上午10:21.
 */
public class NettyEncoder extends MessageToByteEncoder<Command> {

    private static final Logger logger = LoggerFactory.getLogger(NettyEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Command remotingCommand, ByteBuf byteBuf) throws Exception {
        try {
            // 1> header length size
            int length = 4;

            // 2> header data length
            byte[] headerData = RemotingSerializable.encode(remotingCommand);
            length += headerData.length;

            ByteBuffer result = ByteBuffer.allocate(length);

            // length
            result.putInt(length - 4);

            // header data
            result.put(headerData);

            result.flip();

            byteBuf.writeBytes(result);

        } catch (Exception e) {
            logger.error("Netty Encoder encounter error.", e);
        }
    }
}
