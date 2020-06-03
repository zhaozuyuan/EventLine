package com.zy.event_line.service.api.receive;

import androidx.annotation.Nullable;

import com.zy.event_line.service.api.pool.IStatusReader;
import com.zy.event_line.service.api.schedule.IScheduler;

/**
 * create by zuyuan on 2020/5/30
 */
public abstract class IEventStrategy {

    /**
     * on receive event, user can sense thread state and switch.
     *
     * @param statusReader
     * @param threadScheduler can use it to switch thread.
     * @return control event transmit.
     */
    public boolean withCustomStatus(@Nullable IStatusReader statusReader,
                                         IScheduler threadScheduler) {
        return true;
    }

    public void scheduleThread1(IScheduler threadScheduler) { }
}
