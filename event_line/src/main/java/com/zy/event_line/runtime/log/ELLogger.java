package com.zy.event_line.runtime.log;

import com.zy.event_line.runtime.RunTimeConfig;

/**
 * create by zuyuan on 2020/5/8
 */
public class ELLogger implements ILogger {

    private ILogger logger;

    private LogLevel level;

    private static volatile ILogger insatnce;

    private ELLogger() {
        ILogger logger = RunTimeConfig.getLogger();
        if (logger == null) {
            throw new NullPointerException("EventLine: the logger must ne not null.");
        }
        this.logger = logger;
        this.level = RunTimeConfig.getLogLevel();
    }

    public static ILogger getInstance() {
        if (ELLogger.insatnce == null) {
            ELLogger.insatnce = Holder.INSTANCE;
        }
        return ELLogger.insatnce;
    }

    public static void debug(String msg) {
        getInstance().logDebug(msg);
    }

    public static void warn(String msg) {
        getInstance().logWarn(msg);
    }

    public static void error(String msg) {
        getInstance().logError(msg);
    }

    @Override
    public void logDebug(String msg) {
        if (level.ifNotLessThanLevel(LogLevel.DEBUG)) {
            logger.logDebug(msg);
        }
    }

    @Override
    public void logWarn(String msg) {
        if (level.ifBiggerThanLevel(LogLevel.DEBUG)) {
            logger.logWarn(msg);
        }
    }

    @Override
    public void logError(String msg) {
        if (level == LogLevel.ERROR) {
            logger.logError(msg);
        }
    }

    static class Holder {
        static final ILogger INSTANCE = new ELLogger();
    }
}
