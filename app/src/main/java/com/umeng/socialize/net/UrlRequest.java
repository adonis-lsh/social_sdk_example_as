//
// Source code recreated from BitmapUtil .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.umeng.socialize.net;

import android.content.Context;

import com.umeng.socialize.net.base.SocializeRequest;
import com.umeng.socialize.net.utils.URequest.RequestMethod;
import com.umeng.socialize.utils.SocializeUtils;

public class UrlRequest extends SocializeRequest {
    private static final String a = "/link/add/";
    private String b;
    private String c;
    private static final int d = 26;

    public UrlRequest(Context var1, String var2, String var3) {
        super(var1, "", UrlResponse.class, 26, RequestMethod.POST);
        this.mContext = var1;
        this.b = var3;
        this.c = var2;
    }

    public void onPrepareRequest() {
        super.onPrepareRequest();
        this.addStringParams("url", this.b);
        this.addStringParams("to", this.c);
    }

    protected String getPath() {
        return "/link/add/" + SocializeUtils.getAppkey(this.mContext) + "/";
    }
}
