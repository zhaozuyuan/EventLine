package com.zy.event_line.send;

import com.zy.event_line.service.api.send.ISender;

/**
 * create by zuyuan on 2020/6/3
 */
public class SenderInstance {

    private static RealSender sender = new RealSender();

    public static ISender getSender() {
        return sender;
    }
}
