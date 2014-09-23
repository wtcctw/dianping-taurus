package com.dp.bigdata.taurus.core;

import java.util.ArrayList;
import java.util.List;

import com.dianping.lion.EnvZooKeeperConfig;
import com.dianping.lion.client.ConfigCache;
import com.dianping.lion.client.LionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.cat.Cat;

/**
 * MaximumConcurrentTaskFilter
 * 
 * @author damon.zhu
 */
public class MaximumConcurrentTaskFilter implements Filter {

    private Filter next;
    private Scheduler scheduler;

    @Autowired
    public MaximumConcurrentTaskFilter(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public List<AttemptContext> filter(List<AttemptContext> contexts) {
        List<AttemptContext> results;
        String maxJobNums;
        try {
            maxJobNums  =  ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.engine.maxtasknum");
        } catch (LionException e) {
            maxJobNums = "100";
        }
        int max = Integer.parseInt(maxJobNums) - scheduler.getAllRunningAttempt().size();

        if (max <= 0) {
            results = new ArrayList<AttemptContext>();
            
            Cat.logEvent("Exception", "Capacity-Error");
        } else if (max >= contexts.size()) {
            results = contexts;
        } else {
            results = contexts.subList(0, max);
        }

        if (next != null) {
            return next.filter(results);
        } else {
            return results;
        }
    }

    public Filter getNext() {
        return next;
    }

    public void setNext(Filter next) {
        this.next = next;
    }
}
