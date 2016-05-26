package com.cip.crane.common.lion;

public enum LionKeys {

	ADMIN_WECHAT_AGENTID("taurus.wechat.admin.agentid"),
	AGENT_DOWN_MAIL_TO("taurus.agent.down.mail.to"),
	SERVER_BASE_URL("taurus.web.serverName"),
	WECHAT_API("taurus.wechat.api"),
	ON_DUTY_PHONE("taurus.onduty.phone"),
	ON_DUTY_QYQQ("taurus.onduty.qyqq"),
	ON_DUTY_ADMIN("taurus.onduty.admin"),
	CONGESTION_ADMIN_USER("taurus.task.congestion.wechat.to"),
	ADMIN_USER("taurus.dbadmin.user");

	private String value;
	
	private LionKeys(String value){
		this.value = value;
	}
	
	public String value(){
		return value;
	}
}
