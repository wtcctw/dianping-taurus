package com.dp.bigdata.taurus.client.callback;

import com.dp.bigdata.taurus.client.TaskNode;
import com.dp.bigdata.taurus.common.netty.protocol.ScheduleTask;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * 基于zk特性做节点发现
 * User: hongbin03
 * Date: 16/1/8
 * Time: 下午4:58
 * MailTo: hongbin03@meituan.com
 */
public class ZKSelector implements CallbackAddressSelect {

    private static final Logger logger = LoggerFactory.getLogger(ZKSelector.class);
    private final TaskNode taskNode;
    public  ZKSelector(TaskNode taskNode){
        this.taskNode = taskNode;
    }
    @Override
    public String select(ScheduleTask task) {
        Set<String> nodes = taskNode.getScheduleNodes();
        List<String> ns = Lists.newArrayList();
        ns.addAll(nodes);
        if (ns == null || ns.size() == 0) {
            String errMsg = "MSchedule-Client:Cant get callback address.";
            logger.error(errMsg);
            throw new RuntimeException("Maybe, Schedule nodes all crashed.");
        }

        Collections.sort(ns);
        //随机获取一台task机器
        Random random = new Random();
        int randomScope = ns.size();
        int randomIndex = random.nextInt(randomScope);
        return ns.get(randomIndex);
    }
}
