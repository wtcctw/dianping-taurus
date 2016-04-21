package com.dp.bigdata.taurus.core.structure;

import java.util.ArrayList;

/**
 * Author   mingdongli
 * 16/4/20  下午3:48.
 */
public class DefaultMaxCapacityList<E> extends ArrayList<E> implements MaxCapacityList<E>{

    @Override
    public boolean addOrDiscard(E entry) {

        int size = size();
        if(size < MAX_CAPACITY_SIZE){
            add(entry);
            return true;
        }
        return false;
    }
git }
