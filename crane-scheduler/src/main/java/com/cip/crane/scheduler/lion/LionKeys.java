package com.cip.crane.scheduler.lion;

public enum LionKeys {

	MESSAGE_AGENTID("taurus.wechatapi.agentid"),
	
	BLACK_LIST_ID("push-wechat-service.blacklist.id"),
	
	BLACK_LIST_NAME("push-wechat-service.blacklist.name"),
	
	BLACK_LIST_EMAIL("push-wechat-service.blacklist.email");
	
	private String value;
	
	private LionKeys(String value){
		this.value = value;
	}
	
	public String value(){
		return value;
	}
}
