package com.dp.bigdata.taurus.springmvc.utils;

import com.dp.bigdata.taurus.common.Scheduler;
import com.dp.bigdata.taurus.common.structure.BooleanConverter;
import com.dp.bigdata.taurus.common.structure.Converter;
import com.dp.bigdata.taurus.common.lion.AbstractLionPropertyInitializer;
import com.dp.bigdata.taurus.common.zookeeper.elect.LeaderElector;
import com.dp.bigdata.taurus.common.zookeeper.infochannel.guice.LeaderElectorChanelModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Author   mingdongli
 * 16/5/10  下午6:33.
 */
public abstract class AbstractAttemptTask extends AbstractLionPropertyInitializer<Boolean> implements InitializingBean {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected LeaderElector leaderElector;

    @Autowired
    protected Scheduler scheduler;

    @Override
    public void afterPropertiesSet() throws Exception {

        super.afterPropertiesSet();
        Injector injector = Guice.createInjector(new LeaderElectorChanelModule());
        leaderElector = injector.getInstance(LeaderElector.class);
    }

    @Override
    protected Boolean getDefaultValue() {
        return false;
    }

    @Override
    protected Converter<Boolean> getConvert() {
        return new BooleanConverter();
    }
}
