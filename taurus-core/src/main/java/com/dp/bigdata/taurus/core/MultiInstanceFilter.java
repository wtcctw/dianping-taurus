package com.dp.bigdata.taurus.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.dianping.cat.Cat;
import com.dianping.lion.EnvZooKeeperConfig;
import com.dianping.lion.client.ConfigCache;
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
                // do nothing 拥堵了~应该告警用户任务堵住了~
                if (null == jobAlert.get(context.getTaskid())  ||  jobAlert.get(context.getTaskid()) == 0) {
                    String alertontext = "您好，你的Taurus Job【" +
                            context.getTask().getName() + "】发生拥堵，请及时关注，谢谢~";
                    try {
                        MailHelper.sendWeChat("kirin.li", alertontext);
                        MailHelper.sendWeChat(context.getCreator(), alertontext);
                        MailHelper.sendMail(context.getCreator() + "@dianping.com", alertontext);
                        if (null == jobAlert.get(context.getTaskid())){
                            jobAlert.put(context.getTaskid(), 0);
                        }else {
                            jobAlert.put(context.getTaskid(), jobAlert.get(context.getTaskid()) + 1);
                        }

                    } catch (Exception e) {
                        Cat.logError(e);
                    }
                }else {
                    jobAlert.put(context.getTaskid(), jobAlert.get(context.getTaskid()) + 1);
                }

            } else {
                if (jobAlert.get(context.getTaskid()) == null && jobAlert.get(context.getTaskid()) != 0) {
                    if(jobAlert.get(context.getTaskid()) == 0){
                        //恢复了
                        String alertontext = "您好，你的Taurus Job【" +
                                context.getTask().getName() + "】拥堵状况已经恢复正常~";
                        try {
                            MailHelper.sendWeChat("kirin.li", alertontext);
                            MailHelper.sendWeChat(context.getCreator(), alertontext);
                            MailHelper.sendMail(context.getCreator() + "@dianping.com", alertontext);
                            jobAlert.remove(context.getTaskid());
                        } catch (Exception e) {
                            Cat.logError(e);
                        }

                    }
                    jobAlert.put(context.getTaskid(), jobAlert.get(context.getTaskid()) - 1);
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