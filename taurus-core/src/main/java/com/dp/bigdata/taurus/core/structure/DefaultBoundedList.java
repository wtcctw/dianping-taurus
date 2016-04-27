package com.dp.bigdata.taurus.core.structure;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

/**
 * Author   mingdongli
 * 16/4/20  下午3:48.
 */
public class DefaultBoundedList<E> extends ArrayList<E> implements BoundedList<E> {

    @Autowired
    private DynamicBounded dynamicMaxCapacity;

    @Override
    public boolean addOrDiscard(E entry) {

        int size = size();
        int capacity = dynamicMaxCapacity.getCapacity();

        if(size < capacity){
            add(entry);
            return true;
        }else {
            removeRange(capacity, size);
        }

        return false;
    }

    @Override
    public int getMaxCapacity() {
        return dynamicMaxCapacity.getCapacity();
    }

}
