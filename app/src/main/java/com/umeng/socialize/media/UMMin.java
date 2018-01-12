package com.umeng.socialize.media;

import com.umeng.socialize.net.utils.SocializeProtocolConstants;

import java.util.HashMap;
import java.util.Map;


public class UMMin
        extends BaseMediaObject {
    private String f;
    private String g;

    public MediaType getMediaType() {
        return MediaType.WEBPAGE;
    }

    public Map<String, Object> toUrlExtraParams() {
        HashMap localHashMap = new HashMap();
        if (isUrlMedia()) {
            localHashMap.put(SocializeProtocolConstants.PROTOCOL_KEY_FURL, this.url);
            localHashMap.put(SocializeProtocolConstants.PROTOCOL_KEY_FTYPE,
                    getMediaType());
            localHashMap.put(SocializeProtocolConstants.PROTOCOL_KEY_TITLE, this.b);
        }

        return localHashMap;
    }

    public UMMin(String paramString) {
        super(paramString);
    }

    public void setUserName(String paramString) {
        this.f = paramString;
    }

    public String getUserName() {
        return this.f;
    }

    public void setPath(String paramString) {
        this.g = paramString;
    }

    public String getPath() {
        return this.g;
    }

    public byte[] toByte() {
        if (this.e != null) {
            return this.e.toByte();
        }
        return null;
    }
}


/* Location:              /Users/zl/Desktop/未命名文件夹 3/umeng_social_net.jar!/com/umeng/socialize/media/UMMin.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */