package com.dp.bigdata.taurus.alert.healthcheck;

/**
 * Author   mingdongli
 * 16/3/16  下午2:49.
 */
public class LeaderElectorHealthChecker extends AbstractHealthChecker implements HealthChecker{

    @Override
    protected String getCheckPath() {
        return "/taurus/leader/election";
    }

    public static void main(String[] args) {
        LeaderElectorHealthChecker leaderElectorHealthChecker = new LeaderElectorHealthChecker();
        try {
            leaderElectorHealthChecker.afterPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(leaderElectorHealthChecker.getData(leaderElectorHealthChecker.getCheckPath()));
    }

}
