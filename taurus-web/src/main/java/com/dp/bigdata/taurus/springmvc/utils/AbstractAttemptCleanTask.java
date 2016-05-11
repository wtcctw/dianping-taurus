package com.dp.bigdata.taurus.springmvc.utils;

import com.dianping.cat.Cat;
import com.dp.bigdata.taurus.generated.mapper.TaskAttemptMapper;
import com.dp.bigdata.taurus.generated.module.TaskAttempt;
import com.dp.bigdata.taurus.generated.module.TaskAttemptExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Author   mingdongli
 * 16/5/10  下午7:28.
 */
public abstract class AbstractAttemptCleanTask extends AbstractAttemptTask {

    private static final String ATTEEMPT_CLEANUP = "taurus.taskattempt.cleanup";

    private static final int CLEAR_DAY = 2;

    private static final int RESERVE_MONTH = -6;

    @Autowired
    protected TaskAttemptMapper taskAttemptMapper;

    @Scheduled(cron = "0/20 * * * * ?")
    public void taskAttemptCleanExecute() {

        if (lionValue && leaderElector.amILeader()) {
            doAttemptTask();
        }

    }

    private void doAttemptTask() {

        TaskAttempt firstTaskAttempt = retrieveFirstTaskAttempt();

        if (firstTaskAttempt == null) {
            return;
        }

        Date startTime = firstTaskAttempt.getEndtime();
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startTime);
        startCal.add(Calendar.DAY_OF_YEAR, getCleanDay());
        startTime = startCal.getTime();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, getReserveMonth());
        Date stopTime = cal.getTime();

        if (startTime != null && stopTime != null && startTime.before(stopTime)) {
            int deleted = doDeleteTaskAttempts(startTime);
            Cat.logEvent(getClass().getSimpleName(), String.format("%s:%d", startTime.toString(), deleted));
            logger.info(String.format("%s [end] : delete %d records", getClass().getSimpleName(), deleted));
        }
    }

    protected TaskAttempt retrieveFirstTaskAttempt() {

        TaskAttemptExample example = new TaskAttemptExample();
        example.or().andEndtimeIsNotNull();
        String orderByClause = "endTime asc limit 1";
        example.setOrderByClause(orderByClause);
        List<TaskAttempt> taskAttemptList = taskAttemptMapper.selectByExample(example);
        if (taskAttemptList != null && !taskAttemptList.isEmpty()) {
            return taskAttemptList.get(0);
        }
        return null;

    }

    protected abstract int doDeleteTaskAttempts(Date endTime);

    protected int getCleanDay(){
        return CLEAR_DAY;
    }

    protected int getReserveMonth(){
        return RESERVE_MONTH;
    }

    @Override
    protected String getKey() {
        return ATTEEMPT_CLEANUP;
    }
}
