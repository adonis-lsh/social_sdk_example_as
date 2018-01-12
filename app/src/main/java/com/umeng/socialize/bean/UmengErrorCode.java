//
// Source code recreated from BitmapUtil .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.umeng.socialize.bean;

public enum UmengErrorCode {
    UnKnowCode(2000),
    AuthorizeFailed(2002),
    ShareFailed(2003),
    RequestForUserProfileFailed(2004),
    ShareDataNil(2004),
    ShareDataTypeIllegal(2004),
    NotInstall(2008);

    private final int a;

    private UmengErrorCode(int var3) {
        this.a = var3;
    }

    public String getMessage() {
        return this == UnKnowCode ? this.a() + "未知错误----" : (this == AuthorizeFailed ? this.a() + "授权失败----" : (this == ShareFailed ? this.a() + "分享失败----" : (this == RequestForUserProfileFailed ? this.a() + "获取用户资料失败----" : (this == ShareDataNil ? this.a() + "分享内容为空" : (this == ShareDataTypeIllegal ? this.a() + "分享内容不合法----" : (this == NotInstall ? this.a() + "没有安装应用" + " 点击查看错误：" + "https://at.umeng.com/ve4Pbm?cid=476" : "unkonw"))))));
    }

    private String a() {
        return "错误码：" + this.a + " 错误信息：";
    }
}
