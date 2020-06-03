package com.zy.event_line.service.open;

import com.zy.event_line.pool.ELPool;
import com.zy.event_line.runtime.ConfigParams;
import com.zy.event_line.runtime.RunTimeConfig;

/**
 * event sender
 *
 * create by zuyuan on 2020/4/19
 * @email: zhaozy-android@qq.com
 */
public class EventLineInitialization {

    private EventLineInitialization() { }

    private volatile static boolean hasInit = false;

    public static void init() {
        init(new ConfigParams());
    }

    /**
     * prohibit direct call to this method !!!
     */
    public static void init(ConfigParams params) {
        if (!hasInit) {
            RunTimeConfig.init(params);
            ELPool.init();
            hasInit = true;
        } else  {
            throw new IllegalStateException(
                    "EventLine: the EventLineInitialization.init() can call only once.");
        }
    }

    public static boolean hasInitialization() {
        return !hasInit;
    }

}
