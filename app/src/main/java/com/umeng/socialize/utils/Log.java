package com.umeng.socialize.utils;

import android.content.Context;
import android.widget.Toast;

import com.umeng.socialize.Config;


public class Log {
    public static final String TAG = "umengsocial";
    public static final String LOGTAG = "6.4.6umeng_tool----";
    public static final String NETTAG = "6.4.6net_test----";
    public static boolean LOG = true;
    private static boolean LOGNET = false;

    public static void i(String paramString1, String paramString2) {
        if ((LOG) && (Config.DEBUG)) {
            android.util.Log.i(paramString1, paramString2);
        }
    }

    public static void i(String paramString1, String paramString2, Exception paramException) {
        if ((LOG) && (Config.DEBUG)) {
            android.util.Log.i(paramString1, paramException.toString() + ":  [" + paramString2 + "]");
        }
    }

    public static void e(String paramString1, String paramString2) {
        if ((LOG) && (Config.DEBUG)) {
            android.util.Log.e(paramString1, paramString2);
        }
    }

    public static void e(String paramString1, String paramString2, Exception paramException) {
        android.util.Log.e(paramString1, paramException.toString() + ":  [" + paramString2 + "]");
        StackTraceElement[] arrayOfStackTraceElement1 = paramException.getStackTrace();
        for (StackTraceElement localStackTraceElement : arrayOfStackTraceElement1) {
            android.util.Log.e(paramString1, "        at\t " + localStackTraceElement.toString());
        }
    }

    public static void d(String paramString1, String paramString2) {
        if ((LOG) && (Config.DEBUG)) {
            android.util.Log.d(paramString1, paramString2);
        }
    }

    public static void d(String paramString1, String paramString2, Exception paramException) {
        if ((LOG) && (Config.DEBUG)) {
            android.util.Log.d(paramString1, paramException.toString() + ":  [" + paramString2 + "]");
        }
    }

    public static void v(String paramString1, String paramString2) {
        if ((LOG) && (Config.DEBUG)) {
            android.util.Log.v(paramString1, paramString2);
        }
    }

    public static void v(String paramString1, String paramString2, Exception paramException) {
        if ((LOG) && (Config.DEBUG)) {
            android.util.Log.v(paramString1, paramException.toString() + ":  [" + paramString2 + "]");
        }
    }

    public static void w(String paramString1, String paramString2) {
        if ((LOG) && (Config.DEBUG)) {
            android.util.Log.w(paramString1, paramString2);
        }
    }

    public static void w(String paramString1, String paramString2, Exception paramException) {
        if ((LOG) && (Config.DEBUG)) {
            android.util.Log.w(paramString1, paramException.toString() + ":  [" + paramString2 + "]");
            StackTraceElement[] arrayOfStackTraceElement1 = paramException.getStackTrace();
            for (StackTraceElement localStackTraceElement : arrayOfStackTraceElement1) {
                android.util.Log.w(paramString1, "        at\t " + localStackTraceElement.toString());
            }
        }
    }

    public static void i(String paramString) {
        if ((LOG) && (Config.DEBUG)) {
            android.util.Log.i("umengsocial", paramString);
        }
    }

    public static void e(String paramString) {
        if ((LOG) && (Config.DEBUG))
            android.util.Log.e("umengsocial", paramString);
    }

    public static void um(String paramString) {
        e("6.4.6umeng_tool----" + paramString);
    }

    public static void umd(String paramString) {
        d("6.4.6umeng_tool----" + paramString);
    }

    public static void y(String paramString) {
        if (LOG)
            android.util.Log.e("umengsocial", paramString);
    }

    public static void d(String paramString) {
        if ((LOG) && (Config.DEBUG)) {
            android.util.Log.d("umengsocial", paramString);
        }
    }

    public static void v(String paramString) {
        if ((LOG) && (Config.DEBUG)) {
            android.util.Log.v("umengsocial", paramString);
        }
    }

    public static void w(String paramString) {
        if ((LOG) && (Config.DEBUG))
            android.util.Log.w("umengsocial", paramString);
    }

    public static void net(String paramString) {
        if (LOGNET)
            android.util.Log.d("6.4.6net_test----", paramString);
    }

    public static void toast(Context paramContext, String paramString) {
        if ((LOG) && (Config.DEBUG))
            Toast.makeText(paramContext, paramString, 1).show();
    }

    public static void url(String paramString1, String paramString2) {
        if (Config.DEBUG) {
            d("6.4.6umeng_tool--------------------问题---------------");
            d("6.4.6umeng_tool----" + paramString1);
            d("6.4.6umeng_tool--------------------解决方案------------");
            d("6.4.6umeng_tool----请访问：" + paramString2);
            d("6.4.6umeng_tool----|-------------------------------|");
        }
    }

    public static void url(String paramString1, String paramString2, String paramString3) {
        if (Config.DEBUG) {
            d("6.4.6umeng_tool--------------------" + paramString1 + "---------------");
            d("6.4.6umeng_tool----" + paramString2);
            d("6.4.6umeng_tool--------------------解决方案------------");
            d("6.4.6umeng_tool----请访问：" + paramString3);
            d("6.4.6umeng_tool----|-------------------------------|");
        }
    }
}