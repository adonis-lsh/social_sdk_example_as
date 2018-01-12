package com.umeng.socialize.utils;

import android.content.Context;
import android.os.Build;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.net.utils.AesHelper;


public class URLBuilder {
    private String mReqType = "0";

    private String mHost = null;
    private String mPath = null;
    private String mAppkey = null;
    private String mEntityKey = null;
    private String mOpId = null;
    private String mUID = null;
    private String mSessionId = null;
    private String mPlatfrom = null;


    private String imei = null;
    private String idmd5 = null;
    private String mac = null;
    private String network = null;
    private String model = null;
    private String sdkversion = null;
    private String os = null;
    private String ts = null;
    private String protoversion = null;

    public URLBuilder(Context paramContext) {
        this.imei = DeviceConfig.getDeviceId(paramContext);
        if (this.imei != null) this.idmd5 = AesHelper.md5(this.imei);
        this.mac = DeviceConfig.getMac(paramContext);
        String[] arrayOfString = DeviceConfig.getNetworkAccessMode(paramContext);
        this.network = arrayOfString[0];
        this.model = Build.MODEL;
        this.sdkversion = "6.4.6";
        this.os = "Android";
        this.ts = String.valueOf(System.currentTimeMillis());
        this.protoversion = "2.0";
    }

    public URLBuilder setHost(String paramString) {
        this.mHost = paramString;
        return this;
    }

    public URLBuilder setPath(String paramString) {
        this.mPath = paramString;
        return this;
    }

    public URLBuilder setAppkey(String paramString) {
        this.mAppkey = paramString;
        return this;
    }

    public URLBuilder setEntityKey(String paramString) {
        this.mEntityKey = paramString;
        return this;
    }

    public URLBuilder withMedia(SHARE_MEDIA paramSHARE_MEDIA) {
        this.mPlatfrom = paramSHARE_MEDIA.toString();
        return this;
    }

    public URLBuilder withOpId(String paramString) {
        this.mOpId = paramString;
        return this;
    }

    public URLBuilder withSessionId(String paramString) {
        this.mSessionId = paramString;
        return this;
    }

    public URLBuilder withUID(String paramString) {
        this.mUID = paramString;
        return this;
    }


    public String to() {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append(this.mHost);
        localStringBuilder.append(this.mPath);
        localStringBuilder.append(this.mAppkey);
        localStringBuilder.append("/");
        localStringBuilder.append(this.mEntityKey);
        localStringBuilder.append("/?");

        localStringBuilder.append(buildParams());

        return localStringBuilder.toString();
    }

    public String toEncript() {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append(this.mHost);
        localStringBuilder.append(this.mPath);
        localStringBuilder.append(this.mAppkey);
        localStringBuilder.append("/");
        localStringBuilder.append(this.mEntityKey);
        localStringBuilder.append("/?");

        String str1 = buildParams();

        Log.net("base url: " + localStringBuilder.toString());
        Log.net("params: " + str1);

        AesHelper.setPassword(this.mAppkey);
        String str2 = null;
        try {
            Log.net("URLBuilder url=" + str1);
            str2 = AesHelper.encryptNoPadding(str1, "UTF-8");
            localStringBuilder.append("ud_get=");
            localStringBuilder.append(str2);
        } catch (Exception localException) {
            Log.w("fail to encrypt query string");
            localStringBuilder.append(str1);
        }
        return localStringBuilder.toString();
    }

    private String buildParams() {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("via=").append(this.mPlatfrom.toLowerCase());
        localStringBuilder.append("&opid=").append(this.mOpId);
        localStringBuilder.append("&ak=").append(this.mAppkey);
        localStringBuilder.append("&pcv=").append(this.protoversion);
        localStringBuilder.append("&tp=").append(this.mReqType);
        if (this.imei != null) localStringBuilder.append("&imei=").append(this.imei);
        if (this.idmd5 != null) localStringBuilder.append("&md5imei=").append(this.idmd5);
        if (this.mac != null) localStringBuilder.append("&mac=").append(this.mac);
        if (this.network != null) localStringBuilder.append("&en=").append(this.network);
        if (this.model != null) localStringBuilder.append("&de=").append(this.model);
        if (this.sdkversion != null) localStringBuilder.append("&sdkv=").append(this.sdkversion);
        if (this.os != null) localStringBuilder.append("&os=").append(this.os);
        if (this.ts != null) {
            localStringBuilder.append("&dt=").append(this.ts);
        }
        if (this.mUID != null) localStringBuilder.append("&uid=").append(this.mUID);
        if (this.mEntityKey != null) localStringBuilder.append("&ek=").append(this.mEntityKey);
        if (this.mSessionId != null) {
            localStringBuilder.append("&sid=").append(this.mSessionId);
        }
        return localStringBuilder.toString();
    }
}


/* Location:              /Users/zl/Desktop/未命名文件夹 3/umeng_social_net.jar!/com/umeng/socialize/utils/URLBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */