package com.dp.bigdata.taurus.common.netty;

import com.dp.bigdata.taurus.zookeeper.common.execute.ExecuteContext;
import com.dp.bigdata.taurus.zookeeper.common.execute.ExecutorManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Author   mingdongli
 * 16/5/18  上午10:41.
 */
@Component
@Qualifier("proxy")
public class ExecutorManagerFactory implements FactoryBean<ExecutorManager>, InvocationHandler {

    private static final Log logger = LogFactory.getLog(ExecutorManagerFactory.class);

    @Qualifier("netty")
    @Autowired
    public ExecutorManager netty;

    @Qualifier("zookeeper")
    @Autowired
    public ExecutorManager zookeeper;

    @Override
    public ExecutorManager getObject() throws Exception {

        if(logger.isInfoEnabled()){
            logger.info("[getObject]");
        }

        return (ExecutorManager) Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[]{ExecutorManager.class}, this);
    }

    @Override
    public Class<ExecutorManager> getObjectType() {
        return ExecutorManager.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if(args.length == 1){
            ExecuteContext executeContext = (ExecuteContext) args[0];
            String type = executeContext.getType();
            if(MscheduleExecutorManager.MSCHEDULE_TYPE.equalsIgnoreCase(type)){
                return method.invoke(netty, args);
            }else {
                return method.invoke(zookeeper, args);
            }

        }else if(args.length == 2){
            logger.info(String.format("invoke %s", method.toString()));
            return method.invoke(zookeeper, args);
        }else {
            logger.info(String.format("invoke %s error", method.toString()));
            return null;
        }
    }

    public ExecutorManager getNetty() {
        return netty;
    }

    public void setNetty(ExecutorManager netty) {
        this.netty = netty;
    }

    public void setZookeeper(ExecutorManager zookeeper) {
        this.zookeeper = zookeeper;
    }

    public ExecutorManager getZookeeper() {
        return zookeeper;
    }
}
