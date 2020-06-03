package com.zy.event_line.service.api.send;

import androidx.annotation.NonNull;

import com.zy.event_line.pool.StatusController;

/**
 * create by zuyuan on 2020/4/19
 */
public interface ISender {

    /**
     * the most common way to send normal events.
     */
    void sendEvent(@NonNull Class senderContext, @NonNull Object event);

    /**
     * the most common way to send normal events.
     */
    void sendEvent(@NonNull Class senderContext, @NonNull Object event, Class[] target);

    /**
     * to send sticky event
     */
    void sendStickyEvent(@NonNull Class senderContext, @NonNull Object stickyEvent);

    /**
     * to send sticky event
     */
    void sendStickyEvent(@NonNull Class senderContext, @NonNull Object stickyEvent, Class[] target);

    /**
     * to send sticky event
     * @return status controller, you can use it to change status.
     */
    StatusController sendStickyEventWithController(@NonNull Class senderContext,
                                                   @NonNull Object stickyEvent,
                                                   int defaultStatus);


    /**
     * to send sticky event
     * @return status controller, you can use it to change status.
     */
    StatusController sendStickyEventWithController(@NonNull Class senderContext,
                                                   @NonNull Object stickyEvent,
                                                   int defaultStatus,
                                                   Class[] target);
}