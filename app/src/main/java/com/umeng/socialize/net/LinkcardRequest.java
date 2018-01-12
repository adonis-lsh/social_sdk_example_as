package com.umeng.socialize.net;

import android.content.Context;
import android.text.TextUtils;

import com.umeng.socialize.Config;
import com.umeng.socialize.media.BaseMediaObject;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.media.UMusic;
import com.umeng.socialize.net.base.SocializeRequest;
import com.umeng.socialize.net.utils.URequest;
import com.umeng.socialize.net.utils.URequest.RequestMethod;
import com.umeng.socialize.utils.SocializeUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class LinkcardRequest
        extends SocializeRequest {
    private static final String a = "/share/linkcard/";
    private String b;
    private BaseMediaObject c;

    public LinkcardRequest(Context paramContext) {
        super(paramContext, "", LinkCardResponse.class, 0, URequest.RequestMethod.POST);
    }

    public void setMedia(BaseMediaObject paramBaseMediaObject) {
        this.c = paramBaseMediaObject;
    }

    public void onPrepareRequest() {
        super.onPrepareRequest();
        String str = a().toString();


        addStringParams("linkcard_info", str);
    }

    private JSONObject a() {
        JSONObject localJSONObject = new JSONObject();
        try {
            localJSONObject.put("display_name", this.c.getTitle());
            localJSONObject.put("image", f());
            localJSONObject.put("summary", d());
            localJSONObject.put("full_image", h());
            localJSONObject.put("url", this.c.toUrl());
            localJSONObject.put("links", i());
            localJSONObject.put("tags", e());
            localJSONObject.put("create_at", c());
            localJSONObject.put("object_type", b());
        } catch (JSONException localJSONException) {
            localJSONException.printStackTrace();
        }
        return localJSONObject;
    }

    private String b() {
        if ((this.c instanceof UMWeb))
            return "webpage";
        if ((this.c instanceof UMVideo))
            return "video";
        if ((this.c instanceof UMusic)) {
            return "audio";
        }
        return "webpage";
    }

    private String c() {
        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date localDate = new Date(System.currentTimeMillis());
        return localSimpleDateFormat.format(localDate);
    }

    private String d() {
        if ((!TextUtils.isEmpty(this.c.getDescription())) && (this.c.getDescription().length() > 300)) {
            return this.c.getDescription().substring(0, 300);
        }

        return this.c.getDescription();
    }

    private JSONArray e() {
        JSONArray localJSONArray = new JSONArray();
        try {
            JSONObject localJSONObject = new JSONObject();
            localJSONObject.put("display_name", Config.Descriptor);
            localJSONArray.put(localJSONObject);
        } catch (JSONException localJSONException) {
            localJSONException.printStackTrace();
        }
        return localJSONArray;
    }


    private JSONObject f() {
        JSONObject localJSONObject = new JSONObject();
        try {
            UMImage localUMImage = this.c.getThumbImage();

            if ((localUMImage != null) && (localUMImage.isUrlMedia())) {
                localJSONObject.put("url", localUMImage.asUrlImage());
            } else {
                localJSONObject.put("url", "https://mobile.umeng.com/images/pic/home/social/img-1.png");
            }
            int[] arrayOfInt = g();
            localJSONObject.put("width", arrayOfInt[0]);
            localJSONObject.put("height", arrayOfInt[1]);
        } catch (JSONException localJSONException) {
            localJSONException.printStackTrace();
        }
        return localJSONObject;
    }

    private int[] g() {
        int[] arrayOfInt = new int[2];
        arrayOfInt[0] = 120;
        arrayOfInt[1] = 120;
        if ((this.c != null) && (this.c.getmExtra() != null)) {
            Map localMap = this.c.getmExtra();
            Integer localInteger;
            if (localMap.containsKey("width")) {
                localInteger = (Integer) localMap.get("width");
                arrayOfInt[0] = localInteger.intValue();
            }

            if (localMap.containsKey("height")) {
                localInteger = (Integer) localMap.get("height");
                arrayOfInt[1] = localInteger.intValue();
            }
        }

        return arrayOfInt;
    }

    private JSONObject h() {
        JSONObject localJSONObject = new JSONObject();
        try {
            UMImage localUMImage = this.c.getThumbImage();

            if ((localUMImage != null) && (localUMImage.isUrlMedia())) {
                localJSONObject.put("url", localUMImage.asUrlImage());
            } else {
                localJSONObject.put("url", "https://mobile.umeng.com/images/pic/home/social/img-1.png");
            }
            int[] arrayOfInt = g();
            localJSONObject.put("width", arrayOfInt[0]);
            localJSONObject.put("height", arrayOfInt[1]);
        } catch (JSONException localJSONException) {
            localJSONException.printStackTrace();
        }
        return localJSONObject;
    }

    private JSONObject i() {
        JSONObject localJSONObject = new JSONObject();
        try {
            localJSONObject.put("url", this.c.toUrl());
        } catch (JSONException localJSONException) {
            localJSONException.printStackTrace();
        }
        return localJSONObject;
    }

    protected String getPath() {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("/share/linkcard/");
        localStringBuilder.append(SocializeUtils.getAppkey(this.mContext));
        localStringBuilder.append("/").append(Config.EntityKey).append("/");
        return localStringBuilder.toString();
    }
}


/* Location:              /Users/zl/Desktop/未命名文件夹 3/umeng_social_net.jar!/com/umeng/socialize/net/LinkcardRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */