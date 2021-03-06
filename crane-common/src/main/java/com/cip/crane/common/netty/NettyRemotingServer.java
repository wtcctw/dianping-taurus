package com.cip.crane.common.netty;

import com.cip.crane.common.netty.codec.NettyDecoder;
import com.cip.crane.common.netty.codec.NettyEncoder;
import com.cip.crane.common.netty.config.NettyServerConfig;
import com.cip.crane.common.netty.processor.NettyRequestProcessor;
import com.cip.crane.common.netty.protocol.Command;
import com.cip.crane.common.netty.protocol.CommandType;
import com.cip.crane.common.netty.remote.AbstractRemotingService;
import com.cip.crane.common.netty.remote.RemotingHelper;
import com.cip.crane.common.netty.remote.RemotingServer;
import com.cip.crane.common.utils.Pair;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author   mingdongli
 * 16/5/18  下午2:33.
 */
public class NettyRemotingServer extends AbstractRemotingService implements RemotingServer {

    @Value("${task.callback.port}")
    public int callbackPort = -1;

    private NettyServerConfig nettyServerConfig;

    private ServerBootstrap serverBootstrap = new ServerBootstrap();

    private EventLoopGroup eventLoopGroupWorker;

    private ExecutorService defaultService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    protected final HashMap<CommandType/* request code */, Pair<NettyRequestProcessor, ExecutorService>> processorTable = new HashMap<CommandType, Pair<NettyRequestProcessor, ExecutorService>>(64);

    @Autowired
    public NettyRemotingServer(final NettyServerConfig nettyServerConfig) {

        this.nettyServerConfig = nettyServerConfig;
        this.eventLoopGroup = new NioEventLoopGroup(1, new ThreadFactory() {
            private AtomicInteger threadIndex = new AtomicInteger(0);


            public Thread newThread(Runnable r) {
                return new Thread(r, String.format("NettyBossSelector_%d", this.threadIndex.incrementAndGet()));
            }
        });

        this.eventLoopGroupWorker = new NioEventLoopGroup(nettyServerConfig.getServerSelectorThreads(), new ThreadFactory() {
            private AtomicInteger threadIndex = new AtomicInteger(0);
            private int threadTotal = nettyServerConfig.getServerSelectorThreads();


            public Thread newThread(Runnable r) {
                return new Thread(r, String.format("NettyServerSelector_%d_%d", threadTotal, this.threadIndex.incrementAndGet()));
            }
        });

    }

    @PostConstruct
    public void start() {

        super.start();
        if (callbackPort > 0) {
            this.nettyServerConfig.setListenPort(callbackPort);
        }

        this.serverBootstrap.group(this.eventLoopGroup, this.eventLoopGroupWorker).channel(NioServerSocketChannel.class)
                //
                .option(ChannelOption.SO_BACKLOG, 1024)
                        //
                .option(ChannelOption.SO_REUSEADDR, true)
                        //
                .childOption(ChannelOption.TCP_NODELAY, true)
                        //
                .childOption(ChannelOption.SO_SNDBUF, nettyServerConfig.getSocketSndbufSize())
                        //
                .childOption(ChannelOption.SO_RCVBUF, nettyServerConfig.getSocketSndbufSize())

                .localAddress(new InetSocketAddress(this.nettyServerConfig.getListenPort())).childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(defaultEventExecutorGroup, //
                        new NettyEncoder(), //编码
                        new NettyDecoder(), //解码
                        new NettyServerHandler(), //业务处理
                        new NettyConnectManageHandler()); //异常处理
            }
        });

        try {
            this.serverBootstrap.bind().sync();
        } catch (InterruptedException e1) {
            throw new RuntimeException("this.serverBootstrap.bind().sync() InterruptedException", e1);
        }
    }

    public void registerProcessor(CommandType commandType, NettyRequestProcessor processor, ExecutorService executor) {
        ExecutorService executorThis = executor;
        if (null == executor) {
            executorThis = this.defaultService;
        }

        Pair<NettyRequestProcessor, ExecutorService> pair = new Pair<NettyRequestProcessor, ExecutorService>(processor, executorThis);
        this.processorTable.put(commandType, pair);

    }

    @Override
    protected int getThreadCount() {
        return nettyServerConfig.getServerWorkerThreads();
    }

    class NettyServerHandler extends SimpleChannelInboundHandler<Command> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Command msg) throws Exception {
            processMessageReceived(ctx, msg);
        }
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

    public void processMessageReceived(final ChannelHandlerContext ctx, final Command msg) throws Exception {
        CommandType commandType = msg.getType();
        final Pair<NettyRequestProcessor, ExecutorService> pair = processorTable.get(commandType);
        if (pair != null) {
            Runnable run = new Runnable() {
                public void run() {
                    try {
                        pair.getFirst().processRequest(ctx, msg);
                    } catch (Throwable e) {
                        logger.error("process request exception", e);
                        logger.error(msg.toString());
                    }
                }
            };

            try {
                // 这里需要做流控，要求线程池对应的队列必须是有大小限制的
                pair.getSecond().submit(run);
            } catch (RejectedExecutionException e) {
                logger.warn(RemotingHelper.parseChannelRemoteAddr(ctx.channel()) //
                        + ", too many requests and system thread pool busy, RejectedExecutionException " //
                        + pair.getSecond().toString() //
                        + " request code: " + msg);

            }


        } else {
            logger.warn("Command {} not support yet !!", msg);
        }
    }


    @PreDestroy
    public void shutdown() {
        logger.info("Shutdown netty remoting server....");
        try {
            this.eventLoopGroup.shutdownGracefully();
            this.eventLoopGroupWorker.shutdownGracefully();
            if (this.defaultEventExecutorGroup != null) {
                this.defaultEventExecutorGroup.shutdownGracefully();
            }
        } catch (Exception e) {
            logger.error("NettyRemotingServer shutdown exception, ", e);
        }

        if (this.defaultService != null) {
            try {
                this.defaultService.shutdown();
            } catch (Exception e) {
                logger.error("NettyRemotingServer shutdown exception, ", e);
            }
        }
    }

    public Pair<NettyRequestProcessor, ExecutorService> getProcessorByCommandType
            (CommandType commandType) {
        return processorTable.get(commandType);
    }
}
