package com.umeng.socialize.net;


import android.content.Context;
import android.text.TextUtils;

import com.umeng.socialize.Config;
import com.umeng.socialize.net.base.SocializeRequest;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import com.umeng.socialize.net.utils.URequest;
import com.umeng.socialize.utils.SocializeUtils;


public class ActionBarRequest extends SocializeRequest {
    private static final String a = "/bar/get/";
    private static final int b = 1;
    private int c = 0;


    public ActionBarRequest(Context paramContext, boolean paramBoolean) {
        super(paramContext, "", ActionBarResponse.class, 1, URequest.RequestMethod.GET);
        this.mContext = paramContext;
        this.c = (paramBoolean ? 1 : 0);
        this.mMethod = URequest.RequestMethod.GET;
    }

    public void onPrepareRequest() {
        addStringParams("dc", Config.Descriptor);
        addStringParams(SocializeProtocolConstants.PROTOCOL_KEY_NEW_INSTALL, String.valueOf(this.c));

        if (!TextUtils.isEmpty(Config.EntityName)) {
            addStringParams(SocializeProtocolConstants.PROTOCOL_KEY_ENTITY_NAME, Config.EntityName);
        }
    }


    protected String getPath() {
        return "/bar/get/" + SocializeUtils.getAppkey(this.mContext) + "/";
    }
}