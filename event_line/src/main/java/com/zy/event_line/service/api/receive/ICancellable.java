package com.zy.event_line.service.api.receive;

/**
 * create by zuyuan on 2020/5/30
 */
public interface ICancellable {

    /**
     * cancel observer, and user can initiative cancel.
     */
    void cancelObserver();

    boolean isCancel();
}
