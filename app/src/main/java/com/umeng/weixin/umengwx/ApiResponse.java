package com.umeng.weixin.umengwx;

import android.os.Bundle;

public class ApiResponse extends BaseResponse{
    public ApiResponse() {
    }

    public ApiResponse(Bundle paramBundle) {

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

