package com.umeng.socialize;

import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

public abstract interface UMAuthListener {
    public static final int ACTION_AUTHORIZE = 0;
    public static final int ACTION_DELETE = 1;
    public static final int ACTION_GET_PROFILE = 2;

    public abstract void onStart(SHARE_MEDIA share_media);

    public abstract void onComplete(SHARE_MEDIA share_media, int action, Map<String, String> resultMap);

    public abstract void onError(SHARE_MEDIA share_media, int action, Throwable paramThrowable);

    public abstract void onCancel(SHARE_MEDIA share_media, int action);
}