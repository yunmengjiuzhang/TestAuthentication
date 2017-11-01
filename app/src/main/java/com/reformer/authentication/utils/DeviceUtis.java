package com.reformer.authentication.utils;

import android.content.Context;
import android.provider.Settings;

/**
 * Created by Administrator on 2017/5/23 0023.
 */

public class DeviceUtis {
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
