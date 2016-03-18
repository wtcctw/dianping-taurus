package com.dp.bigdata.taurus.web.servlet.web.common.config;

import com.dp.bigdata.taurus.config.AbstractConfig;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Author   mingdongli
 * 16/3/8  下午2:26.
 */
@Component
public class WebComponentConfig extends AbstractConfig{

    private static final String COMPONENT_SWITCH_FILE = "/data/appdatas/taurusweb/taurus-web-switch.properties";

    private boolean isSsoEnable = true;

    public WebComponentConfig() {
        super(COMPONENT_SWITCH_FILE);
    }

    @PostConstruct
    public void init() {
        try {
            loadConfig();
        } catch (Exception e) {
            logger.error("loadConfig error.", e);
        }
    }

    public boolean isSsoEnable() {
        return isSsoEnable;
    }

    public void setIsSsoEnable(boolean isSsoEnable) {
        this.isSsoEnable = isSsoEnable;
    }
}
