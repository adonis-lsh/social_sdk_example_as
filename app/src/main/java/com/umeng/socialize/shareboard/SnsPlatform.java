package com.umeng.socialize.shareboard;

import com.umeng.socialize.bean.SHARE_MEDIA;


public final class SnsPlatform {
    public String mKeyword;
    public String mShowWord;
    public String mIcon;
    public String mGrayIcon;
    public int mIndex;
    public SHARE_MEDIA mPlatform;

    public SnsPlatform(String paramString) {
        this.mKeyword = paramString;
        this.mPlatform = SHARE_MEDIA.convertToEmun(paramString);
    }

    public SnsPlatform() {
    }
}


/* Location:              /Users/zl/Desktop/未命名文件夹 3/umeng_social_net.jar!/com/umeng/socialize/shareboard/SnsPlatform.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */