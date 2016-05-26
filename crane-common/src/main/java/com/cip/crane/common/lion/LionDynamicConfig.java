package com.cip.crane.common.lion;

import com.dianping.lion.client.ConfigCache;
import com.dianping.lion.client.ConfigChange;
import com.dianping.lion.client.LionException;

/**
 * Author   mingdongli
 * 16/4/21  下午06:20.
 */
public class LionDynamicConfig extends AbstractDynamicConfig{

    private ConfigCache cc = ConfigCache.getInstance();

    public LionDynamicConfig() {

    }

    @Override
    public String get(String key) {
        try {
            return cc.getProperty(key);
        } catch (LionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Object doAddConfigChangeListener(final ConfigChangeListener listener) {

        ConfigChange configChange = new ConfigChange() {

            @Override
            public void onChange(String key, String value) {
                try{
                    listener.onConfigChange(key, value);
                }catch(Exception e){
                    logger.error("[onChange]" + key + "," + value, e);
                }
            }
        };
        cc.addChange(configChange);

        return configChange;
    }

    @Override
    protected void doRemoveConfigChangeListener(Object change) {

        if (change != null) {
            cc.removeChange((ConfigChange) change);
        }
    }
}
