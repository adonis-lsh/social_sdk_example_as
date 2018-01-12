package com.umeng.socialize.media;

import android.os.Parcel;

import com.umeng.socialize.net.utils.SocializeProtocolConstants;

import java.util.HashMap;
import java.util.Map;


public class UMusic
        extends BaseMediaObject {
    private String f;
    private String g;
    private String h;
    private String i;
    private int j;
    private String k;

    public void setmTargetUrl(String paramString) {
        this.k = paramString;
    }

    public String getmTargetUrl() {
        return this.k;
    }

    public int getDuration() {
        return this.j;
    }

    public void setDuration(int paramInt) {
        this.j = paramInt;
    }

    public String getLowBandUrl() {
        return this.i;
    }

    public void setLowBandUrl(String paramString) {
        this.i = paramString;
    }


    public UMusic(String paramString) {
        super(paramString);
    }

    public String getHighBandDataUrl() {
        return this.g;
    }

    public void setHighBandDataUrl(String paramString) {
        this.g = paramString;
    }

    public String getH5Url() {
        return this.h;
    }

    public void setH5Url(String paramString) {
        this.h = paramString;
    }


    public MediaType getMediaType() {
        return MediaType.MUSIC;
    }


    protected UMusic(Parcel paramParcel) {
        super(paramParcel);
    }


    public final Map<String, Object> toUrlExtraParams() {
        HashMap localHashMap = new HashMap();
        if (isUrlMedia()) {
            localHashMap.put(SocializeProtocolConstants.PROTOCOL_KEY_FURL, this.url);
            localHashMap.put(SocializeProtocolConstants.PROTOCOL_KEY_FTYPE,
                    getMediaType());
            localHashMap.put(SocializeProtocolConstants.PROTOCOL_KEY_TITLE, this.b);
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
        return "UMusic [title=" + this.b + "media_url=" + this.url + ", qzone_title=" + this.b + ", qzone_thumb=" + "]";
    }


    public UMImage getThumbImage() {
        return this.e;
    }

    public String getLowBandDataUrl() {
        return this.f;
    }

    public void setLowBandDataUrl(String paramString) {
        this.f = paramString;
    }
}


/* Location:              /Users/zl/Desktop/未命名文件夹 3/umeng_social_net.jar!/com/umeng/socialize/media/UMusic.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */