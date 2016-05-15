package com.dp.bigdata.taurus.common.zookeeper.deploy;

public interface Deployer {

    public String deploy(String agentIp, DeploymentContext context) throws DeploymentException;

    public void undeploy(String agentIp, DeploymentContext context) throws DeploymentException;

}
