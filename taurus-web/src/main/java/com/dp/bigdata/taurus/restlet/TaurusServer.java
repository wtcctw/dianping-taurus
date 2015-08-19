package com.dp.bigdata.taurus.restlet;

import java.util.Date;

import org.restlet.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.dp.bigdata.taurus.alert.TaurusAlert;
import com.dp.bigdata.taurus.core.Engine;
import com.dp.bigdata.taurus.restlet.utils.ClearLogsTimerManager;
import com.dp.bigdata.taurus.restlet.utils.LionConfigUtil;
import com.dp.bigdata.taurus.restlet.utils.MonitorAgentOffLineTaskTimer;
import com.dp.bigdata.taurus.restlet.utils.ReFlashHostLoadTaskTimer;
import com.dp.bigdata.taurus.zookeeper.common.utils.IPUtils;

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
        	
        	if(LionConfigUtil.loadServerConf()){
        		if(LionConfigUtil.SERVER_MASTER_IP.equals(IPUtils.getFirstNoLoopbackIP4Address())){
        			
                    ClearLogsTimerManager.getClearLogsTimerManager().start();
                    MonitorAgentOffLineTaskTimer.getMonitorAgentOffLineTimeManager().start();
                    ReFlashHostLoadTaskTimer.getReFlashHostLoadManager().start();
                    System.out.println(new Date() + " [" + this.getClass().getName() + "] start master server....");
                    
        		}else{
            		alert.isInterrupt(true);
            		engine.isInterrupt(true);
            		System.out.println(new Date() + " [" + this.getClass().getName() + "] change master server....");
            		
            	}
        		
        	}else{
        		alert.isInterrupt(true);
        		engine.isInterrupt(true);
        		System.out.println(new Date() + " [" + this.getClass().getName() + "] lion config error....");
        	}
        	
        	restlet.start();
            alert.start(-1);
            engine.start();
            
            System.out.println(new Date() + " [" + this.getClass().getName() + "] taurus start....");
            
            //MyGrizzlyApp.init();
            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void stop() {
        System.setProperty("org.restlet.engine.loggerFacadeClass", "org.restlet.ext.slf4j.Slf4jLoggerFacade");

      //MyGrizzlyApp.stop();
        
        engine.stop();
        
        try {
            restlet.stop();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
