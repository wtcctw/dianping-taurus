package com.dp.bigdata.taurus.springmvc.utils;

import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Author   mingdongli
 * 16/5/8  上午11:33.
 */
@Component
public class AttemptCleanTask extends AbstractAttemptCleanTask{

    @Override
    protected int doDeleteTaskAttempts(Date endTime) {
        return taskAttemptMapper.deleteTaskAttemptsByEndTime(endTime);
    }

}
