package com.cip.crane.common.netty.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;

/**
 * Author   mingdongli
 * 16/5/18  下午4:20.
 */
public abstract class AbstractListener implements TreeCacheListener {

    @Override
    public final void childEvent(final CuratorFramework client, final TreeCacheEvent event) throws Exception {
        String path = null == event.getData() ? "" : event.getData().getPath();
        if (path.isEmpty()) {
            return;
        }
        dataChanged(client, event, path);
    }

    protected abstract void dataChanged(final CuratorFramework client, final TreeCacheEvent event, final String path);
}
