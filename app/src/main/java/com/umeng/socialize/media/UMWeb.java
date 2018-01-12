package com.umeng.socialize.media;

import com.umeng.socialize.net.utils.SocializeProtocolConstants;

import java.util.HashMap;
import java.util.Map;


public class UMWeb
        extends BaseMediaObject {
    public UMWeb(String paramString) {
        super(paramString);
    }


    public UMWeb(String paramString1, String paramString2, String paramString3, UMImage paramUMImage) {
        this.url = paramString1;
        setThumb(paramUMImage);
        this.d = paramString3;
        setTitle(paramString2);
    }

    public MediaType getMediaType() {
        return MediaType.WEBPAGE;
    }

    public Map<String, Object> toUrlExtraParams() {
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
        return "UMWEB [media_url=" + this.url + ", title=" + this.b + "" + "media_url=" + this.url + ", des=" + this.d + ", qzone_thumb=" + "]";
    }
}


/* Location:              /Users/zl/Desktop/未命名文件夹 3/umeng_social_net.jar!/com/umeng/socialize/media/UMWeb.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */