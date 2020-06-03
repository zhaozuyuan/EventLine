package com.zy.event_line.pool;

import com.zy.event_line.service.api.pool.IStatusReader;

/**
 * create by zuyuan on 2020/4/19
 */
public class StatusController implements IStatusReader {

    private int senderStatus = -1;

    /**
     * refresh sender's status, and the receiver can read it.
     */
    public void refreshSenderStatus(int status) {
        this.senderStatus = status;
    }

    @Override
    public int readSenderStatus() {
        return this.senderStatus;
    }
}
