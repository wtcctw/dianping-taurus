package com.dp.bigdata.taurus.common.lion;

/**
 * Author   mingdongli
 * 16/4/21  下午06:15.
 */
public interface ConfigChangeListener {

    void onConfigChange(String key, String value) throws Exception;
}
