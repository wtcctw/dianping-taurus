package com.dp.bigdata.taurus.core.structure;

/**
 * Author   mingdongli
 * 16/4/22  上午12:17.
 */
public interface Converter<T> {

    T convertTo(String lionValue);
}
