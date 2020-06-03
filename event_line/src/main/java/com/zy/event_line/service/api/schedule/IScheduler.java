package com.zy.event_line.service.api.schedule;

/**
 * create by zuyuan on 2020/4/19
 */
public interface IScheduler {

    void scheduleTask(Runnable task);

    void currentThread();

    void postMainThread();

    void toMainThread();

    void toIOThread();

    void toCPUThread();

    void toSingleThread();

    void toNewThread();
}
