package com.umeng.weixin.handler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareContent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.UmengErrorCode;
import com.umeng.socialize.common.QueuedWork;
import com.umeng.socialize.common.ResContainer;
import com.umeng.socialize.handler.UMSSOHandler;
import com.umeng.socialize.media.UMMin;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.utils.ContextUtil;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.SocializeUtils;
import com.umeng.socialize.utils.UmengText;
import com.umeng.weixin.umengwx.ApiResp;
import com.umeng.weixin.umengwx.BaseReq;
import com.umeng.weixin.umengwx.BaseResp;
import com.umeng.weixin.umengwx.IWXAPIEventHandler;
import com.umeng.weixin.umengwx.SendMessageToWeiXinReq;
import com.umeng.weixin.umengwx.WeApi;
import com.umeng.weixin.umengwx.SendMessageToWeiXinResp;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import static com.umeng.socialize.bean.SHARE_MEDIA.WEIXIN;

public class UmengWXHandler extends UMSSOHandler {
    private WeixinPreferences mWeixinPreferences;
    //微信会话
    public static final int WXSceneSession = 0;
    //微信朋友圈
    public static final int WXSceneTimeline = 1;
    //微信收藏
    public static final int WXSceneFavorite = 2;
    private WeApi mWeApi;
    private String VERSION = "6.4.6";
    private WXShareContent mWXShareContent;
    private PlatformConfig.APPIDPlatform config;
    private SHARE_MEDIA mTarget = WEIXIN;
    private UMAuthListener mAuthListener;
    private static final int REFRESH_TOKEN_EXPIRES = 604800;
    private static final int RESP_TYPE_AUTH = 1;
    private static final int RESP_TYPE_SHARE = 2;
    private UMShareListener mUMShareListener;
    private static String sScope = "snsapi_userinfo,snsapi_friend,snsapi_message";
    private static final String REFRESH_TOKEN_EXPIRES_KEY = "refresh_token_expires";
    private static final String NICKNAME = "nickname";
    private static final String LANGUAGE = "language";
    private static final String HEADIMGURL = "headimgurl";
    private static final String SEX = "sex";
    private static final String PRIVILEGE = "privilege";
    private static final String ERRORCODE = "errcode";
    private static final String ERRMSG = "errmsg";
    private static final String ERROR_CODE_TOKEN_FAIL = "40001";
    private static final String ERROR_CODE_TOKEN_REFESH_FAIL = "40030";
    private static final String ERROR_CODE_TOKEN_ACCESS_FAIL = "42002";
    private IWXAPIEventHandler mWxHandler = new IWXAPIEventHandler() {
        @Override
        public void onResp(BaseResp resp) {
            int type = resp.getType();
            switch(type) {
                case RESP_TYPE_AUTH:
                    onAuthCallback((SendMessageToWeiXinResp)resp);
                    break;
                case RESP_TYPE_SHARE:
                    onShareCallback((ApiResp)resp);
            }
        }

        @Override
        public void onReq(BaseReq req) {

        }
    };

    public void onCreate(Context paramContext, PlatformConfig.Platform paramPlatform) {
        super.onCreate(paramContext, paramPlatform);
        this.mWeixinPreferences = new WeixinPreferences(paramContext.getApplicationContext(), "weixin");
        this.config = ((PlatformConfig.APPIDPlatform) paramPlatform);
        this.mWeApi = new WeApi(paramContext.getApplicationContext(), this.config.appId);
        this.mWeApi.registerApp(this.config.appId);
        Log.um("wechat simplify:" + this.VERSION);
    }

    public boolean isAuthorize() {
        if (this.mWeixinPreferences != null) {
            return this.mWeixinPreferences.isWxAuthorize();
        }
        return false;
    }

    public boolean share(ShareContent shareContent, final UMShareListener umShareListener) {
        this.mTarget = this.config.getName();
        if (!isInstall()) {
            if (Config.isJumptoAppStore) {
                Intent var4 = new Intent("android.intent.action.VIEW");
                var4.setData(Uri.parse("http://log.umsns.com/link/weixin/download/"));
                ((Activity) this.mWeakAct.get()).startActivity(var4);
            }
            QueuedWork.runInMain(new Runnable() {
                @Override
                public void run() {
                    umShareListener.onError(mTarget, new Throwable(UmengErrorCode.NotInstall.getMessage()));
                }
            });
            return false;
        } else {
            ShareContent wxShareContent = filterShareContent(shareContent);
            this.mWXShareContent = new WXShareContent(wxShareContent);
            if ((this.mWXShareContent.getmStyle() == 64) && ((this.mTarget == SHARE_MEDIA.WEIXIN_CIRCLE) || (this.mTarget == SHARE_MEDIA.WEIXIN_FAVORITE))) {
                QueuedWork.runInMain(new Runnable() {
                    @Override
                    public void run() {
                        umShareListener.onError(mTarget, new Throwable(UmengErrorCode.ShareDataTypeIllegal.getMessage() + UmengText.WX_CIRCLE_NOT_SUPPORT_EMOJ));
                    }
                });
                Toast.makeText(getContext(), UmengText.WX_CIRCLE_NOT_SUPPORT_EMOJ, 0).show();
                return false;
            }
            this.mUMShareListener = umShareListener;
            return shareTo(new WXShareContent(shareContent));
        }

    }

    private ShareContent filterShareContent(ShareContent shareContent) {
        if ((shareContent.getShareType() == 128) && (getWXAppSupportAPI() < 620756993)) {
            UMMin localUMMin = (UMMin) shareContent.mMedia;
            UMWeb localUMWeb = new UMWeb(localUMMin.toUrl());
            localUMWeb.setThumb(localUMMin.getThumbImage());
            localUMWeb.setDescription(localUMMin.getDescription());
            localUMWeb.setTitle(localUMMin.getTitle());
            shareContent.mMedia = localUMWeb;
        }
        return shareContent;
    }

    public int getWXAppSupportAPI() {
        if (!isInstall()) {
            return 0;
        }
        int i1 = 0;
        try {
            i1 = getContext().getPackageManager().getApplicationInfo("com.tencent.mm", 128).metaData.getInt("com.tencent.mm.BuildInfo.OPEN_SDK_VERSION", 0);
        } catch (Exception localException) {
        }
        return i1;
    }

    public boolean shareTo(WXShareContent wxShareContent) {
        final Bundle localBundle = wxShareContent.getWxShareBundle();
        localBundle.putString("_wxapi_basereq_transaction", buildTransaction(this.mWXShareContent.getStrStyle()));
        if (!TextUtils.isEmpty(localBundle.getString("error"))) {
            QueuedWork.runInMain(new Runnable() {
                @Override
                public void run() {
                    mUMShareListener.onError(mTarget, new Throwable(UmengErrorCode.UnKnowCode.getMessage() + localBundle.getString("error")));
                }
            });
            return false;
        }
        switch (mTarget) {
            case WEIXIN:
                localBundle.putInt("_wxapi_sendmessagetowx_req_scene", WXSceneSession);
                break;
            case WEIXIN_CIRCLE:
                localBundle.putInt("_wxapi_sendmessagetowx_req_scene", WXSceneTimeline);
                break;
            case WEIXIN_FAVORITE:
                localBundle.putInt("_wxapi_sendmessagetowx_req_scene", WXSceneFavorite);
                break;
            default:
                localBundle.putInt("_wxapi_sendmessagetowx_req_scene", WXSceneSession);
        }
        this.mWeApi.pushare(localBundle);
        return true;
    }

    public boolean isInstall() {
        return this.mWeApi.isWXAppInstalled();
    }

    public boolean isSupportAuth() {
        return true;
    }

    public void deleteAuth(final UMAuthListener umAuthListener) {
        clearWxCache();
        QueuedWork.runInMain(new Runnable() {
            @Override
            public void run() {
                umAuthListener.onComplete(WEIXIN, UMAuthListener.ACTION_DELETE, null);
            }
        });
    }

    //生份验证是有效的
    private boolean isAuthValid() {
        if (this.mWeixinPreferences != null) {
            return this.mWeixinPreferences.isAuthValid();
        }
        return false;
    }

    private boolean isAccessTokenAvailable() {
        if (this.mWeixinPreferences != null) {
            return this.mWeixinPreferences.isAccessTokenAvailable();
        }
        return false;
    }

    private void clearWxCache() {
        if (this.mWeixinPreferences != null) {
            this.mWeixinPreferences.clearAuthInfo();
        }
    }

    private String getRefreshToken() {
        if (this.mWeixinPreferences != null) {
            return this.mWeixinPreferences.getRefreshToken();
        }
        return "";
    }

    public void authorize(final UMAuthListener umAuthListener) {
        this.mAuthListener = umAuthListener;
        this.mTarget = this.config.getName();
        String refresh_token;
        if (!isInstall()) {
            if (Config.isJumptoAppStore) {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setData(Uri.parse("http://log.umsns.com/link/weixin/download/"));
                ((Activity) this.mWeakAct.get()).startActivity(intent);
            }
            QueuedWork.runInMain(new Runnable() {
                @Override
                public void run() {
                    umAuthListener.onError(mTarget, UMAuthListener.ACTION_AUTHORIZE, new Throwable(UmengErrorCode.NotInstall.getMessage()));
                }
            });
        } else {
            if (isAuthValid()) {
                if (!isAccessTokenAvailable()) {
                    //请求刷新refresh_token
                    refresh_token = getRefreshToken();
                    String url = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=" + this.config.appId + "&grant_type=refresh_token&refresh_token=" + (String) refresh_token;
                    loadOauthData(url);
                }
                refresh_token = getRefreshToken();
                final Map<String,String> resultMap = getAuthWithRefreshToken(refresh_token);
                if ((( resultMap).containsKey(ERRORCODE)) && ((( ( resultMap).get(ERRORCODE)).equals(ERROR_CODE_TOKEN_REFESH_FAIL)) || (( resultMap).get("errcode")).equals(ERROR_CODE_TOKEN_ACCESS_FAIL))) {
                    clearWxCache();
                    authorize(umAuthListener);
                    return;
                }
                QueuedWork.runInMain(new Runnable() {
                    @Override
                    public void run() {
                        mAuthListener.onComplete(WEIXIN, UMAuthListener.ACTION_AUTHORIZE, resultMap);
                    }
                });
            } else {
                SendMessageToWeiXinReq weiXinRequest = new SendMessageToWeiXinReq();
                weiXinRequest.req_scope = sScope;
                weiXinRequest.req_state = "123";
                this.mWeApi.sendReq(weiXinRequest);
            }
        }

    }

    private void loadOauthData(String url) {
        String response = WXAuthUtils.request(url);
        Bundle localBundle = parseAuthData(response);
        setBundle(localBundle);
    }

    private Map<String,String> getAuthWithRefreshToken(String refresh_token) {
        StringBuilder requestBuilfer = new StringBuilder();
        requestBuilfer.append("https://api.weixin.qq.com/sns/oauth2/refresh_token?");
        requestBuilfer.append("appid=").append(this.config.appId);
        requestBuilfer.append("&grant_type=refresh_token");
        requestBuilfer.append("&refresh_token=").append(refresh_token);
        String result = WXAuthUtils.request(requestBuilfer.toString());
        Map<String,String> localMap = null;
        try {
            localMap = SocializeUtils.jsonToMap(result);
            localMap.put("unionid", getUnionid());
        } catch (Exception localException) {
        }
        return localMap;
    }

    private String buildTransaction(String paramString) {
        return paramString + System.currentTimeMillis();
    }

    public IWXAPIEventHandler getWXEventHandler() {
        return this.mWxHandler;
    }

    public WeApi getWXApi() {
        return this.mWeApi;
    }

    protected void onShareCallback(BaseResp baseResp) {
        Log.e("1111111111",baseResp.toString());
        switch (baseResp.wxErrorCode) {
            case 0:
                if (this.mUMShareListener != null) {
                    this.mUMShareListener.onResult(this.mTarget);
                }
                break;
            case -2:
                if (this.mUMShareListener != null) {
                    this.mUMShareListener.onCancel(this.mTarget);
                }
                break;
            case -3:
            case -1:
                if (this.mUMShareListener != null) {
                    this.mUMShareListener.onError(this.mTarget, new Throwable(UmengErrorCode.ShareFailed.getMessage() + baseResp.wxErrStr));
                }
                break;
            case -6:
                if (this.mUMShareListener != null) {
                    this.mUMShareListener.onError(this.mTarget, new Throwable(UmengErrorCode.ShareFailed.getMessage() + UmengText.errorWithUrl(UmengText.AUTH_DENIED, "https://at.umeng.com/f8HHDi?cid=476")));
                }
                break;
            case -5:
                if (this.mUMShareListener != null) {
                    this.mUMShareListener.onError(this.mTarget, new Throwable(UmengErrorCode.ShareFailed.getMessage() + UmengText.VERSION_NOT_SUPPORT));
                }
                break;
            case -4:
            default:
                if (this.mUMShareListener != null) {
                    this.mUMShareListener.onError(this.mTarget, new Throwable(UmengErrorCode.ShareFailed.getMessage() + baseResp.wxErrStr));
                }
                break;
        }
    }

    protected String getToName() {
        return "wxsession";
    }

    protected void onAuthCallback(SendMessageToWeiXinResp result) {
        if (result.wxErrorCode == 0) {
            getAuthWithCode(result.resp_token, this.mAuthListener);
        } else if (result.wxErrorCode == -2) {
            if (this.mAuthListener != null) {
                this.mAuthListener.onCancel(WEIXIN, UMAuthListener.ACTION_AUTHORIZE);
            }
        } else if (result.wxErrorCode == -6) {
            if (this.mAuthListener != null) {
                this.mAuthListener.onError(WEIXIN, UMAuthListener.ACTION_AUTHORIZE, new Throwable(UmengErrorCode.AuthorizeFailed.getMessage() + UmengText.errorWithUrl(UmengText.AUTH_DENIED, "https://at.umeng.com/f8HHDi?cid=476")));
            }
        } else {
            CharSequence localCharSequence = TextUtils.concat(new CharSequence[]{"weixin auth error (", String.valueOf(result.wxErrorCode), "):", result.wxErrStr});
            if (this.mAuthListener != null) {
                this.mAuthListener.onError(WEIXIN, UMAuthListener.ACTION_AUTHORIZE, new Throwable(UmengErrorCode.AuthorizeFailed.getMessage() + localCharSequence.toString()));
            }
        }
    }

    public void setAuthListener(UMAuthListener paramUMAuthListener) {
        super.setAuthListener(paramUMAuthListener);
        this.mAuthListener = paramUMAuthListener;
    }

    public Map<String, String> getMap() {
        if (this.mWeixinPreferences != null) {
            return this.mWeixinPreferences.getmap();
        }
        return null;
    }

    private void setBundle(Bundle paramBundle) {
        if (this.mWeixinPreferences != null) {
            this.mWeixinPreferences.setBundle(paramBundle).save();
        }
    }

    private void getAuthWithCode(String code, final UMAuthListener umAuthListener) {
        final StringBuilder authURL = new StringBuilder();
        authURL.append("https://api.weixin.qq.com/sns/oauth2/access_token?");
        authURL.append("appid=").append(this.config.appId);
        authURL.append("&secret=").append(this.config.appkey);
        authURL.append("&code=").append(code);
        authURL.append("&grant_type=authorization_code");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String response = WXAuthUtils.request(authURL.toString());
                final Map map;
                try {
                    map = SocializeUtils.jsonToMap(response);
                    if(map == null || map.size() == 0) {
                        getMap();
                    }

                    final Bundle bundle = parseAuthData(response);
                    setBundle(bundle);
                    QueuedWork.runInMain(new Runnable() {
                        public void run() {
                            uploadAuthData(bundle);
                            if(umAuthListener != null) {
                                if(map.get("errcode") != null) {
                                    umAuthListener.onError(WEIXIN, 0, new Throwable(UmengErrorCode.AuthorizeFailed.getMessage() + (String)map.get("errmsg")));
                                } else {
                                    umAuthListener.onComplete(WEIXIN, 0, map);
                                }
                            }

                        }
                    });
                } catch (Exception var5) {
                }
            }
        }).start();
    }

    public Bundle parseAuthData(String response) {
        Bundle localBundle = new Bundle();
        if (TextUtils.isEmpty(response)) {
            return localBundle;
        }
        try {
            JSONObject localJSONObject = new JSONObject(response);
            Iterator localIterator = localJSONObject.keys();
            String str = "";
            while (localIterator.hasNext()) {
                str = (String) localIterator.next();
                localBundle.putString(str, localJSONObject.optString(str));
            }
            localBundle.putLong(REFRESH_TOKEN_EXPIRES_KEY, REFRESH_TOKEN_EXPIRES);
            localBundle.putString("accessToken", localBundle.getString("access_token"));
            localBundle.putString("expiration", localBundle.getString("expires_in"));
            localBundle.putString("refreshToken", localBundle.getString("refresh_token"));
            localBundle.putString("uid", localBundle.getString("unionid"));
        } catch (JSONException localJSONException) {
            localJSONException.printStackTrace();
        }
        return localBundle;
    }

    private Map parseUserInfo(String paramString) {
        HashMap<String,String> localHashMap = new HashMap();
        try {
            JSONObject localJSONObject = new JSONObject(paramString);
            boolean bool = localJSONObject.has(ERRORCODE);
            if (bool) {
                localHashMap.put(ERRORCODE, localJSONObject.getString(ERRORCODE));
                localHashMap.put(ERRMSG, localJSONObject.getString(ERRMSG));
                return localHashMap;
            }
            localHashMap.put("openid", localJSONObject.optString("openid"));
            localHashMap.put("screen_name", localJSONObject.optString(NICKNAME));
            localHashMap.put("name", localJSONObject.optString(NICKNAME));
            localHashMap.put("language", localJSONObject.optString(LANGUAGE));
            localHashMap.put("city", localJSONObject.optString("city"));
            localHashMap.put("province", localJSONObject.optString("province"));
            localHashMap.put("country", localJSONObject.optString("country"));
            localHashMap.put("profile_image_url", localJSONObject.optString(HEADIMGURL));
            localHashMap.put("iconurl", localJSONObject.optString(HEADIMGURL));
            localHashMap.put("unionid", localJSONObject.optString("unionid"));
            localHashMap.put("uid", localJSONObject.optString("unionid"));
            localHashMap.put("gender", getGender(localJSONObject.optString(SEX)));
            JSONArray localJSONArray = localJSONObject.getJSONArray(PRIVILEGE);
            int i1 = localJSONArray.length();
            if (i1 > 0) {
                String[] arrayOfString = new String[i1];
                for (int i2 = 0; i2 < i1; i2++) {
                    arrayOfString[i2] = localJSONArray.get(i2).toString();
                }
                localHashMap.put(PRIVILEGE, arrayOfString.toString());
            }
            localHashMap.put("access_token", getAccessToken());
            localHashMap.put("refreshToken", getRefreshToken());
            localHashMap.put("expires_in", String.valueOf(i()));
            localHashMap.put("accessToken", getAccessToken());
            localHashMap.put("refreshToken", getRefreshToken());
            localHashMap.put("expiration", String.valueOf(i()));
        } catch (JSONException localJSONException) {
            localJSONException.printStackTrace();
            return Collections.emptyMap();
        }
        return localHashMap;
    }

    //判断性别
    public String getGender(Object paramObject) {
        String str1 = ResContainer.getString(ContextUtil.getContext(), "umeng_socialize_male");
        String str2 = ResContainer.getString(ContextUtil.getContext(), "umeng_socialize_female");
        if (paramObject == null) {
            return "";
        }
        if ((paramObject instanceof String)) {
            if ((paramObject.equals("REFRESH_TOKEN_EXPIRES_KEY")) || (paramObject.equals("1")) || (paramObject.equals(UmengText.MAN))) {
                return str1;
            }
            if ((paramObject.equals("mTarget")) || (paramObject.equals("2")) || (paramObject.equals(UmengText.WOMAN))) {
                return str2;
            }
            return paramObject.toString();
        }
        if ((paramObject instanceof Integer)) {
            if (((Integer) paramObject).intValue() == 1) {
                return str1;
            }
            if (((Integer) paramObject).intValue() == 2) {
                return str2;
            }
            return paramObject.toString();
        }
        return paramObject.toString();
    }

    public void getPlatformInfo(UMAuthListener paramUMAuthListener) {
        if (getShareConfig().isNeedAuthOnGetUserInfo()) {
            clearWxCache();
        }
        authorize(new WXAuthListener(this, paramUMAuthListener));
    }

    private String getOpenid() {
        if (this.mWeixinPreferences != null) {
            return this.mWeixinPreferences.getOpenid();
        }
        return "";
    }

    private String getUnionid() {
        if (this.mWeixinPreferences != null) {
            return this.mWeixinPreferences.getUnionid();
        }
        return "";
    }

    private String getAccessToken() {
        if (this.mWeixinPreferences != null) {
            return this.mWeixinPreferences.getAccessToken();
        }
        return "";
    }

    private long i() {
        if (this.mWeixinPreferences != null) {
            return this.mWeixinPreferences.getExpires_in();
        }
        return 0L;
    }

    public void getActionProfile(final UMAuthListener umAuthListener) {
        String openid = getOpenid();
        String accessToken = getAccessToken();
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("https://api.weixin.qq.com/sns/userinfo?access_token=");
        localStringBuilder.append(accessToken).append("&openid=").append(openid);
        localStringBuilder.append("&lang=zh_CN");
        final String result = WXAuthUtils.request(localStringBuilder.toString());
        if ((TextUtils.isEmpty(result)) || (result.startsWith("##"))) {
            QueuedWork.runInMain(new Runnable() {
                @Override
                public void run() {
                    umAuthListener.onError(WEIXIN, RESP_TYPE_SHARE, new Throwable(UmengErrorCode.RequestForUserProfileFailed.getMessage() + result));
                }
            });
            return;
        }
        final Map resultMap = parseUserInfo(result);
        if (resultMap == null) {
            QueuedWork.runInMain(new Runnable() {
                @Override
                public void run() {
                    umAuthListener.onError(WEIXIN, UMAuthListener.ACTION_GET_PROFILE, new Throwable(UmengErrorCode.RequestForUserProfileFailed.getMessage() + result));
                }
            });
        } else if (resultMap.containsKey(ERRORCODE)) {
            if (((String) resultMap.get(ERRORCODE)).equals(ERROR_CODE_TOKEN_FAIL)) {
                clearWxCache();
                authorize(umAuthListener);
            } else {
                QueuedWork.runInMain(new Runnable() {
                    @Override
                    public void run() {
                        umAuthListener.onError(WEIXIN, UMAuthListener.ACTION_GET_PROFILE, new Throwable(UmengErrorCode.RequestForUserProfileFailed.getMessage() + resultMap.get(ERRORCODE)));
                    }
                });
            }
        } else {
            QueuedWork.runInMain(new Runnable() {
                @Override
                public void run() {
                    umAuthListener.onComplete(WEIXIN, UMAuthListener.ACTION_GET_PROFILE, resultMap);
                }
            });
        }
    }

    public boolean isHasAuthListener() {
        return this.mAuthListener != null;
    }

    public boolean isSupport() {
        return mWeApi.isWXAppSupportAPI();
    }

    public void release() {
        super.release();
        this.mAuthListener = null;
    }
}