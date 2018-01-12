package com.umeng.socialize.net;

import android.content.Context;

import com.umeng.socialize.Config;
import com.umeng.socialize.ShareContent;
import com.umeng.socialize.net.base.SocializeRequest;
import com.umeng.socialize.net.base.SocializeReseponse;
import com.umeng.socialize.net.utils.URequest;
import com.umeng.socialize.net.utils.URequest.RequestMethod;
import com.umeng.socialize.utils.SocializeUtils;


public class SharePostRequest
        extends SocializeRequest {
    private static final String a = "/share/add/";
    private static final int b = 9;
    private String c;
    private String d;
    private ShareContent e;

    public SharePostRequest(Context paramContext, String paramString1, String paramString2, ShareContent paramShareContent) {
        super(paramContext, "", SocializeReseponse.class, 9, URequest.RequestMethod.POST);

        this.mContext = paramContext;
        this.c = paramString1;
        this.d = paramString2;
        this.e = paramShareContent;
    }


    public void onPrepareRequest() {
        addStringParams("to", this.c);
        addStringParams("ct", this.e.mText);
        addStringParams("usid", this.d);

        addStringParams("ak", SocializeUtils.getAppkey(this.mContext));
        addStringParams("ek", Config.EntityKey);
        addMediaParams(this.e.mMedia);
    }

    protected String getPath() {
        String str = "/share/add/" + SocializeUtils.getAppkey(this.mContext) + "/" + Config.EntityKey + "/";
        return str;
    }
}


/* Location:              /Users/zl/Desktop/未命名文件夹 3/umeng_social_net.jar!/com/umeng/socialize/net/SharePostRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */