package com.zy.event_line.receive;

import com.zy.event_line.pool.SenderFactor;

/**
 * create by zuyuan on 2020/6/1
 */
public interface IRealReceiver {

    /**
     * when receiver received event
     * @param factor
     */
    void onReceive(SenderFactor factor);
}
