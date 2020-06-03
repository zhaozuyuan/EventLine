package com.zy.event_line.service.open;

import com.zy.event_line.pool.StatusController;
import com.zy.event_line.send.SenderInstance;

/**
 * event sender
 *
 * create by zuyuan on 2020/4/19
 * @email: zhaozy-android@qq.com
 */
public class EventSender {

    static {
        if (EventLineInitialization.hasInitialization()) {
            EventLineInitialization.init();
        }
    }


    public static void sendEvent(Class context, Object event) {
        SenderInstance.getSender().sendEvent(context, event);
    }

    public static void sendEvent(Class context, Object event, Class[] target) {
        SenderInstance.getSender().sendEvent(context, event, target);
    }

    public static void sendStickyEvent(Class context, Object event) {
        SenderInstance.getSender().sendStickyEvent(context, event);
    }

    public static void sendStickyEvent(Class context, Object event, Class[] target) {
        SenderInstance.getSender().sendStickyEvent(context, event, target);
    }

    public static StatusController sendStickyEventWithController(Class context,
                                                                 Object event,
                                                                 int defaultStatus) {
        return SenderInstance.getSender().sendStickyEventWithController(context, event,
                defaultStatus);
    }

    public static StatusController sendStickyEventWithController(Class context,
                                                                 Object event,
                                                                 int defaultStatus,
                                                                 Class[] target) {
        return SenderInstance.getSender().sendStickyEventWithController(context, event,
                defaultStatus, target);
    }

}
