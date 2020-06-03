package com.zy.event_line.service.api.receive;

import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;

/**
 * create by zuyuan on 2020/5/30
 */
public interface IReceiverBuilder<E>  {

    /**
     * can receive sticky event.
     * @param canReceive
     * @return
     */
    IReceiverBuilder<E> canReceiveStickyEvent(boolean canReceive);

    /**
     * control receiver receive event's child.
     * @param canReceive
     * @return
     */
    IReceiverBuilder<E> canReceiveEventChild(boolean canReceive);

    /**
     *  on receive event, user can sense thread state and switch.
     * @return
     */
    IReceiverBuilder<E> setStatusStrategy(IEventStrategy strategy);

    /**
     * the receiver can receive events from the specified sender.
     * @param senders when it is null, receiver can receive all sender's event.
     * @return
     */
    IReceiverBuilder<E> setSpecifiedSenders(@Nullable Class[] senders);

    /**
     * when lifecycle changed and lifecycle'event is qualified, will cancel observer time.
     * @return
     */
    IReceiverBuilder<E> setAutoCancelObserver(Lifecycle lifecycle, Lifecycle.Event event);

    /**
     * when lifecycle changed and lifecycle'event is ON_DESTROY, will cancel observer time.
     * @return
     */
    IReceiverBuilder<E> setAutoCancelObserver(Lifecycle lifecycle);

    /**
     * simple schedule thread, if need more, can use IScheduler.
     * @return
     */
    IReceiverBuilder<E> postMainThread();
    IReceiverBuilder<E> scheduleToMainThread();
    IReceiverBuilder<E> scheduleToIOThread();
    IReceiverBuilder<E> scheduleCPUThread();
    IReceiverBuilder<E> scheduleSingleThread();
    IReceiverBuilder<E> scheduleNewThread();

    /**
     * when the event arrives, eventReceiver will be called.
     * @param eventReceiver
     */
    ICancellable observer(IReceiver<E> eventReceiver);
}
