package com.umeng.socialize.net;

import com.umeng.socialize.net.base.SocializeReseponse;
import com.umeng.socialize.utils.Log;

import org.json.JSONException;
import org.json.JSONObject;


public class ActionBarResponse
        extends SocializeReseponse {
    public int mPv;
    public int mCommentCount;
    public int mLikeCount;
    public String mSid;
    public String mEntityKey;
    public int mFirstTime;
    public int mFavorite;
    public String mUid;
    public String mUk;
    public int mShareCount;

    public ActionBarResponse(Integer paramInteger, JSONObject paramJSONObject) {
        super(paramJSONObject);
    }

    public void parseJsonObject() {
        JSONObject localJSONObject = this.mJsonData;
        if (localJSONObject == null) {
            Log.e("SocializeReseponse", "data json is null....");
        } else {
            try {
                if (localJSONObject.has("cm")) {
                    this.mCommentCount = localJSONObject.getInt("cm");
                }

                if (localJSONObject.has("ek")) {
                    this.mEntityKey = localJSONObject.getString("ek");
                }

                if (localJSONObject.has("ft")) {
                    this.mFirstTime = localJSONObject.getInt("ft");
                }

                if (localJSONObject.has("fr")) {
                    this.mFavorite = localJSONObject.optInt("fr", 0);
                }

                if (localJSONObject.has("lk")) {
                    this.mLikeCount = localJSONObject.getInt("lk");
                }

                if (localJSONObject.has("pv")) {
                    this.mPv = localJSONObject.getInt("pv");
                }

                if (localJSONObject.has("sid")) {
                    this.mSid = localJSONObject.getString("sid");
                }

                if (localJSONObject.has("uid")) {
                    this.mUid = localJSONObject.getString("uid");
                }

                if (localJSONObject.has("sn")) {
                    this.mShareCount = localJSONObject.getInt("sn");
                }
            } catch (JSONException localJSONException) {
                Log.e("SocializeReseponse", "Parse json error[ " + localJSONObject.toString() + " ]", localJSONException);
            }
        }
    }
}


/* Location:              /Users/zl/Desktop/未命名文件夹 3/umeng_social_net.jar!/com/umeng/socialize/net/ActionBarResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */