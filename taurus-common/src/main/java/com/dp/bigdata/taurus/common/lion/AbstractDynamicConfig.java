package com.dp.bigdata.taurus.common.lion;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Author   mingdongli
 * 16/4/21  下午06:18.
 */
public abstract class AbstractDynamicConfig implements DynamicConfig{

    protected final Log logger = LogFactory.getLog(getClass());

    private Map<ConfigChangeListener, Object> listeners = new HashMap<ConfigChangeListener, Object>();

    @Override
    public synchronized void addConfigChangeListener(final ConfigChangeListener listener) {

        if (listeners.get(listener) != null) {
            throw new IllegalArgumentException("[addConfigChangeListener][already add]" + listener);
        }

        Object o = doAddConfigChangeListener(listener);
        listeners.put(listener, o);

    }

    protected abstract Object doAddConfigChangeListener(ConfigChangeListener listener);

    @Override
    public synchronized void removeConfigChangeListener(ConfigChangeListener listener) {

        Object change = listeners.get(listener);

        doRemoveConfigChangeListener(change);

    }

    protected abstract void doRemoveConfigChangeListener(Object change);
}
