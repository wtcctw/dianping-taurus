package com.dp.bigdata.taurus.core.structure;

import java.util.List;

/**
 * Author   mingdongli
 * 16/4/20  下午3:52.
 */
public interface MaxCapacityList<E> extends List<E>{

    boolean addOrDiscard(E entry);

    int getMaxCapacity();
}
