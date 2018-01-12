package com.umeng.weixin.umengwx;

import android.os.Bundle;

public abstract class BaseResponse {
    public int wxErrorCode;
    public String wxErrStr;
    public String wxTransaction;
    public String wxOpenId;

    public abstract int getType();

    public void toBundle(Bundle paramBundle) {
        paramBundle.putInt("_wxapi_command_type", getType());
        paramBundle.putInt("_wxapi_baseresp_errcode", this.wxErrorCode);
        paramBundle.putString("_wxapi_baseresp_errstr", this.wxErrStr);
        paramBundle.putString("_wxapi_baseresp_transaction", this.wxTransaction);
        paramBundle.putString("_wxapi_baseresp_openId", this.wxOpenId);
    }

    public void fromBundle(Bundle paramBundle) {
        this.wxErrorCode = paramBundle.getInt("_wxapi_baseresp_errcode");
        this.wxErrStr = paramBundle.getString("_wxapi_baseresp_errstr");
        this.wxTransaction = paramBundle.getString("_wxapi_baseresp_transaction");
        this.wxOpenId = paramBundle.getString("_wxapi_baseresp_openId");
    }

    public abstract boolean check();
}