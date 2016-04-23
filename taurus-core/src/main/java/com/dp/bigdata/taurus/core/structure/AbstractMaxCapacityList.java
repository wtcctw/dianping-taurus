package com.dp.bigdata.taurus.core.structure;

import com.dp.bigdata.taurus.lion.AbstractLionPropertyInitializer;

/**
 * Author   mingdongli
 * 16/4/22  下午08:57.
 */
public abstract class AbstractMaxCapacityList extends AbstractLionPropertyInitializer<Integer> {

    private static final String DEPENDENCY_PASS = "taurus.dependencypass.max";

    @Override
    protected String getKey() {
        return DEPENDENCY_PASS;
    }

    @Override
    protected Integer getDefaultValue() {
        return 12 * 60;  //对于每隔5s的调度，允许1个小时。
    }

    @Override
    protected StringTo<Integer> getConvert() {
        return new StringToInteger();
    }
}
