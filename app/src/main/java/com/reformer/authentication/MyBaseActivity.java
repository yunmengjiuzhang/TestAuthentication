package com.reformer.authentication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

/**
 * ====================
 * 版权所有 违法必究
 *
 * @author wf
 *         ======================
 *         1. 将相同的方法抽取出来
 *         2. 统一管理所有的acitivity
 */
public class MyBaseActivity extends Activity {

    // 共享资源
    public static List<MyBaseActivity> activities = new ArrayList<>();
    public static MyBaseActivity activity;//当前的Activity

    protected Context mCtx;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams params =  getWindow().getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LOW_PROFILE;
//        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
//        params.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE;
        getWindow().setAttributes(params);

        //添加到集合中统一维护
        synchronized (activities) {
            activities.add(this);
        }
        mCtx = this;
        init();
        initViews();
        initDatas();
        initToolBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
    }

    @Override
    protected void onPause() {
        super.onPause();
        activity = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        synchronized (activities) {
            activities.remove(this);
        }
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public void killAll() {
        //遍历中不允许增删
        //1. 复制一份
        //2. CopyOnWriteArrayList 可以在遍历中做增删操作
        List<MyBaseActivity> copy;
        synchronized (activities) {
            copy = new ArrayList<>(activities);
        }
        for (MyBaseActivity activity : copy) {
            activity.finish();
        }

        //  自杀进程
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 初始化操作
     */
    protected void init() {

    }

    /**
     * 初始化所有的控件
     */
    protected void initViews() {

    }

    protected void initDatas() {

    }

    /**
     * 初始化toolbar
     */
    protected void initToolBar() {

    }
}
