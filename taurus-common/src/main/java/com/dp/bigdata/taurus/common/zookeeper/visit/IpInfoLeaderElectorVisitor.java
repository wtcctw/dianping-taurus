package com.dp.bigdata.taurus.common.zookeeper.visit;

import com.dp.bigdata.taurus.common.zookeeper.elect.LeaderElector;
import org.apache.commons.lang.StringUtils;

/**
 * Author   mingdongli
 * 16/3/16  下午2:22.
 */
public class IpInfoLeaderElectorVisitor implements LeaderElectorVisitor{

    private String content;

    @Override
    public void visitLeaderElector(LeaderElector leaderElector) {
        String previousLeader = leaderElector.getPreviousLeaderIp();
        if(previousLeader == null){
            previousLeader = StringUtils.EMPTY;
        }
        StringBuilder sb =  new StringBuilder().append("taurus master server 从 ").append(previousLeader)
                .append(" 切换到 ").append(leaderElector.getCurrentLeaderIp());
        content =  sb.toString();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
