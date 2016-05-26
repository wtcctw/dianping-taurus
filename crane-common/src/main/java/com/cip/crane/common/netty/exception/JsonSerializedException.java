package com.cip.crane.common.netty.exception;

/**
 * Author   mingdongli
 * 16/5/18  上午10:25.
 */
public class JsonSerializedException extends RuntimeException {

    private static final long serialVersionUID = -869823934291269320L;

    public JsonSerializedException(String message) {
        super(message);
    }

    public JsonSerializedException(Throwable cause) {
        super(cause);
    }

    public JsonSerializedException(String message, Throwable cause) {
        super(message, cause);
    }

}
