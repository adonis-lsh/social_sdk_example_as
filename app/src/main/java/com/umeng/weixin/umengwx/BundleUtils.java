package com.umeng.weixin.umengwx;

import android.os.Bundle;

public class BundleUtils {
    public static int getIntBundle(Bundle paramBundle, String paramString) {
        if (paramBundle == null) {
            return -1;
        }
        int i;
        try {
            i = paramBundle.getInt(paramString, -1);
        } catch (Exception localException) {
            i = -1;
        }
        return i;
    }

    public static String getStringBundle(Bundle paramBundle, String paramString) {
        if (paramBundle == null) {
            return null;
        }
        String str;
        try {
            str = paramBundle.getString(paramString);
        } catch (Exception localException) {
            str = null;
        }
        return str;
    }
}
