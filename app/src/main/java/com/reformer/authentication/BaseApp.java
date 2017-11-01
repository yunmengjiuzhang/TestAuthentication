package com.reformer.authentication;

import android.app.Application;
import android.content.Context;

import com.reformer.authentication.utils.CrashHandler;

/**
 * Created by Administrator on 2017/2/23 0023.
 */

public class BaseApp extends Application {

    private static BaseApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        CrashHandler.getInstance().init(this);//初始化崩溃工具
    }

    public static Context getInstance() {
        return instance;
    }
}
