package com.umeng.weixin.umengwx;

import android.os.Bundle;

public class SendMessageToWeiXinReq extends BaseReq {
    private static final String e = "MicroMsg.SDK.SendAuth.Req";
    private static final int f = 1024;
    public String req_scope;
    public String req_state;

    public SendMessageToWeiXinReq() {
    }

    public SendMessageToWeiXinReq(Bundle paramBundle) {
        fromBundle(paramBundle);
    }

    public int getType() {
        return 1;
    }

    public void fromBundle(Bundle paramBundle) {
        super.fromBundle(paramBundle);
        this.req_scope = paramBundle.getString("_wxapi_sendauth_req_scope");
        this.req_state = paramBundle.getString("_wxapi_sendauth_req_state");
    }

    public void toBundle(Bundle paramBundle) {
        super.toBundle(paramBundle);
        paramBundle.putString("_wxapi_sendauth_req_scope", this.req_scope);
        paramBundle.putString("_wxapi_sendauth_req_state", this.req_state);
    }

    public boolean check() {
        if ((this.req_scope != null) && (this.req_scope.length() != 0) && (this.req_scope.length() <= 1024)) {
            return (this.req_state == null) || (this.req_state.length() <= 1024);
        }
        return false;
    }
}
