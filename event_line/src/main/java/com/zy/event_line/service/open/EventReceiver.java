package com.zy.event_line.service.open;

import com.zy.event_line.receive.ReceiverBuilder;
import com.zy.event_line.service.api.receive.IReceiverBuilder;

/**
 * event sender
 *
 * create by zuyuan on 2020/4/19
 * @email: zhaozy-android@qq.com
 */
public class EventReceiver {

    static {
        if (EventLineInitialization.hasInitialization()) {
            EventLineInitialization.init();
        }
    }

    public static <E> IReceiverBuilder<E> generateReceiver(Class receiverContext,
                                                           Class<E> eventClass) {
        return new ReceiverBuilder<>(receiverContext, eventClass);
    }
}
