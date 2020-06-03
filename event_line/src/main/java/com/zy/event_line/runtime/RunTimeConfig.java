package com.zy.event_line.runtime;

import android.os.Handler;
import android.os.Looper;

import com.zy.event_line.runtime.log.ILogger;
import com.zy.event_line.runtime.log.LogLevel;

import java.util.concurrent.Executor;

/**
 * create by zuyuan on 2020/5/7
 */
public class RunTimeConfig {

    private static LogLevel logLevel;

    private static ILogger logger;

    // find super class of event class.
    private static boolean canFindEventParent;

    private static volatile boolean hasInit = false;

    private static Executor ioExecutor;

    private static Executor cpuExecutor;

    private static  Executor singleExecutor;

    private static Handler mainHandler;

    /**
     * prohibit direct call to this method !!!
     */
    public static void init(ConfigParams params) {
        if (!RunTimeConfig.hasInit) {
            RunTimeConfig.logger = params.getLogger();
            RunTimeConfig.logLevel = params.getLogLevel();
            RunTimeConfig.canFindEventParent = params.isCanFindEventParent();
            RunTimeConfig.cpuExecutor = params.getCPUExecutor();
            RunTimeConfig.ioExecutor = params.getIOExecutor();
            RunTimeConfig.singleExecutor = params.getSingleExecutor();
            mainHandler = new Handler(Looper.getMainLooper());
            hasInit = true;
        } else {
            throw new IllegalStateException(
                    "EventLine: the RunTimeConfig.init() can call only once.");
        }
    }

    public static LogLevel getLogLevel() {
        return logLevel;
    }

    public static ILogger getLogger() {
        return logger;
    }

    public static Executor getSingleExecutor() {
        return singleExecutor;
    }

    public static boolean isCanFindEventAllTypes() {
        return canFindEventParent;
    }

    public static Executor getCPUThread() {
        return cpuExecutor;
    }

    public static Executor getIOExecutor() {
        return ioExecutor;
    }

    public static Handler getMainHandler() {
        return mainHandler;
    }
}
