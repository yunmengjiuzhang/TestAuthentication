package com.reformer.authentication.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * ====================
 * 版权所有 违法必究
 *
 * @author wangx
 * @project GooglePlay
 * @file ToastUtils
 * @create_time 2016/8/20 0020
 * @github https://github.com/wangxujie
 * @blog http://wangxujie.github.io
 * <p/>
 * ======================
 */
public class ToastUtils {


    private static Toast toast;

    /**
     * 静态toast
     *
     * @param context
     * @param text
     */
    public static void showToast(final Context context, final String text) {
        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // toast消失了  toast 会自动为null
                if (toast == null) {// 消失了
                    toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                }

                toast.setText(text);
                toast.show();
            }
        });
    }


    public static void showToast(String text) {
        showToast(UIUtils.getContext(), text);
    }
}
