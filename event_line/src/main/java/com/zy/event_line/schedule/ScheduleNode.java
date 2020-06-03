package com.zy.event_line.schedule;

import android.os.Looper;

import com.zy.event_line.runtime.RunTimeConfig;
import com.zy.event_line.service.api.schedule.IScheduler;

import java.util.concurrent.Executor;

/**
 * create by zuyuan on 2020/5/30
 */
public class ScheduleNode implements IScheduler {

    private Executor mExecutor = null;

    private boolean postToMain = false;

    private boolean isNewThread = false;

    @Override
    public void scheduleTask(Runnable task) {
        if (mExecutor != null) {
            mExecutor.execute(task);
        } else if (postToMain){
            RunTimeConfig.getMainHandler().post(task);
        } else if (isNewThread) {
            Thread newThread = new Thread(task);
            newThread.setDaemon(true);
            newThread.start();
        } else {
            task.run();
        }
    }

    @Override
    public void currentThread() {
        clear();
    }

    @Override
    public void postMainThread() {
        clear();
        postToMain = true;
    }

    @Override
    public void toMainThread() {
        clear();
        postToMain = Looper.myLooper() == null || Looper.myLooper() != Looper.getMainLooper();
    }

    @Override
    public void toIOThread() {
        clear();
        mExecutor = RunTimeConfig.getIOExecutor();
    }

    @Override
    public void toCPUThread() {
        clear();
        mExecutor = RunTimeConfig.getCPUThread();
    }

    @Override
    public void toSingleThread() {
        clear();
        mExecutor = RunTimeConfig.getSingleExecutor();
    }

    @Override
    public void toNewThread() {
        clear();
        isNewThread = true;
    }

    private void clear() {
        mExecutor = null;
        postToMain = false;
        isNewThread = false;
    }
}
