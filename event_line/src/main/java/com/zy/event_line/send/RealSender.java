package com.zy.event_line.send;

import androidx.annotation.NonNull;

import com.zy.event_line.pool.ELPool;
import com.zy.event_line.pool.SenderFactor;
import com.zy.event_line.pool.StatusController;
import com.zy.event_line.receive.RealReceiver;
import com.zy.event_line.runtime.log.ELLogger;
import com.zy.event_line.service.api.send.ISender;

import java.util.List;

/**
 * create by zuyuan on 2020/4/19
 */
public class RealSender implements ISender {

    public static final String TAG = "RealSender ";

    @Override
    public void sendEvent(@NonNull Class senderContext, @NonNull Object event) {
       sendEvent(senderContext, event, null);
    }

    @Override
    public void sendEvent(@NonNull Class senderContext, @NonNull Object event, Class[] target) {
        ELLogger.debug(TAG + "send event: " + event.getClass().getSimpleName());
        SenderFactor factor = new SenderFactor(event, false, target,
                null, senderContext);
        ELPool.getInstance().saveSenderFactor(factor);

        sendEventToReceiver(factor);
    }

    @Override
    public void sendStickyEvent(@NonNull Class senderContext,
                                @NonNull Object stickyEvent) {
       sendStickyEvent(senderContext, stickyEvent, null);
    }

    @Override
    public void sendStickyEvent(@NonNull Class senderContext, @NonNull Object stickyEvent, Class[] target) {
        ELLogger.debug(TAG + "send sticky event: " + stickyEvent.getClass().getSimpleName());
        SenderFactor factor = new SenderFactor(stickyEvent, true, target,
                null, senderContext);
        ELPool.getInstance().saveSenderFactor(factor);
        ELPool.getInstance().saveStickyFactor(factor);

        sendEventToReceiver(factor);
    }

    @Override
    public StatusController sendStickyEventWithController(@NonNull Class senderContext,
                                                          @NonNull Object stickyEvent,
                                                          int defaultStatus) {
        return sendStickyEventWithController(senderContext, stickyEvent, defaultStatus, null);
    }

    @Override
    public StatusController sendStickyEventWithController(@NonNull Class senderContext,
                                                          @NonNull Object stickyEvent,
                                                          int defaultStatus,
                                                          Class[] target) {
        ELLogger.debug(TAG + "send sticky event(with status controller): "
                + stickyEvent.getClass().getSimpleName());
        StatusController statusController = new StatusController();
        SenderFactor factor = new SenderFactor(stickyEvent, true, target,
                statusController, senderContext);
        ELPool.getInstance().saveSenderFactor(factor);
        ELPool.getInstance().saveStickyFactor(factor);
        statusController.refreshSenderStatus(defaultStatus);

        sendEventToReceiver(factor);
        return statusController;
    }

    private void sendEventToReceiver(SenderFactor factor) {
        Class[] targets = factor.getTargets();
        Class eventClass = factor.getEventClazz();

        if (targets != null) {
            //has target
            if (targets.length == 0) {
                ELLogger.error("sender's targets's length is 0.");
            }
            for (Class oneTarget : targets) {
                List<RealReceiver> receivers = ELPool.getInstance().getReceiversByContext(oneTarget);
                toReceiver(receivers, factor);
            }
        } else {
            //no target
            //normal event
            List<RealReceiver> receivers = ELPool.getInstance().getReceivers(eventClass);
            toReceiver(receivers, factor);

            //sticky event
            if (factor.isStickyEvent()) {
                List<RealReceiver> stickyReceivers = ELPool.getInstance().getReceivers(eventClass);
                toReceiver(stickyReceivers, factor);
            }
        }
    }

    private void toReceiver(List<RealReceiver> receivers, SenderFactor factor) {
        Class eventClass = factor.getEventClazz();
        List<Class> types = factor.getEventTypes();
        for (RealReceiver receiver : receivers) {
            Class receiverEventClazz = receiver.getEventClass();
            Class[] senders = receiver.getSenders();
            if (senders != null && !arrayContainClass(senders, factor.getSenderContext())) {
                continue;
            }
            if (receiverEventClazz == eventClass) {
                receiver.onReceive(factor);
            } else if (types != null
                    && receiver.canReceiveEventChild()
                    && types.contains(receiverEventClazz)) {
                receiver.onReceive(factor);
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
