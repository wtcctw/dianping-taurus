package com.dp.bigdata.taurus.restlet.utils;

import com.dp.bigdata.taurus.common.Engine;
import org.restlet.Component;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by mkirin on 14-8-12.
 */
public class StartServlet implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("#########RESTLET START!############");
        ApplicationContext context = new FileSystemXmlApplicationContext("classpath:applicationContext-core.xml",
                "classpath:applicationContext-restlet.xml");
        Engine engine = (Engine) context.getBean("engine");
        Component restlet = (Component) context.getBean("component");
        try {
            restlet.start();

            engine.start();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("#########RESTLET STOP!############");
    }
}
