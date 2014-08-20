package com.dp.bigdata.taurus.agent.utils;

import com.dp.bigdata.taurus.agent.AgentServer;
import com.dp.bigdata.taurus.agent.AgentServerModule;
import com.dp.bigdata.taurus.agent.common.BaseEnvManager;
import com.dp.bigdata.taurus.agent.exec.TaurusExecutor;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.restlet.Component;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Map;

/**
 * Created by mkirin on 14-8-12.
 */
public class StartServlet implements ServletContextListener {
    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Injector injector = Guice.createInjector(new AgentServerModule());
        AgentServer as = injector.getInstance(AgentServer.class);
        ApplicationContext context = new FileSystemXmlApplicationContext("classpath:applicationContext-restlet.xml");
        Component restlet = (Component) context.getBean("component");
        ClearLogsTimerManager.getClearLogsTimerManager().start();

        try {
            //restlet.start();
            as.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
