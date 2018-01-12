package com.umeng.socialize.net.base;

import android.text.TextUtils;

import com.umeng.socialize.net.utils.UResponse;
import com.umeng.socialize.utils.Log;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;


public class SocializeReseponse
        extends UResponse {
    protected static final String TAG = "SocializeReseponse";
    protected JSONObject mJsonData;
    public String mMsg;
    public int mStCode = -103;


    private int mHttpCode;


    public SocializeReseponse(JSONObject paramJSONObject) {
        super(paramJSONObject);
        this.mJsonData = parseStatus(paramJSONObject);
        parseJsonObject();
    }

    public SocializeReseponse(Integer paramInteger, JSONObject paramJSONObject) {
        this(paramJSONObject);
        this.mHttpCode = (paramInteger == null ? -1 : paramInteger.intValue());
    }

    public boolean isHttpOK() {
        return this.mHttpCode == 200;
    }

    public boolean isOk() {
        Log.d("umeng_share_response", "is http 200:" + (this.mStCode == 200));
        return this.mStCode == 200;
    }

    public JSONObject getJsonData() {
        return this.mJsonData;
    }


    private JSONObject parseStatus(JSONObject paramJSONObject) {
        JSONObject localJSONObject = paramJSONObject;
        if (localJSONObject == null) {
            return null;
        }
        try {
            this.mStCode = localJSONObject.optInt("st", 1998);


            if (this.mStCode == 0) {
                Log.e("SocializeReseponse", "no status code in response.");
                return null;
            }
            this.mMsg = localJSONObject.optString("msg", "");

            String str = localJSONObject.optString("data", null);

            if (TextUtils.isEmpty(str)) {
                return null;
            }
            if (this.mStCode != 200) {
                parseErrorMsg(str);
            }
            return new JSONObject(str);
        } catch (JSONException localJSONException) {
            localJSONException.printStackTrace();
            Log.e("SocializeReseponse", "Data body can`t convert to json ");
        }
        return null;
    }


    public void parseJsonObject() {
    }


    private void parseErrorMsg(String paramString) {
        try {
            JSONObject localJSONObject1 = new JSONObject(paramString);

            Iterator localIterator = localJSONObject1.keys();
            while (localIterator.hasNext()) {
                String str1 = (String) localIterator.next();
                JSONObject localJSONObject2 = localJSONObject1.getJSONObject(str1);
                String str2 = localJSONObject2.getString("msg");
                if (!TextUtils.isEmpty(str2)) {
                    printLog(str1, str2);
                } else {
                    JSONObject localJSONObject3 = localJSONObject2.getJSONObject("data");
                    str2 = localJSONObject3.getString("platform_error");
                    printLog(str1, str2);
                }
            }
        } catch (Exception localException) {
        }
    }


    private void printLog(String paramString1, String paramString2) {
        Log.e("SocializeReseponse", "error message -> " + paramString1 + " : " + paramString2);
    }
}


/* Location:              /Users/zl/Desktop/未命名文件夹 3/umeng_social_net.jar!/com/umeng/socialize/net/base/SocializeReseponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */