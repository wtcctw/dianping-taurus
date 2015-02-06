package com.dp.bigdata.taurus.restlet.resource.impl;

import com.dianping.cat.Cat;
import com.dp.bigdata.taurus.core.Scheduler;
import com.dp.bigdata.taurus.generated.mapper.TaskAttemptMapper;
import com.dp.bigdata.taurus.generated.mapper.TaskMapper;
import com.dp.bigdata.taurus.generated.module.Task;
import com.dp.bigdata.taurus.generated.module.TaskAttempt;
import com.dp.bigdata.taurus.restlet.resource.IHostTaskExecTime;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.lang.StringUtils;
import org.restlet.resource.ServerResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by kirinli on 15/2/2.
 */
public class HostTaskExecTime extends ServerResource implements IHostTaskExecTime {
    @Autowired
    private TaskAttemptMapper taskAttemptMapper;
    @Autowired
    private TaskMapper taskMapper;

    @Override
    public String retrieve() {
        String time = (String) getRequestAttributes().get("time");
        String ip = (String) getRequestAttributes().get("ip");

        time = time.replace("%20", " ");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
        Date now;
        String startTime;
        try {
            now = simpleDateFormat.parse(time);

        } catch (ParseException e) {
            now = new Date();
        }

        Date startDate = new Date(now.getTime() - 24 * 60 * 60 * 1000);
        startTime = simpleDateFormat.format(startDate);

        ArrayList<TaskAttempt> taskAttempts = taskAttemptMapper.getTaskAttemptsHistory(ip, startTime);
        if (taskAttempts != null && taskAttempts.size() > 0) {
            JsonArray taskExecHistorys = new JsonArray();
            HashMap<String, String> taskMap = new HashMap<String, String>();

            for (TaskAttempt taskAttempt : taskAttempts) {
                String tmp = taskMap.get(taskAttempt.getTaskid());
                long start = getBufferPos(now, taskAttempt.getStarttime());
                Date tmpDate = taskAttempt.getStarttime();
                if (tmpDate == null) {
                    tmpDate = new Date();// 如果为空默认为现在，因为该任务还未执行完成
                }

                long end = getBufferPos(now, tmpDate);
                StringBuffer runningHistoryMap = new StringBuffer();
                for (long i = start; i <= end; i++) {
                    runningHistoryMap.append(i);
                    runningHistoryMap.append("#");
                    runningHistoryMap.append(taskAttempt.getStatus());
                    if (i != end) {
                        runningHistoryMap.append(",");
                    }
                }
                if (StringUtils.isNotBlank(tmp)) {

                    taskMap.put(taskAttempt.getTaskid(), tmp + "," + runningHistoryMap.toString());
                } else {
                    taskMap.put(taskAttempt.getTaskid(), runningHistoryMap.toString());
                }

            }

            /*
            JsonObject taskExecHistory = new JsonObject();
                taskExecHistory.addProperty("taskId", taskAttempt.getTaskid());
                taskExecHistory.addProperty("startTime", taskAttempt.getStarttime().toString());
                long start = getBufferPos(taskAttempt.getStarttime());
                long end = getBufferPos(taskAttempt.getEndtime());
                StringBuffer runningHistoryMap = new StringBuffer();
                for(long i = start; i <= end; i++){
                    runningHistoryMap.append(i);
                    if (i != end){
                        runningHistoryMap.append(",");
                    }
                }
                taskExecHistory.addProperty("runningMap", runningHistoryMap.toString());
                taskExecHistory.addProperty("endTime", taskAttempt.getEndtime().toString());
                taskExecHistory.addProperty("status", taskAttempt.getStatus());
                taskExecHistorys.add(taskExecHistory);
            * */
            Iterator iter = taskMap.entrySet().iterator();
            ArrayList<Task> taskArrayList = taskMapper.getRealTasksByHost(ip);
            HashMap<String, Task> hostTaskMap = new HashMap<String, Task>();
            for (Task task : taskArrayList) {
                hostTaskMap.put(task.getTaskid(), task);
            }
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = (String) entry.getKey();
                String val = (String) entry.getValue();

                JsonObject taskExecHistory = new JsonObject();
                Task task = hostTaskMap.get(key);
                if (task != null) {
                    taskExecHistory.addProperty("taskId", key);
                    taskExecHistory.addProperty("taskName", task.getName());
                    taskExecHistory.addProperty("creator", task.getCreator());
                    taskExecHistory.addProperty("appName", task.getAppname());
                } else {
                    taskExecHistory.addProperty("taskId", key);
                }

                taskExecHistory.addProperty("runningMap", val);
                taskExecHistorys.add(taskExecHistory);
            }

            return taskExecHistorys.toString();
        } else {
            return null;
        }

    }

    long getBufferPos(Date time, Date start) {
        if (start == null) {
            start = new Date();
        }
        Long different;
        Date end = new Date(time.getTime() - 24 * 60 * 60 * 1000);
        try {


            different = start.getTime() - end.getTime();
            different = different / (1000 * 60);
            return (different / 20 + 1);
        } catch (Exception e) {
            Cat.logError("getBufferPos Error" , e);
            return 0;
        }


    }
}
