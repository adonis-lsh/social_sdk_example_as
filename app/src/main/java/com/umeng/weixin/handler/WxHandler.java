package com.umeng.weixin.handler;

import com.umeng.weixin.umengwx.ApiResponse;
import com.umeng.weixin.umengwx.BaseRequest;
import com.umeng.weixin.umengwx.BaseResponse;
import com.umeng.weixin.umengwx.IWxHandler;
import com.umeng.weixin.umengwx.SendMessageToWeiXinResponse;

class WxHandler implements IWxHandler {
    private UmengWXHandler mUmengWXHandler;

    WxHandler(UmengWXHandler umengWXHandler) {
        mUmengWXHandler = umengWXHandler;
    }

    public void response(BaseResponse baseResponse) {
        int i = baseResponse.getType();
        switch (i) {
            case 1:
                mUmengWXHandler.getResult(baseResponse);
                break;
            case 2:
                mUmengWXHandler.getResult(baseResponse);
                break;
        }
    }

    public void request(BaseRequest baseRequest) {
    }
}