package com.zy.event_line.service.api.receive;

/**
 * create by zuyuan on 2020/5/30
 */
public interface IReceiver<E> {

    /**
     * when receiver received event
     * @param event
     */
    void onReceive(E event);
}
