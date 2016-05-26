package com.cip.crane.common.netty.exception;

/**
 * Author   mingdongli
 * 16/5/18  上午10:26.
 */
public class JsonDeserializedException extends RuntimeException {

    private static final long serialVersionUID = -869823934291269320L;

    public JsonDeserializedException(String message) {
        super(message);
    }

    public JsonDeserializedException(Throwable cause) {
        super(cause);
    }

    public JsonDeserializedException(String message, Throwable cause) {
        super(message, cause);
    }

}
