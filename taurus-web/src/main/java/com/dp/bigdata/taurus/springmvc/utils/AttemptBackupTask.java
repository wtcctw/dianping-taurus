package com.dp.bigdata.taurus.springmvc.utils;

import com.dianping.cat.Cat;
import com.dp.bigdata.taurus.generated.mapper.AttemptBackupMapper;
import com.dp.bigdata.taurus.generated.module.TaskAttempt;
import com.dp.bigdata.taurus.generated.module.TaskAttemptExample;
import com.dp.bigdata.taurus.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Author   mingdongli
 * 16/5/10  下午6:32.
 */
@Component
public class AttemptBackupTask extends AbstractAttemptCleanTask {

    private static final int BATCH_SIZE = 1000;

    @Autowired
    private AttemptBackupMapper attemptBackupMapper;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void taskAttemptBackupExecute() {

        if (leaderElector.amILeader()) {
            backupDatabase();
        }

    }

    private void backupDatabase() {

        Date stopDate = new Date();
        Date startDate;
        TaskAttempt firstTaskAttempt = retrieveLastBackupTaskAttempt();

        if (firstTaskAttempt == null) {
            startDate = DateUtils.yesterdayOfLastYear().getTime();
            TaskAttempt taskAttempt = retrieveFirstTaskAttempt();
            if (taskAttempt == null) { //没有数据
                return;
            } else {
                Date initDate = taskAttempt.getEndtime();
                initDate = DateUtils.zeroHour(initDate);
                if (initDate.after(startDate)) {
                    startDate = initDate;
                }
            }
        } else {
            startDate = firstTaskAttempt.getEndtime();
            startDate = DateUtils.tomorrow(startDate);
            startDate = DateUtils.zeroHour(startDate);
        }

        Date tomorrow = DateUtils.tomorrow(startDate);
        List<TaskAttempt> taskAttemptList = new ArrayList<TaskAttempt>();
        while (taskAttemptList != null && taskAttemptList.isEmpty() && tomorrow.before(stopDate)) { //防止中间某天没有数据
            taskAttemptList = taskAttemptMapper.getTaskAttempt(startDate, tomorrow);
            if (taskAttemptList != null && !taskAttemptList.isEmpty()) {
                int size = taskAttemptList.size();

                if (size <= BATCH_SIZE) {
                    attemptBackupMapper.batchiInsert(taskAttemptList);
                } else {
                    int times = 0;
                    int startIndex = times * BATCH_SIZE;
                    int stopIndex = (times + 1) * BATCH_SIZE;
                    while (stopIndex <= size) {
                        attemptBackupMapper.batchiInsert(taskAttemptList.subList(startIndex, stopIndex));
                        times++;
                        startIndex = times * BATCH_SIZE;
                        stopIndex = (times + 1) * BATCH_SIZE;
                    }
                    attemptBackupMapper.batchiInsert(taskAttemptList.subList(startIndex, size));
                }
                Cat.logEvent(getClass().getSimpleName(), String.format("backup:%s:%d", startDate.toString(), taskAttemptList.size()));
                break;
            }
            startDate = tomorrow;
            tomorrow = DateUtils.tomorrow(tomorrow);
        }
    }

    @Override
    protected int doDeleteTaskAttempts(Date endTime) {
        return attemptBackupMapper.deleteTaskAttemptsByEndTime(endTime);
    }

    private TaskAttempt retrieveTaskAttempt(String orderByClause){
        TaskAttemptExample example = new TaskAttemptExample();
        example.or().andEndtimeIsNotNull();
        example.setOrderByClause(orderByClause);
        List<TaskAttempt> taskAttemptList = attemptBackupMapper.selectByExample(example);
        if (taskAttemptList != null && !taskAttemptList.isEmpty()) {
            return taskAttemptList.get(0);
        }
        return null;
    }

    private TaskAttempt retrieveLastBackupTaskAttempt() {

        String orderByClause = "endTime desc limit 1";
        return retrieveTaskAttempt(orderByClause);
    }

    protected int getReserveMonth() {
        return 3;
    }

}
