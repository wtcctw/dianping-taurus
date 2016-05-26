package com.cip.crane.restlet.resource.impl;

import com.cip.crane.generated.mapper.AlertRuleMapper;
import com.cip.crane.restlet.resource.IUpdateAlertRule;
import org.restlet.resource.ServerResource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by kirinli on 14/11/23.
 */
public class UpdateAlertRule   extends ServerResource implements IUpdateAlertRule {
    @Autowired
    AlertRuleMapper alertRuleMapper;

    @Override
    public int retrieve() {

        int result = -1;
        String userId = (String) getRequestAttributes().get("userId");
        String jobId = (String) getRequestAttributes().get("jobId");
        result = alertRuleMapper.updateAlert(userId,jobId);
        return result;
    }
}
