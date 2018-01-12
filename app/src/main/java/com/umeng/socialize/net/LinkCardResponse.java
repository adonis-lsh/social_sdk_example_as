package com.umeng.socialize.net;

import com.umeng.socialize.net.base.SocializeReseponse;
import com.umeng.socialize.utils.Log;

import org.json.JSONObject;


public class LinkCardResponse
        extends SocializeReseponse {
    public String url;

    public LinkCardResponse(JSONObject paramJSONObject) {
        super(paramJSONObject);
    }

    public LinkCardResponse(Integer paramInteger, JSONObject paramJSONObject) {
        super(paramInteger, paramJSONObject);
    }


    public void parseJsonObject() {
        JSONObject localJSONObject = this.mJsonData;
        if (localJSONObject == null) {
            Log.e("SocializeReseponse", "data json is null....");
        } else {
            this.url = localJSONObject.optString("linkcard_url");
        }
    }
}


/* Location:              /Users/zl/Desktop/未命名文件夹 3/umeng_social_net.jar!/com/umeng/socialize/net/LinkCardResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */