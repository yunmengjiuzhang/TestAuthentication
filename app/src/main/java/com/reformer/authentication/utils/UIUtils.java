package com.reformer.authentication.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.reformer.authentication.BaseActivity;
import com.reformer.authentication.BaseApp;


/**
 * ====================
 * 版权所有 违法必究
 *
 * @author wangx
 * @project GooglePlay
 * @file UIUtils
 * @create_time 2016/8/20 0020
 * @github https://github.com/wangxujie
 * @blog http://wangxujie.github.io
 * <p>
 * ======================
 */
public class UIUtils {
    /**
     * 根据资源id 获取字符串数组
     *
     * @param id
     * @return
     */
    public static String[] getStringArray(int id) {
        return getResources().getStringArray(id);
    }

    /**
     * 获取Resources对象
     *
     * @return
     */
    public static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 获取上下文对象
     *
     * @return
     */
    public static Context getContext() {
        return BaseApp.getInstance();
    }

    /**
     * xml --->View对象
     *
     * @param id
     * @return
     */
    public static View inflate(int id) {
        return View.inflate(getContext(), id, null);
    }

    /**
     * 获取dimens
     *
     * @param id
     * @return
     */
    public static int getDimens(int id) {
        return getResources().getDimensionPixelSize(id);
    }

    /**
     * 启动新的Activity
     *
     * @param intent
     */
    public static void startActivity(Intent intent) {

        if (BaseActivity.activity == null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//指定任务栈
            getContext().startActivity(intent);
        } else {
            //不需要指定任务栈
            BaseActivity.activity.startActivity(intent);
        }

    }

    /**
     * id --- >string
     *
     * @param id
     * @return
     */
    public static String getString(int id) {
        return getResources().getString(id);
    }

    /**
     * id_---->Drawable
     *
     * @param id
     * @return
     */
    public static Drawable getDrawable(int id) {
        return getResources().getDrawable(id);
    }
}
