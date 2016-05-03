package com.dp.bigdata.taurus.springmvc.utils;

/**
 * Author   mingdongli
 * 16/4/28  下午2:14.
 */
public class TaurusApiException extends Exception{

    private Integer code;

    private String message;

    public TaurusApiException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
