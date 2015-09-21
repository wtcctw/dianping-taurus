package com.dp.bigdata.taurus.pigeon.service;

import java.util.ArrayList;

import org.restlet.resource.ClientResource;

import com.dianping.pigeon.remoting.provider.config.annotation.Service;
import com.dianping.taurus.service.TaurusHelperService;
import com.dp.bigdata.taurus.restlet.shared.AttemptDTO;
import com.dp.bigdata.taurus.restlet.utils.LionConfigUtil;

@Service
public class TaurusHelperServiceImpl implements TaurusHelperService{

	@Override
	public String getTaurusAttemptInfoByTaskID(String taskID) {
		
		String url = LionConfigUtil.RESTLET_API_BASE + "attempt?task_id=" + taskID;
		ClientResource cr = new ClientResource(url);
        cr.setRequestEntityBuffering(true);
        ArrayList<AttemptDTO> attempts = cr.get(ArrayList.class);
		return attempts.toString();
		
	}

}
