package com.umeng.weixin.umengwx;

public interface IWXAPIEventHandler {
    void onResp(BaseResp baseResp);
    void onReq(BaseReq req);
}