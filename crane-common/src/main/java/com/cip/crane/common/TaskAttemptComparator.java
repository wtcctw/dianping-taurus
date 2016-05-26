package com.cip.crane.common;

import com.cip.crane.generated.module.TaskAttempt;

import java.util.Comparator;

/**
 * Author   mingdongli
 * 16/4/19  下午1:06.
 */
public class TaskAttemptComparator implements Comparator<TaskAttempt>{

    @Override
    public int compare(TaskAttempt t0, TaskAttempt t1) {
        return t0.getScheduletime().before(t1.getScheduletime()) == true ? -1 :
                (t0.getScheduletime().after(t1.getScheduletime()) ? 1 : 0);
    }
}
