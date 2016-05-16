package com.dp.bigdata.taurus.zookeeper.common.deploy;

public interface Deployer {

    public String deploy(String agentIp, DeploymentContext context) throws DeploymentException;

    public void undeploy(String agentIp, DeploymentContext context) throws DeploymentException;

}
