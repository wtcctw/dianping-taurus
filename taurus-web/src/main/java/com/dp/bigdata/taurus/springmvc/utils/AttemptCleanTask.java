package com.dp.bigdata.taurus.springmvc.utils;

import com.dianping.cat.Cat;
import com.dp.bigdata.taurus.common.utils.SleepUtils;
import com.dp.bigdata.taurus.generated.module.Task;
import com.dp.bigdata.taurus.generated.module.TaskAttempt;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.Map;

/**
 * Author   mingdongli
 * 16/5/8  上午11:33.
 */
//@Component
public class AttemptCleanTask extends AbstractAttemptCleanTask {

    @Override
    protected int doDeleteTaskAttempts(Date endTime) {
        int deleted = taskAttemptMapper.deleteTaskAttemptsByEndTime(endTime);
        Cat.logEvent(getClass().getSimpleName(), String.format("%s:%d", endTime.toString(), deleted));
        return deleted;
    }

    @Scheduled(cron = "45 2/30 * * * ?")
    public void fixSizeRecord() {  //每30分钟执行一次

        if (lionValue && leaderElector.amILeader()) {

            SleepUtils.sleep(90000);  //等待90秒，备份完成。
            int recordCount;
            Map<String, Task> registedTask = scheduler.getAllRegistedTask();
            for (String taskId : registedTask.keySet()) {
                int count = countOfTaskAttempt(taskId);
                if (count > getReserveRecord()) {
                    TaskAttempt taskAttempt = retrieveNthTaskAttempt(taskId, getReserveRecord());
                    if (taskAttempt != null) {
                        Date date = taskAttempt.getEndtime();
                        recordCount = taskAttemptMapper.deleteTaskAttempts(date, taskId);
                        Cat.logEvent(getClass().getSimpleName(), String.format("%s:%s:%d", new Date().toString(), taskId, recordCount));
                    }
                }
            }
        }
    }

}
