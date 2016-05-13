package com.dp.bigdata.taurus.core.structure;

/**
 * Author   mingdongli
 * 16/4/22  上午10:08.
 */
public class BooleanConverter implements Converter<Boolean> {

    @Override
    public Boolean convertTo(String lionValue) {
        return Boolean.parseBoolean(lionValue);
    }
}
