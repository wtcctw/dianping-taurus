package com.dp.bigdata.taurus.restlet;

import com.dp.bigdata.taurus.alert.TaurusAlert;
import com.dp.bigdata.taurus.restlet.utils.ClearLogsTimerManager;
import org.restlet.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.dp.bigdata.taurus.core.Engine;

/**
 * TaurusRestletServer mode: standalone | all
 *
 * @author damon.zhu
 */
public class TaurusServer {

    @Autowired
    public Engine engine;
    @Autowired
    public TaurusAlert alert;
    @Autowired
    public Component restlet;

    public void start() {

        System.setProperty("org.restlet.engine.loggerFacadeClass", "org.restlet.ext.slf4j.Slf4jLoggerFacade");

        try {
            restlet.start();
            alert.start(-1);
            engine.start();
            ClearLogsTimerManager.getClearLogsTimerManager().start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void stop() {
        System.setProperty("org.restlet.engine.loggerFacadeClass", "org.restlet.ext.slf4j.Slf4jLoggerFacade");

        engine.stop();
        try {
            restlet.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
