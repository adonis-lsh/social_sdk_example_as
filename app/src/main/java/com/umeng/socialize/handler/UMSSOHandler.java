
package com.umeng.socialize.handler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareContent;
import com.umeng.socialize.SocializeException;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.common.ResContainer;
import com.umeng.socialize.net.PlatformTokenUploadReq;
import com.umeng.socialize.net.PlatformTokenUploadResponse;
import com.umeng.socialize.net.RestAPI;
import com.umeng.socialize.utils.ContextUtil;
import com.umeng.socialize.utils.Log;

import java.lang.ref.WeakReference;


public abstract class UMSSOHandler {
    private Context mContext = null;

    private PlatformConfig.Platform mConfig = null;

    protected static final String UID = "uid";

    protected static final String USID = "usid";

    protected static final String UNIONID = "unionid";

    protected static final String OPENID = "openid";

    protected static final String ACCESSTOKEN = "accessToken";

    protected static final String ACCESS_TOKEN = "access_token";
    protected static final String REFRESHTOKEN = "refreshToken";
    protected static final String REFRESH_TOKEN = "refresh_token";
    protected static final String EXPIRATION = "expiration";
    protected static final String EXPIRES_IN = "expires_in";
    protected static final String NAME = "name";
    protected static final String ICON = "iconurl";
    protected static final String GENDER = "gender";

    @Deprecated
    protected static final String SCREEN_NAME = "screen_name";

    @Deprecated
    protected static final String PROFILE_IMAGE_URL = "profile_image_url";
    protected static final String CITY = "city";
    protected static final String PROVINCE = "province";
    protected static final String COUNTRY = "country";
    protected static final String ACCESS_SECRET = "access_secret";
    protected static final String EMAIL = "email";
    protected static final String ID = "id";
    protected static final String FIRST_NAME = "first_name";
    protected static final String LAST_NAME = "last_name";
    protected static final String MIDDLE_NAME = "middle_name";
    protected static final String JSON = "json";
    protected int mThumbLimit = 32768;

    protected WeakReference<Activity> mWeakAct;
    protected UMShareConfig mShareConfig;
    private static final UMShareConfig mDefaultShareConfig = new UMShareConfig();


    public void onCreate(Context context, PlatformConfig.Platform p) {

        this.mContext = ContextUtil.getContext();

        this.mConfig = p;

        if ((context instanceof Activity)) {

            this.mWeakAct = new WeakReference((Activity) context);

        }

    }


    public Context getContext() {

        return this.mContext;

    }


    public PlatformConfig.Platform getConfig() {

        return this.mConfig;

    }


    public final void setShareConfig(UMShareConfig config) {

        this.mShareConfig = config;

    }


    protected final UMShareConfig getShareConfig() {

        if (this.mShareConfig == null) {

            return mDefaultShareConfig;

        }

        return this.mShareConfig;

    }


    public void authorize(UMAuthListener listener) {
    }


    public void deleteAuth(UMAuthListener listener) {
    }


    public void setAuthListener(UMAuthListener listener) {
    }


    public boolean isHasAuthListener() {
        return true;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }


    public void onResume() {
    }


    public void getPlatformInfo(UMAuthListener listener) {

        Log.d("'getPlatformInfo', it works!");

    }


    public boolean isInstall() {

        Log.e("该平台不支持查询安装");

        return true;

    }


    public boolean isSupport() {

        Log.e("该平台不支持查询sdk支持");

        return true;

    }


    public boolean isAuthorize() {

        Log.e("该平台不支持查询是否授权");
        return true;

    }


    public String getSDKVersion() {
        return "";
    }


    public int getRequestCode() {
        return 0;

    }


    public boolean isSupportAuth() {
        return false;

    }


    public abstract boolean share(ShareContent paramShareContent, UMShareListener paramUMShareListener);


    public void release() {
    }


    public String getGender(Object object) {
        String male = ResContainer.getString(ContextUtil.getContext(), "umeng_socialize_male");
        String female = ResContainer.getString(ContextUtil.getContext(), "umeng_socialize_female");
        if (object == null) {
            return "";

        }
        if ((object instanceof String)) {
            if ((object.equals("m")) || (object.equals("1")) || (object.equals("男")))
                return male;
            if ((object.equals("resp_state")) || (object.equals("0")) || (object.equals("女"))) {
                return female;

            }
            return object.toString();

        }
        if ((object instanceof Integer)) {
            if (((Integer) object).intValue() == 1)
                return male;
            if (((Integer) object).intValue() == 0) {
                return female;

            }
            return object.toString();

        }
        return object.toString();

    }


    protected void uploadAuthData(final Bundle bundle) throws SocializeException {
        new Thread(new Runnable() {
            public void run() {
                PlatformTokenUploadReq req = new PlatformTokenUploadReq(UMSSOHandler.this.getContext());
                req.addStringParams("to", UMSSOHandler.this.getToName());
                req.addStringParams("usid", bundle.getString("uid"));
                req.addStringParams("access_token", bundle.getString("access_token"));
                req.addStringParams("refresh_token", bundle.getString("refresh_token"));
                req.addStringParams("expires_in", bundle.getString("expires_in"));
                PlatformTokenUploadResponse resp = RestAPI.uploadPlatformToken(req);
                Log.e("upload token resp = " + (resp == null ? "is null" : resp.mMsg));

            }

        }).start();

    }

    protected String getToName() {
        return "";
    }

}