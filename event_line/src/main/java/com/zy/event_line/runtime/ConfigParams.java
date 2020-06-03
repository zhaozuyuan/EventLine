package com.zy.event_line.runtime;

import com.zy.event_line.runtime.log.DefaultLogger;
import com.zy.event_line.runtime.log.ILogger;
import com.zy.event_line.runtime.log.LogLevel;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ConfigParams {

    private ILogger logger;

    private LogLevel logLevel = LogLevel.DEBUG;

    private boolean canFindEventParent = true;

    private Executor cpuExecutor;

    private Executor ioExecutor;

    private Executor singleExecutor;

    public ConfigParams setLogger(ILogger logger) {
        this.logger = logger;
        return this;
    }

    public ConfigParams setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
        return this;
    }

    public ConfigParams setCanFindEventParent(boolean canFindEventParent) {
        this.canFindEventParent = canFindEventParent;
        return this;
    }

    public ILogger getLogger() {
        if (logger == null) {
            logger = new DefaultLogger();
        }
        return logger;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public boolean isCanFindEventParent() {
        return canFindEventParent;
    }

    public ConfigParams setCPUExecutor(Executor cpuExecutor) {
        this.cpuExecutor = cpuExecutor;
        return this;
    }

    public ConfigParams setIOExecutor(Executor ioExecutor) {
        this.ioExecutor = ioExecutor;
        return this;
    }

    public Executor getCPUExecutor() {
        if (cpuExecutor == null) {
            int size = Runtime.getRuntime().availableProcessors();
            cpuExecutor = Executors.newScheduledThreadPool(size);
        }
        return cpuExecutor;
    }

    public Executor getIOExecutor() {
        if (ioExecutor == null) {
            int maxSize = 128;
            int corePoolSize = 0;
            long keepAliveTime = 30L;
            ioExecutor = new ThreadPoolExecutor(corePoolSize, maxSize, keepAliveTime,
                    TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
        }
        return ioExecutor;
    }

    public Executor getSingleExecutor() {
        if (singleExecutor == null) {
            singleExecutor = Executors.newSingleThreadExecutor();
        }
        return singleExecutor;
    }

    public ConfigParams setSingleExecutor(Executor singleExecutor) {
        this.singleExecutor = singleExecutor;
        return this;
    }
}