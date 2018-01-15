package com.umeng.weixin.umengwx;

import android.os.Bundle;

public abstract class BaseReq {
    public String wxTransaction;
    public String wxOpenid;

    public abstract int getType();

    public void toBundle(Bundle paramBundle) {
        paramBundle.putInt("_wxapi_command_type", getType());
        paramBundle.putString("_wxapi_basereq_transaction", this.wxTransaction);
        paramBundle.putString("_wxapi_basereq_openid", this.wxOpenid);
    }

    public void fromBundle(Bundle paramBundle) {
        this.wxTransaction = BundleUtils.getStringBundle(paramBundle, "_wxapi_basereq_transaction");
        this.wxOpenid = BundleUtils.getStringBundle(paramBundle, "_wxapi_basereq_openid");
    }

    public abstract boolean check();
}