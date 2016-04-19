package com.dp.bigdata.taurus.core;

import com.dp.bigdata.taurus.core.listener.GenericAttemptListener;

import java.util.List;

/**
 * 
 * Filter all the triggled taskattempts.
 * @author damon.zhu
 *
 */
public interface Filter {
    
    /**
     * filter the input contexts
     * @param contexts
     * @return List<AttemptContext>
     */
    List<AttemptContext> filter(List<AttemptContext> contexts);

    void registerAttemptListener(GenericAttemptListener genericAttemptListener);
}
