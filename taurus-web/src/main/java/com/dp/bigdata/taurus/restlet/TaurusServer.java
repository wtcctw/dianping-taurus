package com.dp.bigdata.taurus.restlet;

import org.restlet.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.cat.Cat;
import com.dp.bigdata.taurus.alert.TaurusAlert;
import com.dp.bigdata.taurus.alert.WeChatHelper;
import com.dp.bigdata.taurus.core.Engine;
import com.dp.bigdata.taurus.lion.ConfigHolder;
import com.dp.bigdata.taurus.lion.LionKeys;
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

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
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
                    log.info("start master server....");
                    Cat.logEvent("Taurus.Master", IPUtils.getFirstNoLoopbackIP4Address());
                    WeChatHelper.sendWeChat(ConfigHolder.get(LionKeys.ADMIN_USER), "Taurus master start: "+ IPUtils.getFirstNoLoopbackIP4Address(), ConfigHolder.get(LionKeys.ADMIN_WECHAT_AGENTID));
                    
        		}else{
            		alert.isInterrupt(true);
            		engine.isInterrupt(true);
            		log.info("start slave server....");
            		Cat.logEvent("Taurus.Slave", IPUtils.getFirstNoLoopbackIP4Address());
            		WeChatHelper.sendWeChat(ConfigHolder.get(LionKeys.ADMIN_USER), "Taurus slave start: "+ IPUtils.getFirstNoLoopbackIP4Address(), ConfigHolder.get(LionKeys.ADMIN_WECHAT_AGENTID));
            	}
        		
        	}else{
        		alert.isInterrupt(true);
        		engine.isInterrupt(true);
        		log.info("lion config error....");
        	}
        	
        	restlet.start();
            alert.start(-1);
            engine.start();
            
            log.info("taurus start....");
            
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
