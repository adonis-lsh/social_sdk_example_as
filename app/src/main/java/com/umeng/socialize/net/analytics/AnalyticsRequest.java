//
// Source code recreated from BitmapUtil .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.umeng.socialize.net.analytics;

import android.content.Context;
import android.text.TextUtils;

import com.umeng.socialize.Config;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.media.UMediaObject;
import com.umeng.socialize.media.UMusic;
import com.umeng.socialize.net.base.SocializeRequest;
import com.umeng.socialize.utils.SocializeUtils;

public class AnalyticsRequest extends SocializeRequest {
    private static final String a = "/share/multi_add/";
    private static final int b = 9;
    private String c;
    private String d;
    private String e;
    private String f;
    private String g;
    private String h;
    private UMediaObject i;

    public AnalyticsRequest(Context var1, String var2, String var3) {
        super(var1, "", ShareMultiResponse.class, 9, RequestMethod.POST);
        this.mContext = var1;
        this.c = var2;
        this.h = var3;
        this.setReqType(1);
    }

    public void a(String var1) {
        this.c = var1;
    }

    public void b(String var1) {
        this.d = var1;
    }

    public void c(String var1) {
        this.e = var1;
    }

    public void d(String var1) {
        this.h = var1;
    }

    public void a(UMediaObject var1) {
        if (var1 instanceof UMImage) {
            this.i = var1;
        } else if (var1 instanceof UMusic) {
            this.f = ((UMusic) var1).getTitle();
            this.g = ((UMusic) var1).toUrl();
            this.h = ((UMusic) var1).getDescription();
            this.i = ((UMusic) var1).getThumbImage();
        } else if (var1 instanceof UMVideo) {
            this.f = ((UMVideo) var1).getTitle();
            this.g = ((UMVideo) var1).toUrl();
            this.h = ((UMVideo) var1).getDescription();
            this.i = ((UMVideo) var1).getThumbImage();
        } else if (var1 instanceof UMWeb) {
            this.f = ((UMWeb) var1).getTitle();
            this.g = ((UMWeb) var1).toUrl();
            this.h = ((UMWeb) var1).getDescription();
            this.i = ((UMWeb) var1).getThumbImage();
        }

    }

    public void onPrepareRequest() {
        super.onPrepareRequest();
        String var1 = String.format("{\"%WXShareContent\":\"%WXShareContent\"}", new Object[]{this.c, this.d == null ? "" : this.d});
        String var2 = SocializeUtils.getAppkey(this.mContext);
        this.addStringParams("dc", Config.Descriptor);
        this.addStringParams("to", var1);
        this.addStringParams("sns", var1);
        this.addStringParams("ak", var2);
        this.addStringParams("type", this.e);
        this.addStringParams("ct", this.h);
        if (!TextUtils.isEmpty(this.g)) {
            this.addStringParams("url", this.g);
        }

        if (!TextUtils.isEmpty(this.f)) {
            this.addStringParams("title", this.f);
        }

        this.addMediaParams(this.i);
    }

    protected String getPath() {
        StringBuilder var1 = new StringBuilder();
        var1.append("/share/multi_add/");
        var1.append(SocializeUtils.getAppkey(this.mContext));
        var1.append("/").append(Config.EntityKey).append("/");
        return var1.toString();
    }
}
