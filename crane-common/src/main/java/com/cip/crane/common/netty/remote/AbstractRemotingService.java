package com.cip.crane.common.netty.remote;

import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author   mingdongli
 * 16/5/20  上午10:24.
 */
public abstract class AbstractRemotingService implements RemotingService{

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected EventLoopGroup eventLoopGroup;

    protected DefaultEventExecutorGroup defaultEventExecutorGroup;

    @Override
    public void start() {

        this.defaultEventExecutorGroup = new DefaultEventExecutorGroup(//
                getThreadCount(), new ThreadFactory() {

                    private AtomicInteger threadIndex = new AtomicInteger(0);


                    public Thread newThread(Runnable r) {
                        return new Thread(r, "NettyClientWorkerThread_" + this.threadIndex.incrementAndGet());
                    }
                });
    }

    protected abstract int getThreadCount();

}
