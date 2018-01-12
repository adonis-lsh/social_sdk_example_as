package com.umeng.socialize.utils;

import android.content.Context;


public class ContextUtil {
    private static Context context;

    public static Context getContext() {
        if (context == null) {
            Log.um(UmengText.CONTEXT_ERROR);
        }
        return context;
    }

    public static void setContext(Context paramContext) {
        context = paramContext;
    }

    public static final String getPackageName() {
        return null == context ? "" : context.getPackageName();
    }
}