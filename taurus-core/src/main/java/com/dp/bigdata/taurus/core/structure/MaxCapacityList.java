package com.dp.bigdata.taurus.core.structure;

import com.dp.bigdata.taurus.lion.ConfigHolder;
import com.dp.bigdata.taurus.lion.LionKeys;

import java.util.List;

/**
 * Author   mingdongli
 * 16/4/20  下午3:52.
 */
public interface MaxCapacityList<E> extends List<E>{

    int MAX_CAPACITY_SIZE = Integer.parseInt(ConfigHolder.get(LionKeys.MAX_CAPACITY.value(), "150"));  //防止秒级调度发生阻塞后大量堆积，打爆内存

    boolean addOrDiscard(E entry);

}
