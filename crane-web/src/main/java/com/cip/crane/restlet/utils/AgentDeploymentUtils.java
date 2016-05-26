package com.cip.crane.restlet.utils;

import com.cip.crane.zookeeper.common.deploy.DeploymentException;
import com.cip.crane.generated.module.Task;

/**
 * 
 * AgentDeploymentUtils
 * @author damon.zhu
 *
 */
public interface AgentDeploymentUtils {

    /**
     * notify all agent to deploy or un-deploy a task
     * @param task
     * @param option
     * @throws DeploymentException
     */
    public void notifyAllAgent(final Task task, final DeployOptions option) throws DeploymentException;

}
