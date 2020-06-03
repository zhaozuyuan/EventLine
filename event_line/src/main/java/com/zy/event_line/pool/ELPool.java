package com.zy.event_line.pool;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zy.event_line.receive.RealReceiver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * create by zuyuan on 2020/5/22
 */
public class ELPool {

    private final Map<Class, SenderFactor> mStickyEvents = new ConcurrentHashMap<>();

    //event type cache, cache not contains event's self.
    private final Map<Class, List<Class>> mEventTypeCache = new ConcurrentHashMap<>();

    private final Map<Class, List<RealReceiver>> mReceivers = new ConcurrentHashMap<>();

    private final Map<Class, List<RealReceiver>> mTargetReceivers = new ConcurrentHashMap<>();

    private final Map<Class, List<RealReceiver>> mStickyReceivers = new ConcurrentHashMap<>();

    private static volatile ELPool instance;

    public static void init() {
        if (instance == null) {
            getInstance();
        }
    }

    public static ELPool getInstance() {
        if (ELPool.instance == null) {
            ELPool.instance = ELPool.Holder.INSTANCE;
        }
        return ELPool.instance;
    }

    public void saveSenderFactor(SenderFactor factor) {
        mStickyEvents.put(factor.getEventClazz(), factor);
    }

    @Nullable
    public SenderFactor removeStickySenderFactor(Class eventClass) {
        return mStickyEvents.remove(eventClass);
    }

    @Nullable
    public SenderFactor getStickySenderFactor(Class eventClass) {
        return mStickyEvents.get(eventClass);
    }

    public Set<Class> getStickyEventKeys() {
        return mStickyEvents.keySet();
    }

    public void saveStickyFactor(SenderFactor factor) {
        mStickyEvents.put(factor.getEventClazz(), factor);
    }

    @Nullable
    public List<Class> getEventTypeFromCache(Class eventClass) {
        return mEventTypeCache.get(eventClass);
    }

    public void saveEventTypeCache(Class eventClass, List<Class> eventTypes) {
        mEventTypeCache.put(eventClass, eventTypes);
    }

    /**
     * make sure there only one list instance.
     * @param realReceiver
     */
    public synchronized void saveReceiver(RealReceiver realReceiver) {
        Class eventClass = realReceiver.getEventClass();
        List<RealReceiver> realReceivers = mReceivers.get(eventClass);
        if (realReceivers == null) {
            realReceivers = new ArrayList<>();
        }
        realReceivers.add(realReceiver);
        mReceivers.put(eventClass, realReceivers);
    }

    /**
     * make sure there only one list instance.
     * @param realReceiver
     */
    public synchronized void saveStickyReceiver(RealReceiver realReceiver) {
        Class eventClass = realReceiver.getEventClass();
        List<RealReceiver> realReceivers = mStickyReceivers.get(eventClass);
        if (realReceivers == null) {
            realReceivers = new ArrayList<>();
        }
        realReceivers.add(realReceiver);
        mReceivers.put(eventClass, realReceivers);
    }

    @NonNull
    public List<RealReceiver> getReceivers(Class eventClass) {
        List<RealReceiver> realReceivers = mReceivers.get(eventClass);
        return realReceivers == null ? Collections.<RealReceiver>emptyList() : realReceivers;
    }

    @NonNull
    public List<RealReceiver> getStickyReceivers(Class eventClass) {
        List<RealReceiver> realReceivers = mStickyReceivers.get(eventClass);
        return realReceivers == null ? Collections.<RealReceiver>emptyList() : realReceivers;
    }

    public void removeReceiver(RealReceiver realReceiver) {
        mReceivers.remove(realReceiver);
    }

    public synchronized void saveTargetReceiver(RealReceiver realReceiver) {
        Class context = realReceiver.getTargetContext();
        List<RealReceiver> realReceivers = mTargetReceivers.get(context);
        if (realReceivers == null) {
            realReceivers = new ArrayList<>();
        }
        realReceivers.add(realReceiver);
        mReceivers.put(context, realReceivers);
    }

    @NonNull
    public List<RealReceiver> getReceiversByContext(Class context) {
        List<RealReceiver> realReceivers = mTargetReceivers.get(context);
        return realReceivers == null ? Collections.<RealReceiver>emptyList() : realReceivers;
    }

    static class Holder {
        static final ELPool INSTANCE = new ELPool();
    }
}
