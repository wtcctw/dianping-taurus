package com.cip.crane.client.callback;

import com.cip.crane.common.netty.protocol.ScheduleTask;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * 直接从用户传入的地址选择
 * User: hongbin03
 * Date: 16/1/8
 * Time: 下午4:59
 * MailTo: hongbin03@meituan.com
 */
public class DirectAddressSelector implements CallbackAddressSelect {

    private static final Logger logger = LoggerFactory.getLogger(DirectAddressSelector.class);
    @Override
    public String select(ScheduleTask task) {
        Set<String>  callbackAddrs =  task.getCallbackAddress();
        List<String> ns = Lists.newArrayList();
        ns.addAll(callbackAddrs);
        if (ns == null || ns.size() == 0) {
            String errMsg = "MSchedule-Client:Cant found callback address.";
            logger.error(errMsg);
            throw new RuntimeException(errMsg);
        }

        Collections.sort(ns);
        //随机获取一台task机器
        Random random = new Random();
        int randomScope = ns.size();
        int randomIndex = random.nextInt(randomScope);
        return ns.get(randomIndex);
    }
}
