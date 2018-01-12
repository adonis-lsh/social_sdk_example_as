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
import com.umeng.weixin.umengwx.BaseResponse;
import com.umeng.weixin.umengwx.IWxHandler;
import com.umeng.weixin.umengwx.SendMessageToWeiXinRequest;
import com.umeng.weixin.umengwx.WeChat;
import com.umeng.weixin.umengwx.SendMessageToWeiXinResponse;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UmengWXHandler extends UMSSOHandler {
    private WeixinPreferences mWeixinPreferences;
    //微信会话
    public static final int WXSceneSession = 0;
    //微信朋友圈
    public static final int WXSceneTimeline = 1;
    //微信收藏
    public static final int WXSceneFavorite = 2;
    private WeChat weChat;
    private String VERSION = "6.4.6";
    private WXShareContent mWXShareContent;
    private PlatformConfig.APPIDPlatform mAPPIDPlatform;
    private SHARE_MEDIA mPlatform = SHARE_MEDIA.WEIXIN;
    private UMAuthListener mUMAuthListener;
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
    private IWxHandler mIWxHandler = new WxHandler(this);

    public void onCreate(Context paramContext, PlatformConfig.Platform paramPlatform) {
        super.onCreate(paramContext, paramPlatform);
        this.mWeixinPreferences = new WeixinPreferences(paramContext.getApplicationContext(), "weixin");
        this.mAPPIDPlatform = ((PlatformConfig.APPIDPlatform) paramPlatform);
        this.weChat = new WeChat(paramContext.getApplicationContext(), this.mAPPIDPlatform.appId);
        this.weChat.registerApp(this.mAPPIDPlatform.appId);
        Log.um("wechat simplify:" + this.VERSION);
    }

    public boolean isAuthorize() {
        if (this.mWeixinPreferences != null) {
            return this.mWeixinPreferences.isWxAuthorize();
        }
        return false;
    }

    public boolean share(ShareContent shareContent, final UMShareListener umShareListener) {
        this.mPlatform = this.mAPPIDPlatform.getName();
        if (!isInstall()) {
            if (Config.isJumptoAppStore) {
                Intent var4 = new Intent("android.intent.action.VIEW");
                var4.setData(Uri.parse("http://log.umsns.com/link/weixin/download/"));
                ((Activity) this.mWeakAct.get()).startActivity(var4);
            }
            QueuedWork.runInMain(new Runnable() {
                @Override
                public void run() {
                    umShareListener.onError(mPlatform, new Throwable(UmengErrorCode.NotInstall.getMessage()));
                }
            });
            return false;
        }
        Object localObject = a(shareContent);
        this.mWXShareContent = new WXShareContent((ShareContent) localObject);
        if ((this.mWXShareContent != null) && (this.mWXShareContent.getmStyle() == 64) && ((this.mPlatform == SHARE_MEDIA.WEIXIN_CIRCLE) || (this.mPlatform == SHARE_MEDIA.WEIXIN_FAVORITE))) {
            QueuedWork.runInMain(new Runnable() {
                @Override
                public void run() {
                    umShareListener.onError(mPlatform, new Throwable(UmengErrorCode.ShareDataTypeIllegal.getMessage() + UmengText.WX_CIRCLE_NOT_SUPPORT_EMOJ));
                }
            });
            Toast.makeText(getContext(), UmengText.WX_CIRCLE_NOT_SUPPORT_EMOJ, 0).show();
            return false;
        }
        this.mUMShareListener = umShareListener;
        return shareTo(new WXShareContent(shareContent));
    }

    private ShareContent a(ShareContent paramShareContent) {
        if ((paramShareContent.getShareType() == 128) && (getWXAppSupportAPI() < 620756993)) {
            UMMin localUMMin = (UMMin) paramShareContent.mMedia;
            UMWeb localUMWeb = new UMWeb(localUMMin.toUrl());
            localUMWeb.setThumb(localUMMin.getThumbImage());
            localUMWeb.setDescription(localUMMin.getDescription());
            localUMWeb.setTitle(localUMMin.getTitle());
            paramShareContent.mMedia = localUMWeb;
        }
        return paramShareContent;
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

    public boolean shareTo(WXShareContent params) {
        final Bundle localBundle = params.getWxShareBundle();
        localBundle.putString("_wxapi_basereq_transaction", c(this.mWXShareContent.getStrStyle()));
        if (!TextUtils.isEmpty(localBundle.getString("error"))) {
            QueuedWork.runInMain(new Runnable() {
                @Override
                public void run() {
                    mUMShareListener.onError(mPlatform, new Throwable(UmengErrorCode.UnKnowCode.getMessage() + localBundle.getString("error")));
                }
            });
            return false;
        }
        switch (mPlatform.ordinal()) {
            case 1:
                localBundle.putInt("_wxapi_sendmessagetowx_req_scene", WXSceneSession);
                break;
            case 2:
                localBundle.putInt("_wxapi_sendmessagetowx_req_scene", WXSceneTimeline);
                break;
            case 3:
                localBundle.putInt("_wxapi_sendmessagetowx_req_scene", WXSceneFavorite);
                break;
            default:
                localBundle.putInt("_wxapi_sendmessagetowx_req_scene", WXSceneSession);
        }
        this.weChat.pushare(localBundle);
        return true;
    }

    public boolean isInstall() {
        return this.weChat.isWXAppInstalled();
    }

    public boolean isSupportAuth() {
        return true;
    }

    public void deleteAuth(final UMAuthListener umAuthListener) {
        clearWxCache();
        QueuedWork.runInMain(new Runnable() {
            @Override
            public void run() {
                umAuthListener.onComplete(SHARE_MEDIA.WEIXIN, UMAuthListener.ACTION_DELETE, null);
            }
        });
    }

    private boolean isRefreshToken() {
        if (this.mWeixinPreferences != null) {
            return this.mWeixinPreferences.isExpirereFreshToken();
        }
        return false;
    }

    private boolean isRefreshAccessToken() {
        if (this.mWeixinPreferences != null) {
            return this.mWeixinPreferences.isExpireAccessToken();
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
        this.mUMAuthListener = umAuthListener;
        this.mPlatform = this.mAPPIDPlatform.getName();
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
                    umAuthListener.onError(mPlatform, UMAuthListener.ACTION_AUTHORIZE, new Throwable(UmengErrorCode.NotInstall.getMessage()));
                }
            });
            return;
        }
        if (isRefreshToken()) {
            if (!isRefreshAccessToken()) {
                //请求刷新refresh_token
                refresh_token = getRefreshToken();
                String localObject2 = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=" + this.mAPPIDPlatform.appId + "&grant_type=refresh_token&refresh_token=" + (String) refresh_token;
                getResultBundle(localObject2);
            }
            refresh_token = getRefreshToken();
            final Map<String,String> resultMap = toMap(refresh_token);
            if ((( resultMap).containsKey(ERRORCODE)) && ((( ( resultMap).get(ERRORCODE)).equals(ERROR_CODE_TOKEN_REFESH_FAIL)) || (( resultMap).get("errcode")).equals(ERROR_CODE_TOKEN_ACCESS_FAIL))) {
                clearWxCache();
                authorize(umAuthListener);
                return;
            }
//            QueuedWork.runInMain(new AuthCompleteRun(this, resultMap));
            QueuedWork.runInMain(new Runnable() {
                @Override
                public void run() {
                    mUMAuthListener.onComplete(SHARE_MEDIA.WEIXIN, UMAuthListener.ACTION_AUTHORIZE, resultMap);
                }
            });
        } else {
            SendMessageToWeiXinRequest weiXinRequest = new SendMessageToWeiXinRequest();
             weiXinRequest.req_scope = sScope;
            weiXinRequest.req_state = "123";
            this.weChat.sendReq(weiXinRequest);
        }
    }

    private void getResultBundle(String requestParams) {
        String result = WXAuthUtils.request(requestParams);
        Bundle localBundle = getParamBundle(result);
        saveBundle(localBundle);
    }

    private Map<String,String> toMap(String paramString) {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("https://api.weixin.qq.com/sns/oauth2/refresh_token?");
        localStringBuilder.append("appid=").append(this.mAPPIDPlatform.appId);
        localStringBuilder.append("&grant_type=refresh_token");
        localStringBuilder.append("&refresh_token=").append(paramString);
        String str = WXAuthUtils.request(localStringBuilder.toString());
        Map<String,String> localMap = null;
        try {
            localMap = SocializeUtils.jsonToMap(str);
            localMap.put("unionid", getUnionid());
        } catch (Exception localException) {
        }
        return localMap;
    }

    private String c(String paramString) {
        return paramString + System.currentTimeMillis();
    }

    public IWxHandler getWXEventHandler() {
        return this.mIWxHandler;
    }

    public WeChat getWXApi() {
        return this.weChat;
    }

    protected void getResult(BaseResponse baseResponse) {
        switch (baseResponse.wxErrorCode) {
            case 0:
                if (this.mUMShareListener != null) {
                    this.mUMShareListener.onResult(this.mPlatform);
                }
                break;
            case -2:
                if (this.mUMShareListener != null) {
                    this.mUMShareListener.onCancel(this.mPlatform);
                }
                break;
            case -3:
            case -1:
                if (this.mUMShareListener != null) {
                    this.mUMShareListener.onError(this.mPlatform, new Throwable(UmengErrorCode.ShareFailed.getMessage() + baseResponse.wxErrStr));
                }
                break;
            case -6:
                if (this.mUMShareListener != null) {
                    this.mUMShareListener.onError(this.mPlatform, new Throwable(UmengErrorCode.ShareFailed.getMessage() + UmengText.errorWithUrl(UmengText.AUTH_DENIED, "https://at.umeng.com/f8HHDi?cid=476")));
                }
                break;
            case -5:
                if (this.mUMShareListener != null) {
                    this.mUMShareListener.onError(this.mPlatform, new Throwable(UmengErrorCode.ShareFailed.getMessage() + UmengText.VERSION_NOT_SUPPORT));
                }
                break;
            case -4:
            default:
                if (this.mUMShareListener != null) {
                    this.mUMShareListener.onError(this.mPlatform, new Throwable(UmengErrorCode.ShareFailed.getMessage() + baseResponse.wxErrStr));
                }
                break;
        }
    }

    protected String getToName() {
        return "wxsession";
    }

    protected void getResult(SendMessageToWeiXinResponse result) {
        if (result.wxErrorCode == 0) {
            authRequest(result.resp_token, this.mUMAuthListener);
        } else if (result.wxErrorCode == -2) {
            if (this.mUMAuthListener != null) {
                this.mUMAuthListener.onCancel(SHARE_MEDIA.WEIXIN, UMAuthListener.ACTION_AUTHORIZE);
            }
        } else if (result.wxErrorCode == -6) {
            if (this.mUMAuthListener != null) {
                this.mUMAuthListener.onError(SHARE_MEDIA.WEIXIN, UMAuthListener.ACTION_AUTHORIZE, new Throwable(UmengErrorCode.AuthorizeFailed.getMessage() + UmengText.errorWithUrl(UmengText.AUTH_DENIED, "https://at.umeng.com/f8HHDi?cid=476")));
            }
        } else {
            CharSequence localCharSequence = TextUtils.concat(new CharSequence[]{"weixin auth error (", String.valueOf(result.wxErrorCode), "):", result.wxErrStr});
            if (this.mUMAuthListener != null) {
                this.mUMAuthListener.onError(SHARE_MEDIA.WEIXIN, UMAuthListener.ACTION_AUTHORIZE, new Throwable(UmengErrorCode.AuthorizeFailed.getMessage() + localCharSequence.toString()));
            }
        }
    }

    public void setAuthListener(UMAuthListener paramUMAuthListener) {
        super.setAuthListener(paramUMAuthListener);
        this.mUMAuthListener = paramUMAuthListener;
    }

    public Map getResultMap() {
        if (this.mWeixinPreferences != null) {
            return this.mWeixinPreferences.getWxId();
        }
        return null;
    }

    private void saveBundle(Bundle paramBundle) {
        if (this.mWeixinPreferences != null) {
            this.mWeixinPreferences.setBundle(paramBundle).save();
        }
    }

    private void authRequest(String code, final UMAuthListener umAuthListener) {
        final StringBuilder requestParams = new StringBuilder();
        requestParams.append("https://api.weixin.qq.com/sns/oauth2/access_token?");
        requestParams.append("appid=").append(this.mAPPIDPlatform.appId);
        requestParams.append("&secret=").append(this.mAPPIDPlatform.appkey);
        requestParams.append("&code=").append(code);
        requestParams.append("&grant_type=authorization_code");
        //TODO 起点分析
//        new Thread(new UMAuthRequestRun(this, localStringBuilder, umAuthListener)).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = WXAuthUtils.request(requestParams.toString());
                Map resultMap = null;
                try {
                    resultMap = SocializeUtils.jsonToMap(result);
                    if ((resultMap == null) || (resultMap.size() == 0)) {
                        getParamBundle(result);
                    }
//            Bundle localBundle = UmengWXHandler.getResult(this.req_scope, result);
//            UmengWXHandler.getResult(this.req_scope, localBundle);
                    final Map localMap2 = resultMap;
//            UmengWXHandler.getOpenid(this.req_scope, localBundle);
//                    QueuedWork.runInMain(new AuthRun(this, localMap2));
                    QueuedWork.runInMain(new Runnable() {
                        @Override
                        public void run() {
                            if (localMap2 != null) {
                                if (localMap2.get("errcode") != null) {
                                    Throwable localThrowable = new Throwable(UmengErrorCode.RequestForUserProfileFailed.getMessage() + (String) localMap2.get("errmsg"));
                                    umAuthListener.onError(SHARE_MEDIA.WEIXIN, UMAuthListener.ACTION_AUTHORIZE, localThrowable);
                                } else {
                                    umAuthListener.onComplete(SHARE_MEDIA.WEIXIN, UMAuthListener.ACTION_AUTHORIZE, localMap2);
                                }
                            }
                        }
                    });
                } catch (Exception localException) {
                }
            }
        }).start();
    }

    public Bundle getParamBundle(String paramString) {
        Bundle localBundle = new Bundle();
        if (TextUtils.isEmpty(paramString)) {
            return localBundle;
        }
        try {
            JSONObject localJSONObject = new JSONObject(paramString);
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

    private Map getResultMap(String paramString) {
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
            if ((paramObject.equals("mPlatform")) || (paramObject.equals("2")) || (paramObject.equals(UmengText.WOMAN))) {
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
                    umAuthListener.onError(SHARE_MEDIA.WEIXIN, RESP_TYPE_SHARE, new Throwable(UmengErrorCode.RequestForUserProfileFailed.getMessage() + result));
                }
            });
            return;
        }
        final Map resultMap = getResultMap(result);
        if (resultMap == null) {
            QueuedWork.runInMain(new Runnable() {
                @Override
                public void run() {
                    umAuthListener.onError(SHARE_MEDIA.WEIXIN, UMAuthListener.ACTION_GET_PROFILE, new Throwable(UmengErrorCode.RequestForUserProfileFailed.getMessage() + result));
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
                        umAuthListener.onError(SHARE_MEDIA.WEIXIN, UMAuthListener.ACTION_GET_PROFILE, new Throwable(UmengErrorCode.RequestForUserProfileFailed.getMessage() + resultMap.get(ERRORCODE)));
                    }
                });
            }
        } else {
            QueuedWork.runInMain(new Runnable() {
                @Override
                public void run() {
                    umAuthListener.onComplete(SHARE_MEDIA.WEIXIN, UMAuthListener.ACTION_GET_PROFILE, resultMap);
                }
            });
        }
    }

    public void release() {
        super.release();
        this.mUMAuthListener = null;
    }
}