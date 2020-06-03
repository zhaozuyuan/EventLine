package com.zy.event_line.runtime.log;

/**
 * create by zuyuan on 2020/5/8
 */
public enum LogLevel {

    /**
     * no log print.
     */
    NO_LOG(-1),

    /**
     * minimum level log.
     */
    DEBUG(100),

    /**
     * warn level log.
     */
    WARN(101),

    /**
     * top level log.
     */
    ERROR(102);

    private int value;

    LogLevel(int value) {
        this.value = value;
    }

    public boolean ifBiggerThanLevel(LogLevel level) {
        return this.value > level.value;
    }

    public boolean ifNotLessThanLevel(LogLevel level) {
        return this.value >= level.value;
    }
}
