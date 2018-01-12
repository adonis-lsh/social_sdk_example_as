package com.umeng.socialize.media;

import com.umeng.socialize.net.utils.SocializeProtocolConstants;

import java.util.HashMap;
import java.util.Map;


public class UMVideo
        extends BaseMediaObject {
    private String f;
    private String g;
    private String h;
    private String i;
    private int j;

    public int getDuration() {
        return this.j;
    }

    public void setDuration(int paramInt) {
        this.j = paramInt;
    }


    public UMVideo(String paramString) {
        super(paramString);
    }

    public String getLowBandUrl() {
        return this.f;
    }

    public String getLowBandDataUrl() {
        return this.g;
    }

    public void setLowBandDataUrl(String paramString) {
        this.g = paramString;
    }

    public String getHighBandDataUrl() {
        return this.h;
    }

    public void setHighBandDataUrl(String paramString) {
        this.h = paramString;
    }

    public String getH5Url() {
        return this.i;
    }

    public void setH5Url(String paramString) {
        this.i = paramString;
    }

    public void setLowBandUrl(String paramString) {
        this.f = paramString;
    }

    public MediaType getMediaType() {
        return MediaType.VEDIO;
    }

    public final Map<String, Object> toUrlExtraParams() {
        HashMap localHashMap = new HashMap();
        if (isUrlMedia()) {
            localHashMap.put(SocializeProtocolConstants.PROTOCOL_KEY_FURL, this.url);
            localHashMap.put(SocializeProtocolConstants.PROTOCOL_KEY_FTYPE,
                    getMediaType());
        }
        return localHashMap;
    }


    public byte[] toByte() {
        if (this.e != null) {
            return this.e.toByte();
        }
        return null;
    }

    public String toString() {
        return "UMVedio [media_url=" + this.url + ", qzone_title=" + this.b + ", qzone_thumb=" + "media_url=" + this.url + ", qzone_title=" + this.b + ", qzone_thumb=" + "]";
    }
}


/* Location:              /Users/zl/Desktop/未命名文件夹 3/umeng_social_net.jar!/com/umeng/socialize/media/UMVideo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */