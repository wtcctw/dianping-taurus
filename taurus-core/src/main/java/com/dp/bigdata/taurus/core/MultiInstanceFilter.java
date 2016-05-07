package com.dp.bigdata.taurus.core;

import com.dianping.cat.Cat;
import com.dp.bigdata.taurus.alert.MailHelper;
import com.dp.bigdata.taurus.alert.WeChatHelper;
import com.dp.bigdata.taurus.core.listener.DependPassAttemptListener;
import com.dp.bigdata.taurus.core.listener.GenericAttemptListener;
import com.dp.bigdata.taurus.core.structure.Converter;
import com.dp.bigdata.taurus.core.structure.ListStringConverter;
import com.dp.bigdata.taurus.generated.mapper.UserMapper;
import com.dp.bigdata.taurus.generated.module.User;
import com.dp.bigdata.taurus.generated.module.UserExample;
import com.dp.bigdata.taurus.lion.AbstractLionPropertyInitializer;
import com.dp.bigdata.taurus.lion.ConfigHolder;
import com.dp.bigdata.taurus.lion.LionKeys;
import com.dp.bigdata.taurus.utils.ThreadUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * MultiInstanceFilter
 *
 * @author damon.zhu
 */
public class MultiInstanceFilter extends AbstractLionPropertyInitializer<List<String>> implements Filter<List<String>> {

    private static final String NOT_ALERT = "taurus.web.taskblock.notalert";  //白名单

    private static final String SERVER_NAME = "taurus.web.serverName";

    private static final String DEPEND_ALERT = "taurus.web.issenddependpassalert";  //重启时秒级调度阻塞不告警

    private static final int ALERT_SILENCE_MAX_COUNT = 30;

    private Filter next;

    private Scheduler scheduler;

    public static HashMap<String, Integer> jobAlert = new HashMap<String, Integer>();

    private List<DependPassAttemptListener> dependPassAttemptListeners = new ArrayList<DependPassAttemptListener>();

    private String serverName;

    private String isSendDependAlert = "true";

    @Autowired
    private UserMapper userMapper;

    private ExecutorService alertExecutor = ThreadUtils.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2, "AlertSender");

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        serverName = lionDynamicConfig.get(SERVER_NAME);
        isSendDependAlert = lionDynamicConfig.get(DEPEND_ALERT);
    }

    @Autowired
    public MultiInstanceFilter(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public List<AttemptContext> filter(List<AttemptContext> contexts) {
        HashMap<String, AttemptContext> maps = new HashMap<String, AttemptContext>();

        for (AttemptContext context : contexts) {
            String taskId = context.getTaskid();
            List<AttemptContext> runnings = scheduler.getRunningAttemptsByTaskID(taskId);
            AttemptContext ctx = maps.get(taskId);

            if (runnings != null && runnings.size() > 0) {
                //TODO 根据用户设置，决定是否设置拥塞的任务调度的新调度为过期状态，不执行
                if (context.getTask().getIskillcongexp()) {
                    scheduler.expireCongestionAttempt(context.getAttemptid());
                    continue;
                }
                // 拥堵了~应该告警用户任务堵住了~
                Integer jobAlertCount = null;
                if (jobAlert.containsKey(taskId)) {
                    jobAlertCount = jobAlert.get(taskId);
                }

                if (null == jobAlertCount) {
                    jobAlert.put(taskId, 0);
                } else if (jobAlertCount == 0) {
                    boolean needAlert = true;
                    for (String notalert : lionValue) {
                        if (notalert.equals(context.getName())) {
                            needAlert = false;
                            break;
                        }
                    }

                    if (needAlert && context.getIsnotconcurrency()) {
                        // 告警加入domain
                        String alertontext = "您好，你的Taurus Job【"
                                + context.getTask().getName()
                                + "】发生拥堵，请及时关注，谢谢~"
                                + "作业调度历史："
                                + serverName
                                + "/attempt?taskID="
                                + taskId;
                        alertExecutor.submit(new AlertTask(context, alertontext));
                    }

                    jobAlert.put(taskId, jobAlertCount + 1);

                } else {
                    jobAlert.put(taskId, jobAlertCount + 1);
                }

            } else {

                Integer jobAlertCount = null;
                if (jobAlert.containsKey(taskId)) {
                    jobAlertCount = jobAlert.get(taskId);
                }

                if (jobAlertCount != null) {
                    //如果超出了静默告警数，者清除MAP中得count，就会重新告警一次，默认每20个拥堵告警一次
                    if (jobAlertCount >= ALERT_SILENCE_MAX_COUNT) {
                        jobAlert.remove(taskId);
                    }
                }
                //这里控制同时只有一个执行
                if (ctx == null) {
                    maps.put(taskId, context);
                }else{ //是否允许多实例同时执行
                    boolean isnotconcurrency = context.getIsnotconcurrency();
                    if(!isnotconcurrency){
                        maps.put(taskId, context);
                    }
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

    @Override
    public void registerAttemptListener(GenericAttemptListener genericAttemptListener) {
        if (genericAttemptListener instanceof DependPassAttemptListener) {
            dependPassAttemptListeners.add((DependPassAttemptListener) genericAttemptListener);
        }
    }

    public Filter getNext() {
        return next;
    }

    public void setNext(Filter next) {
        this.next = next;
    }

    @Override
    protected String getKey() {
        return NOT_ALERT;
    }

    @Override
    protected List<String> getDefaultValue() {
        return Collections.emptyList();
    }

    @Override
    protected Converter<List<String>> getConvert() {
        return new ListStringConverter();
    }

    @Override
    public List<String> fetchLionValue() {
        return lionValue;
    }

    @Override
    public void onConfigChange(String key, String value) throws Exception {

        if (key != null && key.equals(getKey())) {
            logger.info("[onChange][" + getKey() + "]" + value);
            lionValue = converter.convertTo(value.trim());
        } else if (key != null && key.equals(DEPEND_ALERT)) {
            logger.info("[onChange][" + DEPEND_ALERT + "]" + value);
            isSendDependAlert = value;
        } else {
            logger.info("not match");
        }
    }

    class AlertTask extends Thread {

        private String content;

        private AttemptContext context;

        public AlertTask(AttemptContext context, String content) {
            this.context = context;
            this.content = content;
        }

        @Override
        public void run() {

            if ("true".equalsIgnoreCase(isSendDependAlert)) {
                sendAlerm(context, content);
            }else {
                Cat.logEvent("DependAlertSwitch", isSendDependAlert + ":" + context.getName());
            }
        }

        private void sendAlerm(AttemptContext context, String content) {
            String alertAdmin = ConfigHolder.get(LionKeys.CONGESTION_ADMIN_USER);
            WeChatHelper.sendWeChat(alertAdmin, content, "Taurus-Job拥塞告警服务", ConfigHolder.get(LionKeys.ADMIN_WECHAT_AGENTID));
            WeChatHelper.sendWeChat(context.getCreator(), content, "Taurus-Job拥塞告警服务", "12");
            try {
                UserExample example = new UserExample();
                String creator = context.getCreator();
                example.or().andNameEqualTo(creator);
                List<User> users = userMapper.selectByExample(example);
                String mailTo;

                if (users != null && !users.isEmpty()) {
                    mailTo = users.get(0).getMail();
                    MailHelper.sendMail(mailTo, content, "Taurus-Job拥塞告警服务");
                } else {
                    MailHelper.sendMail(creator + "@dianping.com", content, "Taurus-Job拥塞告警服务");
                    MailHelper.sendMail(creator + "@meituan.com", content, "Taurus-Job拥塞告警服务");
                }

            } catch (MessagingException e) {
                Cat.logError(e);
            }
        }
    }
}