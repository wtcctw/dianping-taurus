package com.cip.crane.agent;

import com.cip.crane.agent.common.TaskType;
import com.cip.crane.agent.utils.ClearLogsTimerManager;
import com.google.inject.Guice;
import com.google.inject.Injector;

import org.junit.Test;

public class AgentTest {

	@Test
	public void test1(){
		System.out.println(TaskType.SPRING.name());
		System.out.println(TaskType.getString(TaskType.SPRING));
	}
	
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new AgentServerModule());
        AgentServer as = injector.getInstance(AgentServer.class);
        try {
            ClearLogsTimerManager.getClearLogsTimerManager().start();
            as.start();
            System.out.println(String.format("[%s] [INFO] Press any key to stop server ... ", 1));
            System.in.read();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
