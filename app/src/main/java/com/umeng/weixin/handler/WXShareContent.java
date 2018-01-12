package com.umeng.weixin.handler;

import android.os.Bundle;
import android.text.TextUtils;

import com.umeng.socialize.ShareContent;
import com.umeng.socialize.media.SimpleShareContent;
import com.umeng.socialize.media.UMEmoji;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMMin;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.media.UMusic;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.UmengText;

import java.io.File;

public class WXShareContent extends SimpleShareContent {

    public WXShareContent(ShareContent paramShareContent) {
        super(paramShareContent);
    }

    //根据不同的分享类型去进行选择
    public Bundle getWxShareBundle() {
        Bundle localBundle;
        //分享类型是图片
        if ((getmStyle() == 2) || (getmStyle() == 3)) {
            localBundle = f();
        } else if (getmStyle() == 16) {
            localBundle = g();
        } else if (getmStyle() == 4) {
            localBundle = d();
        } else if (getmStyle() == 8) {
            localBundle = e();
        } else if (getmStyle() == 64) {
            localBundle = c();
        } else if (getmStyle() == 128) {
            localBundle = h();
        } else {
            localBundle = b();
        }
        localBundle.putString("_wxobject_message_action", null);
        localBundle.putString("_wxobject_message_ext", null);
        localBundle.putString("_wxobject_mediatagname", null);
        return localBundle;
    }

    private Bundle b() {
        Bundle localBundle = new Bundle();
        localBundle.putInt("_wxobject_sdkVer", 0);
        localBundle.putInt("_wxapi_sendmessagetowx_req_media_type", 1);
        localBundle.putString("_wxobject_description", objectSetText(getText()));
        localBundle.putByteArray("_wxobject_thumbdata", null);
        localBundle.putInt("_wxapi_command_type", 2);
        localBundle.putString("_wxobject_title", null);
        localBundle.putString("_wxapi_basereq_openid", null);
        localBundle.putString("_wxtextobject_text", getText());
        localBundle.putString("_wxobject_identifier_", "com.tencent.mm.sdk.openapi.WXTextObject");
        if (TextUtils.isEmpty(getText())) {
            localBundle.putString("error", UmengText.EMPTY_TEXT);
        }
        if (getText().length() > 10240) {
            localBundle.putString("error", UmengText.LONG_TEXT);
        }
        return localBundle;
    }

    private Bundle c() {
        UMEmoji localUMEmoji = getUmEmoji();
        String str1 = "";
        if ((localUMEmoji != null) && (localUMEmoji.asFileImage() != null)) {
            str1 = localUMEmoji.asFileImage().toString();
        }
        String str2 = "";
        byte[] arrayOfByte = objectSetThumb(localUMEmoji);
        Bundle localBundle = new Bundle();
        localBundle.putInt("_wxobject_sdkVer", 0);
        localBundle.putInt("_wxapi_sendmessagetowx_req_media_type", 8);
        localBundle.putString("_wxobject_description", getText());
        localBundle.putByteArray("_wxobject_thumbdata", arrayOfByte);
        localBundle.putString("_wxemojiobject_emojiPath", str1);
        localBundle.putInt("_wxapi_command_type", 2);
        localBundle.putString("_wxobject_title", null);
        localBundle.putString("_wxapi_basereq_openid", null);
        localBundle.putString("_wxobject_identifier_", "com.tencent.mm.sdk.openapi.WXEmojiObject");
        if (!TextUtils.isEmpty(str2)) {
            localBundle.putString("error", str2);
        }
        return localBundle;
    }

    private Bundle d() {
        UMusic localUMusic = getMusic();
        String str5 = null;
        String str6 = "";
        String str7 = null;
        String str4;
        if (TextUtils.isEmpty(localUMusic.getmTargetUrl())) {
            str4 = localUMusic.toUrl();
        } else {
            str4 = localUMusic.getmTargetUrl();
        }
        String str3 = localUMusic.toUrl();
        if (!TextUtils.isEmpty(localUMusic.getLowBandDataUrl())) {
            str7 = localUMusic.getLowBandDataUrl();
        }
        if (!TextUtils.isEmpty(localUMusic.getLowBandUrl())) {
            str5 = localUMusic.getLowBandUrl();
        }
        String str2 = objectSetTitle(localUMusic);
        String str1 = objectSetDescription(localUMusic);
        byte[] arrayOfByte = null;
        arrayOfByte = objectSetThumb(localUMusic);
        if ((arrayOfByte == null) || (arrayOfByte.length <= 0)) {
            str6 = UmengText.SHARECONTENT_THUMB_ERROR;
        }
        Bundle localBundle = new Bundle();
        localBundle.putInt("_wxobject_sdkVer", 0);
        localBundle.putInt("_wxapi_sendmessagetowx_req_media_type", 3);
        localBundle.putString("_wxobject_description", str1);
        localBundle.putByteArray("_wxobject_thumbdata", arrayOfByte);
        localBundle.putInt("_wxapi_command_type", 2);
        localBundle.putString("_wxobject_title", str2);
        localBundle.putString("_wxmusicobject_musicUrl", str4);
        localBundle.putString("_wxmusicobject_musicLowBandUrl", str5);
        localBundle.putString("_wxmusicobject_musicDataUrl", str3);
        localBundle.putString("_wxmusicobject_musicLowBandDataUrl", str7);
        localBundle.putString("_wxapi_basereq_openid", null);
        localBundle.putString("_wxtextobject_text", str1);
        localBundle.putString("_wxobject_identifier_", "com.tencent.mm.sdk.openapi.WXMusicObject");
        if (!TextUtils.isEmpty(str6)) {
            localBundle.putString("error", str6);
        }
        return localBundle;
    }

    private Bundle e() {
        UMVideo localUMVideo = getVideo();
        String str3 = "";
        String str5 = null;
        String str4 = localUMVideo.toUrl();
        if (!TextUtils.isEmpty(localUMVideo.getLowBandUrl())) {
            str5 = localUMVideo.getLowBandUrl();
        }
        String str2 = objectSetTitle(localUMVideo);
        String str1 = objectSetDescription(localUMVideo);
        byte[] arrayOfByte = null;
        arrayOfByte = objectSetThumb(localUMVideo);
        if ((arrayOfByte == null) || (arrayOfByte.length <= 0)) {
            str3 = UmengText.SHARECONTENT_THUMB_ERROR;
        }
        Bundle localBundle = new Bundle();
        localBundle.putInt("_wxobject_sdkVer", 0);
        localBundle.putInt("_wxapi_sendmessagetowx_req_media_type", 4);
        localBundle.putString("_wxobject_description", str1);
        localBundle.putByteArray("_wxobject_thumbdata", arrayOfByte);
        localBundle.putInt("_wxapi_command_type", 2);
        localBundle.putString("_wxobject_title", str2);
        localBundle.putString("_wxvideoobject_videoUrl", str4);
        localBundle.putString("_wxvideoobject_videoLowBandUrl", str5);
        localBundle.putString("_wxapi_basereq_openid", null);
        localBundle.putString("_wxtextobject_text", str1);
        localBundle.putString("_wxobject_identifier_", "com.tencent.mm.sdk.openapi.WXVideoObject");
        if (!TextUtils.isEmpty(str3)) {
            localBundle.putString("error", str3);
        }
        return localBundle;
    }

    private Bundle f() {
        String str1 = "";
        UMImage localUMImage = getImage();
        byte[] arrayOfByte1 = localUMImage.asBinImage();
        String str2 = "";
        if (canFileValid(localUMImage)) {
            str2 = localUMImage.asFileImage().toString();
        } else {
            arrayOfByte1 = getStrictImageData(localUMImage);
        }
        byte[] arrayOfByte2 = getImageThumb(localUMImage);
        Bundle localBundle = new Bundle();
        localBundle.putInt("_wxobject_sdkVer", 0);
        localBundle.putInt("_wxapi_sendmessagetowx_req_media_type", 2);
        localBundle.putString("_wxobject_description", getText());
        localBundle.putByteArray("_wxobject_thumbdata", arrayOfByte2);
        if (!TextUtils.isEmpty(str2)) {
            localBundle.putString("_wximageobject_imagePath", str2);
            localBundle.putByteArray("_wximageobject_imageData", null);
        } else {
            localBundle.putByteArray("_wximageobject_imageData", arrayOfByte1);
            localBundle.putString("_wximageobject_imagePath", str2);
        }
        localBundle.putInt("_wxapi_command_type", 2);
        localBundle.putString("_wxobject_title", null);
        localBundle.putString("_wxapi_basereq_openid", null);
        localBundle.putString("_wxobject_identifier_", "com.tencent.mm.sdk.openapi.WXImageObject");
        if (!TextUtils.isEmpty(str1)) {
            localBundle.putString("error", str1);
        }
        return localBundle;
    }

    private Bundle g() {
        String str1 = "";
        UMWeb localUMWeb = getUmWeb();
        String str2 = objectSetTitle(localUMWeb);
        byte[] arrayOfByte = null;
        arrayOfByte = objectSetThumb(localUMWeb);
        if ((arrayOfByte == null) || (arrayOfByte.length <= 0)) {
            Log.um(UmengText.SHARECONTENT_THUMB_ERROR);
        }
        Bundle localBundle = new Bundle();
        localBundle.putInt("_wxobject_sdkVer", 0);
        localBundle.putInt("_wxapi_sendmessagetowx_req_media_type", 5);
        localBundle.putString("_wxobject_description", objectSetDescription(localUMWeb));
        localBundle.putByteArray("_wxobject_thumbdata", arrayOfByte);
        localBundle.putInt("_wxapi_command_type", 2);
        localBundle.putString("_wxobject_title", str2);
        localBundle.putString("_wxwebpageobject_webpageUrl", localUMWeb.toUrl());
        localBundle.putString("_wxapi_basereq_openid", null);
        localBundle.putString("_wxtextobject_text", objectSetDescription(localUMWeb));
        localBundle.putString("_wxobject_description", objectSetDescription(localUMWeb));
        localBundle.putString("_wxobject_identifier_", "com.tencent.mm.sdk.openapi.WXWebpageObject");
        localBundle.putString("_wxwebpageobject_extInfo", null);
        localBundle.putString("_wxwebpageobject_canvaspagexml", null);
        if (TextUtils.isEmpty(localUMWeb.toUrl())) {
            localBundle.putString("error", UmengText.EMPTY_WEB_URL);
        }
        if (localUMWeb.toUrl().length() > 10240) {
            localBundle.putString("error", UmengText.LONG_URL);
        }
        if (!TextUtils.isEmpty(str1)) {
            localBundle.putString("error", str1);
        }
        return localBundle;
    }

    private Bundle h() {
        String str1 = "";
        UMMin localUMMin = getUmMin();
        String str2 = objectSetTitle(localUMMin);
        byte[] arrayOfByte = null;
        arrayOfByte = objectSetThumbMinApp(localUMMin);
        if ((arrayOfByte == null) || (arrayOfByte.length <= 0)) {
            Log.um(UmengText.SHARECONTENT_THUMB_ERROR);
        }
        Bundle localBundle = new Bundle();
        localBundle.putInt("_wxobject_sdkVer", 0);
        localBundle.putInt("_wxapi_sendmessagetowx_req_media_type", 36);
        String str3 = localUMMin.getPath();
        if (!TextUtils.isEmpty(str3)) {
            String[] arrayOfString;
            if ((arrayOfString = str3.split("\\?")).length > 1) {
                str3 = arrayOfString[0] + ".html?" + arrayOfString[1];
            } else {
                str3 = arrayOfString[0] + ".html";
            }
            localBundle.putString("_wxminiprogram_path", str3);
        }
        localBundle.putString("_wxobject_description", objectSetDescription(localUMMin));
        localBundle.putByteArray("_wxobject_thumbdata", arrayOfByte);
        localBundle.putInt("_wxapi_command_type", 2);
        localBundle.putString("_wxminiprogram_username", localUMMin.getUserName() + "@app");
        localBundle.putString("_wxobject_title", str2);
        localBundle.putString("_wxminiprogram_webpageurl", localUMMin.toUrl());
        localBundle.putString("_wxapi_basereq_openid", null);
        localBundle.putString("_wxobject_identifier_", "com.tencent.mm.sdk.openapi.WXMiniProgramObject");
        if (TextUtils.isEmpty(localUMMin.toUrl())) {
            localBundle.putString("error", UmengText.EMPTY_WEB_URL);
        }
        if (localUMMin.toUrl().length() > 10240) {
            localBundle.putString("error", UmengText.LONG_URL);
        }
        if (!TextUtils.isEmpty(str1)) {
            localBundle.putString("error", str1);
        }
        if (TextUtils.isEmpty(localUMMin.getPath())) {
            localBundle.putString("error", "UMMin path is null");
        }
        if (TextUtils.isEmpty(localUMMin.toUrl())) {
            localBundle.putString("error", "UMMin url is null");
        }
        return localBundle;
    }
}