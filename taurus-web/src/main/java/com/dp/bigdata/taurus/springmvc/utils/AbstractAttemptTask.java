package com.dp.bigdata.taurus.springmvc.utils;

import com.dp.bigdata.taurus.core.structure.BooleanConverter;
import com.dp.bigdata.taurus.core.structure.Converter;
import com.dp.bigdata.taurus.lion.AbstractLionPropertyInitializer;
import com.dp.bigdata.taurus.zookeeper.common.elect.LeaderElector;
import com.dp.bigdata.taurus.zookeeper.common.infochannel.guice.LeaderElectorChanelModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * Author   mingdongli
 * 16/5/10  下午6:33.
 */
public abstract class AbstractAttemptTask extends AbstractLionPropertyInitializer<Boolean> implements InitializingBean {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected LeaderElector leaderElector;

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
