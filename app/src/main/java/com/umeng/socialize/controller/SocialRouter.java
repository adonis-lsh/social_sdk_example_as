//
// Source code recreated from BitmapUtil .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.umeng.socialize.controller;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.util.SparseArray;

import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.ShareContent;
import com.umeng.socialize.SocializeException;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.PlatformConfig.Platform;
import com.umeng.socialize.bean.HandlerRequestCode;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.UmengErrorCode;
import com.umeng.socialize.common.QueuedWork;
import com.umeng.socialize.handler.UMMoreHandler;
import com.umeng.socialize.handler.UMSSOHandler;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.media.UMusic;
import com.umeng.socialize.net.analytics.SocialAnalytics;
import com.umeng.socialize.net.utils.SocializeNetUtils;
import com.umeng.socialize.utils.ContextUtil;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.SocializeUtils;
import com.umeng.socialize.utils.UmengText;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class SocialRouter {
    private SHARE_MEDIA mAuthPlatform;
    private static final String BUNDLE_KEY_AUTH_PLATFORM = "umeng_share_platform";
    private static final String BUNDLE_KEY_ACTION = "share_action";
    private String APIVERSION = "6.4.6";
    private final Map<SHARE_MEDIA, UMSSOHandler> platformHandlers = new HashMap();
    private final List<Pair<SHARE_MEDIA, String>> supportedPlatform = new ArrayList();
    private SocialRouter.ParamsGuard guard;
    private Context mContext;
    private SparseArray<UMAuthListener> mAuthListenerContainer;
    private SparseArray<UMShareListener> mShareListenerContainer;
    private SparseArray<UMAuthListener> mFetchUserInfoListenerContainer;

    public void setmContext(Context mContext) {
        this.mContext = mContext.getApplicationContext();
    }

    public SocialRouter(Context context) {
        List<Pair<SHARE_MEDIA, String>> platforms = this.supportedPlatform;
        String pkg = "com.umeng.socialize.handler.";
        String pkgwx = "com.umeng.weixin.handler.";
        String pkgqq = "com.umeng.qq.handler.";
        platforms.add(new Pair(SHARE_MEDIA.LAIWANG, "com.umeng.socialize.handler.UMLWHandler"));
        platforms.add(new Pair(SHARE_MEDIA.LAIWANG_DYNAMIC, "com.umeng.socialize.handler.UMLWHandler"));
        platforms.add(new Pair(SHARE_MEDIA.SINA, "com.umeng.socialize.handler.SinaSimplyHandler"));
        platforms.add(new Pair(SHARE_MEDIA.PINTEREST, "com.umeng.socialize.handler.UMPinterestHandler"));
        platforms.add(new Pair(SHARE_MEDIA.QZONE, "com.umeng.qq.handler.UmengQZoneHandler"));
        platforms.add(new Pair(SHARE_MEDIA.QQ, "com.umeng.qq.handler.UmengQQHandler"));
        platforms.add(new Pair(SHARE_MEDIA.RENREN, "com.umeng.socialize.handler.RenrenSsoHandler"));
        platforms.add(new Pair(SHARE_MEDIA.TENCENT, "com.umeng.socialize.handler.TencentWBSsoHandler"));
        platforms.add(new Pair(SHARE_MEDIA.WEIXIN, "com.umeng.weixin.handler.UmengWXHandler"));
        platforms.add(new Pair(SHARE_MEDIA.WEIXIN_CIRCLE, "com.umeng.weixin.handler.UmengWXHandler"));
        platforms.add(new Pair(SHARE_MEDIA.WEIXIN_FAVORITE, "com.umeng.weixin.handler.UmengWXHandler"));
        platforms.add(new Pair(SHARE_MEDIA.YIXIN, "com.umeng.socialize.handler.UMYXHandler"));
        platforms.add(new Pair(SHARE_MEDIA.YIXIN_CIRCLE, "com.umeng.socialize.handler.UMYXHandler"));
        platforms.add(new Pair(SHARE_MEDIA.EMAIL, "com.umeng.socialize.handler.EmailHandler"));
        platforms.add(new Pair(SHARE_MEDIA.EVERNOTE, "com.umeng.socialize.handler.UMEvernoteHandler"));
        platforms.add(new Pair(SHARE_MEDIA.FACEBOOK, "com.umeng.socialize.handler.UMFacebookHandler"));
        platforms.add(new Pair(SHARE_MEDIA.FACEBOOK_MESSAGER, "com.umeng.socialize.handler.UMFacebookHandler"));
        platforms.add(new Pair(SHARE_MEDIA.FLICKR, "com.umeng.socialize.handler.UMFlickrHandler"));
        platforms.add(new Pair(SHARE_MEDIA.FOURSQUARE, "com.umeng.socialize.handler.UMFourSquareHandler"));
        platforms.add(new Pair(SHARE_MEDIA.GOOGLEPLUS, "com.umeng.socialize.handler.UMGooglePlusHandler"));
        platforms.add(new Pair(SHARE_MEDIA.INSTAGRAM, "com.umeng.socialize.handler.UMInstagramHandler"));
        platforms.add(new Pair(SHARE_MEDIA.KAKAO, "com.umeng.socialize.handler.UMKakaoHandler"));
        platforms.add(new Pair(SHARE_MEDIA.LINE, "com.umeng.socialize.handler.UMLineHandler"));
        platforms.add(new Pair(SHARE_MEDIA.LINKEDIN, "com.umeng.socialize.handler.UMLinkedInHandler"));
        platforms.add(new Pair(SHARE_MEDIA.POCKET, "com.umeng.socialize.handler.UMPocketHandler"));
        platforms.add(new Pair(SHARE_MEDIA.WHATSAPP, "com.umeng.socialize.handler.UMWhatsAppHandler"));
        platforms.add(new Pair(SHARE_MEDIA.YNOTE, "com.umeng.socialize.handler.UMYNoteHandler"));
        platforms.add(new Pair(SHARE_MEDIA.SMS, "com.umeng.socialize.handler.SmsHandler"));
        platforms.add(new Pair(SHARE_MEDIA.DOUBAN, "com.umeng.socialize.handler.DoubanHandler"));
        platforms.add(new Pair(SHARE_MEDIA.TUMBLR, "com.umeng.socialize.handler.UMTumblrHandler"));
        platforms.add(new Pair(SHARE_MEDIA.TWITTER, "com.umeng.socialize.handler.TwitterHandler"));
        platforms.add(new Pair(SHARE_MEDIA.ALIPAY, "com.umeng.socialize.handler.AlipayHandler"));
        platforms.add(new Pair(SHARE_MEDIA.MORE, "com.umeng.socialize.handler.UMMoreHandler"));
        platforms.add(new Pair(SHARE_MEDIA.DINGTALK, "com.umeng.socialize.handler.UMDingSSoHandler"));
        platforms.add(new Pair(SHARE_MEDIA.VKONTAKTE, "com.umeng.socialize.handler.UMVKHandler"));
        platforms.add(new Pair(SHARE_MEDIA.DROPBOX, "com.umeng.socialize.handler.UMDropBoxHandler"));
        this.guard = new SocialRouter.ParamsGuard(this.platformHandlers);
        this.mContext = null;
        this.mAuthListenerContainer = new SparseArray();
        this.mShareListenerContainer = new SparseArray();
        this.mFetchUserInfoListenerContainer = new SparseArray();
        this.mContext = context;
        this.init();
    }

    private void checkAppkey(Context context) {
        String appkey = SocializeUtils.getAppkey(context);
        if (TextUtils.isEmpty(appkey)) {
            throw new SocializeException(UmengText.errorWithUrl(UmengText.APPKEY_NOT_FOUND, "https://at.umeng.com/bObWzC?cid=476"));
        } else if (SocializeNetUtils.isConSpeCharacters(appkey)) {
            throw new SocializeException(UmengText.errorWithUrl(UmengText.APPKEY_NOT_FOUND, "https://at.umeng.com/ya4Dmy?cid=476"));
        } else if (SocializeNetUtils.isSelfAppkey(appkey)) {
            throw new SocializeException(UmengText.errorWithUrl(UmengText.APPKEY_NOT_FOUND, "https://at.umeng.com/ya4Dmy?cid=476"));
        }
    }

    private void init() {
        Pair<SHARE_MEDIA, Object> pair;
        UMSSOHandler umssoHandler;
        for (Iterator var1 = this.supportedPlatform.iterator(); var1.hasNext(); this.platformHandlers.put(pair.first, umssoHandler)) {
            pair = (Pair) var1.next();
            if (pair.first != SHARE_MEDIA.WEIXIN_CIRCLE && pair.first != SHARE_MEDIA.WEIXIN_FAVORITE) {
                if (pair.first == SHARE_MEDIA.FACEBOOK_MESSAGER) {
                    umssoHandler = (UMSSOHandler) this.platformHandlers.get(SHARE_MEDIA.FACEBOOK);
                } else if (pair.first == SHARE_MEDIA.YIXIN_CIRCLE) {
                    umssoHandler = (UMSSOHandler) this.platformHandlers.get(SHARE_MEDIA.YIXIN);
                } else if (pair.first == SHARE_MEDIA.LAIWANG_DYNAMIC) {
                    umssoHandler = (UMSSOHandler) this.platformHandlers.get(SHARE_MEDIA.LAIWANG);
                } else if (pair.first == SHARE_MEDIA.TENCENT) {
                    umssoHandler = this.newHandler((String) pair.second);
                } else if (pair.first == SHARE_MEDIA.MORE) {
                    umssoHandler = new UMMoreHandler();
                } else if (pair.first == SHARE_MEDIA.SINA) {
                    if (Config.isUmengSina.booleanValue()) {
                        umssoHandler = this.newHandler((String) pair.second);
                    } else {
                        umssoHandler = this.newHandler("com.umeng.socialize.handler.SinaSsoHandler");
                    }
                } else if (pair.first == SHARE_MEDIA.WEIXIN) {
                    if (Config.isUmengWx.booleanValue()) {
                        umssoHandler = this.newHandler((String) pair.second);
                    } else {
                        umssoHandler = this.newHandler("com.umeng.socialize.handler.UMWXHandler");
                    }
                } else if (pair.first == SHARE_MEDIA.QQ) {
                    if (Config.isUmengQQ.booleanValue()) {
                        umssoHandler = this.newHandler((String) pair.second);
                    } else {
                        umssoHandler = this.newHandler("com.umeng.socialize.handler.UMQQSsoHandler");
                    }
                } else if (pair.first == SHARE_MEDIA.QZONE) {
                    if (Config.isUmengQQ.booleanValue()) {
                        umssoHandler = this.newHandler((String) pair.second);
                    } else {
                        umssoHandler = this.newHandler("com.umeng.socialize.handler.QZoneSsoHandler");
                    }
                } else {
                    umssoHandler = this.newHandler((String) pair.second);
                }
            } else {
                umssoHandler = (UMSSOHandler) this.platformHandlers.get(SHARE_MEDIA.WEIXIN);
            }
        }

    }

    private UMSSOHandler newHandler(String classpath) {
        UMSSOHandler handler = null;

        try {
            Class<UMSSOHandler> pHandler = (Class<UMSSOHandler>) Class.forName(classpath);
            handler = (UMSSOHandler) pHandler.newInstance();
        } catch (Exception var4) {
            ;
        }

        if (handler == null) {
            if (classpath.contains("SinaSimplyHandler")) {
                Config.isUmengSina = Boolean.valueOf(false);
                return this.newHandler("com.umeng.socialize.handler.SinaSsoHandler");
            }

            if (classpath.contains("UmengQQHandler")) {
                Config.isUmengQQ = Boolean.valueOf(false);
                return this.newHandler("com.umeng.socialize.handler.UMQQSsoHandler");
            }

            if (classpath.contains("UmengQZoneHandler")) {
                Config.isUmengQQ = Boolean.valueOf(false);
                return this.newHandler("com.umeng.socialize.handler.QZoneSsoHandler");
            }

            if (classpath.contains("UmengWXHandler")) {
                Config.isUmengWx = Boolean.valueOf(false);
                return this.newHandler("com.umeng.socialize.handler.UMWXHandler");
            }
        }

        return handler;
    }

    public UMSSOHandler getHandler(SHARE_MEDIA name) {
        UMSSOHandler handler = (UMSSOHandler) this.platformHandlers.get(name);
        if (handler != null) {
            Platform p = PlatformConfig.getPlatform(name);
            handler.onCreate(this.mContext, p);
        }

        return handler;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        UMSSOHandler handler = this.getHandler(requestCode);
        if (handler != null) {
            handler.onActivityResult(requestCode, resultCode, data);
        }

    }


    @Deprecated
    public void onCreate(Activity context, int requestCode, UMAuthListener listener) {
        UMSSOHandler handler = this.getHandler(requestCode);
        if (handler != null && (requestCode == HandlerRequestCode.REQUEST_QQ_SHARE || requestCode == 11101)) {
            handler.onCreate(context, PlatformConfig.getPlatform(this.getShareMediaByrequestCode(requestCode)));
            String tag = String.valueOf(System.currentTimeMillis());
            this.setAuthListener(SHARE_MEDIA.QQ, listener, handler, tag);
        }

    }

    private UMSSOHandler getHandler(int requestCode) {
        int code = requestCode;
        if (requestCode == HandlerRequestCode.REQUEST_QQ_SHARE || requestCode == 11101) {
            code = HandlerRequestCode.REQUEST_QQ_SHARE;
        }

        if (requestCode == '𢡊' || requestCode == '龜' || requestCode == '𢡄') {
            code = '龜';
        }

        if (requestCode == '胍' || requestCode == 765) {
            code = HandlerRequestCode.SINA_REQUEST_CODE;
        }

        if (requestCode == 5650) {
            code = HandlerRequestCode.SINA_REQUEST_CODE;
        }

        Iterator var3 = this.platformHandlers.values().iterator();

        UMSSOHandler handler;
        do {
            if (!var3.hasNext()) {
                return null;
            }

            handler = (UMSSOHandler) var3.next();
        } while (handler == null || code != handler.getRequestCode());

        return handler;
    }

    public SHARE_MEDIA getShareMediaByrequestCode(int requestCode) {
        return requestCode != HandlerRequestCode.REQUEST_QQ_SHARE && requestCode != 11101 ? (requestCode != '胍' && requestCode != 765 ? SHARE_MEDIA.QQ : SHARE_MEDIA.SINA) : SHARE_MEDIA.QQ;
    }

    public void deleteOauth(Activity context, SHARE_MEDIA platform, UMAuthListener listener) {
        if (this.guard.auth(context, platform)) {
            if (listener == null) {
                listener = new UMAuthListener() {
                    public void onStart(SHARE_MEDIA platform) {
                    }

                    public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
                    }

                    public void onError(SHARE_MEDIA platform, int action, Throwable t) {
                    }

                    public void onCancel(SHARE_MEDIA platform, int action) {
                    }
                };
            }

            ((UMSSOHandler) this.platformHandlers.get(platform)).onCreate(context, PlatformConfig.getPlatform(platform));
            ((UMSSOHandler) this.platformHandlers.get(platform)).deleteAuth(listener);
        }
    }

    public void getPlatformInfo(final Activity context, final SHARE_MEDIA platform, final UMAuthListener listener) {
        if (this.guard.auth(context, platform)) {
            UMSSOHandler handler = (UMSSOHandler) this.platformHandlers.get(platform);
            handler.onCreate(context, PlatformConfig.getPlatform(platform));
            String tag = String.valueOf(System.currentTimeMillis());
            final int platformOrdinal = platform.ordinal();
            this.saveFetchUserInfoListener(platformOrdinal, listener);
            UMAuthListener getListener = new UMAuthListener() {
                public void onStart(SHARE_MEDIA platform) {
                    UMAuthListener umAuthListener = SocialRouter.this.getAndRemoveFetchUserInfoListener(platformOrdinal);
                    if (umAuthListener != null) {
                        umAuthListener.onStart(platform);
                    }

                }

                public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
                    UMAuthListener umAuthListener = SocialRouter.this.getAndRemoveFetchUserInfoListener(platformOrdinal);
                    if (umAuthListener != null) {
                        umAuthListener.onComplete(platform, action, data);
                    }

                }

                public void onError(SHARE_MEDIA platform, int action, Throwable t) {
                    UMAuthListener umAuthListener = SocialRouter.this.getAndRemoveFetchUserInfoListener(platformOrdinal);
                    if (umAuthListener != null) {
                        umAuthListener.onError(platform, action, t);
                    }

                    if (t != null) {
                        Log.toast(context, UmengText.AUTH_FAIL_LOG);
                        Log.um(t.getMessage());
                        Log.um(UmengText.SOLVE + "https://at.umeng.com/CuKXbi?cid=476");
                    } else {
                        Log.um("null");
                    }

                }

                public void onCancel(SHARE_MEDIA platform, int action) {
                    UMAuthListener umAuthListener = SocialRouter.this.getAndRemoveFetchUserInfoListener(platformOrdinal);
                    if (umAuthListener != null) {
                        umAuthListener.onCancel(platform, action);
                    }

                }
            };
            QueuedWork.runInMain(new Runnable() {
                public void run() {
                    listener.onStart(platform);
                }
            });
            handler.getPlatformInfo(getListener);
        }
    }

    public boolean isInstall(Activity context, SHARE_MEDIA platform) {
        ((UMSSOHandler) this.platformHandlers.get(platform)).onCreate(context, PlatformConfig.getPlatform(platform));
        return ((UMSSOHandler) this.platformHandlers.get(platform)).isInstall();
    }

    public boolean isSupport(Activity context, SHARE_MEDIA platform) {
        ((UMSSOHandler) this.platformHandlers.get(platform)).onCreate(context, PlatformConfig.getPlatform(platform));
        return ((UMSSOHandler) this.platformHandlers.get(platform)).isSupport();
    }

    public String getSDKVersion(Activity context, SHARE_MEDIA platform) {
        if (!this.guard.auth(context, platform)) {
            return "";
        } else {
            ((UMSSOHandler) this.platformHandlers.get(platform)).onCreate(context, PlatformConfig.getPlatform(platform));
            return ((UMSSOHandler) this.platformHandlers.get(platform)).getSDKVersion();
        }
    }

    public boolean isAuthorize(Activity context, SHARE_MEDIA platform) {
        if (!this.guard.auth(context, platform)) {
            return false;
        } else {
            ((UMSSOHandler) this.platformHandlers.get(platform)).onCreate(context, PlatformConfig.getPlatform(platform));
            return ((UMSSOHandler) this.platformHandlers.get(platform)).isAuthorize();
        }
    }

    public void doOauthVerify(Activity activity, final SHARE_MEDIA platform, final UMAuthListener listener) {
        if (this.guard.auth(activity, platform)) {
            UMSSOHandler handler = (UMSSOHandler) this.platformHandlers.get(platform);
            handler.onCreate(activity, PlatformConfig.getPlatform(platform));
            String tag = String.valueOf(System.currentTimeMillis());
            int platformOrdinal = platform.ordinal();
            this.saveAuthListener(platformOrdinal, listener);
            UMAuthListener authListener = this.getAuthListener(platformOrdinal, tag);
            QueuedWork.runInMain(new Runnable() {
                public void run() {
                    listener.onStart(platform);
                }
            });
            handler.authorize(authListener);
            this.mAuthPlatform = platform;
        }
    }

    private UMAuthListener getAuthListener(final int platformOrdinal, String tag) {
        return new UMAuthListener() {
            public void onStart(SHARE_MEDIA platform) {
                UMAuthListener umAuthListener = SocialRouter.this.getAndRemoveAuthListener(platformOrdinal);
                if (umAuthListener != null) {
                    umAuthListener.onStart(platform);
                }

            }

            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
                UMAuthListener umAuthListener = SocialRouter.this.getAndRemoveAuthListener(platformOrdinal);
                if (umAuthListener != null) {
                    umAuthListener.onComplete(platform, action, data);
                }

            }

            public void onError(SHARE_MEDIA platform, int action, Throwable t) {
                UMAuthListener umAuthListener = SocialRouter.this.getAndRemoveAuthListener(platformOrdinal);
                if (umAuthListener != null) {
                    umAuthListener.onError(platform, action, t);
                }

                if (t != null) {
                    Log.um("error:" + t.getMessage());
                } else {
                    Log.um("error:null");
                }

            }

            public void onCancel(SHARE_MEDIA platform, int action) {
                UMAuthListener umAuthListener = SocialRouter.this.getAndRemoveAuthListener(platformOrdinal);
                if (umAuthListener != null) {
                    umAuthListener.onCancel(platform, action);
                }

            }
        };
    }

    private void parseShareContent(ShareContent shareContent) {
        Log.umd("sharetext=" + shareContent.mText);
        if (shareContent.mMedia != null) {
            if (shareContent.mMedia instanceof UMImage) {
                UMImage image = (UMImage) shareContent.mMedia;
                if (image.isUrlMedia()) {
                    Log.umd("urlimage=" + image.asUrlImage() + " compressStyle=" + image.compressStyle + " isLoadImgByCompress=" + image.isLoadImgByCompress + "  compressFormat=" + image.compressFormat);
                } else {
                    byte[] binImage = image.asBinImage();
                    Log.umd("localimage=" + (binImage == null ? 0 : binImage.length) + " compressStyle=" + image.compressStyle + " isLoadImgByCompress=" + image.isLoadImgByCompress + "  compressFormat=" + image.compressFormat);
                }

                if (image.getThumbImage() != null) {
                    UMImage thumb = image.getThumbImage();
                    if (thumb.isUrlMedia()) {
                        Log.umd("urlthumbimage=" + thumb.asUrlImage());
                    } else {
                        Log.umd("localthumbimage=" + thumb.asBinImage().length);
                    }
                }
            }

            if (shareContent.mMedia instanceof UMVideo) {
                UMVideo video = (UMVideo) shareContent.mMedia;
                Log.umd("video=" + video.toUrl());
                Log.umd("video title=" + video.getTitle());
                Log.umd("video desc=" + video.getDescription());
                if (TextUtils.isEmpty(video.toUrl())) {
                    Log.um(UmengText.urlEmpty(0));
                }

                if (video.getThumbImage() != null) {
                    if (video.getThumbImage().isUrlMedia()) {
                        Log.umd("urlthumbimage=" + video.getThumbImage().asUrlImage());
                    } else {
                        Log.umd("localthumbimage=" + video.getThumbImage().asBinImage());
                    }
                }
            }

            if (shareContent.mMedia instanceof UMusic) {
                UMusic music = (UMusic) shareContent.mMedia;
                Log.umd("music=" + music.toUrl());
                Log.umd("music title=" + music.getTitle());
                Log.umd("music desc=" + music.getDescription());
                Log.umd("music target=" + music.getmTargetUrl());
                if (TextUtils.isEmpty(music.toUrl())) {
                    Log.um(UmengText.urlEmpty(1));
                }

                if (music.getThumbImage() != null) {
                    if (music.getThumbImage().isUrlMedia()) {
                        Log.umd("urlthumbimage=" + music.getThumbImage().asUrlImage());
                    } else {
                        Log.umd("localthumbimage=" + music.getThumbImage().asBinImage());
                    }
                }
            }

            if (shareContent.mMedia instanceof UMWeb) {
                UMWeb web = (UMWeb) shareContent.mMedia;
                Log.umd("web=" + web.toUrl());
                Log.umd("web title=" + web.getTitle());
                Log.umd("web desc=" + web.getDescription());
                if (web.getThumbImage() != null) {
                    if (web.getThumbImage().isUrlMedia()) {
                        Log.umd("urlthumbimage=" + web.getThumbImage().asUrlImage());
                    } else {
                        Log.umd("localthumbimage=" + web.getThumbImage().asBinImage());
                    }
                }

                if (TextUtils.isEmpty(web.toUrl())) {
                    Log.um(UmengText.urlEmpty(2));
                }
            }
        }

        if (shareContent.file != null) {
            Log.umd("file=" + shareContent.file.getName());
        }

    }

    public void share(Activity activity, final ShareAction action, final UMShareListener listener) {
        this.checkAppkey(activity);
        WeakReference<Activity> mWeakAct = new WeakReference(activity);
        if (this.guard.share(action)) {
            if (Config.DEBUG) {
                Log.umd("api version:" + this.APIVERSION);
                Log.umd("sharemedia=" + action.getPlatform().toString());
                Log.umd(UmengText.SHARE_STYLE + action.getShareContent().getShareType());
                this.parseShareContent(action.getShareContent());
            }

            SHARE_MEDIA platform = action.getPlatform();
            UMSSOHandler handler = (UMSSOHandler) this.platformHandlers.get(platform);
            handler.onCreate((Context) mWeakAct.get(), PlatformConfig.getPlatform(platform));
            if (!platform.toString().equals("TENCENT") && !platform.toString().equals("RENREN") && !platform.toString().equals("DOUBAN")) {
                if (platform.toString().equals("WEIXIN")) {
                    SocialAnalytics.log((Context) mWeakAct.get(), "wxsession", action.getShareContent().mText, action.getShareContent().mMedia);
                } else if (platform.toString().equals("WEIXIN_CIRCLE")) {
                    SocialAnalytics.log((Context) mWeakAct.get(), "wxtimeline", action.getShareContent().mText, action.getShareContent().mMedia);
                } else if (platform.toString().equals("WEIXIN_FAVORITE")) {
                    SocialAnalytics.log((Context) mWeakAct.get(), "wxfavorite", action.getShareContent().mText, action.getShareContent().mMedia);
                } else {
                    SocialAnalytics.log((Context) mWeakAct.get(), platform.toString().toLowerCase(), action.getShareContent().mText, action.getShareContent().mMedia);
                }
            }

            String tag = String.valueOf(System.currentTimeMillis());
            if (ContextUtil.getContext() != null) {
                boolean isWaterMask = false;
                if (action.getShareContent().mMedia instanceof UMImage) {
                    UMImage image = (UMImage) action.getShareContent().mMedia;
                    isWaterMask = image.isHasWaterMark();
                }
            }

            final int platformOrdinal = platform.ordinal();
            this.saveShareListener(platformOrdinal, listener);
            final UMShareListener shareListener = new UMShareListener() {
                public void onStart(SHARE_MEDIA platform) {
                    UMShareListener umShareListener = SocialRouter.this.getAndRemoveShareListener(platformOrdinal);
                    if (umShareListener != null) {
                        umShareListener.onStart(platform);
                    }

                }

                public void onResult(SHARE_MEDIA platform) {
                    UMShareListener umShareListener = SocialRouter.this.getAndRemoveShareListener(platformOrdinal);
                    if (umShareListener != null) {
                        umShareListener.onResult(platform);
                    }

                }

                public void onError(SHARE_MEDIA platform, Throwable t) {
                    UMShareListener umShareListener = SocialRouter.this.getAndRemoveShareListener(platformOrdinal);
                    if (umShareListener != null) {
                        umShareListener.onError(platform, t);
                    }

                    if (t != null) {
                        Log.um("error:" + t.getMessage());
                        Log.um(UmengText.SOLVE + "https://at.umeng.com/LXzm8D?cid=476");
                    } else {
                        Log.um("error:null");
                    }

                }

                public void onCancel(SHARE_MEDIA platform) {
                    UMShareListener umShareListener = SocialRouter.this.getAndRemoveShareListener(platformOrdinal);
                    if (umShareListener != null) {
                        umShareListener.onCancel(platform);
                    }

                }
            };
            if (!action.getUrlValid()) {
                QueuedWork.runInMain(new Runnable() {
                    public void run() {
                        shareListener.onError(action.getPlatform(), new Throwable(UmengErrorCode.NotInstall.getMessage() + UmengText.WEB_HTTP));
                    }
                });
            } else {
                QueuedWork.runInMain(new Runnable() {
                    public void run() {
                        if (listener != null) {
                            listener.onStart(action.getPlatform());
                        }

                    }
                });
                handler.share(action.getShareContent(), shareListener);
            }
        }
    }

    private synchronized void saveAuthListener(int platformOrdinal, UMAuthListener listener) {
        this.mAuthListenerContainer.put(platformOrdinal, listener);
    }

    private synchronized UMAuthListener getAndRemoveAuthListener(int platformOrdinal) {
        this.mAuthPlatform = null;
        UMAuthListener listener = (UMAuthListener) this.mAuthListenerContainer.get(platformOrdinal, null);
        if (listener != null) {
            this.mAuthListenerContainer.remove(platformOrdinal);
        }

        return listener;
    }

    private synchronized void saveFetchUserInfoListener(int platformOrdinal, UMAuthListener listener) {
        this.mFetchUserInfoListenerContainer.put(platformOrdinal, listener);
    }

    private synchronized UMAuthListener getAndRemoveFetchUserInfoListener(int platformOrdinal) {
        UMAuthListener listener = (UMAuthListener) this.mFetchUserInfoListenerContainer.get(platformOrdinal, null);
        if (listener != null) {
            this.mFetchUserInfoListenerContainer.remove(platformOrdinal);
        }

        return listener;
    }

    private synchronized void saveShareListener(int platformOrdinal, UMShareListener listener) {
        this.mShareListenerContainer.put(platformOrdinal, listener);
    }

    private synchronized UMShareListener getAndRemoveShareListener(int platformOrdinal) {
        UMShareListener listener = (UMShareListener) this.mShareListenerContainer.get(platformOrdinal, null);
        if (listener != null) {
            this.mShareListenerContainer.remove(platformOrdinal);
        }

        return listener;
    }

    private synchronized void clearListener() {
        this.mAuthListenerContainer.clear();
        this.mShareListenerContainer.clear();
        this.mFetchUserInfoListenerContainer.clear();
    }

    private void setAuthListener(SHARE_MEDIA shareMedia, UMAuthListener listener, UMSSOHandler handler, String tag) {
        if (!handler.isHasAuthListener()) {
            int platformOrdinal = shareMedia.ordinal();
            this.saveAuthListener(platformOrdinal, listener);
            UMAuthListener authListener = this.getAuthListener(platformOrdinal, tag);
            handler.setAuthListener(authListener);
        }
    }

    public void release() {
        this.clearListener();
        UMSSOHandler sina = (UMSSOHandler) this.platformHandlers.get(SHARE_MEDIA.SINA);
        if (sina != null) {
            sina.release();
        }

        UMSSOHandler moreHandler = (UMSSOHandler) this.platformHandlers.get(SHARE_MEDIA.MORE);
        if (moreHandler != null) {
            moreHandler.release();
        }

        UMSSOHandler dingHandler = (UMSSOHandler) this.platformHandlers.get(SHARE_MEDIA.DINGTALK);
        if (dingHandler != null) {
            dingHandler.release();
        }

        UMSSOHandler weixinHandler = (UMSSOHandler) this.platformHandlers.get(SHARE_MEDIA.WEIXIN);
        if (weixinHandler != null) {
            weixinHandler.release();
        }

        UMSSOHandler qqHandler = (UMSSOHandler) this.platformHandlers.get(SHARE_MEDIA.QQ);
        if (qqHandler != null) {
            qqHandler.release();
        }

        this.mAuthPlatform = null;
    }

    private void safelyCloseDialog(Dialog dialog) {
        if (dialog != null) {
            try {
                dialog.dismiss();
            } catch (Exception var3) {
                var3.printStackTrace();
            }

        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        String platform = "";
        int action = -1;
        if (this.mAuthPlatform != null && (this.mAuthPlatform == SHARE_MEDIA.WEIXIN || this.mAuthPlatform == SHARE_MEDIA.QQ || this.mAuthPlatform == SHARE_MEDIA.SINA)) {
            platform = this.mAuthPlatform.toString();
            action = 0;
        }

        bundle.putString(BUNDLE_KEY_AUTH_PLATFORM, platform);
        bundle.putInt(BUNDLE_KEY_ACTION, action);
        this.mAuthPlatform = null;
    }

    public void fetchAuthResultWithBundle(Activity context, Bundle bundle, UMAuthListener listener) {
        if (bundle != null && listener != null) {
            String platform = bundle.getString(BUNDLE_KEY_AUTH_PLATFORM, (String) null);
            int action = bundle.getInt(BUNDLE_KEY_ACTION, -1);
            if (action == 0 && !TextUtils.isEmpty(platform)) {
                SHARE_MEDIA shareMedia = SHARE_MEDIA.convertToEmun(platform);
                if (shareMedia != null) {
                    UMSSOHandler handler;
                    if (shareMedia == SHARE_MEDIA.QQ) {
                        handler = (UMSSOHandler) this.platformHandlers.get(shareMedia);
                        handler.onCreate(context, PlatformConfig.getPlatform(shareMedia));
                    } else {
                        Log.e("SocialRouter","开始执行1");
                        handler = this.getHandler(shareMedia);
                    }

                    if (handler != null) {
                        String tag = String.valueOf(System.currentTimeMillis());
                        this.setAuthListener(shareMedia, listener, handler, tag);
                    }
                }
            }
        }

    }

    public void setShareConfig(UMShareConfig config) {
        if (this.platformHandlers != null && !this.platformHandlers.isEmpty()) {
            Iterator var2 = this.platformHandlers.entrySet().iterator();

            while (var2.hasNext()) {
                Entry<SHARE_MEDIA, UMSSOHandler> entry = (Entry) var2.next();
                UMSSOHandler handler = (UMSSOHandler) entry.getValue();
                if (handler != null) {
                    handler.setShareConfig(config);
                }
            }
        }

    }

    static class ParamsGuard {
        private Map<SHARE_MEDIA, UMSSOHandler> handlers;

        public ParamsGuard(Map<SHARE_MEDIA, UMSSOHandler> handlers) {
            this.handlers = handlers;
        }

        public boolean auth(Context context, SHARE_MEDIA platform) {
            if (!this.checkContext(context)) {
                return false;
            } else if (!this.checkPlatformConfig(platform)) {
                return false;
            } else {
                UMSSOHandler handler = (UMSSOHandler) this.handlers.get(platform);
                if (!handler.isSupportAuth()) {
                    Log.w(platform.toString() + UmengText.NOT_SUPPORT_PLATFROM);
                    return false;
                } else {
                    return true;
                }
            }
        }

        public boolean share(ShareAction action) {
            SHARE_MEDIA platform = action.getPlatform();
            if (platform == null) {
                return false;
            } else if ((platform == SHARE_MEDIA.SINA || platform == SHARE_MEDIA.QQ || platform == SHARE_MEDIA.WEIXIN) && !((Platform) PlatformConfig.configs.get(platform)).isConfigured()) {
                Log.um(UmengText.errorWithUrl(UmengText.noKey(platform), "https://at.umeng.com/8Tfmei?cid=476"));
                return false;
            } else {
                return this.checkPlatformConfig(platform);
            }
        }

        private boolean checkContext(Context context) {
            if (context == null) {
                Log.e("Context is null");
                return false;
            } else {
                return true;
            }
        }

        private boolean checkPlatformConfig(SHARE_MEDIA media) {
            Platform platform = (Platform) PlatformConfig.configs.get(media);
            UMSSOHandler handler = (UMSSOHandler) this.handlers.get(media);
            if (handler == null) {
                Log.url(UmengText.noJar(media), "https://at.umeng.com/9T595j?cid=476");
                return false;
            } else {
                return true;
            }
        }
    }
}
