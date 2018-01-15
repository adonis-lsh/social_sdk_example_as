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