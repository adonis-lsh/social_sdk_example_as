package com.umeng.socialize.net;

import android.text.TextUtils;

import com.umeng.socialize.net.base.SocializeReseponse;

import org.json.JSONException;
import org.json.JSONObject;


public class PlatformTokenUploadResponse
        extends SocializeReseponse {
    public String mTencentUid;
    public String mExpiresIn;

    public PlatformTokenUploadResponse(Integer paramInteger, JSONObject paramJSONObject) {
        super(paramJSONObject);
    }


    public void parseJsonObject() {
        super.parseJsonObject();
        a();
        b();
    }


    private void a() {
        if (this.mJsonData != null) {
            try {
                JSONObject localJSONObject = this.mJsonData.getJSONObject("tencent");
                if (localJSONObject != null) {
                    String str = localJSONObject.optString("user_id");
                    if (!TextUtils.isEmpty(str)) {
                        this.mTencentUid = str;
                    }
                }
            } catch (JSONException localJSONException) {
            }
        }
    }

    private void b() {
    }
}


/* Location:              /Users/zl/Desktop/未命名文件夹 3/umeng_social_net.jar!/com/umeng/socialize/net/PlatformTokenUploadResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */