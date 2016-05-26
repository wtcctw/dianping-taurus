package com.dp.bigdata.taurus.common.netty;

import com.dp.bigdata.taurus.common.netty.codec.NettyDecoder;
import com.dp.bigdata.taurus.common.netty.codec.NettyEncoder;
import com.dp.bigdata.taurus.common.netty.config.NettyClientConfig;
import com.dp.bigdata.taurus.common.netty.exception.RemotingSendRequestException;
import com.dp.bigdata.taurus.common.netty.protocol.Command;
import com.dp.bigdata.taurus.common.netty.remote.AbstractRemotingService;
import com.dp.bigdata.taurus.common.netty.remote.RemotingClient;
import com.dp.bigdata.taurus.common.netty.remote.RemotingHelper;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author   mingdongli
 * 16/5/18  上午10:14.
 */
public class NettyRemotingClient extends AbstractRemotingService implements RemotingClient {

    @Value("${task.executor.port}")
    public int sendPort = -1;

    private NettyClientConfig nettyClientConfig;

    private final Bootstrap bootstrap = new Bootstrap();

    private final ConcurrentMap<String, Channel> channels = new ConcurrentHashMap<String, Channel>();

    @Autowired
    public NettyRemotingClient(NettyClientConfig nettyClientConfig) {

        this.nettyClientConfig = nettyClientConfig;
        eventLoopGroup = new NioEventLoopGroup(1, new ThreadFactory() {
            private AtomicInteger threadIndex = new AtomicInteger(0);

            public Thread newThread(Runnable r) {
                return new Thread(r, String.format("NettyClientSelector_%d", this.threadIndex.incrementAndGet()));
            }
        });
    }

    public boolean send(String address, int port, final Command command) throws
            RemotingSendRequestException {
        Channel channel = getChannel(address, port);
        if (channel == null) {
            logger.error("Please check  specified target address. If the address is ok, check " +
                    "network.");
            throw new RemotingSendRequestException("Network encounter error!");
        }
        try {
            ChannelFuture future = channel.writeAndFlush(command).await();
            if (future.isSuccess()) {
                logger.info("Command : {} Send successfully.", command);
                return true;
            } else {
                logger.info("Command : {} Send failed.", command);
                logger.info("Failed caused by :", future.cause());
                throw new RemotingSendRequestException("Send command: + " + command + ",to " +
                        "address:" + address + "failed.");
            }
        } catch (Exception e) {
            logger.error("Send command {} to address {} encounter error.", command, address);
            throw new RemotingSendRequestException("Send command: + " + command + ",to " +
                    "address:" + address + "encounter error.", e);
        }

    }

    @Override
    public boolean send(String address, final Command command) throws RemotingSendRequestException {
        Channel channel = getChannel(address);
        if (channel == null) {
            logger.error("Please check  specified target address. If the address is ok, check " +
                    "network.");
            throw new RemotingSendRequestException("Network encounter error!");
        }
        try {
            ChannelFuture future = channel.writeAndFlush(command).await();
            if (future.isSuccess()) {
                logger.info("Command : {} Send successfully.", command);
                return true;
            } else {
                logger.info("Command : {} Send failed.", command);
                logger.info("Failed caused by :", future.cause());
                throw new RemotingSendRequestException("Send command: + " + command + ",to " +
                        "address:" + address + "failed.");
            }
        } catch (Exception e) {
            logger.error("Send command {} to address {} encounter error.", command, address);
            throw new RemotingSendRequestException("Send command: + " + command + ",to " +
                    "address:" + address + "encounter error.", e);
        }
    }

    @PostConstruct
    public void start() {

        super.start();
        if (sendPort > 0) {
            this.nettyClientConfig.setConnectPort(sendPort);
        }

        this.bootstrap.group(this.eventLoopGroup).channel(NioSocketChannel.class)//
                //
                .option(ChannelOption.TCP_NODELAY, true)
                        //
                .option(ChannelOption.SO_SNDBUF, nettyClientConfig.getSocketSndbufSize())
                        //
                .option(ChannelOption.SO_RCVBUF, nettyClientConfig.getSocketSndbufSize())
                        //
                .handler(new ChannelInitializer<SocketChannel>() {
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(//
                                defaultEventExecutorGroup, //
                                new NettyEncoder(), //
                                new NettyDecoder(), //
                                new NettyConnectManageHandler());
                    }
                });
    }

    @Override
    protected int getThreadCount() {
        return nettyClientConfig.getClientWorkerThreads();
    }

    class NettyConnectManageHandler extends ChannelDuplexHandler {

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            final String remoteAddress = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
            logger.warn("NETTY SERVER PIPELINE: exceptionCaught {}", remoteAddress);
            logger.warn("NETTY SERVER PIPELINE: exceptionCaught exception.", cause);

            ctx.channel().close();
        }
    }

    @PreDestroy
    public void shutdown() {
        logger.info("Shutdown netty remoting client...");
        try {
            for (Channel cw : this.channels.values()) {
                cw.close();
            }
            this.channels.clear();
            this.eventLoopGroup.shutdownGracefully();

            if (this.defaultEventExecutorGroup != null) {
                this.defaultEventExecutorGroup.shutdownGracefully();
            }
        } catch (Exception e) {
            logger.error("NettyRemotingClient shutdown exception, ", e);
        }


    }

    public Channel getChannel(String address, int port) {
        if (address == null) {
            return null;
        }
        Channel id = channels.get(address + ":" + port);
        if (id == null || !id.isActive()) {
            return createNewChannel(address, port);
        }
        return id;
    }


    public Channel getChannel(String address) {
        return getChannel(address, this.nettyClientConfig.getConnectPort());
    }

    //TODO 需要枷锁吗？
    private Channel createNewChannel(String address, int port) {
        ChannelFuture future;
        try {
            future = bootstrap.connect(new InetSocketAddress(address, port)).await();
        } catch (Exception e) {
            logger.info("Connect to TargetServer encounter error.", e);
            return null;
        }
        if (future.isSuccess()) {
            Channel channel = future.channel();
            channels.put(address + ":" + port, channel);
            return channel;
        } else {
            logger.error("Connect to TargetServer failed.", future.cause());
            return null;
        }
    }
}
