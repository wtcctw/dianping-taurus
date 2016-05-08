package com.dp.bigdata.taurus.springmvc.utils;

import com.dp.bigdata.taurus.generated.mapper.TaskAttemptMapper;
import com.dp.bigdata.taurus.generated.module.TaskAttempt;
import com.dp.bigdata.taurus.generated.module.TaskAttemptExample;
import com.dp.bigdata.taurus.zookeeper.common.elect.LeaderElector;
import com.dp.bigdata.taurus.zookeeper.common.infochannel.guice.LeaderElectorChanelModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Author   mingdongli
 * 16/5/8  上午11:33.
 */
@Component
public class TaskAttemptCleanTask {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final int CLEAR_DAY = 2;

    private static final int RESERVE_MONTH = -6;

    private LeaderElector leaderElector;

    @PostConstruct
    public void initLeaderElector() {

        Injector injector = Guice.createInjector(new LeaderElectorChanelModule());
        leaderElector = injector.getInstance(LeaderElector.class);
    }

    @Autowired
    protected TaskAttemptMapper taskAttemptMapper;

    @Scheduled(cron = "0/20 * * * * ?")
    public void taskAttemptCleanExecute() {

        if(leaderElector.amILeader()){
            Date startTime = null;
            List<TaskAttempt> firstTaskAttempt = retrieveLatestTaskAttemptByTaskID();
            if (firstTaskAttempt != null && !firstTaskAttempt.isEmpty()) {
                startTime = firstTaskAttempt.get(0).getEndtime();
                Calendar startCal = Calendar.getInstance();
                startCal.setTime(startTime);
                startCal.add(Calendar.DAY_OF_YEAR, CLEAR_DAY);
                startTime = startCal.getTime();
            }

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, RESERVE_MONTH);
            Date stopTime = cal.getTime();

            if (startTime != null && stopTime != null && startTime.before(stopTime)) {
                logger.info("TaskAttemptCleanTask [begin] : " + startTime);
                int deleted = taskAttemptMapper.deleteTaskAttemptsByEndTime(startTime);
                logger.info(String.format("TaskAttempCleanTask [end] : delete %d records", deleted));
            }
        }

    }

    private List<TaskAttempt> retrieveLatestTaskAttemptByTaskID() {
        TaskAttemptExample example = new TaskAttemptExample();
        example.or().andEndtimeIsNotNull();
        String orderByClause = "endTime asc limit 1";
        example.setOrderByClause(orderByClause);
        return taskAttemptMapper.selectByExample(example);
    }

}
