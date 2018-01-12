//
// Source code recreated from BitmapUtil .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.umeng.socialize.bean;

import android.text.TextUtils;

import com.umeng.socialize.Config;
import com.umeng.socialize.shareboard.SnsPlatform;

public enum SHARE_MEDIA {
    GOOGLEPLUS,
    GENERIC,
    SMS,
    EMAIL,
    SINA,
    QZONE,
    QQ,
    RENREN,
    WEIXIN,
    WEIXIN_CIRCLE,
    WEIXIN_FAVORITE,
    TENCENT,
    DOUBAN,
    FACEBOOK,
    FACEBOOK_MESSAGER,
    TWITTER,
    LAIWANG,
    LAIWANG_DYNAMIC,
    YIXIN,
    YIXIN_CIRCLE,
    INSTAGRAM,
    PINTEREST,
    EVERNOTE,
    POCKET,
    LINKEDIN,
    FOURSQUARE,
    YNOTE,
    WHATSAPP,
    LINE,
    FLICKR,
    TUMBLR,
    ALIPAY,
    KAKAO,
    DROPBOX,
    VKONTAKTE,
    DINGTALK,
    MORE;

    public static SHARE_MEDIA convertToEmun(String var0) {
        if (TextUtils.isEmpty(var0)) {
            return null;
        } else if (var0.equals("wxtimeline")) {
            return WEIXIN_CIRCLE;
        } else if (var0.equals("wxsession")) {
            return WEIXIN;
        } else {
            SHARE_MEDIA[] var1 = values();
            SHARE_MEDIA[] var2 = var1;
            int var3 = var1.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                SHARE_MEDIA var5 = var2[var4];
                if (var5.toString().trim().equals(var0)) {
                    return var5;
                }
            }

            return null;
        }
    }

    public static SnsPlatform createSnsPlatform(String var0, String var1, String var2, String var3, int var4) {
        SnsPlatform var5 = new SnsPlatform();
        var5.mShowWord = var0;
        var5.mIcon = var2;
        var5.mGrayIcon = var3;
        var5.mIndex = var4;
        var5.mKeyword = var1;
        return var5;
    }

    public SnsPlatform toSnsPlatform() {
        SnsPlatform var1 = new SnsPlatform();
        if (this.toString().equals("QQ")) {
            var1.mShowWord = "umeng_socialize_text_qq_key";
            var1.mIcon = "umeng_socialize_qq";
            var1.mGrayIcon = "umeng_socialize_qq";
            var1.mIndex = 0;
            var1.mKeyword = "qq";
        } else if (this.toString().equals("SMS")) {
            var1.mShowWord = "umeng_socialize_sms";
            var1.mIcon = "umeng_socialize_sms";
            var1.mGrayIcon = "umeng_socialize_sms";
            var1.mIndex = 1;
            var1.mKeyword = "sms";
        } else if (this.toString().equals("GOOGLEPLUS")) {
            var1.mShowWord = "umeng_socialize_text_googleplus_key";
            var1.mIcon = "umeng_socialize_google";
            var1.mGrayIcon = "umeng_socialize_google";
            var1.mIndex = 0;
            var1.mKeyword = "gooleplus";
        } else if (!this.toString().equals("GENERIC")) {
            if (this.toString().equals("EMAIL")) {
                var1.mShowWord = "umeng_socialize_mail";
                var1.mIcon = "umeng_socialize_gmail";
                var1.mGrayIcon = "umeng_socialize_gmail";
                var1.mIndex = 2;
                var1.mKeyword = "email";
            } else if (this.toString().equals("SINA")) {
                var1.mShowWord = "umeng_socialize_sina";
                var1.mIcon = "umeng_socialize_sina";
                var1.mGrayIcon = "umeng_socialize_sina";
                var1.mIndex = 0;
                var1.mKeyword = "sina";
            } else if (this.toString().equals("QZONE")) {
                var1.mShowWord = "umeng_socialize_text_qq_zone_key";
                var1.mIcon = "umeng_socialize_qzone";
                var1.mGrayIcon = "umeng_socialize_qzone";
                var1.mIndex = 0;
                var1.mKeyword = "qzone";
            } else if (this.toString().equals("RENREN")) {
                var1.mShowWord = "umeng_socialize_text_renren_key";
                var1.mIcon = "umeng_socialize_renren";
                var1.mGrayIcon = "umeng_socialize_renren";
                var1.mIndex = 0;
                var1.mKeyword = "renren";
            } else if (this.toString().equals("WEIXIN")) {
                var1.mShowWord = "umeng_socialize_text_weixin_key";
                var1.mIcon = "umeng_socialize_wechat";
                var1.mGrayIcon = "umeng_socialize_weichat";
                var1.mIndex = 0;
                var1.mKeyword = "wechat";
            } else if (this.toString().equals("WEIXIN_CIRCLE")) {
                var1.mShowWord = "umeng_socialize_text_weixin_circle_key";
                var1.mIcon = "umeng_socialize_wxcircle";
                var1.mGrayIcon = "umeng_socialize_wxcircle";
                var1.mIndex = 0;
                var1.mKeyword = "wxcircle";
            } else if (this.toString().equals("WEIXIN_FAVORITE")) {
                var1.mShowWord = "umeng_socialize_text_weixin_fav_key";
                var1.mIcon = "umeng_socialize_fav";
                var1.mGrayIcon = "umeng_socialize_fav";
                var1.mIndex = 0;
                var1.mKeyword = "wechatfavorite";
            } else if (this.toString().equals("TENCENT")) {
                var1.mShowWord = "umeng_socialize_text_tencent_key";
                var1.mIcon = "umeng_socialize_tx";
                var1.mGrayIcon = "umeng_socialize_tx";
                var1.mIndex = 0;
                var1.mKeyword = "tencent";
            } else if (this.toString().equals("FACEBOOK")) {
                var1.mShowWord = "umeng_socialize_text_facebook_key";
                var1.mIcon = "umeng_socialize_facebook";
                var1.mGrayIcon = "umeng_socialize_facebook";
                var1.mIndex = 0;
                var1.mKeyword = "facebook";
            } else if (this.toString().equals("FACEBOOK_MESSAGER")) {
                var1.mShowWord = "umeng_socialize_text_facebookmessager_key";
                var1.mIcon = "umeng_socialize_fbmessage";
                var1.mGrayIcon = "umeng_socialize_fbmessage";
                var1.mIndex = 0;
                var1.mKeyword = "facebook_messager";
            } else if (this.toString().equals("YIXIN")) {
                var1.mShowWord = "umeng_socialize_text_yixin_key";
                var1.mIcon = "umeng_socialize_yixin";
                var1.mGrayIcon = "umeng_socialize_yixin";
                var1.mIndex = 0;
                var1.mKeyword = "yinxin";
            } else if (this.toString().equals("TWITTER")) {
                var1.mShowWord = "umeng_socialize_text_twitter_key";
                var1.mIcon = "umeng_socialize_twitter";
                var1.mGrayIcon = "umeng_socialize_twitter";
                var1.mIndex = 0;
                var1.mKeyword = "twitter";
            } else if (this.toString().equals("LAIWANG")) {
                var1.mShowWord = "umeng_socialize_text_laiwang_key";
                var1.mIcon = "umeng_socialize_laiwang";
                var1.mGrayIcon = "umeng_socialize_laiwang";
                var1.mIndex = 0;
                var1.mKeyword = "laiwang";
            } else if (this.toString().equals("LAIWANG_DYNAMIC")) {
                var1.mShowWord = "umeng_socialize_text_laiwangdynamic_key";
                var1.mIcon = "umeng_socialize_laiwang_dynamic";
                var1.mGrayIcon = "umeng_socialize_laiwang_dynamic";
                var1.mIndex = 0;
                var1.mKeyword = "laiwang_dynamic";
            } else if (this.toString().equals("INSTAGRAM")) {
                var1.mShowWord = "umeng_socialize_text_instagram_key";
                var1.mIcon = "umeng_socialize_instagram";
                var1.mGrayIcon = "umeng_socialize_instagram";
                var1.mIndex = 0;
                var1.mKeyword = "instagram";
            } else if (this.toString().equals("YIXIN_CIRCLE")) {
                var1.mShowWord = "umeng_socialize_text_yixincircle_key";
                var1.mIcon = "umeng_socialize_yixin_circle";
                var1.mGrayIcon = "umeng_socialize_yixin_circle";
                var1.mIndex = 0;
                var1.mKeyword = "yinxincircle";
            } else if (this.toString().equals("PINTEREST")) {
                var1.mShowWord = "umeng_socialize_text_pinterest_key";
                var1.mIcon = "umeng_socialize_pinterest";
                var1.mGrayIcon = "umeng_socialize_pinterest";
                var1.mIndex = 0;
                var1.mKeyword = "pinterest";
            } else if (this.toString().equals("EVERNOTE")) {
                var1.mShowWord = "umeng_socialize_text_evernote_key";
                var1.mIcon = "umeng_socialize_evernote";
                var1.mGrayIcon = "umeng_socialize_evernote";
                var1.mIndex = 0;
                var1.mKeyword = "evernote";
            } else if (this.toString().equals("POCKET")) {
                var1.mShowWord = "umeng_socialize_text_pocket_key";
                var1.mIcon = "umeng_socialize_pocket";
                var1.mGrayIcon = "umeng_socialize_pocket";
                var1.mIndex = 0;
                var1.mKeyword = "pocket";
            } else if (this.toString().equals("LINKEDIN")) {
                var1.mShowWord = "umeng_socialize_text_linkedin_key";
                var1.mIcon = "umeng_socialize_linkedin";
                var1.mGrayIcon = "umeng_socialize_linkedin";
                var1.mIndex = 0;
                var1.mKeyword = "linkedin";
            } else if (this.toString().equals("FOURSQUARE")) {
                var1.mShowWord = "umeng_socialize_text_foursquare_key";
                var1.mIcon = "umeng_socialize_foursquare";
                var1.mGrayIcon = "umeng_socialize_foursquare";
                var1.mIndex = 0;
                var1.mKeyword = "foursquare";
            } else if (this.toString().equals("YNOTE")) {
                var1.mShowWord = "umeng_socialize_text_ydnote_key";
                var1.mIcon = "umeng_socialize_ynote";
                var1.mGrayIcon = "umeng_socialize_ynote";
                var1.mIndex = 0;
                var1.mKeyword = "ynote";
            } else if (this.toString().equals("WHATSAPP")) {
                var1.mShowWord = "umeng_socialize_text_whatsapp_key";
                var1.mIcon = "umeng_socialize_whatsapp";
                var1.mGrayIcon = "umeng_socialize_whatsapp";
                var1.mIndex = 0;
                var1.mKeyword = "whatsapp";
            } else if (this.toString().equals("LINE")) {
                var1.mShowWord = "umeng_socialize_text_line_key";
                var1.mIcon = "umeng_socialize_line";
                var1.mGrayIcon = "umeng_socialize_line";
                var1.mIndex = 0;
                var1.mKeyword = "line";
            } else if (this.toString().equals("FLICKR")) {
                var1.mShowWord = "umeng_socialize_text_flickr_key";
                var1.mIcon = "umeng_socialize_flickr";
                var1.mGrayIcon = "umeng_socialize_flickr";
                var1.mIndex = 0;
                var1.mKeyword = "flickr";
            } else if (this.toString().equals("TUMBLR")) {
                var1.mShowWord = "umeng_socialize_text_tumblr_key";
                var1.mIcon = "umeng_socialize_tumblr";
                var1.mGrayIcon = "umeng_socialize_tumblr";
                var1.mIndex = 0;
                var1.mKeyword = "tumblr";
            } else if (this.toString().equals("KAKAO")) {
                var1.mShowWord = "umeng_socialize_text_kakao_key";
                var1.mIcon = "umeng_socialize_kakao";
                var1.mGrayIcon = "umeng_socialize_kakao";
                var1.mIndex = 0;
                var1.mKeyword = "kakao";
            } else if (this.toString().equals("DOUBAN")) {
                var1.mShowWord = "umeng_socialize_text_douban_key";
                var1.mIcon = "umeng_socialize_douban";
                var1.mGrayIcon = "umeng_socialize_douban";
                var1.mIndex = 0;
                var1.mKeyword = "douban";
            } else if (this.toString().equals("ALIPAY")) {
                var1.mShowWord = "umeng_socialize_text_alipay_key";
                var1.mIcon = "umeng_socialize_alipay";
                var1.mGrayIcon = "umeng_socialize_alipay";
                var1.mIndex = 0;
                var1.mKeyword = "alipay";
            } else if (this.toString().equals("MORE")) {
                var1.mShowWord = "umeng_socialize_text_more_key";
                var1.mIcon = "umeng_socialize_more";
                var1.mGrayIcon = "umeng_socialize_more";
                var1.mIndex = 0;
                var1.mKeyword = "more";
            } else if (this.toString().equals("DINGTALK")) {
                var1.mShowWord = "umeng_socialize_text_dingding_key";
                var1.mIcon = "umeng_socialize_ding";
                var1.mGrayIcon = "umeng_socialize_ding";
                var1.mIndex = 0;
                var1.mKeyword = "ding";
            } else if (this.toString().equals("VKONTAKTE")) {
                var1.mShowWord = "umeng_socialize_text_vkontakte_key";
                var1.mIcon = "vk_icon";
                var1.mGrayIcon = "vk_icon";
                var1.mIndex = 0;
                var1.mKeyword = "vk";
            } else if (this.toString().equals("DROPBOX")) {
                var1.mShowWord = "umeng_socialize_text_dropbox_key";
                var1.mIcon = "umeng_socialize_dropbox";
                var1.mGrayIcon = "umeng_socialize_dropbox";
                var1.mIndex = 0;
                var1.mKeyword = "dropbox";
            }
        }

        var1.mPlatform = this;
        return var1;
    }

    private SHARE_MEDIA() {
    }

    public String getauthstyle(boolean var1) {
        return this.toString().equals("QQ") ? "sso" : (this.toString().equals("SINA") ? (var1 ? "sso" : (Config.isUmengSina.booleanValue() ? "cloudy self" : "cloudy third")) : (this.toString().equals("QZONE") ? "sso" : (this.toString().equals("RENREN") ? "cloudy self" : (this.toString().equals("WEIXIN") ? "sso" : (this.toString().equals("TENCENT") ? "cloudy self" : (this.toString().equals("FACEBOOK") ? "sso" : (this.toString().equals("YIXIN") ? "sso" : (this.toString().equals("TWITTER") ? "sso" : (this.toString().equals("LAIWANG") ? "sso" : (this.toString().equals("LINE") ? "sso" : (this.toString().equals("DOUBAN") ? "cloudy self" : null)))))))))));
    }

    public String getsharestyle(boolean var1) {
        return this.toString().equals("QQ") ? "sso" : (this.toString().equals("SINA") ? (Config.isUmengSina.booleanValue() ? "cloudy self" : (var1 ? "sso" : "cloudy third")) : (this.toString().equals("RENREN") ? "cloudy self" : (this.toString().equals("DOUBAN") ? "cloudy self" : (this.toString().equals("TENCENT") ? "cloudy self" : "sso"))));
    }

    public String toString() {
        return super.toString();
    }
}
