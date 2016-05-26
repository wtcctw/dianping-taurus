package com.cip.crane.restlet.resource.impl;

import java.util.ArrayList;

import com.cip.crane.restlet.shared.StatusDTO;
import com.cip.crane.restlet.resource.IAttemptStatusResource;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.cip.crane.common.AttemptStatus;

/**
 * AttemptStatusResource url : http://xxx/api/status
 * 
 * @author damon.zhu
 */
public class AttemptStatusResource extends ServerResource implements IAttemptStatusResource {

    @Override
    @Get
    public ArrayList<StatusDTO> retrieve() {
        ArrayList<StatusDTO> status = new ArrayList<StatusDTO>();
        status.add(new StatusDTO(1, "成功", AttemptStatus.getInstanceRunState(AttemptStatus.SUCCEEDED)));
        status.add(new StatusDTO(1, "自动杀死", AttemptStatus.getInstanceRunState(AttemptStatus.AUTO_KILLED)));
        status.add(new StatusDTO(1, "手动杀死", AttemptStatus.getInstanceRunState(AttemptStatus.MAN_KILLED)));
        status.add(new StatusDTO(1, "超时", AttemptStatus.getInstanceRunState(AttemptStatus.TIMEOUT)));
        status.add(new StatusDTO(1, "失败", AttemptStatus.getInstanceRunState(AttemptStatus.FAILED)));
        return status;
    }

}
