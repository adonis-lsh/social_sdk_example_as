package com.umeng.weixin.umengwx;

import android.os.Bundle;

public class SendMessageToWeiXinResp extends BaseResp {
    private static final String j = "MicroMsg.SDK.SendAuth.Resp";
    private static final int k = 1024;
    public String resp_token;
    public String resp_state;
    public String wxUrl;
    public String wxLang;
    public String wxCountry;

    public SendMessageToWeiXinResp() {
    }

    public SendMessageToWeiXinResp(Bundle paramBundle) {
        fromBundle(paramBundle);
    }

    public int getType() {
        return 1;
    }

    public void fromBundle(Bundle paramBundle) {
        super.fromBundle(paramBundle);
        this.resp_token = paramBundle.getString("_wxapi_sendauth_resp_token");
        this.resp_state = paramBundle.getString("_wxapi_sendauth_resp_state");
        this.wxUrl = paramBundle.getString("_wxapi_sendauth_resp_url");
        this.wxLang = paramBundle.getString("_wxapi_sendauth_resp_lang");
        this.wxCountry = paramBundle.getString("_wxapi_sendauth_resp_country");
    }

    public void toBundle(Bundle paramBundle) {
        super.toBundle(paramBundle);
        paramBundle.putString("_wxapi_sendauth_resp_token", this.resp_token);
        paramBundle.putString("_wxapi_sendauth_resp_state", this.resp_state);
        paramBundle.putString("_wxapi_sendauth_resp_url", this.wxUrl);
        paramBundle.putString("_wxapi_sendauth_resp_lang", this.wxLang);
        paramBundle.putString("_wxapi_sendauth_resp_country", this.wxCountry);
    }

    public boolean check() {
        return (this.resp_state == null) || (this.resp_state.length() <= 1024);
    }
}
