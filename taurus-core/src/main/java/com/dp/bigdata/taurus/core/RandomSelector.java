package com.dp.bigdata.taurus.core;

import java.util.*;

/**
 * Author   mingdongli
 * 16/5/9  下午5:23.
 */
public class RandomSelector<T> implements Selector<T> {

    @Override
    public T select(final Collection<T> source) {

        if (source == null || source.size() == 0) {
            throw new IllegalArgumentException("Empty source.");
        }

        if (source.size() == 1) {
            return (T) source.toArray()[0];
        }

        Random random = new Random();
        int randomScope = source.size();
        int randomIndex = random.nextInt(randomScope);

        return (T) source.toArray()[randomIndex];
    }

}
