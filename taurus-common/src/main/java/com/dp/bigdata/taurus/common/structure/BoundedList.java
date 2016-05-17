package com.dp.bigdata.taurus.common.structure;

import java.util.List;

/**
 * Author   mingdongli
 * 16/4/20  下午3:52.
 */
public interface BoundedList<E> extends List<E>{

    boolean addOrDiscard(E entry);

    int getMaxCapacity();
}
