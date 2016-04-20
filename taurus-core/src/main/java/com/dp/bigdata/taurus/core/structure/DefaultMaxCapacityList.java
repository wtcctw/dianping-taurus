package com.dp.bigdata.taurus.core.structure;

import com.dp.bigdata.taurus.alert.WeChatHelper;
import com.dp.bigdata.taurus.lion.ConfigHolder;
import com.dp.bigdata.taurus.lion.LionKeys;
import com.dp.bigdata.taurus.utils.EnvUtils;

import java.util.ArrayList;

/**
 * Author   mingdongli
 * 16/4/20  下午3:48.
 */
public class DefaultMaxCapacityList<E> extends ArrayList<E> implements MaxCapacityList<E>{

    @Override
    public boolean addOrDiscard(E entry) {

        int size = size();
        if(size < MAX_CAPACITY_SIZE){
            add(entry);
            if(size > MAX_CAPACITY_SIZE / 2){
                sendAlarm();
            }
            return true;
        }
        return false;
    }

    private void sendAlarm(){

        String content = new StringBuilder().append(EnvUtils.getEnv()).append(": ").append("调度状态为DEPENDENCY_PASS的个数多于")
                .append(MaxCapacityList.MAX_CAPACITY_SIZE / 2).append("个, 如果再不处理，新的调度实例将被丢弃").toString();
        WeChatHelper.sendWeChat(ConfigHolder.get(LionKeys.ADMIN_USER), content, ConfigHolder.get(LionKeys.ADMIN_WECHAT_AGENTID));
    }
}
