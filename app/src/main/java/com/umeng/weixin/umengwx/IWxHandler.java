package com.umeng.weixin.umengwx;

public interface IWxHandler {
//    public abstract void Request(BaseRequest parama);
//
//    public abstract void BaseResponse(BaseResponse paramb);

    void response(BaseResponse baseResponse);

    void request(BaseRequest baseRequest);
}