package com.umeng.socialize.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;


public class SocializeSpUtils {
    private static SharedPreferences getSharedPreferences(Context paramContext) {
        if (paramContext == null) {
            return null;
        }
        return paramContext.getSharedPreferences("umeng_socialize", 0);
    }

    public static String getUMId(Context paramContext) {
        SharedPreferences localSharedPreferences = getSharedPreferences(paramContext);
        if (localSharedPreferences != null) {
            return localSharedPreferences.getString("uid", null);
        }
        return null;
    }

    public static long getTime(Context paramContext) {
        SharedPreferences localSharedPreferences = getSharedPreferences(paramContext);
        if (localSharedPreferences != null) {
            return localSharedPreferences.getLong("request_time", 0L);
        }
        return 0L;
    }

    public static String getUMEk(Context paramContext) {
        SharedPreferences localSharedPreferences = getSharedPreferences(paramContext);
        if (localSharedPreferences != null) {
            return localSharedPreferences.getString("ek", null);
        }
        return null;
    }

    public static String getMac(Context paramContext) {
        SharedPreferences localSharedPreferences = getSharedPreferences(paramContext);
        if (localSharedPreferences != null) {
            return localSharedPreferences.getString("mac", null);
        }
        return null;
    }

    public static boolean putUMId(Context paramContext, String paramString) {
        SharedPreferences localSharedPreferences = getSharedPreferences(paramContext);
        if (localSharedPreferences == null) {
            return false;
        }
        if (TextUtils.isEmpty(paramString)) {
            paramString = "";
        }
        return localSharedPreferences.edit().putString("uid", paramString).commit();
    }

    public static boolean putUMEk(Context paramContext, String paramString) {
        SharedPreferences localSharedPreferences = getSharedPreferences(paramContext);
        if (localSharedPreferences == null) {
            return false;
        }
        if (TextUtils.isEmpty(paramString)) {
            paramString = "";
        }
        return localSharedPreferences.edit().putString("ek", paramString).commit();
    }


    public static boolean putTime(Context paramContext) {
        SharedPreferences localSharedPreferences = getSharedPreferences(paramContext);


        return (localSharedPreferences != null) && (localSharedPreferences.edit().putLong("request_time", System.currentTimeMillis()).commit());
    }

    public static boolean putMac(Context paramContext, String paramString) {
        SharedPreferences localSharedPreferences = getSharedPreferences(paramContext);
        if (localSharedPreferences == null) {
            return false;
        }
        if (TextUtils.isEmpty(paramString)) {
            paramString = "";
        }
        return localSharedPreferences.edit().putString("mac", paramString).commit();
    }

    public static String getString(Context paramContext, String paramString) {
        return getSharedPreferences(paramContext).getString(paramString, "");
    }

    public static void putString(Context paramContext, String paramString1, String paramString2) {
        getSharedPreferences(paramContext).edit().putString(paramString1, paramString2).commit();
    }

    public static int getInt(Context paramContext, String paramString, int paramInt) {
        return getSharedPreferences(paramContext).getInt(paramString, paramInt);
    }

    public static void putInt(Context paramContext, String paramString, int paramInt) {
        getSharedPreferences(paramContext).edit().putInt(paramString, paramInt).commit();
    }

    public static void remove(Context paramContext, String paramString) {
        getSharedPreferences(paramContext).edit().remove(paramString).commit();
    }


    public static synchronized boolean putShareBoardConfig(Context paramContext, String paramString) {
        SharedPreferences localSharedPreferences = getSharedPreferences(paramContext);
        if (localSharedPreferences == null) {
            return false;
        }
        return localSharedPreferences.edit().putString("shareboardconfig", paramString).commit();
    }


    public static synchronized String getShareBoardConfig(Context paramContext) {
        SharedPreferences localSharedPreferences = getSharedPreferences(paramContext);
        if (localSharedPreferences == null) {
            return null;
        }
        return localSharedPreferences.getString("shareboardconfig", null);
    }
}


/* Location:              /Users/zl/Desktop/未命名文件夹 3/umeng_social_net.jar!/com/umeng/socialize/utils/SocializeSpUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */