package com.umeng.socialize.uploadlog;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

import com.umeng.socialize.Config;
import com.umeng.socialize.utils.ContextUtil;


public class UMLog {
    private static boolean isAuth = false;
    private static boolean isShare = false;

    public static void putShare() {
        if ((ContextUtil.getContext() != null) && (!isShare)) {
            SharedPreferences localSharedPreferences = ContextUtil.getContext().getSharedPreferences("umeng_socialize", 0);


            Editor localEditor = localSharedPreferences.edit();
            localEditor.putBoolean("share", true);
            localEditor.putBoolean("isjump", Config.isJumptoAppStore);
            localEditor.commit();
            isShare = true;
        }
    }

    public static void putAuth() {
        if ((ContextUtil.getContext() != null) && (!isAuth)) {
            SharedPreferences localSharedPreferences = ContextUtil.getContext().getSharedPreferences("umeng_socialize", 0);


            Editor localEditor = localSharedPreferences.edit();
            localEditor.putBoolean("auth", true);
            localEditor.putBoolean("isjump", Config.isJumptoAppStore);
            localEditor.commit();
            isShare = true;
        }
    }

    public static Bundle getShareAndAuth() {
        Bundle localBundle = new Bundle();
        if (ContextUtil.getContext() != null) {
            SharedPreferences localSharedPreferences = ContextUtil.getContext().getSharedPreferences("umeng_socialize", 0);

            localBundle.putBoolean("share", localSharedPreferences.getBoolean("share", false));
            localBundle.putBoolean("auth", localSharedPreferences.getBoolean("auth", false));
            localBundle.putBoolean("isjump", localSharedPreferences.getBoolean("isjump", false));
        } else {
            localBundle.putBoolean("share", false);
            localBundle.putBoolean("auth", false);
            localBundle.putBoolean("isjump", false);
        }
        return localBundle;
    }


    public static boolean isOpenShareEdit() {
        if (ContextUtil.getContext() != null) {
            SharedPreferences localSharedPreferences = ContextUtil.getContext().getSharedPreferences("umeng_socialize", 0);

            return localSharedPreferences.getBoolean("is_open_share_edit", true);
        }
        return true;
    }


    public static void setIsOpenShareEdit(boolean paramBoolean) {
        if (ContextUtil.getContext() != null) {
            SharedPreferences localSharedPreferences = ContextUtil.getContext().getSharedPreferences("umeng_socialize", 0);

            Editor localEditor = localSharedPreferences.edit();
            localEditor.putBoolean("is_open_share_edit", paramBoolean).apply();
        }
    }
}


/* Location:              /Users/zl/Desktop/未命名文件夹 3/umeng_social_net.jar!/com/umeng/socialize/uploadlog/UMLog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */