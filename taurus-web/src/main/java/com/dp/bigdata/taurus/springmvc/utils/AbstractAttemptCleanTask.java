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

    private static final String ATTEMPT_CLEANUP = "taurus.taskattempt.cleanup";

    private static final int CLEAR_HOUR = 1;

    private static final int RESERVE_MONTH = 1;

    private static final int RESERVE_RECORD = 100;

    @Autowired
    protected TaskAttemptMapper taskAttemptMapper;

    @Scheduled(cron = "30 2/30 * * * ?")
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
        startCal.add(Calendar.HOUR_OF_DAY, getCleanHour());
        startTime = startCal.getTime();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -getReserveMonth());
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

    protected TaskAttempt retrieveNthTaskAttempt(String taskId, int N){

        TaskAttemptExample example = new TaskAttemptExample();
        example.or().andTaskidEqualTo(taskId).andEndtimeIsNotNull();
        String orderByClause = "endTime desc limit " + N + ",1";
        example.setOrderByClause(orderByClause);
        List<TaskAttempt> taskAttemptList = taskAttemptMapper.selectByExample(example);
        if (taskAttemptList != null && !taskAttemptList.isEmpty()) {
            return taskAttemptList.get(0);
        }
        return null;
    }

    protected int countOfTaskAttempt(String taskId){
        TaskAttemptExample example = new TaskAttemptExample();
        example.or().andTaskidEqualTo(taskId);
        int count = taskAttemptMapper.countByExample(example);
        return count;
    }

    protected abstract int doDeleteTaskAttempts(Date endTime);

    protected int getCleanHour(){
        return CLEAR_HOUR;
    }

    protected int getReserveMonth(){
        return RESERVE_MONTH;
    }

    protected int getReserveRecord(){
        return RESERVE_RECORD;
    }

    @Override
    protected String getKey() {
        return ATTEMPT_CLEANUP;
    }
}
