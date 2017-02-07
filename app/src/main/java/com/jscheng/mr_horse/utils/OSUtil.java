package com.jscheng.mr_horse.utils;

import android.os.Build;

/**
 * Created by cheng on 17-2-7.
 */
public class OSUtil {
    public static String gettMobileModel(){
        return Build.MODEL;
    }

    public static int getModelSDK(){
        return Build.VERSION.SDK_INT;
    }

    public static String getRelease(){
        return Build.VERSION.RELEASE;
    }

    public static String getMobileInfo(){
        return "["+ android.os.Build.MODEL + " "
                + Build.VERSION.SDK_INT + " "
                + android.os.Build.VERSION.RELEASE+" ]";
    }
}
