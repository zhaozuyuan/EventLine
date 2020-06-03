package com.zy.event_line.runtime.log;

/**
 * create by zuyuan on 2020/5/9
 */
public interface ILogger {

    String EVENT_LINE_TAG = "EventLine";

    void logDebug(String msg);

    void logWarn(String msg);

    void logError(String msg);

}
