package com.umeng.weixin.callback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.Log;
import com.umeng.weixin.handler.UmengWXHandler;
import com.umeng.weixin.umengwx.BaseReq;
import com.umeng.weixin.umengwx.BaseResp;
import com.umeng.weixin.umengwx.IWXAPIEventHandler;

public abstract class WXCallbackActivity extends Activity implements IWXAPIEventHandler {
    private final String mSimpleName = WXCallbackActivity.class.getSimpleName();
    protected UmengWXHandler mUmengWXHandler = null;

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        initWx();
        handleIntent(getIntent());
    }

    private void initWx() {
        UMShareAPI localUMShareAPI = UMShareAPI.get(getApplicationContext());
        Log.um("WXCallbackActivity");
        this.mUmengWXHandler = ((UmengWXHandler) localUMShareAPI.getHandler(SHARE_MEDIA.WEIXIN));
        this.mUmengWXHandler.onCreate(getApplicationContext(), PlatformConfig.getPlatform(SHARE_MEDIA.WEIXIN));
    }

    protected void handleIntent(Intent intent) {
        this.mUmengWXHandler.getWXApi().handleIntent(intent, this);
    }

    protected void onNewIntent(Intent paramIntent) {
        super.onNewIntent(paramIntent);
        setIntent(paramIntent);
        initWx();
        handleIntent(paramIntent);
    }

    public void onResp(BaseResp baseResp) {
        if ((this.mUmengWXHandler != null) && (baseResp != null)) {
            try {
                this.mUmengWXHandler.getWXEventHandler().onResp(baseResp);
            } catch (Exception localException) {
                localException.printStackTrace();
            }
        }
        finish();
    }

    public void onReq(BaseReq req) {
        if (this.mUmengWXHandler != null) {
            this.mUmengWXHandler.getWXEventHandler().onReq(req);
        }
        finish();
    }
}