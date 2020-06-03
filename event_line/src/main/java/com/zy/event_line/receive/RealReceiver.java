package com.zy.event_line.receive;

import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;

import com.zy.event_line.pool.ELPool;
import com.zy.event_line.pool.SenderFactor;
import com.zy.event_line.receive.auto_cancel.AutoCancelObserver;
import com.zy.event_line.receive.auto_cancel.IAutoCancel;
import com.zy.event_line.runtime.log.ELLogger;
import com.zy.event_line.service.api.pool.IStatusReader;
import com.zy.event_line.service.api.receive.ICancellable;
import com.zy.event_line.service.api.receive.IEventStrategy;
import com.zy.event_line.service.api.schedule.IScheduler;

import java.util.List;
import java.util.Set;

/**
 * create by zuyuan on 2020/5/30
 */
public class RealReceiver<E> implements ICancellable, IRealReceiver {

    private ReceiverBuilder<E> mReceiverBuilder;

    private Class mEventClass;

    @Nullable
    private IAutoCancel mAutoCancel = null;

    private volatile boolean mIsCancel = false;

    RealReceiver(ReceiverBuilder<E> builder, Class eventClass) {
        this.mReceiverBuilder = builder;
        this.mEventClass = eventClass;
    }

    private boolean once = true;
    void prepare() {
        if (!once) {
            throw new IllegalArgumentException("RealReceiver: prepare() can be used only once.");
        }
        once = false;

        if (mReceiverBuilder.isCanReceiveStickyEvent()) {
            ELPool.getInstance().saveStickyReceiver(this);
        } else {
            ELPool.getInstance().saveReceiver(this);
        }
        ELPool.getInstance().saveTargetReceiver(this);
        tryAutoCancelObserver();
        if (mReceiverBuilder.isCanReceiveStickyEvent()) {
            readStickyEvent();
        }
    }

    @Override
    public void cancelObserver() {
        if (!mIsCancel) {
            ELLogger.debug("the receiver" + hashCode() + " cancelObserver() be called");

            if (mAutoCancel != null) {
                mAutoCancel.cancelObserver();
            }
            ELPool.getInstance().removeReceiver(this);
        } else {
            ELLogger.warn("the receiver" + hashCode() + " cancelObserver() be called multiple times.");
            mIsCancel = true;
        }
    }

    @Override
    public boolean isCancel() {
        return mIsCancel;
    }

    public Class getEventClass() {
        return mEventClass;
    }

    @Nullable
    public Class[] getSenders() {
        return mReceiverBuilder.getSenders();
    }

    private void tryAutoCancelObserver() {
        Lifecycle lifecycle = mReceiverBuilder.getLifecycle();
        Lifecycle.Event cancelEvent = mReceiverBuilder.getLifecycleEvent();
        if (lifecycle != null) {
            AutoCancelObserver observer = new AutoCancelObserver(lifecycle, cancelEvent) {
                @Override
                public void onCancelObserver() {
                    ELLogger.debug("the receiver" + hashCode() + " auto cancel.");
                    cancelObserver();
                }
            };
            mAutoCancel = observer;
            lifecycle.addObserver(observer);
        }
    }

    @Override
    public void onReceive(SenderFactor factor) {
        if (checkSenders(factor) && checkPassStatusStrategy(factor)) {
            //safe
            E event = (E) factor.getEvent();
            ELLogger.debug("the receiver" + hashCode() + " receive event: " + event.toString());
            passThreadChannel(mReceiverBuilder.getScheduler(), event);
        }
    }

    public boolean canReceiveEventChild() {
        return mReceiverBuilder.isCanReceiveEventChild();
    }

    public Class getTargetContext() {
        return mReceiverBuilder.getReceiverContext();
    }

    private boolean checkPassStatusStrategy(SenderFactor factor) {
        IEventStrategy strategy = mReceiverBuilder.getStatusStrategy();
        if (strategy != null) {
            IStatusReader reader = factor.getStatusReader();
            IScheduler scheduler = mReceiverBuilder.getScheduler();
            strategy.scheduleThread1(scheduler);
            boolean result = strategy.withCustomStatus(reader, scheduler);
            if (!result) {
                ELLogger.debug("the receiver" + hashCode() + " intercept event.");
                return false;
            }
        }
        return true;
    }

    private boolean checkSenders(SenderFactor factor) {
        Class[] senders = mReceiverBuilder.getSenders();
        Class sender = factor.getSenderContext();
        if (senders == null) {
            return true;
        } else {
            for (Class clazz : senders) {
                if (clazz == sender) {
                    return true;
                }
            }
        }
        ELLogger.error("the receiver" + hashCode() + " not contains sender:" + sender);
        return false;
    }

    private void passThreadChannel(IScheduler scheduler, final E event) {
        scheduler.scheduleTask(new Runnable() {
            @Override
            public void run() {
                mReceiverBuilder.getEventReceiver().onReceive(event);
            }
        });
    }

    private void readStickyEvent() {
        SenderFactor factor = ELPool.getInstance().removeStickySenderFactor(getEventClass());
        Class[] senders = mReceiverBuilder.getSenders();
        if (factor != null) {
            onReceive(factor);
        } else {
            Set<Class> keys = ELPool.getInstance().getStickyEventKeys();
            for (Class key : keys) {
                SenderFactor senderFactor = ELPool.getInstance().getStickySenderFactor(key);
                if (senderFactor != null && senderFactor.getEventClazz() == getEventClass()) {
                    Class[] targets = senderFactor.getTargets();
                    boolean hasReceiver = targets == null
                            || arrayContainClass(targets, getEventClass());
                    if (!hasReceiver) continue;
                    boolean hasSender = senders == null || arrayContainClass(senders,
                            senderFactor.getSenderContext());
                    if (!hasSender) continue;
                    List<Class> types = senderFactor.getEventTypes();
                    if (types != null && types.contains(getEventClass())) {
                        onReceive(senderFactor);
                        break;
                    }
                }
            }
        }
    }

    private boolean arrayContainClass(Class[] classes, Class clazz) {
        for (Class ca : classes) {
            if (ca == clazz) {
                return true;
            }
        }
        return false;
    }
}
