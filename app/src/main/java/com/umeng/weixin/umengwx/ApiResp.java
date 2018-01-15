package com.umeng.weixin.umengwx;

import android.os.Bundle;

public class ApiResp extends BaseResp {
    public ApiResp() {
    }

    public ApiResp(Bundle bundle) {
        fromBundle(bundle);
    }

    public int getType() {
        return 2;
    }

    public void toBundle(Bundle bundle) {
        super.toBundle(bundle);
    }

    public void fromBundle(Bundle bundle){
        super.fromBundle(bundle);
    }


    public boolean check() {
        return true;
    }
}

