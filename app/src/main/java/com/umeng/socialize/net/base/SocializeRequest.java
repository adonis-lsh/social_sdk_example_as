//
// Source code recreated from BitmapUtil .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.umeng.socialize.net.base;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.umeng.socialize.Config;
import com.umeng.socialize.SocializeException;
import com.umeng.socialize.cache.util.BitmapUtil;
import com.umeng.socialize.common.SocializeConstants;
import com.umeng.socialize.media.BaseMediaObject;
import com.umeng.socialize.media.UMediaObject;
import com.umeng.socialize.net.utils.AesHelper;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import com.umeng.socialize.net.utils.URequest;
import com.umeng.socialize.utils.DeviceConfig;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.SocializeUtils;

import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;

public abstract class SocializeRequest extends URequest {
    public static final int REQUEST_SOCIAL = 0;
    public static final int REQUEST_ANALYTIC = 1;
    public static final int REQUEST_API = 2;
    private static final String BASE_URL = "http://log.umsns.com/";
    private Map<String, FilePair> mFileMap = new HashMap();
    public int mOpId;
    private int mReqType = 1;
    private static final String TAG = "SocializeRequest";

    public SocializeRequest(Context var1, String var2, Class<? extends SocializeReseponse> var3, int var4, RequestMethod var5) {
        super("");
        this.mResponseClz = var3;
        this.mOpId = var4;
        this.mContext = var1;
        this.mMethod = var5;
        this.setBaseUrl("http://log.umsns.com/");
        AesHelper.setPassword(SocializeUtils.getAppkey(var1));
    }

    public void setReqType(int var1) {
        this.mReqType = var1;
    }

    public void addFileParams(byte[] var1, SocializeRequest.FILE_TYPE var2, String var3) {
        if (SocializeRequest.FILE_TYPE.IMAGE == var2) {
            String var4 = BitmapUtil.d(var1);
            if (TextUtils.isEmpty(var4)) {
                var4 = "png";
            }

            if (TextUtils.isEmpty(var3)) {
                var3 = String.valueOf(System.currentTimeMillis());
            }

            FilePair var5 = new FilePair(var3 + "." + var4, var1);
            this.mFileMap.put(SocializeProtocolConstants.PROTOCOL_KEY_IMAGE, var5);
        }

    }

    public void addMediaParams(UMediaObject var1) {
        if (var1 != null) {
            if (var1 instanceof BaseMediaObject) {
                BaseMediaObject var2 = (BaseMediaObject) var1;
                this.addStringParams(SocializeProtocolConstants.PROTOCOL_KEY_TITLE, var2.getTitle());
            }

            if (var1.isUrlMedia()) {
                Map var5 = var1.toUrlExtraParams();
                Iterator var3 = var5.entrySet().iterator();

                while (var3.hasNext()) {
                    Entry var4 = (Entry) var3.next();
                    this.addStringParams((String) var4.getKey(), var4.getValue().toString());
                }
            } else {
                byte[] var6 = var1.toByte();
                if (var6 != null) {
                    this.addFileParams(var6, SocializeRequest.FILE_TYPE.IMAGE, (String) null);
                }
            }

        }
    }

    public Map<String, Object> getBodyPair() {
        Map var1 = this.buildParams();
        String var2 = this.mapTostring(var1);
        if (var2 != null) {
            try {
                Log.net("SocializeRequest body=" + var2);
                String var3 = AesHelper.encryptNoPadding(URLEncoder.encode(var2, "UTF-8"), "UTF-8");
                var1.clear();
                var1.put("ud_post", var3);
            } catch (Exception var4) {
                var4.printStackTrace();
            }
        }

        return var1;
    }

    public Map<String, FilePair> getFilePair() {
        return this.mFileMap;
    }

    public JSONObject toJson() {
        return null;
    }

    public String toGetUrl() {
        Map var1 = this.buildParams();
        return this.generateGetURL(this.getBaseUrl(), var1);
    }

    public Map<String, Object> buildParams() {
        Map var1 = getBaseQuery(this.mContext);
        if (!TextUtils.isEmpty(Config.EntityKey)) {
            var1.put("ek", Config.EntityKey);
        }

        if (!TextUtils.isEmpty(Config.SessionId)) {
            var1.put("sid", Config.SessionId);
        }

        var1.put("tp", Integer.valueOf(this.mReqType));
        var1.put("opid", Integer.valueOf(this.mOpId));
        var1.put("uid", Config.UID);
        var1.putAll(this.mParams);
        return var1;
    }

    public void setBaseUrl(String var1) {
        String var2 = "";

        try {
            if (!TextUtils.isEmpty(this.getPath())) {
                URL var3 = new URL(var1);
                var2 = (new URL(var3, this.getPath())).toString();
            }
        } catch (Exception var4) {
            throw new SocializeException("Can not generate correct url in SocializeRequest [" + var1 + "]", var4);
        }

        super.setBaseUrl(var2);
    }

    protected abstract String getPath();

    public void onPrepareRequest() {
        this.addStringParams("pcv", "2.0");
        this.addStringParams("u_sharetype", Config.shareType);
        String var1 = DeviceConfig.getDeviceId(this.mContext);
        this.addStringParams("imei", var1);
        this.addStringParams("md5imei", AesHelper.md5(var1));
        this.addStringParams("de", Build.MODEL);
        this.addStringParams("mac", DeviceConfig.getMac(this.mContext));
        this.addStringParams("os", "Android");
        this.addStringParams("en", DeviceConfig.getNetworkAccessMode(this.mContext)[0]);
        this.addStringParams("uid", (String) null);
        this.addStringParams("sdkv", "6.4.6");
        this.addStringParams("dt", String.valueOf(System.currentTimeMillis()));
    }

    private String mapTostring(Map<String, Object> var1) {
        if (this.mParams.isEmpty()) {
            return null;
        } else {
            try {
                return (new JSONObject(var1)).toString();
            } catch (Exception var3) {
                var3.printStackTrace();
                return null;
            }
        }
    }

    protected String getHttpMethod() {
        switch (mMethod) {
            case POST:
                return POST;
            case GET:
            default:
                return GET;
        }
    }

    public String getEcryptString(String var1) {
        try {
            return "ud_get=" + AesHelper.encryptNoPadding(var1, "UTF-8");
        } catch (Exception var3) {
            var3.printStackTrace();
            return "ud_get=" + var1;
        }
    }

    public String getDecryptString(String var1) {
        try {
            return AesHelper.decryptNoPadding(var1, "UTF-8").trim();
        } catch (Exception var3) {
            var3.printStackTrace();
            return var1;
        }
    }

    public static Map<String, Object> getBaseQuery(Context var0) {
        HashMap var1 = new HashMap();
        String var2 = DeviceConfig.getDeviceId(var0);
        if (!TextUtils.isEmpty(var2)) {
            var1.put("imei", var2);
            var1.put("md5imei", AesHelper.md5(var2));
        }

        String var3 = DeviceConfig.getMac(var0);
        if (TextUtils.isEmpty(var3)) {
            Log.w("SocializeRequest", "Get MacAddress failed. Check permission android.permission.ACCESS_WIFI_STATE [" + DeviceConfig.checkPermission(var0, "android.permission.ACCESS_WIFI_STATE") + "]");
        } else {
            var1.put("mac", var3);
        }

        if (!TextUtils.isEmpty(SocializeConstants.UID)) {
            var1.put("uid", SocializeConstants.UID);
        }

        try {
            String[] var4 = DeviceConfig.getNetworkAccessMode(var0);
            var1.put("en", var4[0]);
        } catch (Exception var7) {
            var1.put("en", "Unknown");
        }

        var1.put("de", Build.MODEL);
        var1.put("sdkv", "6.4.6");
        var1.put("os", "Android");
        var1.put("android_id", DeviceConfig.getAndroidID(var0));
        var1.put("sn", DeviceConfig.getDeviceSN());
        var1.put("os_version", DeviceConfig.getOsVersion());
        var1.put("dt", Long.valueOf(System.currentTimeMillis()));
        String var8 = SocializeUtils.getAppkey(var0);
        var1.put("ak", var8);
        var1.put(SocializeProtocolConstants.PROTOCOL_VERSION, "2.0");
        var1.put("u_sharetype", Config.shareType);
        if (!TextUtils.isEmpty(Config.EntityKey)) {
            var1.put("ek", Config.EntityKey);
        }

        if (!TextUtils.isEmpty(Config.SessionId)) {
            var1.put("sid", Config.SessionId);
        }

        try {
            var1.put("tp", Integer.valueOf(0));
        } catch (Exception var6) {
            ;
        }

        return var1;
    }

    public static enum FILE_TYPE {
        IMAGE,
        VEDIO;

        private FILE_TYPE() {
        }
    }
}
