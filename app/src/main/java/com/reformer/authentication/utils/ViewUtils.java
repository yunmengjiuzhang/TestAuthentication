package com.reformer.authentication.utils;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

/**
 * ====================
 * 版权所有 违法必究
 *
 * @author wangx
 * @project GooglePlay
 * @file ViewUtils
 * @create_time 2016/8/21 0021
 * @github https://github.com/wangxujie
 * @blog http://wangxujie.github.io
 * <p/>
 * ======================
 */
public class ViewUtils {

    /**
     * 和父view 断绝关系
     * @param view
     */
    public static  void removeFromParent(View view){
        if (view != null){
            ViewParent parent = view.getParent();
            if (parent!= null && parent instanceof ViewGroup){//instanceof类属于
                ViewGroup group = (ViewGroup) parent;
                group.removeView(view);
            }
        }
    }
}
