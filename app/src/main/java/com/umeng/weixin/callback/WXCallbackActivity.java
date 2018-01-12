package com.umeng.weixin.callback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.Log;
import com.umeng.weixin.handler.UmengWXHandler;
import com.umeng.weixin.umengwx.BaseRequest;
import com.umeng.weixin.umengwx.BaseResponse;
import com.umeng.weixin.umengwx.IWxHandler;

public abstract class WXCallbackActivity extends Activity implements IWxHandler {
    private final String mSimpleName = WXCallbackActivity.class.getSimpleName();
    protected UmengWXHandler mUmengWXHandler = null;

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        UMShareAPI localUMShareAPI = UMShareAPI.get(getApplicationContext());
        Log.um("WXCallbackActivity");
        this.mUmengWXHandler = ((UmengWXHandler) localUMShareAPI.getHandler(SHARE_MEDIA.WEIXIN));
        this.mUmengWXHandler.onCreate(getApplicationContext(), PlatformConfig.getPlatform(SHARE_MEDIA.WEIXIN));
        handleIntent(getIntent());
    }

    protected void handleIntent(Intent paramIntent) {
        this.mUmengWXHandler.getWXApi().handleIntent(paramIntent, this);
    }

    protected void onNewIntent(Intent paramIntent) {
        super.onNewIntent(paramIntent);
        setIntent(paramIntent);
        UMShareAPI localUMShareAPI = UMShareAPI.get(getApplicationContext());
        this.mUmengWXHandler = ((UmengWXHandler) localUMShareAPI.getHandler(SHARE_MEDIA.WEIXIN));
        Log.e(this.mSimpleName, "handleid=" + this.mUmengWXHandler);
        this.mUmengWXHandler.onCreate(getApplicationContext(), PlatformConfig.getPlatform(SHARE_MEDIA.WEIXIN));
        handleIntent(paramIntent);
    }

    public void response(BaseResponse baseResponse) {
        if ((this.mUmengWXHandler != null) && (baseResponse != null)) {
            try {
                this.mUmengWXHandler.getWXEventHandler().response(baseResponse);
            } catch (Exception localException) {
            }
        }
        finish();
    }

    public void request(BaseRequest baseRequest) {
        if (this.mUmengWXHandler != null) {
            this.mUmengWXHandler.getWXEventHandler().request(baseRequest);
        }
        finish();
    }
}