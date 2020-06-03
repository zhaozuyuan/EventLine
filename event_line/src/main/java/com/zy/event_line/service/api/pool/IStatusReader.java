package com.zy.event_line.service.api.pool;

import androidx.annotation.Nullable;

/**
 * create by zuyuan on 2020/4/20
 */
public interface IStatusReader {

    /**
     * @return sender's status.
     */
    int readSenderStatus();
}
