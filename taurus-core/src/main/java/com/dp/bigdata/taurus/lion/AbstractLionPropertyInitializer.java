package com.dp.bigdata.taurus.lion;

import com.dp.bigdata.taurus.core.structure.StringTo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Author   mingdongli
 * 16/4/21  下午08:52.
 */
public abstract class AbstractLionPropertyInitializer<T> implements InitializingBean, ConfigChangeListener {

    protected final Log logger = LogFactory.getLog(getClass());

    protected T lionValue;

    protected StringTo<T> converter;

    @Autowired
    protected LionDynamicConfig lionDynamicConfig;

    @Override
    public void afterPropertiesSet() throws Exception {

        converter = getConvert();

        try {
            String value = lionDynamicConfig.get(getKey());
            lionValue = converter.stringConvertTo(value);
        } catch (Exception e) {
            //lion无法获取值，或者转换失败取默认值
            lionValue = getDefaultValue();
        }
        if (logger.isInfoEnabled()) {
            logger.info(getClass().getSimpleName() + " : Init [lionValue] to " + lionValue);
        }

        lionDynamicConfig.addConfigChangeListener(this);
    }

    @Override
    public void onConfigChange(String key, String value) throws Exception {

        if (key != null && key.equals(getKey())) {
            if (logger.isInfoEnabled()) {
                logger.info("[onChange][" + getKey() + "]" + value);
            }
            lionValue = converter.stringConvertTo(value.trim());
        } else {
            if (logger.isInfoEnabled()) {
                logger.info("not match");
            }
        }
    }

    protected abstract String getKey();

    protected abstract T getDefaultValue();

    protected abstract StringTo<T> getConvert();
}
