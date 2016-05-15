package com.dp.bigdata.taurus.common.alert;

/**
 * 运维告警接口异常
 * @author chenchongze
 *
 */
public class OpsAlarmException extends Exception {

	private static final long serialVersionUID = -5719937676795452369L;

	public OpsAlarmException(String msg) {
		super(msg);
	}
}
