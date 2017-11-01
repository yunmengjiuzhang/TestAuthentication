package com.reformer.authentication.utils;

import android.os.Handler;

import com.reformer.authentication.manager.ThreadPoolManager;


/**
 * ====================
 * 版权所有 违法必究
 *
 * @author wangx
 * @project GooglePlay
 * @file ThreadUtils
 * @create_time 2016/8/21 0021
 * @github https://github.com/wangxujie
 * @blog http://wangxujie.github.io
 * <p>
 * ======================
 * 1. 子线程执行方法
 * 2. 切换到主线程
 */
public class ThreadUtils {

    /**
     * 在子线程执行
     *
     * @param runnable
     */
    public static void runOnBackThread(Runnable runnable) {
//        new Thread(runnable).start();   // 线程池
        ThreadPoolManager.getInstance().createThreadPool().execture(runnable);
    }

    private static Handler handler = new Handler();

    /**
     * 在主线程执行
     *
     * @param runnable
     */
    public static void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }

    public static void runOnUiThreadDelayed(Runnable runnable, int time) {
        handler.postDelayed(runnable, time);
    }
}
