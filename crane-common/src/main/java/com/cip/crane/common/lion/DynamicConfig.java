package com.cip.crane.common.lion;

/**
 * Author   mingdongli
 * 16/4/21  下午06:15.
 */
public interface DynamicConfig {

    String get(String key);

    void addConfigChangeListener(ConfigChangeListener listener);

    void removeConfigChangeListener(ConfigChangeListener listener);
}
