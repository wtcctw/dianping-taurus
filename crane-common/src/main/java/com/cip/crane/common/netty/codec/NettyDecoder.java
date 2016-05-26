package com.cip.crane.common.netty.codec;

import com.cip.crane.common.netty.protocol.CallBackTask;
import com.cip.crane.common.netty.protocol.ScheduleTask;
import com.cip.crane.common.netty.protocol.CommandType;
import com.cip.crane.common.netty.protocol.PeerTask;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * Author   mingdongli
 * 16/5/18  上午10:30.
 */
public class NettyDecoder extends LengthFieldBasedFrameDecoder {

    private static final Logger logger = LoggerFactory.getLogger(NettyDecoder.class);
    private static final int FRAME_MAX_LENGTH = Integer.parseInt(System.getProperty("com.meituan.mschedule.remoting" + ".frameMaxLength", "9437184"));

    private static final String CALLBACK_TASK = "runState";
    private static final String PEER_TASK = "leaderIP";

    public NettyDecoder() {
        super(FRAME_MAX_LENGTH, 0, 4, 0, 4);
    }


    @Override
    public Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = null;
        try {
            frame = (ByteBuf) super.decode(ctx, in);
            if (null == frame) {
                return null;
            }

            //length
            int dataLength = frame.capacity();
            ByteBuffer byteBuffer = frame.nioBuffer();

            //data
            byte[] headerData = new byte[dataLength];
            byteBuffer.get(headerData);

            String data = new String(headerData, "utf-8");
            logger.info("Netty Decoder received data : {}", data);

            //ugly!!!为了兼容老客户端,待客户端升级后，靠CommandType来区分
            if (data.contains(CALLBACK_TASK)) {
                CallBackTask callBackTask = RemotingSerializable.decode(headerData, CallBackTask.class);
                callBackTask.setType(CommandType.ScheduleReceiveResult);
                return callBackTask;
            } else if (data.contains(PEER_TASK)) {
                PeerTask peerTask = RemotingSerializable.decode(headerData, PeerTask.class);
                peerTask.setType(CommandType.TaskSendResult);
                return peerTask;
            } else {
                ScheduleTask scheduleTask = RemotingSerializable.decode(headerData, ScheduleTask.class);
                scheduleTask.setType(CommandType.ScheduleSendTask);
                return scheduleTask;
            }

        } catch (Exception e) {
            logger.error("Decode byte stream encounter error.", e);
            ctx.channel().close();
            e.printStackTrace();
        } finally {
            if (null != frame) {
                frame.release();
            }
        }

        return null;
    }
}

