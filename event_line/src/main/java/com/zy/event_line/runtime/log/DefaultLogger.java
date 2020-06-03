package com.zy.event_line.runtime.log;

import android.util.Log;

/**
 * create by zuyuan on 2020/5/23
 */
public class DefaultLogger implements ILogger {
    @Override
    public void logDebug(String msg) {
        Log.d(EVENT_LINE_TAG, msg);
    }

    @Override
    public void logWarn(String msg) {
        Log.w(EVENT_LINE_TAG, msg);
    }

    @Override
    public void logError(String msg) {
        Log.e(EVENT_LINE_TAG, msg);
    }
}
