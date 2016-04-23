package com.dp.bigdata.taurus.core.structure;

import com.dp.bigdata.taurus.lion.AbstractLionPropertyInitializer;

/**
 * Author   mingdongli
 * 16/4/23  上午12:16.
 */
public class DefaultDynamicMaxCapacity extends AbstractLionPropertyInitializer<Integer> implements DynamicMaxCapacity {

    private static final String DEPENDENCY_PASS = "taurus.dependencypass.max";

    @Override
    protected String getKey() {
        return DEPENDENCY_PASS;
    }

    @Override
    protected Integer getDefaultValue() {
        return 12 * 60 * 24;  //对于每隔5s的调度，允许24个小时，到达一半会告警。
    }

    @Override
    protected StringTo<Integer> getConvert() {
        return new StringToInteger();
    }

    @Override
    public int getMaxCapacity() {
        return lionValue;
    }
}
