package com.dp.bigdata.taurus.zookeeper.common.elect;

import com.dp.bigdata.taurus.common.lock.LockActionWrapper;
import com.dp.bigdata.taurus.zookeeper.common.event.LeaderChangedListener;
import org.I0Itec.zkclient.IZkStateListener;

import java.io.File;

/**
 * Author   mingdongli
 * 16/3/15  下午1:53.
 */
public interface LeaderElector extends ZkOperator{

    String LEADER = "leader";

    String LEADER_ELECTION = LEADER + File.separatorChar + "election";

    String SCHEDULE_SCHEDULING = "taurus/taskscheduling";

    void startup();

    boolean amILeader();

    boolean hasLeader();

    boolean elect();

    void close();

    String getLeaderElectionPath();

    void addLeaderChangeListener(LeaderChangedListener leaderChangedListener);

    void removeLeaderChangeListener(LeaderChangedListener leaderChangedListener);

    void addStateListener(IZkStateListener stateListener);

    LockActionWrapper getLock();

    String getCurrentLeaderIp();

    String getPreviousLeaderIp();

    boolean needAlarm();

}
