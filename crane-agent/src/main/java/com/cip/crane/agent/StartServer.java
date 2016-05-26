package com.cip.crane.agent;

import com.cip.crane.agent.utils.ClearLogsTimerManager;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class StartServer {

	public  void start() {
		Injector injector = Guice.createInjector(new AgentServerModule());
		AgentServer as = injector.getInstance(AgentServer.class);
        ClearLogsTimerManager.getClearLogsTimerManager().start();
        try {
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
