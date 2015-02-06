package com.dp.bigdata.taurus.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.dianping.cat.Cat;
import com.dianping.lion.EnvZooKeeperConfig;
import com.dianping.lion.client.ConfigCache;
import com.dianping.lion.client.LionException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * MultiInstanceFilter
 *
 * @author damon.zhu
 */
public class MultiInstanceFilter implements Filter {

    private Filter next;

    private Scheduler scheduler;
    public static HashMap<String, Integer> jobAlert = new HashMap<String, Integer>();
    private static final int ALERT_SILENCE_MAX_COUNT = 19;

    @Autowired
    public MultiInstanceFilter(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public List<AttemptContext> filter(List<AttemptContext> contexts) {
        HashMap<String, AttemptContext> maps = new HashMap<String, AttemptContext>();

        for (AttemptContext context : contexts) {
            List<AttemptContext> runnings = scheduler.getRunningAttemptsByTaskID(context.getTaskid());
            AttemptContext ctx = maps.get(context.getTaskid());

            if (runnings != null && runnings.size() > 0) {
                // 拥堵了~应该告警用户任务堵住了~
                Integer jobAlertCount = null;
                if (jobAlert.containsKey(context.getTaskid())) {
                    jobAlertCount = jobAlert.get(context.getTaskid());
                }

                if (null == jobAlertCount) {
                    jobAlert.put(context.getTaskid(), 0);
                } else if (jobAlertCount == 0) {
                    String notAlertJobs;
                    try {
                        notAlertJobs = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.web.taskblock.notalert");
                    } catch (LionException e) {
                        notAlertJobs = "";
                    }
                    boolean needAlert = true;
                    if (StringUtils.isNotBlank(notAlertJobs)) {

                        String[] notAlertLists = notAlertJobs.split(",");

                        for (String notalert : notAlertLists) {
                            if (notalert.equals(context.getName())) {
                                needAlert = false;
                                break;
                            }
                        }
                    }

                    if (needAlert) {
                        String alertontext = "您好，你的Taurus Job【" +
                                context.getTask().getName() + "】发生拥堵，请及时关注，谢谢~";
                        try {
                            MailHelper.sendWeChat("kirin.li", alertontext);
                            MailHelper.sendWeChat(context.getCreator(), alertontext);
                            MailHelper.sendMail(context.getCreator() + "@dianping.com", alertontext);

                        } catch (Exception e) {
                            Cat.logError(e);
                        }
                    }


                    jobAlert.put(context.getTaskid(), jobAlertCount + 1);

                } else {
                    jobAlert.put(context.getTaskid(), jobAlertCount + 1);
                }

            } else {

                Integer jobAlertCount = null;
                if (jobAlert.containsKey(context.getTaskid())) {
                    jobAlertCount = jobAlert.get(context.getTaskid());
                }

                if (jobAlertCount != null) {
                    //如果超出了静默告警数，者清除MAP中得count，就会重新告警一次，默认每20个拥堵告警一次
                    if (jobAlertCount >= ALERT_SILENCE_MAX_COUNT) {
                        jobAlert.remove(context.getTaskid());
                    }
                }

                if (ctx == null) {
                    maps.put(context.getTaskid(), context);
                }
            }
        }

        List<AttemptContext> results = new ArrayList<AttemptContext>();
        for (AttemptContext context : maps.values()) {
            results.add(context);
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