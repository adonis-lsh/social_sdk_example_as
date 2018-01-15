package com.umeng.weixin.handler;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

public class WeixinPreferences {
    private static final String a = "access_token";
    private static final String b = "expires_in";
    private static final String c = "refresh_token";
    private static final String d = "rt_expires_in";
    private static final String e = "openid";
    private static final String f = "unionid";
    private static final String g = "expires_in";
    private SharedPreferences mSharedPreferences = null;
    private String unionid;
    private String openid;
    private String access_token;
    private long expires_in;
    private String refresh_token;
    private long rt_expires_in;

    public WeixinPreferences(Context paramContext, String paramString) {
        this.mSharedPreferences = paramContext.getSharedPreferences(paramString + "simple", 0);
        this.unionid = this.mSharedPreferences.getString("unionid", null);
        this.openid = this.mSharedPreferences.getString("openid", null);
        this.access_token = this.mSharedPreferences.getString("access_token", null);
        this.expires_in = this.mSharedPreferences.getLong("expires_in", 0L);
        this.refresh_token = this.mSharedPreferences.getString("refresh_token", null);
        this.rt_expires_in = this.mSharedPreferences.getLong("rt_expires_in", 0L);
    }

    public WeixinPreferences setBundle(Bundle paramBundle) {
        this.unionid = paramBundle.getString("unionid");
        this.openid = paramBundle.getString("openid");
        this.access_token = paramBundle.getString("access_token");
        this.refresh_token = paramBundle.getString("refresh_token");
        String str = paramBundle.getString("expires_in");
        if (!TextUtils.isEmpty(str)) {
            this.expires_in = (Long.valueOf(str).longValue() * 1000L + System.currentTimeMillis());
        }
        long refresh_token_expires = paramBundle.getLong("refresh_token_expires");
        if (refresh_token_expires != 0L) {
            this.rt_expires_in = (refresh_token_expires * 1000L + System.currentTimeMillis());
        }
        save();
        return this;
    }

    public String getUnionid() {
        return this.unionid;
    }

    public String getOpenid() {
        return this.openid;
    }

    public String getRefreshToken() {
        return this.refresh_token;
    }

    public Map<String, String> getmap() {
        HashMap<String, String> localHashMap = new HashMap();
        localHashMap.put("access_token", this.access_token);
        localHashMap.put("unionid", this.unionid);
        localHashMap.put("openid", this.openid);
        localHashMap.put("refresh_token", this.refresh_token);
        localHashMap.put("expires_in", String.valueOf(this.expires_in));
        return localHashMap;
    }

    public boolean isAccessTokenAvailable() {
        boolean bool = TextUtils.isEmpty(this.access_token);
        int i1 = this.expires_in - System.currentTimeMillis() <= 0L ? 1 : 0;
        return (!bool) && (i1 == 0);
    }

    public String getAccessToken() {
        return this.access_token;
    }

    public long getExpires_in() {
        return this.expires_in;
    }

    //refresh_token是否到期
    public boolean isAuthValid() {
        boolean bool = TextUtils.isEmpty(this.refresh_token);
        int i1 = this.rt_expires_in - System.currentTimeMillis() <= 0L ? 1 : 0;
        return (!bool) && (i1 == 0);
    }

    public boolean isWxAuthorize() {
        boolean bool = TextUtils.isEmpty(getAccessToken());
        return !bool;
    }

    public void clearAuthInfo() {
        this.mSharedPreferences.edit().clear().commit();
        this.refresh_token = "";
        this.access_token = "";
    }

    public void save() {
        this.mSharedPreferences.edit().putString("unionid", this.unionid).putString("openid", this.openid).putString("access_token", this.access_token).putString("refresh_token", this.refresh_token).putLong("rt_expires_in", this.rt_expires_in).putLong("expires_in", this.expires_in).commit();
    }
}
