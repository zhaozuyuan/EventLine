package com.zy.event_line.receive;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;

import com.zy.event_line.schedule.ScheduleNode;
import com.zy.event_line.service.api.receive.ICancellable;
import com.zy.event_line.service.api.receive.IReceiver;
import com.zy.event_line.service.api.receive.IReceiverBuilder;
import com.zy.event_line.service.api.receive.IEventStrategy;
import com.zy.event_line.service.api.schedule.IScheduler;

/**
 * create by zuyuan on 2020/5/30
 */
public class ReceiverBuilder<E> implements IReceiverBuilder<E> {

    private Lifecycle lifecycle = null;

    private Lifecycle.Event lifecycleEvent = null;

    private boolean canReceiveStickyEvent = false;

    private boolean canReceiveEventChild = false;

    private IEventStrategy statusStrategy = null;

    private Class[] senders = null;

    private IReceiver<E> eventReceiver ;

    private Class eventClass;

    private IScheduler scheduler;

    private Class receiverContext;

    public ReceiverBuilder(Class receiverContext, Class<E> eventClass) {
        if (eventClass == null) {
            throw new NullPointerException("the event class can not be null.");
        }
        this.eventClass = eventClass;
        this.receiverContext = receiverContext;
        scheduler = new ScheduleNode();
        scheduler.currentThread();
    }

    @Override
    public IReceiverBuilder<E> canReceiveStickyEvent(boolean canReceive) {
        canReceiveStickyEvent = canReceive;
        return this;
    }

    @Override
    public IReceiverBuilder<E> canReceiveEventChild(boolean canReceive) {
        canReceiveEventChild = canReceive;
        return this;
    }

    @Override
    public IReceiverBuilder<E> setStatusStrategy(IEventStrategy strategy) {
        this.statusStrategy = strategy;
        return this;
    }

    @Override
    public IReceiverBuilder<E> setSpecifiedSenders(@Nullable Class[] senders) {
        this.senders = senders;
        return this;
    }

    @Override
    public IReceiverBuilder<E> setAutoCancelObserver(Lifecycle lifecycle, Lifecycle.Event event) {
        this.lifecycle = lifecycle;
        this.lifecycleEvent = event;
        return this;
    }

    @Override
    public IReceiverBuilder<E> setAutoCancelObserver(Lifecycle lifecycle) {
        this.lifecycle = lifecycle;
        this.lifecycleEvent = Lifecycle.Event.ON_DESTROY;
        return this;
    }

    @Override
    public IReceiverBuilder<E> postMainThread() {
        scheduler.postMainThread();
        return this;
    }

    @Override
    public IReceiverBuilder<E> scheduleToMainThread() {
        scheduler.toMainThread();
        return this;
    }

    @Override
    public IReceiverBuilder<E> scheduleToIOThread() {
        scheduler.toIOThread();
        return this;
    }

    @Override
    public IReceiverBuilder<E> scheduleCPUThread() {
        scheduler.toCPUThread();
        return this;
    }

    @Override
    public IReceiverBuilder<E> scheduleSingleThread() {
        scheduler.toSingleThread();
        return this;
    }

    @Override
    public IReceiverBuilder<E> scheduleNewThread() {
        scheduler.toNewThread();
        return this;
    }

    @Override
    public  ICancellable observer(IReceiver<E> eventReceiver) {
        this.eventReceiver = eventReceiver;
        RealReceiver receiver = new RealReceiver<>(this, eventClass);
        receiver.prepare();
        return receiver;
    }

    @Nullable
    public Lifecycle getLifecycle() {
        return lifecycle;
    }

    @Nullable
    public Lifecycle.Event getLifecycleEvent() {
        return lifecycleEvent;
    }

    public boolean isCanReceiveStickyEvent() {
        return canReceiveStickyEvent;
    }

    public boolean isCanReceiveEventChild() {
        return canReceiveEventChild;
    }

    @Nullable
    public IEventStrategy getStatusStrategy() {
        return statusStrategy;
    }

    @Nullable
    public Class[] getSenders() {
        return senders;
    }

    @NonNull
    public IReceiver<E> getEventReceiver() {
        return eventReceiver;
    }

    public IScheduler getScheduler() {
        return scheduler;
    }

    public Class getReceiverContext() {
        return receiverContext;
    }
}
