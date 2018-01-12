package com.umeng.socialize;

import com.umeng.socialize.bean.SHARE_MEDIA;

public abstract interface UMShareListener {
    public abstract void onStart(SHARE_MEDIA shareMedia);

    public abstract void onResult(SHARE_MEDIA shareMedia);

    public abstract void onError(SHARE_MEDIA shareMedia, Throwable paramThrowable);

    public abstract void onCancel(SHARE_MEDIA shareMedia);
}
