package com.dp.bigdata.taurus.restlet;

import org.restlet.Component;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.dianping.cat.Cat;
import com.dp.bigdata.taurus.alert.TaurusAlert;
import com.dp.bigdata.taurus.core.Engine;

/**
 * TaurusRestletServer mode: standalone | all
 * 
 * @author damon.zhu
 */
public class TaurusServer {
	public static final String ALONE = "standalone";

	public static final String ALL = "all";

	public static void main(String args[]) {
		@SuppressWarnings("resource")

		ApplicationContext context = new FileSystemXmlApplicationContext("classpath:applicationContext-core.xml",
		      "classpath:applicationContext-restlet.xml");
        System.setProperty("org.restlet.engine.loggerFacadeClass", "org.restlet.ext.slf4j.Slf4jLoggerFacade");

        Engine engine = (Engine) context.getBean("engine");
		TaurusAlert alert = (TaurusAlert) context.getBean("alert");
		Component restlet = (Component) context.getBean("component");

		try {
			alert.start(-1);
		} catch (Exception e) {
			Cat.logError(e);
			e.printStackTrace();
		}

		if (args.length == 1) {
			try {
				if (args[0].equals(ALONE)) {
					restlet.start();
				} else if (args[0].equals(ALL)) {
					restlet.start();
					engine.start();
				}
			} catch (Exception e) {
				Cat.logError(e);
				e.printStackTrace();
			}
		}
	}
}
