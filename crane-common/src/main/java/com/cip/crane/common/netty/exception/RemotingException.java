package com.cip.crane.common.netty.exception;

/**
 * Author   mingdongli
 * 16/5/18  上午10:16.
 */
public class RemotingException extends Exception {

    private static final long serialVersionUID = -1L;


    public RemotingException(String message) {
        super(message);
    }


    public RemotingException(String message, Throwable cause) {
        super(message, cause);
    }

}
