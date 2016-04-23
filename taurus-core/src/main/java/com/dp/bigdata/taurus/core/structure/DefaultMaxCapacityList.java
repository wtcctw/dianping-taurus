package com.dp.bigdata.taurus.core.structure;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

/**
 * Author   mingdongli
 * 16/4/20  下午3:48.
 */
public class DefaultMaxCapacityList<E> extends ArrayList<E> implements MaxCapacityList<E>{

    @Autowired
    private DynamicMaxCapacity dynamicMaxCapacity;

    @Override
    public boolean addOrDiscard(E entry) {

        int size = size();
        int capacity = dynamicMaxCapacity.getMaxCapacity();

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
        return dynamicMaxCapacity.getMaxCapacity();
    }

}
