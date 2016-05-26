package com.cip.crane.common.netty.config;

/**
 * Author   mingdongli
 * 16/5/18  下午2:35.
 */
public class NettyServerConfig {

    //监听端口
    private int listenPort = 8888;
    //
    private int serverWorkerThreads = 32;

    private int serverSelectorThreads = 3;

    private int SocketSndbufSize = 65535;

    public int getListenPort() {
        return listenPort;
    }

    public void setListenPort(int listenPort) {
        this.listenPort = listenPort;
    }

    public int getServerWorkerThreads() {
        return serverWorkerThreads;
    }

    public void setServerWorkerThreads(int serverWorkerThreads) {
        this.serverWorkerThreads = serverWorkerThreads;
    }

    public int getServerSelectorThreads() {
        return serverSelectorThreads;
    }

    public void setServerSelectorThreads(int serverSelectorThreads) {
        this.serverSelectorThreads = serverSelectorThreads;
    }

    public int getSocketSndbufSize() {
        return SocketSndbufSize;
    }

    public void setSocketSndbufSize(int socketSndbufSize) {
        SocketSndbufSize = socketSndbufSize;
    }
}
