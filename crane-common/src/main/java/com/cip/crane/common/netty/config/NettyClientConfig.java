package com.cip.crane.common.netty.config;

/**
 * Author   mingdongli
 * 16/5/18  上午10:20.
 */
public class NettyClientConfig {

    private int SocketSndbufSize = 65535;

    private int clientWorkerThreads = 2;

    private int connectPort = 8383;

    public int getSocketSndbufSize() {
        return SocketSndbufSize;
    }

    public void setSocketSndbufSize(int socketSndbufSize) {
        SocketSndbufSize = socketSndbufSize;
    }

    public int getClientWorkerThreads() {
        return clientWorkerThreads;
    }

    public void setClientWorkerThreads(int clientWorkerThreads) {
        this.clientWorkerThreads = clientWorkerThreads;
    }

    public int getConnectPort() {
        return connectPort;
    }

    public void setConnectPort(int connectPort) {
        this.connectPort = connectPort;
    }

}
