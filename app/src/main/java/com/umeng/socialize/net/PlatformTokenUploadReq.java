//
// Source code recreated from BitmapUtil .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.umeng.socialize.net;

import android.content.Context;

import com.umeng.socialize.net.base.SocializeRequest;
import com.umeng.socialize.net.utils.URequest.RequestMethod;
import com.umeng.socialize.utils.SocializeUtils;

public class PlatformTokenUploadReq extends SocializeRequest {
    private static final String a = "/share/token/";
    private static final int b = 21;

    public PlatformTokenUploadReq(Context var1) {
        super(var1, "", PlatformTokenUploadResponse.class, 21, RequestMethod.POST);
    }

    protected String getPath() {
        String var1 = "/share/token/" + SocializeUtils.getAppkey(this.mContext) + "/";
        return var1;
    }
}
