package com.dp.bigdata.taurus.core;

import com.dianping.cat.Cat;
import com.dp.bigdata.taurus.core.listener.GenericAttemptListener;
import com.dp.bigdata.taurus.core.structure.StringTo;
import com.dp.bigdata.taurus.core.structure.StringToInteger;
import com.dp.bigdata.taurus.lion.AbstractLionPropertyInitializer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Author   mingdongli
 * 16/4/21  下午08:15.
 */
public class MaximumConcurrentTaskFilter extends AbstractLionPropertyInitializer<Integer> implements Filter<Integer> {

    private static final String MAX_TASK_NUM = "taurus.engine.maxtasknum";

    private static final int DEFAULT_MAX_TASK_NUM = 1000;

    private Filter next;

    private Scheduler scheduler;

    @Autowired
    public MaximumConcurrentTaskFilter(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public List<AttemptContext> filter(List<AttemptContext> contexts) {
        List<AttemptContext> results;
        int max = lionValue - scheduler.getAllRunningAttempt().size();

        if (max <= 0) {
            results = new ArrayList<AttemptContext>();

            Cat.logEvent("Exception", "Capacity-Error");
        } else if (max >= contexts.size()) {
            results = contexts;
        } else {
            results = contexts.subList(0, max);
        }

        if (next != null) {
            return next.filter(results);
        } else {
            return results;
        }
    }

    @Override
    public void registerAttemptListener(GenericAttemptListener genericAttemptListener) {
        throw new UnsupportedOperationException("not support");
    }

    public Filter getNext() {
        return next;
    }

    public void setNext(Filter next) {
        this.next = next;
    }

    @Override
    public void onConfigChange(String key, String value) throws Exception {

        if (key != null && key.equals(MAX_TASK_NUM)) {
            if (logger.isInfoEnabled()) {
                logger.info("[onChange][" + MAX_TASK_NUM + "]" + value);
            }
            this.lionValue = converter.stringConvertTo(value.trim());
        } else {
            if (logger.isInfoEnabled()) {
                logger.info("not match");
            }
        }
    }

    @Override
    protected String getKey() {
        return MAX_TASK_NUM;
    }

    @Override
    protected Integer getDefaultValue() {
        return DEFAULT_MAX_TASK_NUM;
    }

    @Override
    protected StringTo getConvert() {
        return new StringToInteger();
    }

    @Override
    public Integer fetchLionValue() {
        return lionValue;
    }
}
