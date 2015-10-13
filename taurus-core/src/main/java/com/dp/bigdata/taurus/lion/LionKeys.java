package com.dp.bigdata.taurus.lion;

public enum LionKeys {

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
