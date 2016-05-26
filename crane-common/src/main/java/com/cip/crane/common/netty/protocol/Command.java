package com.cip.crane.common.netty.protocol;

/**
 * Author   mingdongli
 * 16/5/18  上午9:21.
 */
public abstract class Command {

    private CommandType type;

    public CommandType getType() {
        return type;
    }

    public void setType(CommandType type) {
        this.type = type;
    }
}
