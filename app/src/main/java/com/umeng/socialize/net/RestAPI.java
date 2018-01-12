package com.umeng.socialize.net;

import com.umeng.socialize.net.base.SocializeClient;
import com.umeng.socialize.net.base.SocializeReseponse;

public class RestAPI {
    private static SocializeClient a = new SocializeClient();

    public static ActionBarResponse queryShareId(ActionBarRequest paramActionBarRequest) {
        return (ActionBarResponse) a.execute(paramActionBarRequest);
    }


    public static PlatformTokenUploadResponse uploadPlatformToken(PlatformTokenUploadReq paramPlatformTokenUploadReq) {
        return (PlatformTokenUploadResponse) a.execute(paramPlatformTokenUploadReq);
    }

    public static LinkCardResponse convertLinkCard(LinkcardRequest paramLinkcardRequest) {
        return (LinkCardResponse) a.execute(paramLinkcardRequest);
    }


    public static SocializeReseponse doShare(SharePostRequest paramSharePostRequest) {
        return a.execute(paramSharePostRequest);
    }


    public static UrlResponse uploadUrl(UrlRequest paramUrlRequest) {
        return (UrlResponse) a.execute(paramUrlRequest);
    }
}
