package com.cip.crane.client.schedule;

import java.util.*;

/**
 * User: hongbin03
 * Date: 16/1/25
 * Time: 下午2:12
 * MailTo: hongbin03@meituan.com
 */
public class DefaultSelector implements Selector {

    @Override
    public String select(String leaderIp, Set<String> source) {
        if (source == null || source.size() == 0) {
            throw new IllegalArgumentException("Empty selector source.");
        }

        List<String> sourceCopy = new ArrayList<String>();
        sourceCopy.addAll(source);

        //如果只有一个候选Ip，那直接使用该IP，而且该Ip肯定为LeaderIP
        if(sourceCopy.size() == 1){
            return sourceCopy.get(0);
        }

        //如果多于1个候选IP，则排除LeaderIp
        Iterator<String> iterator = sourceCopy.iterator();
        while (iterator.hasNext()){
            String candidateIp = iterator.next();
            if(candidateIp.startsWith(leaderIp)){
                iterator.remove();
                break;
            }
        }

        //随机获取一台task机器
        Random random = new Random();
        int randomScope = sourceCopy.size();
        int randomIndex = random.nextInt(randomScope);

        return sourceCopy.get(randomIndex);
    }
}
