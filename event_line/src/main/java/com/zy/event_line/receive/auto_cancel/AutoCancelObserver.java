package com.zy.event_line.receive.auto_cancel;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

/**
 * create by zuyuan on 2020/6/1
 */
public abstract class AutoCancelObserver implements LifecycleObserver, IAutoCancel {

    private Lifecycle mLifecycle;

    private int mIntCancelEvent = 0;

    private boolean isCancel = false;

    public AutoCancelObserver(Lifecycle lifecycle, Lifecycle.Event cancelEvent) {
        this.mLifecycle = lifecycle;
        mIntCancelEvent = getEventValue(cancelEvent);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    public void onStateChange(LifecycleOwner owner, Lifecycle.Event event) {
        int intCancelEvent = getEventValue(event);
        if (intCancelEvent >=  mIntCancelEvent) {
            onCancelObserver();
        }
    }

    @Override
    public void cancelObserver() {
        if (!isCancel) {
            isCancel = true;
            mLifecycle.removeObserver(this);
        }
    }

    private int getEventValue(Lifecycle.Event event) {
        switch (event) {
            case ON_CREATE:
                return 1;
            case ON_START:
                return 3;
            case ON_RESUME:
                return 5;
            case ON_PAUSE:
                return 7;
            case ON_STOP:
                return 9;
            case ON_DESTROY:
            default:
                return 11;
        }
    }

    public abstract void onCancelObserver();
}
