package com.cip.crane.zookeeper.common.elect;

import com.cip.crane.common.lock.LockAction;
import com.cip.crane.common.lock.LockActionWrapper;
import com.cip.crane.common.utils.IPUtils;
import com.cip.crane.common.utils.JaasUtils;
import com.cip.crane.zookeeper.common.event.LeaderChangeEvent;
import com.cip.crane.zookeeper.common.event.LeaderChangedListener;
import com.cip.crane.zookeeper.common.infochannel.TaurusZKInfoChannel;
import com.cip.crane.zookeeper.common.visit.LeaderElectorVisitor;
import com.cip.crane.zookeeper.common.visit.ILeaderElectorVisit;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkConnection;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Author   mingdongli
 * 16/3/15  下午2:02.
 */
@Singleton
public class TaurusZKLeaderElector extends TaurusZKInfoChannel implements LeaderElector, ILeaderElectorVisit {

    private final Log logger = LogFactory.getLog(getClass());

    private String hostIp = IPUtils.getFirstNoLoopbackIP4Address();

    private String currentLeaderIp;

    private String previousLeaderIp;

    private ZkConnection zkConnection;

    private List<LeaderChangedListener> listeners = new ArrayList<LeaderChangedListener>();

    public LockActionWrapper lockActionWrapper;

    @Inject
    TaurusZKLeaderElector(ZKPair zkPair) {
        super(zkPair.getZkClient());
        zkConnection = zkPair.getZkConnection();
        lockActionWrapper = zkPair.getLockActionWrapper();
        mkPathIfNotExists(BASE);
        mkPathIfNotExists(BASE, LEADER);
    }

    @Override
    public void startup() {

        lockActionWrapper.doAction(new LockAction() {
            @Override
            public void doAction() {
                addDataListener(new LeaderChangeListener(), BASE, LEADER_ELECTION);
                elect();
            }
        });
    }

    @Override
    public boolean amILeader() {

        if(StringUtils.isBlank(currentLeaderIp)){
            currentLeaderIp = getLeaderIpFromZk();
        }
        if (StringUtils.isNotBlank(hostIp) && hostIp.equalsIgnoreCase(currentLeaderIp)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean needAlarm() {
        if (StringUtils.isNotBlank(previousLeaderIp) && StringUtils.isNotBlank(currentLeaderIp)
                && previousLeaderIp.equalsIgnoreCase(currentLeaderIp)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean hasLeader() {
        String leader = getLeaderIpFromZk();
        return StringUtils.isNotBlank(leader);
    }

    @Override
    public boolean elect() {

        currentLeaderIp = getLeaderIpFromZk();
        if (StringUtils.isNotBlank(currentLeaderIp)) {
            logger.info(String.format("Schedule server %s has been elected as leader, so stopping the election process.", currentLeaderIp));
            boolean isLeader = amILeader();
            if (!isLeader) {
                for (LeaderChangedListener listener : listeners) {
                    listener.onResigningAsLeader(new LeaderChangeEvent(this));
                }
            } else {
                for (LeaderChangedListener listener : listeners) {
                    listener.onBecomingLeader(new LeaderChangeEvent(this));
                }
            }
            return isLeader;
        }

        try {
            ZKEphemeralOperator zKEphemeralOperator = new ZKEphemeralOperator(getLeaderElectionPath(), hostIp, zkConnection.getZookeeper(), JaasUtils.isZkSecurityEnabled());
            zKEphemeralOperator.create();
            logger.info(hostIp + " successfully elected as leader");
            previousLeaderIp = currentLeaderIp;
            currentLeaderIp = hostIp;
            for (LeaderChangedListener listener : listeners) {
                listener.onBecomingLeader(new LeaderChangeEvent(this));
            }
        } catch (ZkNodeExistsException e) {
            // If someone else has written the path, then
            previousLeaderIp = currentLeaderIp;
            currentLeaderIp = getLeaderIpFromZk();

            if (StringUtils.isNotBlank(currentLeaderIp)) {
                logger.debug(String.format("Broker %s was elected as leader instead of broker %s", currentLeaderIp, hostIp));
            } else {
                logger.warn("A leader has been elected but just resigned, this will result in another round of election");
            }

        } catch (Throwable t) {
            logger.error(String.format("Error while electing or becoming leader on broker %s", hostIp), t);
            resign();
        }

        return amILeader();
    }

    @Override
    public void close() {
        previousLeaderIp = null;
        currentLeaderIp = null;
    }

    @Override
    public String getLeaderElectionPath() {
        return File.separatorChar + BASE + File.separatorChar + LEADER_ELECTION;
    }

    @Override
    public LockActionWrapper getLock() {
        return lockActionWrapper;
    }

    public String getCurrentLeaderIp() {
        return currentLeaderIp;
    }

    public String getPreviousLeaderIp() {
        return previousLeaderIp;
    }

    @Override
    public void addLeaderChangeListener(LeaderChangedListener leaderChangeListener) {
        listeners.add(leaderChangeListener);
    }

    @Override
    public void removeLeaderChangeListener(LeaderChangedListener leaderChangeListener) {
        listeners.remove(leaderChangeListener);
    }

    private String getLeaderIpFromZk() {
        try {
            return getData(BASE, LEADER_ELECTION).toString();
        } catch (Exception e) {
            return null;
        }
    }

    private void resign() {
        currentLeaderIp = null;
        rmPath(BASE, LEADER_ELECTION);
    }

    @Override
    public void accept(LeaderElectorVisitor electorVisitor) {
        electorVisitor.visitLeaderElector(this);
    }

    @Override
    public boolean exists(String path) {
        return super.existPath(path);
    }

    @Override
    public void createEphemeral(String path) {
        ZKEphemeralOperator ephemeral = new ZKEphemeralOperator(File.separatorChar + path, "", zkConnection.getZookeeper(), JaasUtils.isZkSecurityEnabled());
        ephemeral.create();
        logger.info(String.format("server %s create %s", hostIp, SCHEDULE_SCHEDULING));
    }

    @Override
    public void createPersistent(String path) {
        if(StringUtils.isBlank(path)){
            return;
        }
        if(path.startsWith(String.valueOf(File.separatorChar))){
            path = path.substring(1, path.length());
        }
        super.mkPathIfNotExists(path);
    }

    //删除taurus/taskscheduling,只能主删
    @Override
    public void delete(String path) {
        if (amILeader()) {
            super.rmPath(path);
            logger.info(String.format("server %s delete %s", hostIp, SCHEDULE_SCHEDULING));
        }else{
            logger.info(String.format("server %s is not master, skip delete %s", hostIp, SCHEDULE_SCHEDULING));
        }
    }

    class LeaderChangeListener implements IZkDataListener {

        @Override
        public void handleDataChange(final String dataPath, final Object data) throws Exception {

            lockActionWrapper.doAction(new LockAction() {
                @Override
                public void doAction() {

                    boolean amILeaderBeforeDataChange = amILeader();
                    previousLeaderIp = currentLeaderIp;
                    currentLeaderIp = data.toString();
                    // The old leader needs to resign leadership if it is no longer the leader
                    if (amILeaderBeforeDataChange && !amILeader()) {
                        logger.info(String.format("New leader is %d", currentLeaderIp));
                        for (LeaderChangedListener listener : listeners) {
                            listener.onResigningAsLeader(new LeaderChangeEvent(this));
                        }
                    } else if (!amILeaderBeforeDataChange && amILeader()) {
                        for (LeaderChangedListener listener : listeners) {
                            listener.onBecomingLeader(new LeaderChangeEvent(this));
                        }
                    } else {
                        logger.info(String.format("Previous leader is %s, current leader is %s", previousLeaderIp, currentLeaderIp));
                    }
                }
            });
        }

        @Override
        public void handleDataDeleted(final String dataPath) throws Exception {

            lockActionWrapper.doAction(new LockAction() {
                @Override
                public void doAction() {
                    logger.debug(String.format("%s leader change listener fired for path %s to handle data deleted: trying to elect as a leader", hostIp, dataPath));
                    if (amILeader()) {
                        for (LeaderChangedListener listener : listeners) {
                            listener.onResigningAsLeader(new LeaderChangeEvent(this));
                        }
                    }
                    elect();
                }
            });
        }
    }

}
