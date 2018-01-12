package com.umeng.weixin.handler;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.common.QueuedWork;

import java.util.Map;

class WXAuthListener implements UMAuthListener {
    private final UmengWXHandler mUmengWXHandler;
    private final UMAuthListener mUMAuthListener;

    WXAuthListener(UmengWXHandler umengWXHandler, UMAuthListener umAuthListener) {
        mUmengWXHandler = umengWXHandler;
        mUMAuthListener = umAuthListener;
    }

    public void onStart(SHARE_MEDIA paramSHARE_MEDIA) {
    }

    public void onComplete(SHARE_MEDIA paramSHARE_MEDIA, int action, Map paramMap) {
        QueuedWork.runInBack(new Runnable() {
            @Override
            public void run() {
                mUmengWXHandler.getActionProfile(mUMAuthListener);
            }
        }, true);
    }

    public void onError(SHARE_MEDIA shareMedia, int action, Throwable paramThrowable) {
        mUMAuthListener.onError(shareMedia, action, paramThrowable);
    }

    public void onCancel(SHARE_MEDIA paramSHARE_MEDIA, int action) {
        mUMAuthListener.onCancel(paramSHARE_MEDIA, action);
    }
}
