package com.dp.bigdata.taurus.zookeeper.common.infochannel;

import com.dp.bigdata.taurus.zookeeper.common.infochannel.bean.ScheduleStatus;
import com.dp.bigdata.taurus.zookeeper.common.infochannel.guice.ZooKeeperProvider;
import com.dp.bigdata.taurus.zookeeper.common.infochannel.interfaces.CleanInfoChannel;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Scopes;
import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.KeeperException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ZooKeeperCleaner {
	private static Log LOGGER = LogFactory.getLog(ZooKeeperCleaner.class);
	private CleanInfoChannel zkChannel;
	private TaurusZKInfoChannel readChannel;
	public static String SCHEDULE_PATH = "taurus/schedules";
	
	public ZooKeeperCleaner() {
		Injector injector = Guice.createInjector(new CleanInfoChannelModule());
		zkChannel = injector.getInstance(CleanInfoChannel.class);
		injector = Guice.createInjector(new ReadInfoChannelModule());
		readChannel = injector.getInstance(TaurusZKScheduleInfoChannel.class);
	}
	public static void clearNodes(int start,int end){
        ZooKeeperCleaner cleaner = new ZooKeeperCleaner();
        for(int i = start; i <= end; i ++){
            cleaner.run(i);
        }
    }
	public void run(int offset) {
	    Date date = new Date();
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    calendar.add(Calendar.DAY_OF_MONTH, offset);
	    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    String dateString = format.format(calendar.getTime());
	    List<String> ipList = null;
        try {
            ipList = readChannel.getChildrenNodeName(SCHEDULE_PATH);
            
        } catch (KeeperException e) {
            LOGGER.error(e,e);
            return;
        } catch (InterruptedException e) {
            LOGGER.error(e,e);
            return;
        }
        for(String ip:ipList) {
            List<String> attemptList = null;
            try {
                if (readChannel.existPath(SCHEDULE_PATH + "/" + ip + "/" + dateString)){
                    attemptList = readChannel.getChildrenNodeName(SCHEDULE_PATH + "/" + ip + "/" + dateString);
                    if (attemptList != null){
                        for(String attemptId:attemptList){

                            if(!zkChannel.rmrPath("/"+ SCHEDULE_PATH + "/" + ip + "/" + attemptId)){
                                LOGGER.error("faile to delete " + SCHEDULE_PATH + "/" + ip + "/" + attemptId);
                            }
                            LOGGER.info("success to delete " + SCHEDULE_PATH + "/" + ip + "/" + attemptId);
                        }
                        zkChannel.rmrPath("/"+ SCHEDULE_PATH + "/" + ip + "/" + dateString);
                        LOGGER.info("success to delete " + "/"+ SCHEDULE_PATH + "/" + ip + "/" + dateString);
                    }


                }


            } catch (KeeperException e) {
                LOGGER.error(e,e);
                continue;
            } catch (InterruptedException e) {
                LOGGER.error(e,e);
                continue;
            }

            
        }
		
	}
	
	
	public void set() {
	    try {
            readChannel.mkPathIfNotExists("taurus/schedules/192.168.26.22/2013-04-05");
            readChannel.mkPathIfNotExists("taurus/schedules/192.168.26.22/2013-04-05/attempt_201302041628_0003_0014_0001");
        } catch(Exception e) {
            LOGGER.error(e,e);
        }
	}
	public void read(String arg) {
	
		ScheduleStatus result = null;
		try {
			result = (ScheduleStatus)readChannel.getData(arg);
		} catch (Exception e) {
		    LOGGER.error(e,e);
		} 
		
	}
	
	public static void main(String []args) {
		ZooKeeperCleaner cleaner = new ZooKeeperCleaner();
		int offset = -7;
		if(args.length == 1 && args[0] != null){
            try { 
                offset = Integer.parseInt(args[0]);
            } catch(Exception e) {
                LOGGER.error("args error",e);
            }
		}
        int start = -500;
        int end = -1;
        for(int i = start; i <= end; i ++){
            cleaner.run(i);
        }



	}
	
	public static class CleanInfoChannelModule extends AbstractModule{
		@Override
		protected void configure() {
			bind(CleanInfoChannel.class).to(TaurusZKCleanerInfoChannel.class).in(Scopes.SINGLETON);
			bindZooKeeper();
		}
		
		protected void bindZooKeeper() {
			bind(ZkClient.class).toProvider(ZooKeeperProvider.class).in(Scopes.SINGLETON);
		}
	}
	
	class ReadInfoChannelModule extends AbstractModule{
		@Override
		protected void configure() {
			bind(TaurusZKInfoChannel.class).to(TaurusZKScheduleInfoChannel.class);
			bindZooKeeper();
		}
		
		protected void bindZooKeeper() {
			bind(ZkClient.class).toProvider(ZooKeeperProvider.class);
		}
	}
}
