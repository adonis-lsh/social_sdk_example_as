package com.umeng.weixin.umengwx;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;

public class WeApi {
    private Context mContext;
    private String appid;
    private boolean c = false;

    public WeApi(Context paramContext, String appid) {
        this.mContext = paramContext;
        this.appid = appid;
    }

    public final boolean registerApp(String appid) {
        if (this.c) {
            throw new IllegalStateException("registerApp fail, WXMsgImpl has been detached");
        }
        if (appid != null) {
            this.appid = appid;
        }
        if (this.mContext == null) {
            return false;
        }
        Intent localIntent = new Intent("com.tencent.mm.plugin.openapi.Intent.ACTION_HANDLE_APP_REGISTER");
        String str = this.mContext.getPackageName();
        localIntent.putExtra("_mmessage_sdkVersion", 587268097);
        localIntent.putExtra("_mmessage_appPackage", str);
        localIntent.putExtra("_mmessage_content", "weixin://registerapp?appid=" + this.appid);
        localIntent.putExtra("_mmessage_checksum", CheckSumUtil.checkSum("weixin://registerapp?appid=" + this.appid, 587268097, str));
        this.mContext.sendBroadcast(localIntent, "com.tencent.mm.permission.MM_MESSAGE");
        return true;
    }

    public final boolean isWXAppInstalled() {
        try {
            return this.mContext.getPackageManager().getPackageInfo("com.tencent.mm", 64) != null;
        } catch (NameNotFoundException localNameNotFoundException) {
        }
        return false;
    }

    public final boolean sendReq(BaseReq baseReq) {
        if (!baseReq.check()) {
            return false;
        }
        Bundle localBundle = new Bundle();
        baseReq.toBundle(localBundle);
        launchShare(localBundle);
        return true;
    }

    public final void pushare(Bundle paramBundle) {
        launchShare(paramBundle);
    }

    public final boolean launchShare(Bundle paramBundle) {
        if (this.mContext == null) {
            return false;
        }
        Intent localIntent;
        (localIntent = new Intent()).setClassName("com.tencent.mm", "com.tencent.mm.plugin.base.stub.WXEntryActivity");
        String packageName = this.mContext.getPackageName();
        localIntent.putExtras(paramBundle);
        localIntent.putExtra("_mmessage_sdkVersion", 620756993);
        localIntent.putExtra("_mmessage_appPackage", packageName);
        localIntent.putExtra("_mmessage_content", "weixin://sendreq?appid=" + this.appid);
        localIntent.putExtra("_mmessage_checksum", CheckSumUtil.checkSum("weixin://sendreq?appid=" + this.appid, 620756993, packageName));
        localIntent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND).addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            this.mContext.startActivity(localIntent);
        } catch (Exception localException) {
            return false;
        }
        return true;
    }

    public final boolean isWXAppSupportAPI() {
        return true;
    }

    public final boolean handleIntent(Intent intent, IWXAPIEventHandler eventHandler) {
        try {
            switch (intent.getIntExtra("_wxapi_command_type", 0)) {
                case 1:
                    SendMessageToWeiXinResp sendMessageToWeiXinResponse = new SendMessageToWeiXinResp(intent.getExtras());
                    eventHandler.onResp(sendMessageToWeiXinResponse);
                    return true;
                case 2:
                    ApiResp apiResponse = new ApiResp(intent.getExtras());
                    eventHandler.onResp(apiResponse);
                    return true;
                case 3:
                    return true;
                case 4:
                    return true;
                case 5:
                    return true;
            }
        } catch (Exception localException) {
        }
        return false;
    }
}