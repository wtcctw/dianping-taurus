package com.dp.bigdata.taurus.springmvc.utils;

/**
 * Created by mingdongli on 16/5/13.
 */
public enum AlertType {

    EMAIL  (  1,  "邮件"),
    WECHAT (  2,  "微信"),
    DAXIANG(  3,  "大象");

    private int num;

    private String alertType;

    AlertType(int num , String alertType) {
        this.num = num;
        this.alertType = alertType;
    }

    public static String findByNum(int num) {
        for (AlertType type : values()) {
            if (num == type.getNum()) {
                return type.getAlertType();
            }
        }
        throw new RuntimeException("Error num : " + num);
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }
}
