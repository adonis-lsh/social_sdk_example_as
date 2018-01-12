//
// Source code recreated from BitmapUtil .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.umeng.socialize.net.utils;

import android.content.Context;
import android.text.TextUtils;

import com.umeng.socialize.net.base.SocializeReseponse;
import com.umeng.socialize.utils.Log;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

public abstract class URequest {
    protected URequest.MIME mMimeType;
    public Map<String, String> mHeaders;
    public Map<String, String> mParams = new HashMap();
    public Class<? extends SocializeReseponse> mResponseClz;
    public Context mContext;
    public URequest.RequestMethod mMethod;
    public URequest.PostStyle postStyle;
    protected static String POST = "POST";
    protected static String GET = "GET";
    protected static String MULTIPART = "multipart/form-data";
    protected static String APPLICATION = "application/x-www-form-urlencoded";
    protected String mBaseUrl;

    public abstract JSONObject toJson();

    public abstract String toGetUrl();

    protected String getHttpMethod() {
        return this.mMethod.toString();
    }

    public URequest(String var1) {
        this.postStyle = URequest.PostStyle.MULTIPART;
        this.mBaseUrl = var1;
    }

    public void setBaseUrl(String var1) {
        this.mBaseUrl = var1;
    }

    public String getBaseUrl() {
        return this.mBaseUrl;
    }

    public Map<String, Object> getBodyPair() {
        return null;
    }

    public Map<String, URequest.FilePair> getFilePair() {
        return null;
    }

    public void onPrepareRequest() {
    }

    public String getEcryptString(String var1) {
        return var1;
    }

    public String getDecryptString(String var1) {
        return var1;
    }

    public String generateGetURL(String var1, Map<String, Object> var2) {
        return this.buildGetUrl(var1, var2);
    }

    public String buildGetUrl(String var1, Map<String, Object> var2) {
        if (!TextUtils.isEmpty(var1) && var2 != null && var2.size() != 0) {
            if (!var1.endsWith("?")) {
                var1 = var1 + "?";
            }

            String var3 = buildGetParams(var2);
            Log.net("urlPath=" + var1 + "  SocializeNetUtils url=" + var3);

            try {
                String var4 = this.getEcryptString(var3);
                var3 = var4;
            } catch (Exception var5) {
                var5.printStackTrace();
            }

            StringBuilder var6 = new StringBuilder(var1);
            var6.append(var3);
            return var6.toString();
        } else {
            return var1;
        }
    }

    public static String buildGetParams(Map<String, Object> var0) {
        StringBuilder var1 = new StringBuilder();
        Set var2 = var0.keySet();
        Iterator var3 = var2.iterator();

        while (var3.hasNext()) {
            String var4 = (String) var3.next();
            if (var0.get(var4) != null) {
                var1 = var1.append(var4 + "=" + URLEncoder.encode(var0.get(var4).toString()) + "&");
            }
        }

        String var5 = var1.substring(0, var1.length() - 1).toString();
        return var5;
    }

    public abstract Map<String, Object> buildParams();

    public void addStringParams(String var1, String var2) {
        if (!TextUtils.isEmpty(var2)) {
            this.mParams.put(var1, var2);
        }

    }

    protected static enum MIME {
        DEFAULT("application/x-www-form-urlencoded;charset=utf-8"),
        JSON("application/json;charset=utf-8");

        private String mimeType;

        private MIME(String var3) {
            this.mimeType = var3;
        }

        public String toString() {
            return this.mimeType;
        }
    }

    public static class FilePair {
        String mFileName;
        byte[] mBinaryData;

        public FilePair(String var1, byte[] var2) {
            this.mFileName = var1;
            this.mBinaryData = var2;
        }
    }

    public static enum PostStyle {
        MULTIPART {
            public String toString() {
                return URequest.MULTIPART;
            }
        },
        APPLICATION {
            public String toString() {
                return URequest.APPLICATION;
            }
        };

        private PostStyle() {
        }
    }

    public static enum RequestMethod {
        GET {
            public String toString() {
                return URequest.GET;
            }
        },
        POST {
            public String toString() {
                return URequest.POST;
            }
        };

        private RequestMethod() {
        }
    }
}
