package com.dp.bigdata.taurus.springmvc.utils;

import com.dianping.cat.Cat;
import com.dp.bigdata.taurus.common.utils.EnvUtils;
import com.dp.bigdata.taurus.common.utils.SleepUtils;
import com.dp.bigdata.taurus.generated.mapper.AttemptBackupMapper;
import com.dp.bigdata.taurus.generated.module.Task;
import com.dp.bigdata.taurus.generated.module.TaskAttempt;
import com.dp.bigdata.taurus.generated.module.TaskAttemptExample;
import com.dp.bigdata.taurus.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Author   mingdongli
 * 16/5/10  下午6:32.
 */
@Component
public class AttemptBackupTask extends AbstractAttemptCleanTask {

    private static final int BATCH_SIZE = 1000;

    @Autowired
    private AttemptBackupMapper attemptBackupMapper;

    @Scheduled(cron = "0 2/10 * * * ?")
    public void taskAttemptBackupExecute() {

        if (leaderElector.amILeader()) {
            backupDatabase();
        }

    }

    @Scheduled(cron = "45 2/30 * * * ?")
    public void fixSizeRecord() {  //每30分钟执行一次，相对备份延迟45秒，备份完后删除

        if (lionValue && leaderElector.amILeader()) {
            SleepUtils.sleep(60000);  //等待60秒，备份完成。
            int recordCount;
            Map<String, Task> registedTask = scheduler.getAllRegistedTask();
            for (String taskId : registedTask.keySet()) {
                int count = countOfTaskAttempt(taskId);
                if (count > getReserveRecord()) {
                    TaskAttempt taskAttempt = retrieveNthTaskAttempt(taskId, getReserveRecord());
                    if (taskAttempt != null) {
                        Date date = taskAttempt.getEndtime();
                        recordCount = attemptBackupMapper.deleteTaskAttempts(date, taskId);
                        Cat.logEvent(getClass().getSimpleName(), String.format("delete:%s:%s:%d", new Date().toString(), taskId, recordCount));
                    }
                }
            }
        }
    }

    private void  backupDatabase() {

        Date stopDate = new Date();
        Date startDate;
        TaskAttempt firstTaskAttempt = retrieveLastBackupTaskAttempt();

        if (firstTaskAttempt == null) {
            startDate = DateUtils.NMonthAgo(getReserveMonth()).getTime();
            TaskAttempt taskAttempt = retrieveFirstTaskAttempt();
            if (taskAttempt == null) { //没有数据
                return;
            } else {
                Date initDate = taskAttempt.getEndtime();
                initDate = DateUtils.zeroMinute(initDate);
                if (initDate.after(startDate)) {
                    startDate = initDate;
                }
            }
        } else {
            startDate = firstTaskAttempt.getEndtime();
            Date firstDate = DateUtils.NMonthAgo(getReserveMonth()).getTime();
            if(startDate.before(firstDate)){
                startDate = firstDate;
            }
            startDate = DateUtils.zeroOrThirtyMinute(startDate);
            startDate = DateUtils.zeroSecond(startDate);
        }

        Date nextHalfHour = DateUtils.nextHalfHour(startDate);
        List<TaskAttempt> taskAttemptList = new ArrayList<TaskAttempt>();
        int time = 0;
        while (taskAttemptList != null && taskAttemptList.isEmpty() && nextHalfHour.before(stopDate)) { //防止中间某天没有数据
            taskAttemptList = taskAttemptMapper.getTaskAttempt(startDate, nextHalfHour);
            if (taskAttemptList != null && !taskAttemptList.isEmpty()) {
                int size = taskAttemptList.size();

                try {
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
                    if(EnvUtils.isProduct() && time < 10){  //加快线上备份
                        startDate = nextHalfHour;
                        nextHalfHour = DateUtils.nextHalfHour(nextHalfHour);
                        taskAttemptList.clear();
                        time++;
                        SleepUtils.sleep(500);
                        continue;
                    }
                    break;
                }catch (Exception e){
                    startDate = nextHalfHour;  //死循环
                    nextHalfHour = DateUtils.nextHalfHour(nextHalfHour);
                    Cat.logEvent(getClass().getSimpleName(), String.format("InfiniteLoop:%s:%s:%d", startDate.toString(), nextHalfHour.toString(), taskAttemptList.size()));
                    taskAttemptList.clear();
                    continue;
                }

            }
            startDate = nextHalfHour;
            nextHalfHour = DateUtils.nextHalfHour(nextHalfHour);
        }
    }

    @Override
    protected int doDeleteTaskAttempts(Date endTime) {
        int deleted = attemptBackupMapper.deleteTaskAttemptsByEndTime(endTime);
        int recordCount = 0;
        Map<String, Task> registedTask = scheduler.getAllRegistedTask();
        for (String taskId : registedTask.keySet()) {
            int count = countOfTaskAttempt(taskId);
            if (count > getReserveRecord()) {
                TaskAttempt taskAttempt = retrieveNthTaskAttempt(taskId, getReserveRecord());
                if (taskAttempt != null) {
                    Date date = taskAttempt.getEndtime();
                    recordCount = attemptBackupMapper.deleteTaskAttempts(date, taskId);
                }
            }
        }
        Cat.logEvent(getClass().getSimpleName(), String.format("%s:%d:%d", endTime.toString(), deleted, recordCount));
        return deleted;
    }

    private TaskAttempt retrieveTaskAttempt(String orderByClause) {
        TaskAttemptExample example = new TaskAttemptExample();
        example.or().andEndtimeIsNotNull();
        example.setOrderByClause(orderByClause);
        List<TaskAttempt> taskAttemptList = attemptBackupMapper.selectByExample(example);
        if (taskAttemptList != null && !taskAttemptList.isEmpty()) {
            return taskAttemptList.get(0);
        }
        return null;
    }

    protected int countOfTaskAttempt(String taskId) {
        TaskAttemptExample example = new TaskAttemptExample();
        example.or().andTaskidEqualTo(taskId);
        int count = attemptBackupMapper.countByExample(example);
        return count;
    }

    protected TaskAttempt retrieveNthTaskAttempt(String taskId, int N) {

        TaskAttemptExample example = new TaskAttemptExample();
        example.or().andTaskidEqualTo(taskId).andEndtimeIsNotNull();
        String orderByClause = "endTime desc limit " + N + ",1";
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

    protected int getReserveRecord() {
        return 300;
    }

}
