package com.umeng.socialize.net;

import com.umeng.socialize.net.base.SocializeReseponse;

import org.json.JSONObject;


public class UrlResponse
        extends SocializeReseponse {
    public String result;
    public int code;

    public UrlResponse(Integer paramInteger, JSONObject paramJSONObject) {
        super(paramJSONObject);
    }

    public void parseJsonObject() {
        if (this.mJsonData != null) {
            this.result = this.mJsonData.optString("new");
        }
    }
}


/* Location:              /Users/zl/Desktop/未命名文件夹 3/umeng_social_net.jar!/com/umeng/socialize/net/UrlResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */