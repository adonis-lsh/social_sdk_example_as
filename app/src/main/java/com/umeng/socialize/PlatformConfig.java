package com.umeng.socialize;

import android.text.TextUtils;

import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class PlatformConfig {
    public static Map<SHARE_MEDIA, Platform> configs = new HashMap();


    static {
        configs.put(SHARE_MEDIA.QQ, new APPIDPlatform(SHARE_MEDIA.QQ));
        configs.put(SHARE_MEDIA.QZONE, new APPIDPlatform(SHARE_MEDIA.QZONE));
        configs.put(SHARE_MEDIA.WEIXIN, new APPIDPlatform(SHARE_MEDIA.WEIXIN));
        configs.put(SHARE_MEDIA.VKONTAKTE, new APPIDPlatform(SHARE_MEDIA.WEIXIN));
        configs.put(SHARE_MEDIA.WEIXIN_CIRCLE, new APPIDPlatform(SHARE_MEDIA.WEIXIN_CIRCLE));
        configs.put(SHARE_MEDIA.WEIXIN_FAVORITE, new APPIDPlatform(SHARE_MEDIA.WEIXIN_FAVORITE));
        configs.put(SHARE_MEDIA.FACEBOOK_MESSAGER, new CustomPlatform(SHARE_MEDIA.FACEBOOK_MESSAGER));
        configs.put(SHARE_MEDIA.DOUBAN, new CustomPlatform(SHARE_MEDIA.DOUBAN));
        configs.put(SHARE_MEDIA.LAIWANG, new APPIDPlatform(SHARE_MEDIA.LAIWANG));
        configs.put(SHARE_MEDIA.LAIWANG_DYNAMIC, new APPIDPlatform(SHARE_MEDIA.LAIWANG_DYNAMIC));
        configs.put(SHARE_MEDIA.YIXIN, new APPIDPlatform(SHARE_MEDIA.YIXIN));
        configs.put(SHARE_MEDIA.YIXIN_CIRCLE, new APPIDPlatform(SHARE_MEDIA.YIXIN_CIRCLE));
        configs.put(SHARE_MEDIA.SINA, new APPIDPlatform(SHARE_MEDIA.SINA));
        configs.put(SHARE_MEDIA.TENCENT, new CustomPlatform(SHARE_MEDIA.TENCENT));
        configs.put(SHARE_MEDIA.ALIPAY, new APPIDPlatform(SHARE_MEDIA.ALIPAY));
        configs.put(SHARE_MEDIA.RENREN, new CustomPlatform(SHARE_MEDIA.RENREN));
        configs.put(SHARE_MEDIA.DROPBOX, new APPIDPlatform(SHARE_MEDIA.DROPBOX));
        configs.put(SHARE_MEDIA.GOOGLEPLUS, new CustomPlatform(SHARE_MEDIA.GOOGLEPLUS));
        configs.put(SHARE_MEDIA.FACEBOOK, new CustomPlatform(SHARE_MEDIA.FACEBOOK));
        configs.put(SHARE_MEDIA.TWITTER, new APPIDPlatform(SHARE_MEDIA.TWITTER));
        configs.put(SHARE_MEDIA.TUMBLR, new CustomPlatform(SHARE_MEDIA.TUMBLR));
        configs.put(SHARE_MEDIA.PINTEREST, new APPIDPlatform(SHARE_MEDIA.PINTEREST));
        configs.put(SHARE_MEDIA.POCKET, new CustomPlatform(SHARE_MEDIA.POCKET));
        configs.put(SHARE_MEDIA.WHATSAPP, new CustomPlatform(SHARE_MEDIA.WHATSAPP));
        configs.put(SHARE_MEDIA.EMAIL, new CustomPlatform(SHARE_MEDIA.EMAIL));
        configs.put(SHARE_MEDIA.SMS, new CustomPlatform(SHARE_MEDIA.SMS));
        configs.put(SHARE_MEDIA.LINKEDIN, new CustomPlatform(SHARE_MEDIA.LINKEDIN));
        configs.put(SHARE_MEDIA.LINE, new CustomPlatform(SHARE_MEDIA.LINE));
        configs.put(SHARE_MEDIA.FLICKR, new CustomPlatform(SHARE_MEDIA.FLICKR));
        configs.put(SHARE_MEDIA.EVERNOTE, new CustomPlatform(SHARE_MEDIA.EVERNOTE));
        configs.put(SHARE_MEDIA.FOURSQUARE, new CustomPlatform(SHARE_MEDIA.FOURSQUARE));
        configs.put(SHARE_MEDIA.YNOTE, new APPIDPlatform(SHARE_MEDIA.YNOTE));
        configs.put(SHARE_MEDIA.KAKAO, new APPIDPlatform(SHARE_MEDIA.KAKAO));
        configs.put(SHARE_MEDIA.INSTAGRAM, new CustomPlatform(SHARE_MEDIA.INSTAGRAM));
        configs.put(SHARE_MEDIA.MORE, new CustomPlatform(SHARE_MEDIA.MORE));
        configs.put(SHARE_MEDIA.DINGTALK, new APPIDPlatform(SHARE_MEDIA.MORE));
    }

    public static void setQQZone(String id, String key) {
        APPIDPlatform qzone = (APPIDPlatform) configs.get(SHARE_MEDIA.QZONE);
        qzone.appId = id;
        qzone.appkey = key;
        APPIDPlatform qq = (APPIDPlatform) configs.get(SHARE_MEDIA.QQ);
        qq.appId = id;
        qq.appkey = key;
    }

    public static void setTwitter(String key, String secret) {
        APPIDPlatform twitter = (APPIDPlatform) configs.get(SHARE_MEDIA.TWITTER);
        twitter.appId = key;
        twitter.appkey = secret;
    }

    public static void setAlipay(String id) {
        APPIDPlatform alipay = (APPIDPlatform) configs.get(SHARE_MEDIA.ALIPAY);
        alipay.appId = id;
    }

    public static void setDropbox(String id, String secret) {
        APPIDPlatform dropbox = (APPIDPlatform) configs.get(SHARE_MEDIA.DROPBOX);
        dropbox.appId = id;
        dropbox.appkey = secret;
    }

    public static void setDing(String id) {
        APPIDPlatform ding = (APPIDPlatform) configs.get(SHARE_MEDIA.DINGTALK);
        ding.appId = id;
    }

    public static void setSinaWeibo(String key, String secret, String redirectUrl) {
        APPIDPlatform weibo = (APPIDPlatform) configs.get(SHARE_MEDIA.SINA);
        weibo.appId = removeBlank(key);
        weibo.appkey = removeBlank(secret);
        weibo.redirectUrl = redirectUrl;
    }

    public static void setVKontakte(String key, String secret) {
        APPIDPlatform vk = (APPIDPlatform) configs.get(SHARE_MEDIA.VKONTAKTE);
        vk.appId = key;
        vk.appkey = secret;
    }

    public static void setWeixin(String id, String secret) {
        APPIDPlatform weixin = (APPIDPlatform) configs.get(SHARE_MEDIA.WEIXIN);
        weixin.appId = id;
        weixin.appkey = secret;
        APPIDPlatform circle = (APPIDPlatform) configs.get(SHARE_MEDIA.WEIXIN_CIRCLE);
        circle.appId = id;
        circle.appkey = secret;
        APPIDPlatform favorite = (APPIDPlatform) configs.get(SHARE_MEDIA.WEIXIN_FAVORITE);
        favorite.appId = id;
        favorite.appkey = secret;
    }

    public static void setLaiwang(String token, String secret) {
        APPIDPlatform laiwang = (APPIDPlatform) configs.get(SHARE_MEDIA.LAIWANG);
        laiwang.appId = token;
        laiwang.appkey = secret;
        APPIDPlatform dynamic = (APPIDPlatform) configs.get(SHARE_MEDIA.LAIWANG_DYNAMIC);
        dynamic.appId = token;
        dynamic.appkey = secret;
    }

    public static void setYixin(String id) {
        APPIDPlatform yixin = (APPIDPlatform) configs.get(SHARE_MEDIA.YIXIN);
        yixin.appId = id;
        APPIDPlatform circle = (APPIDPlatform) configs.get(SHARE_MEDIA.YIXIN_CIRCLE);
        circle.appId = id;
    }

    public static void setPinterest(String id) {
        APPIDPlatform pinterest = (APPIDPlatform) configs.get(SHARE_MEDIA.PINTEREST);
        pinterest.appId = id;
    }

    public static void setKakao(String id) {
        APPIDPlatform kakao = (APPIDPlatform) configs.get(SHARE_MEDIA.KAKAO);
        kakao.appId = id;
    }

    public static void setYnote(String id) {
        APPIDPlatform ynote = (APPIDPlatform) configs.get(SHARE_MEDIA.YNOTE);
        ynote.appId = id;
    }

    public static Platform getPlatform(SHARE_MEDIA name) {
        return (Platform) configs.get(name);
    }


    public static class CustomPlatform implements Platform {
        public static final String Name = "wxUrl+";
        public String appId = null;
        public String appkey = null;
        private SHARE_MEDIA p;

        public CustomPlatform(SHARE_MEDIA p) {
            this.p = p;
        }

        public SHARE_MEDIA getName() {
            return this.p;
        }


        public void parse(JSONObject json) {
        }


        public boolean isConfigured() {
            return true;
        }
    }

    public static class APPIDPlatform implements Platform {
        public String appId = null;
        public String appkey = null;
        public String redirectUrl = null;
        private SHARE_MEDIA share_media;


        public APPIDPlatform(SHARE_MEDIA p) {
            this.share_media = p;
        }

        public SHARE_MEDIA getName() {
            return this.share_media;
        }


        public void parse(JSONObject json) {
        }


        public boolean isConfigured() {
            return (!TextUtils.isEmpty(this.appId)) && (!TextUtils.isEmpty(this.appkey));
        }
    }


    public static String removeBlank(String str) {
        if (!TextUtils.isEmpty(str)) {
            return str.replace(" ", "");
        }
        return str;
    }

    public static abstract interface Platform {
        public abstract SHARE_MEDIA getName();

        public abstract void parse(JSONObject paramJSONObject);

        public abstract boolean isConfigured();
    }
}
