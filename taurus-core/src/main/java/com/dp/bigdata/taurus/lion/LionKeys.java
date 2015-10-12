package com.dp.bigdata.taurus.lion;

public enum LionKeys {

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
