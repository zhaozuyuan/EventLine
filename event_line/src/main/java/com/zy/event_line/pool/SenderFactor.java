package com.zy.event_line.pool;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zy.event_line.runtime.RunTimeConfig;
import com.zy.event_line.runtime.log.ELLogger;
import com.zy.event_line.service.api.pool.IStatusReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * create by zuyuan on 2020/5/7
 *
 * the factor when sender sends event.
 */
public class SenderFactor {

    private Class[] targets;

    private Class eventClazz;

    // not contain event's self.
    private List<Class> eventTypes;

    private Object event;

    private boolean isStickyEvent;

    private IStatusReader statusReader;

    private Class senderContext;

    public SenderFactor(Object event, Class senderContext) {
        this(event, false, senderContext);
    }

    public SenderFactor(Object event,  boolean isStickyEvent, Class senderContext) {
        this(event, isStickyEvent, null, senderContext);
    }

    public SenderFactor(Object event, boolean isStickyEvent, Class[] targets, Class senderContext) {
        this(event, isStickyEvent, targets, null, senderContext);
    }

    public SenderFactor(Object event,
                        boolean isStickyEvent,
                        Class[] targets,
                        IStatusReader statusReader,
                        Class senderContext) {
        if (event == null) {
            throw new NullPointerException("EventLine: the event can not be null.");
        }

        this.targets = targets;
        this.event = event;
        this.isStickyEvent = isStickyEvent;
        this.eventClazz = event.getClass();
        this.statusReader = statusReader;
        this.senderContext = senderContext;

        if (RunTimeConfig.isCanFindEventAllTypes()) {
            saveAllEventTypes();
        }
    }


    private void saveAllEventTypes() {
        List<Class> eventTypes = ELPool.getInstance().getEventTypeFromCache(eventClazz);
        if (eventTypes != null) {
            ELLogger.debug(eventClazz.getSimpleName() + " event find all types by cache.");
            this.eventTypes = eventTypes;
        } else {
            ELLogger.debug(eventClazz.getSimpleName() + " event find all types by super class.");
            Class[] currentTypes = eventClazz.getInterfaces();
            eventTypes = new ArrayList<>(Arrays.asList(currentTypes));
            Class superClass = eventClazz.getSuperclass();
            while (superClass != null) {
                eventTypes.add(superClass);
                Class[] superTypes = superClass.getInterfaces();
                eventTypes.addAll(Arrays.asList(superTypes));
                superClass = superClass.getSuperclass();
            }
        }

        ELPool.getInstance().saveEventTypeCache(eventClazz, eventTypes);
    }

    @Nullable
    public Class[] getTargets() {
        return targets;
    }

    @NonNull
    public Class getEventClazz() {
        return eventClazz;
    }

    @NonNull
    public Object getEvent() {
        return event;
    }

    public boolean isStickyEvent() {
        return isStickyEvent;
    }

    @Nullable
    public List<Class> getEventTypes() {
        return eventTypes;
    }

    @Nullable
    public IStatusReader getStatusReader() {
        return statusReader;
    }

    public Class getSenderContext() {
        return senderContext;
    }

    @NonNull
    @Override
    public String toString() {
        return "LineEvent{" +
                "targets=" + Arrays.toString(targets) +
                ", eventClazz=" + eventClazz +
                '}';
    }

}
