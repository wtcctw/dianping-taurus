package com.dp.bigdata.taurus.common.netty.zookeeper;

import org.apache.zookeeper.KeeperException;

import java.io.IOException;
import java.util.List;

/**
 * Author   mingdongli
 * 16/5/18  下午3:39.
 */
public interface CoordinatorRegistryCenter extends RegistryCenter {

    /**
     * 直接从注册中心而非本地缓存获取数据.
     *
     * @param key 键
     * @return 值
     */
    String getDirectly(String key) throws InterruptedException, IOException, KeeperException;

    /**
     * 获取子节点名称集合.
     *
     * @param key 键
     * @return 子节点名称集合
     */
    List<String> getChildrenKeys(String key) throws InterruptedException, IOException, KeeperException;

    /**
     * 持久化临时注册数据.
     *
     * @param key 键
     * @param value 值
     */
    void persistEphemeral(String key, String value);


    /**
     * 持久化临时节点数据
     * @param key
     * @param value
     * @param overwrite  如果临时节点存在是否覆盖
     */
    void persistEphemeral(String key, String value, boolean overwrite);

    /**
     * 持久化临时顺序注册数据.
     *
     * @param key
     */
    void persistEphemeralSequential(String key);

    /**
     * 获取注册中心数据缓存对象.
     *
     * @return 注册中心数据缓存对象
     */
    Object getRawCache();
}
