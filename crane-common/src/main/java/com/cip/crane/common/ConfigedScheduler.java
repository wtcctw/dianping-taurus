package com.cip.crane.common;

import com.cip.crane.common.structure.Converter;
import com.cip.crane.common.structure.BooleanConverter;
import com.cip.crane.common.lion.AbstractLionPropertyInitializer;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Author   mingdongli
 * 16/4/23  上午11:55.
 */
public class ConfigedScheduler extends AbstractLionPropertyInitializer<Boolean> implements SchedulerConfig{

    protected static final String DEPENDENCE_PASS_DISCARD = "taurus.core.depend.discard";

    protected static final long SCHDUELE_INTERVAL = 500;

    protected final AtomicBoolean interrupted = new AtomicBoolean(false);

    protected volatile boolean triggleThreadRestFlag = false;

    protected volatile boolean refreshThreadRestFlag = false;

    protected int maxConcurrency = 50;

    @Override
    public void setMaxConcurrency(int maxConcurrency) {
        this.maxConcurrency = maxConcurrency;
    }

    @Override
    public boolean isTriggleThreadRestFlag() {
        return triggleThreadRestFlag;
    }

    @Override
    protected String getKey() {
        return DEPENDENCE_PASS_DISCARD;
    }

    @Override
    protected Boolean getDefaultValue() {
        return true;
    }

    @Override
    protected Converter<Boolean> getConvert() {
        return new BooleanConverter();
    }
}
