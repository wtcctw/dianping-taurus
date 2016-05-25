package com.dp.bigdata.taurus.common;

import com.dianping.cat.Cat;
import com.dp.bigdata.taurus.common.netty.MscheduleExecutorManager;
import com.dp.bigdata.taurus.common.structure.BooleanConverter;
import com.dp.bigdata.taurus.common.structure.Converter;
import com.dp.bigdata.taurus.generated.module.Task;
import com.dp.bigdata.taurus.generated.module.TaskAttempt;
import com.dp.bigdata.taurus.common.lion.AbstractLionPropertyInitializer;
import com.dp.bigdata.taurus.common.utils.ThreadUtils;
import com.dp.bigdata.taurus.common.execute.ExecutorManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * AttemptStatusMonitor is to update the TaskAttmpt status.
 *
 * @author damon.zhu
 * @see Engine
 */
public class AttemptStatusMonitor extends AbstractLionPropertyInitializer<Boolean> implements Runnable ,InitializingBean{

    private static final Log LOG = LogFactory.getLog(AttemptStatusMonitor.class);

    private final AtomicBoolean isInterrupt = new AtomicBoolean(false);

    private volatile boolean attemptStatusMonitorRestFlag = false;

    private static final String ZK_CLEANER = "taurus.zk.cleaner";

    @Autowired
    private Scheduler scheduler;

    @Qualifier("zookeeper")
    @Autowired
    private ExecutorManager zookeeper;

    private BlockingQueue<DelayedAttemptZkPath> delayedAttemptZkPaths = new DelayQueue<DelayedAttemptZkPath>();

    private ExecutorService zkCleanerExecutorService = ThreadUtils.newFixedThreadPool(10, "ZkCleanerExecutor");

    @Override
    public void run() {
        LOG.info("Starting to monitor attempts status");

        while (true) {

            while (isInterrupt.get()) {
                attemptStatusMonitorRestFlag = true;
            }
            attemptStatusMonitorRestFlag = false;

            try {
                List<AttemptContext> runningAttempts = scheduler.getAllRunningAttempt();
                for (AttemptContext attempt : runningAttempts) {
                    if(MscheduleExecutorManager.MSCHEDULE_TYPE.equals(attempt.getType())){
                        continue;
                    }
                    AttemptStatus sstatus = scheduler.getAttemptStatus(attempt.getAttemptid());
                    int status = sstatus.getStatus();
                    //LOG.info("Current status for attempt " + attempt.getAttemptid() + " : " + status);

                    switch (status) {
                        case AttemptStatus.SUCCEEDED:
                            attempt.getAttempt().setReturnvalue(sstatus.getReturnCode());
                            scheduler.attemptSucceed(attempt.getAttemptid());
                            delayedAttemptZkPaths.offer(new DelayedAttemptZkPath(attempt.getExechost(), attempt.getAttemptid()));
                            break;
                        case AttemptStatus.FAILED:
                            attempt.getAttempt().setReturnvalue(sstatus.getReturnCode());
                            scheduler.attemptFailed(attempt.getAttemptid());
                            delayedAttemptZkPaths.offer(new DelayedAttemptZkPath(attempt.getExechost(), attempt.getAttemptid()));
                            break;
                        case AttemptStatus.RUNNING:
                            if (attempt.getStatus() != AttemptStatus.TIMEOUT) {
                                int timeout = attempt.getExecutiontimeout();
                                Date start = attempt.getStarttime();
                                long now = System.currentTimeMillis();
                                if (now > start.getTime() + timeout * 1000 * 60) {
                                    LOG.info("attempt " + attempt.getAttemptid() + " executing timeout ");
                                    scheduler.attemptExpired(attempt.getAttemptid());
                                }
                            } else {
                                try {
                                    if (attempt.getIsautokill()) {
                                        String taskID = attempt.getTaskid();
                                        String previousAttemptID = attempt.getAttemptid();
                                        TaskAttempt newFiredAttempt = scheduler.getRecentFiredAttemptByTaskID(taskID);

                                        if (newFiredAttempt != null
                                                && !newFiredAttempt.getAttemptid().equalsIgnoreCase(previousAttemptID)) {
                                            scheduler.killAttempt(previousAttemptID);
                                            delayedAttemptZkPaths.offer(new DelayedAttemptZkPath(attempt.getExechost(), attempt.getAttemptid()));
                                        }
                                    }
                                } catch (ScheduleException e) {
                                    Cat.logError(e);
                                }
                            }
                            break;
                        case AttemptStatus.UNKNOWN:
                            scheduler.attemptUnKnown(attempt.getAttemptid());
                            delayedAttemptZkPaths.offer(new DelayedAttemptZkPath(attempt.getExechost(), attempt.getAttemptid()));
                            break;
                        default:
                            break;
                    }
                }
                Thread.sleep(Engine.SCHDUELE_INTERVAL);

            } catch (Exception ie) {
                LOG.error(ie);

                Cat.logError(ie);
            }

        }
    }

    public void isInterrupt(boolean interrupt) {
        boolean current = isInterrupt.get();
        isInterrupt.compareAndSet(current, interrupt);
    }

    public boolean isAttemptStatusMonitorRestFlag() {
        return attemptStatusMonitorRestFlag;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        Cat.logEvent("AttemptStatusMonitor", "ZkClean");
        Thread consumerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        DelayedAttemptZkPath delayedAttemptZkPath = delayedAttemptZkPaths.take();
                        if(lionValue){
                            zkCleanerExecutorService.submit(new CleanTask(delayedAttemptZkPath.getIp(), delayedAttemptZkPath.getAttemptId()));
                        }
                    } catch (InterruptedException e) {
                        //
                    }
                }
            }
        });
        consumerThread.start();
    }

    @Override
    protected String getKey() {
        return ZK_CLEANER;
    }

    @Override
    protected Boolean getDefaultValue() {
        return false;
    }

    @Override
    protected Converter<Boolean> getConvert() {
        return new BooleanConverter();
    }

    static class AttemptContextComparator implements Comparator<AttemptContext> {

        @Override
        public int compare(AttemptContext a0, AttemptContext a1) {
            return a0.getAttemptid().compareToIgnoreCase(a1.getAttemptid());
        }
    }

    class CleanTask extends Thread {

        private String ip;

        private String attemptId;

        public CleanTask(String ip, String attemptId) {
            this.ip = ip;
            this.attemptId = attemptId;
        }

        @Override
        public void run() {
            boolean res = zookeeper.cleanAttemptNode(ip, attemptId);
            Cat.logEvent("ZkCleaner", attemptId + ":" + res);
        }
    }

    public static void main(String[] args) {
        TaskAttempt attempt1 = new TaskAttempt();
        Task task = new Task();
        attempt1.setAttemptid("123_1");
        AttemptContext attemptContext1 = new AttemptContext(attempt1, task);
        TaskAttempt attempt2 = new TaskAttempt();
        attempt2.setAttemptid("123_0");
        AttemptContext attemptContext2 = new AttemptContext(attempt2, task);
        List<AttemptContext> attemptList = new ArrayList<AttemptContext>();
        attemptList.add(attemptContext1);
        attemptList.add(attemptContext2);
        System.out.println(attemptList);
        Collections.sort(attemptList, new AttemptContextComparator());
        System.out.println(attemptList);
    }
}
