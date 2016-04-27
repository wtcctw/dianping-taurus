package com.dp.bigdata.taurus.core;

import com.dianping.cat.Cat;
import com.dianping.lion.EnvZooKeeperConfig;
import com.dianping.lion.client.ConfigCache;
import com.dianping.lion.client.LionException;
import com.dp.bigdata.taurus.alert.MailHelper;
import com.dp.bigdata.taurus.alert.OpsAlarmHelper;
import com.dp.bigdata.taurus.alert.WeChatHelper;
import com.dp.bigdata.taurus.core.structure.BoundedList;
import com.dp.bigdata.taurus.generated.mapper.TaskAttemptMapper;
import com.dp.bigdata.taurus.generated.mapper.TaskMapper;
import com.dp.bigdata.taurus.generated.module.Task;
import com.dp.bigdata.taurus.generated.module.TaskAttempt;
import com.dp.bigdata.taurus.generated.module.TaskAttemptExample;
import com.dp.bigdata.taurus.generated.module.TaskExample;
import com.dp.bigdata.taurus.lion.ConfigHolder;
import com.dp.bigdata.taurus.lion.LionKeys;
import com.dp.bigdata.taurus.utils.EnvUtils;
import com.dp.bigdata.taurus.utils.SleepUtils;
import com.dp.bigdata.taurus.zookeeper.execute.helper.ExecuteStatus;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.dao.DataAccessException;

import javax.mail.MessagingException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Author   mingdongli
 * 16/4/23  下午12:31.
 */
public abstract class CachedScheduler extends ConfigedScheduler implements SchedulerCache, ApplicationContextAware {

    protected Map<String, Task> registedTasks = new ConcurrentHashMap<String, Task>(); // Map<taskID, task>

    protected Map<String, String> tasksMapCache = new ConcurrentHashMap<String, String>(); // Map<name, taskID>

    protected Map<String, HashMap<String, AttemptContext>> runningAttempts = new ConcurrentHashMap<String, HashMap<String, AttemptContext>>(); // Map<taskID,HashMap<attemptID,AttemptContext>>

    //s级调度增加的4个缓存
    protected Map<String, CronExpression> registeredCron = new HashMap<String, CronExpression>();

    protected List<TaskAttempt> attemptsOfStatusInitialized = new ArrayList<TaskAttempt>();

    protected List<TaskAttempt> attemptsOfStatusDependTimeout = new ArrayList<TaskAttempt>();

    protected ConcurrentMap<String, BoundedList<TaskAttempt>> dependPassMap = new ConcurrentHashMap<String, BoundedList<TaskAttempt>>();

    @Autowired
    protected TaskAttemptMapper taskAttemptMapper;

    @Autowired
    protected TaskMapper taskMapper;

    private ApplicationContext applicationContext;

    public CachedScheduler() {
        super();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        initCache();
    }

    @Override
    public void initCache() {

        List<TaskAttempt> tmpInitialized = taskAttemptMapper.getAttemptByStatus(ExecuteStatus.INITIALIZED);
        List<TaskAttempt> tmpDependTimeout = taskAttemptMapper.getAttemptByStatus(ExecuteStatus.DEPENDENCY_TIMEOUT);
        List<TaskAttempt> tmpDependPass = taskAttemptMapper.getAttemptByStatus(ExecuteStatus.DEPENDENCY_PASS);

        if (tmpInitialized != null) {
            attemptsOfStatusInitialized.addAll(tmpInitialized);
        }
        if (tmpDependTimeout != null) {
            attemptsOfStatusDependTimeout.addAll(tmpDependTimeout);
        }
        if (tmpDependPass != null) {
            for (TaskAttempt taskAttempt : tmpDependPass) {
                addLastTaskAttempt(taskAttempt);
            }
        }

        TaskExample example = new TaskExample();
        example.or().andStatusEqualTo(TaskStatus.RUNNING);
        example.or().andStatusEqualTo(TaskStatus.SUSPEND);
        List<Task> tasks = taskMapper.selectByExample(example);
        for (Task task : tasks) {
            String cronExpression = task.getCrontab();
            try {
                CronExpression ce = new CronExpression(cronExpression);
                registeredCron.put(task.getTaskid(), ce);
            } catch (ParseException e) {
                logger.error(String.format("crontab of %s:%s is wrong", task.getName(), cronExpression));
            }
        }

    }

    @Override
    public void clearCache() {
        attemptsOfStatusInitialized.clear();
        attemptsOfStatusDependTimeout.clear();
        dependPassMap.clear();
        registeredCron.clear();
    }

    /**
     * load data from the database;
     */
    public synchronized void load() {

        Map<String, Task> tmp_registedTasks = new ConcurrentHashMap<String, Task>();
        Map<String, String> tmp_tasksMapCache = new ConcurrentHashMap<String, String>();
        Map<String, HashMap<String, AttemptContext>> tmp_runningAttempts = new ConcurrentHashMap<String, HashMap<String, AttemptContext>>();
        try {
            // load all tasks
            TaskExample example = new TaskExample();
            example.or().andStatusEqualTo(TaskStatus.RUNNING);
            example.or().andStatusEqualTo(TaskStatus.SUSPEND);
            List<Task> tasks = taskMapper.selectByExample(example);
            for (Task task : tasks) {
                tmp_registedTasks.put(task.getTaskid(), task);
                tmp_tasksMapCache.put(task.getName(), task.getTaskid());
            }

            // load running attempts
            TaskAttemptExample example1 = new TaskAttemptExample();
            example1.or().andStatusEqualTo(AttemptStatus.RUNNING);
            example1.or().andStatusEqualTo(AttemptStatus.TIMEOUT);
            List<TaskAttempt> attempts = taskAttemptMapper.selectByExample(example1);
            for (TaskAttempt attempt : attempts) {
                Task task = tmp_registedTasks.get(attempt.getTaskid());

                if (task != null) {
                    AttemptContext context = new AttemptContext(attempt, task);
                    HashMap<String, AttemptContext> contexts = new HashMap<String, AttemptContext>();
                    contexts.put(context.getAttemptid(), context);
                    tmp_runningAttempts.put(context.getTaskid(), contexts);
                }
            }

            // switch
            registedTasks = tmp_registedTasks;
            tasksMapCache = tmp_tasksMapCache;
            runningAttempts = tmp_runningAttempts;
        } catch (DataAccessException e) {
            Cat.logEvent("DataAccessException", e.getMessage());
            String dataBaseUrl = "";
            OpsAlarmHelper oaHelper = new OpsAlarmHelper();

            try {
                dataBaseUrl = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.jdbc.url");

            } catch (LionException le) {
                dataBaseUrl = "jdbc:mysql://10.1.101.216:3306/Taurus?characterEncoding=utf-8";
            }

            String exceptContext = "您好，taurus的数据库连接发生异常 请及时查看"
                    + "数据库连接串："
                    + dataBaseUrl;
            try {
                String admin = ConfigHolder.get(LionKeys.ADMIN_USER);

                if (StringUtils.isNotBlank(admin)) {
                    MailHelper.sendMail(admin + "@dianping.com", exceptContext, "Taurus数据库连接异常告警服务");
                    WeChatHelper.sendWeChat(admin, exceptContext, "Taurus数据库连接异常告警服务", ConfigHolder.get(LionKeys.ADMIN_WECHAT_AGENTID));
                }

                String reportToOps = null;
                try {
                    reportToOps = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.agent.down.ops.report.alarm.post");
                } catch (LionException le) {
                    reportToOps = "http://pulse.dp/report/alarm/post";
                    le.printStackTrace();
                }

                oaHelper.buildTypeObject("Taurus")
                        .buildTypeItem("Service")
                        .buildTypeAttribute("Status")
                        .buildSource("taurus")
                        .buildDomain(dataBaseUrl.split(":")[2].split("/")[2])
                        .buildTitle("Taurus数据库连接异常")
                        .buildContent(exceptContext)
                        .buildUrl(dataBaseUrl)
                        .buildReceiver("dpop@dianping.com")
                        .sendAlarmPost(reportToOps);

            } catch (LionException le) {
                Cat.logEvent("LionException", le.getMessage());
            } catch (MessagingException me) {
                Cat.logEvent("MessagingException", me.getMessage());
            }

        }

    }

    protected synchronized void addLastTaskAttempt(TaskAttempt taskAttempt) {

        String taskId = taskAttempt.getTaskid();
        if (StringUtils.isBlank(taskId)) {
            return;
        }

        BoundedList<TaskAttempt> taskAttemptList = dependPassMap.get(taskId);
        if (taskAttemptList == null) {
            taskAttemptList = applicationContext.getBean(BoundedList.class);
        }

        if (!taskAttemptList.addOrDiscard(taskAttempt)) {
            if (lionValue) {
                Cat.logEvent("DISCARD_DEPENDENCY_PASS", taskId);
            } else {
                taskAttemptList.add(taskAttempt);
                Cat.logEvent("READD_DEPENDENCY_PASS", taskId);
            }
        }

        dependPassMap.put(taskId, taskAttemptList);
    }

    protected void loadTaskAttempt(String taskId) {
        List<TaskAttempt> tmpDependTimeout = taskAttemptMapper.selectDependencyTask(taskId, ExecuteStatus.DEPENDENCY_TIMEOUT);
        if (tmpDependTimeout != null) {
            attemptsOfStatusDependTimeout.addAll(tmpDependTimeout);
        }

        List<TaskAttempt> tmpDependPass = taskAttemptMapper.selectDependencyTask(taskId, ExecuteStatus.DEPENDENCY_PASS);
        if (tmpDependPass != null && !tmpDependPass.isEmpty()) {
            BoundedList<TaskAttempt> origin = dependPassMap.get(taskId);
            if (origin == null) {
                origin = applicationContext.getBean(BoundedList.class);
            }
            origin.addAll(tmpDependPass);
            dependPassMap.put(taskId, origin);
        }
    }


    protected void removeTaskAttempt(String taskId, boolean delete) {

        List<TaskAttempt> removed = new ArrayList<TaskAttempt>();

        if (delete) {
            BoundedList<TaskAttempt> origin = dependPassMap.get(taskId);
            if (origin != null) {
                origin.clear();
            }


            for (TaskAttempt taskAttempt : attemptsOfStatusDependTimeout) {
                if (taskAttempt.getTaskid().equals(taskId)) {
                    removed.add(taskAttempt);
                }
            }
            attemptsOfStatusDependTimeout.removeAll(removed);
            removed.clear();
        }

        for (TaskAttempt taskAttempt : attemptsOfStatusInitialized) {
            if (taskAttempt.getTaskid().equals(taskId)) {
                removed.add(taskAttempt);
            }
        }
        attemptsOfStatusInitialized.removeAll(removed);
    }

    @Override
    public List<AttemptContext> getAllRunningAttempt() {
        List<AttemptContext> contexts = new ArrayList<AttemptContext>();
        for (HashMap<String, AttemptContext> maps : runningAttempts.values()) {
            for (AttemptContext context : maps.values()) {
                contexts.add(context);
            }
        }
        return Collections.unmodifiableList(contexts);
    }

    @Override
    public List<AttemptContext> getRunningAttemptsByTaskID(String taskID) {
        List<AttemptContext> contexts = new ArrayList<AttemptContext>();
        HashMap<String, AttemptContext> maps = runningAttempts.get(taskID);

        if (maps == null) {
            return contexts;
        }

        contexts.addAll(maps.values());

        return Collections.unmodifiableList(contexts);
    }

    @Override
    public Map<String, Task> getAllRegistedTask() {
        return Collections.unmodifiableMap(registedTasks);
    }

    @Override
    public Task getTaskByName(String name) throws ScheduleException {
        if (tasksMapCache.containsKey(name)) {
            String taskID = tasksMapCache.get(name);
            Task task = registedTasks.get(taskID);
            if (task == null) {
                throw new ScheduleException("Cannot found tasks for the given name.");
            } else {
                return task;
            }
        } else {
            throw new ScheduleException("Cannot found tasks for the given name.");
        }
    }

    @Override
    public boolean isRuningAttempt(String attemptID) {
        HashMap<String, AttemptContext> contexts = runningAttempts.get(AttemptID.getTaskID(attemptID));
        AttemptContext context = contexts.get(attemptID);
        if (context == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public CronExpression getCronExpression(String taskId) {
        return registeredCron.get(taskId);
    }

    public ConcurrentMap<String, BoundedList<TaskAttempt>> getDependPassMap() {
        return dependPassMap;
    }

    @Override
    public synchronized void addOrUpdateCronCache(Task task) {

        String cronExpression = task.getCrontab();
        try {
            CronExpression ce = new CronExpression(cronExpression);
            registeredCron.put(task.getTaskid(), ce);
        } catch (ParseException e) {
            logger.error(String.format("crontab of %s:%s is wrong", task.getName(), cronExpression));
        }

    }

    @Override
    public List<TaskAttempt> getAttemptsOfStatusDependTimeout() {
        return attemptsOfStatusDependTimeout;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        this.applicationContext = applicationContext;
    }

    protected void clearInitialized() {
        attemptsOfStatusInitialized.clear();
    }

    class DependPassThread extends Thread {

        @Override
        public void run() {

            while (true) {

                while (interrupted.get()) {
                    refreshThreadRestFlag = true;
                    SleepUtils.sleep(5000);
                }
                refreshThreadRestFlag = false;

                try {
                    Thread.sleep(5 * 60 * 1000);

                    for (Map.Entry<String, BoundedList<TaskAttempt>> entry : dependPassMap.entrySet()) {
                        BoundedList<TaskAttempt> value = entry.getValue();
                        int size = value.size();
                        int capacity = value.getMaxCapacity();
                        if (size > capacity / 2) {
                            String taskId = entry.getKey();
                            Task task = registedTasks.get(taskId);
                            String name;
                            if (task != null) {
                                name = task.getName();
                            } else {
                                name = taskId;
                            }
                            List<String> whitelist = getFilter().fetchLionValue();  //增加白名单
                            if (!whitelist.contains(name)) {
                                String content = new StringBuilder().append(EnvUtils.getEnv()).append(": ").append(name).append("调度状态为DEPENDENCY_PASS的个数为")
                                        .append(size).append("个, 如果再不处理，拥堵个数达到").append(capacity).append("后，新的调度实例将被丢弃").toString();
                                sendAlarm(ConfigHolder.get(LionKeys.ADMIN_USER), content);
                            }
                        }
                    }

                } catch (InterruptedException e) {
                    logger.error("RefreshThread was interrupted!", e);
                }

            }

        }

        private void sendAlarm(String user, String content) {
            WeChatHelper.sendWeChat(user, content, ConfigHolder.get(LionKeys.ADMIN_WECHAT_AGENTID));
        }

    }

    protected abstract Filter<List<String>> getFilter();

}
