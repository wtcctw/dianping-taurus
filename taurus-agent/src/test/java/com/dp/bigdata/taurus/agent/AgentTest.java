package com.dp.bigdata.taurus.agent;

import com.dp.bigdata.taurus.agent.utils.ClearLogsTimerManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.restlet.Component;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class AgentTest {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new AgentServerModule());
        AgentServer as = injector.getInstance(AgentServer.class);
        ApplicationContext context = new FileSystemXmlApplicationContext("classpath:applicationContext-restlet.xml");
        Component restlet = (Component) context.getBean("component");
        try {
            ClearLogsTimerManager.getClearLogsTimerManager().start();
            restlet.start();
            as.start();
            System.out.println(String.format("[%s] [INFO] Press any key to stop server ... ", 1));
            System.in.read();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
