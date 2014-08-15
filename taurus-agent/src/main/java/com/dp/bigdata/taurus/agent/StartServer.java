package com.dp.bigdata.taurus.agent;

import com.dp.bigdata.taurus.agent.utils.ClearLogsTimerManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.restlet.Component;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class StartServer {
	public static void main(String []args) {
		Injector injector = Guice.createInjector(new AgentServerModule());
		AgentServer as = injector.getInstance(AgentServer.class);
        ApplicationContext context = new FileSystemXmlApplicationContext("classpath:applicationContext-restlet.xml");
        Component restlet = (Component) context.getBean("component");
        ClearLogsTimerManager.getClearLogsTimerManager().start();
        try {
            restlet.start();
            as.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

    @Override
    public StartServer clone() {
        StartServer result = null;
        try {
            result = (StartServer) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return result;

    }
}
