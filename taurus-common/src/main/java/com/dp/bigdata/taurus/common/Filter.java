package com.dp.bigdata.taurus.common;

import com.dp.bigdata.taurus.common.listener.AttemptListenable;
import com.dp.bigdata.taurus.common.lion.LionValueGetter;

import java.util.List;

/**
 * 
 * Filter all the triggled taskattempts.
 * @author damon.zhu
 *
 */
public interface Filter<T> extends AttemptListenable, LionValueGetter<T> {
    
    /**
     * filter the input contexts
     * @param contexts
     * @return List<AttemptContext>
     */
    List<AttemptContext> filter(List<AttemptContext> contexts);

}
