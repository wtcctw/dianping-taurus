package com.dp.bigdata.taurus.core;

import java.util.Collection;

/**
 * Author   mingdongli
 * 16/5/9  下午5:22.
 */
public interface Selector<T> {

    T select(Collection<T> source);
}
