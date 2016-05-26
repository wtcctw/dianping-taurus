package com.cip.crane.common.netty.zookeeper;

import org.apache.zookeeper.KeeperException;

import java.io.IOException;

/**
 * Author   mingdongli
 * 16/5/18  下午3:39.
 */
public interface RegistryCenter {

    /**
     * 初始化注册中心.
     */
    void init();

    /**
     * 关闭注册中心.
     */
    void close();

    /**
     * 获取注册数据.
     *
     * @param key 键
     * @return 值
     */
    String get(String key) throws InterruptedException, IOException, KeeperException;

    /**
     * 获取数据是否存在.
     *
     * @param key 键
     * @return 数据是否存在
     */
    boolean isExisted(String key) throws InterruptedException, IOException, KeeperException;

    /**
     * 持久化注册数据.
     *
     * @param key 键
     * @param value 值
     */
    void persist(String key, String value) throws Exception;

    /**
     * 更新注册数据.
     *
     * @param key 键
     * @param value 值
     */
    void update(String key, String value) throws InterruptedException, IOException, KeeperException;

    /**
     * 删除注册数据.
     *
     * @param key 键
     */
    void remove(String key);

    /**
     * 获取注册中心当前时间.
     *
     * @param key 用于获取时间的键
     * @return 注册中心当前时间
     */
    long getRegistryCenterTime(String key);

    /**
     * 直接获取操作注册中心的原生客户端.
     * 如：Zookeeper或Redis等原生客户端.
     *
     * @return 注册中心的原生客户端
     */
    Object getRawClient();
}
