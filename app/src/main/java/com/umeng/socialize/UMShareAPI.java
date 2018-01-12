package com.umeng.socialize;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.common.QueuedWork;
import com.umeng.socialize.controller.SocialRouter;
import com.umeng.socialize.handler.UMSSOHandler;
import com.umeng.socialize.net.ActionBarRequest;
import com.umeng.socialize.net.ActionBarResponse;
import com.umeng.socialize.net.RestAPI;
import com.umeng.socialize.uploadlog.UMLog;
import com.umeng.socialize.utils.ContextUtil;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.SocializeSpUtils;
import com.umeng.socialize.utils.SocializeUtils;
import com.umeng.socialize.utils.UrlUtil;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;


public class UMShareAPI {
    private static UMShareAPI singleton = null;

    private SocialRouter router;

    private UMShareConfig mDefaultShareConfig = new UMShareConfig();

    private UMShareAPI(Context context) {
        ContextUtil.setContext(context.getApplicationContext());
        this.router = new SocialRouter(context.getApplicationContext());
        new InitThread(context.getApplicationContext()).execute();
    }


    public static UMShareAPI get(Context context) {
        if ((singleton == null) || (singleton.router == null)) {
            singleton = new UMShareAPI(context);
        }
        singleton.router.setmContext(context);
        return singleton;
    }

    public static void init(Context context, String appkey) {
        com.umeng.socialize.common.SocializeConstants.APPKEY = appkey;
        get(context);
    }

    public void doOauthVerify(final Activity activity, final SHARE_MEDIA platform, final UMAuthListener listener) {
        UMLog.putAuth();
        singleton.router.setmContext(activity);
        if ((Config.DEBUG) && (!judgePlatform(activity, platform))) {
            return;
        }

        if (activity != null) {


            new QueuedWork.DialogThread(activity) {
                protected Void doInBackground() {
                    if (UMShareAPI.this.router == null) {
                        UMShareAPI.this.router = new SocialRouter(activity);
                    }
                    UMShareAPI.this.router.doOauthVerify(activity, platform, listener);
                    return null;
                }
            }.execute();
        } else {
            Log.d("UMerror", "doOauthVerify activity is null");
        }
    }

    public void deleteOauth(final Activity context, final SHARE_MEDIA platform, final UMAuthListener listener) {
        if (context != null) {
            singleton.router.setmContext(context);
            new QueuedWork.DialogThread(context) {
                protected Object doInBackground() {
                    if (UMShareAPI.this.router != null) {
                        UMShareAPI.this.router.deleteOauth(context, platform, listener);
                    }

                    return null;
                }
            }.execute();
        } else {
            Log.d("UMerror", "deleteOauth activity is null");
        }
    }

    public void getPlatformInfo(final Activity context, final SHARE_MEDIA platform, final UMAuthListener listener) {
        if (context != null) {
            UMLog.putAuth();
            if (Config.DEBUG) {
                if (!judgePlatform(context, platform)) {
                    return;
                }
                UrlUtil.getInfoPrint(platform);
            }
            singleton.router.setmContext(context);
            new QueuedWork.DialogThread(context) {
                protected Object doInBackground() {
                    if (UMShareAPI.this.router != null) {
                        UMShareAPI.this.router.getPlatformInfo(context, platform, listener);
                    }
                    return null;
                }
            }.execute();
        } else {
            Log.d("UMerror", "getPlatformInfo activity argument is null");
        }
    }

    public boolean isInstall(Activity context, SHARE_MEDIA platform) {
        if (this.router != null) {
            return this.router.isInstall(context, platform);
        }
        this.router = new SocialRouter(context);
        return this.router.isInstall(context, platform);
    }

    public boolean isAuthorize(Activity context, SHARE_MEDIA platform) {
        if (this.router != null) {
            return this.router.isAuthorize(context, platform);
        }
        this.router = new SocialRouter(context);

        return this.router.isAuthorize(context, platform);
    }

    public boolean isSupport(Activity context, SHARE_MEDIA platform) {
        if (this.router != null) {
            return this.router.isSupport(context, platform);
        }
        this.router = new SocialRouter(context);

        return this.router.isSupport(context, platform);
    }

    public String getversion(Activity context, SHARE_MEDIA platform) {
        if (this.router != null) {
            return this.router.getSDKVersion(context, platform);
        }
        this.router = new SocialRouter(context);
        return this.router.getSDKVersion(context, platform);
    }


    public void doShare(Activity activity, final ShareAction share, final UMShareListener listener) {
        UMLog.putShare();
        final WeakReference<Activity> weakReference = new WeakReference(activity);
        if (Config.DEBUG) {
            if (!judgePlatform(activity, share.getPlatform())) {
                return;
            }
            UrlUtil.sharePrint(share.getPlatform());
        }

        if ((weakReference.get() != null) && (!((Activity) weakReference.get()).isFinishing())) {
            singleton.router.setmContext(activity);
            new QueuedWork.DialogThread((Context) weakReference.get()) {
                protected Void doInBackground() {
                    if ((weakReference.get() == null) || (((Activity) weakReference.get()).isFinishing())) {
                        return null;
                    }

                    if (UMShareAPI.this.router != null) {
                        UMShareAPI.this.router.share((Activity) weakReference.get(), share, listener);
                    } else {
                        UMShareAPI.this.router = new SocialRouter((Context) weakReference.get());
                        UMShareAPI.this.router.share((Activity) weakReference.get(), share, listener);
                    }
                    return null;
                }
            }.execute();
        } else {
            Log.d("UMerror", "Share activity is null");
        }
    }

    private boolean judgePlatform(Activity activity, SHARE_MEDIA share_media) {
        Method[] bfs = activity.getClass().getDeclaredMethods();
        boolean isHave = false;
        for (Method bm : bfs) {
            if (bm.getName().equals("onActivityResult")) {
                isHave = true;
            }
        }

        if (!isHave) {
            Log.url("您的activity中没有重写onActivityResult方法", "https://at.umeng.com/CCiOHv?cid=476");
        }
        if (share_media == SHARE_MEDIA.QQ) {
            String result = UmengTool.checkQQByself(activity);
            if (result.contains("没有")) {
                if (result.contains("没有在AndroidManifest.xml中检测到")) {
                    UmengTool.showDialogWithURl(activity, result, "https://at.umeng.com/iqmK1D?cid=476");
                } else if (result.contains("android.permission.WRITE_EXTERNAL_STORAGE")) {
                    UmengTool.showDialogWithURl(activity, result, "https://at.umeng.com/19HTvC?cid=476");
                } else if (result.contains("qq应用id")) {
                    UmengTool.showDialogWithURl(activity, result, "https://at.umeng.com/WT95za?cid=476");
                } else if (result.contains("qq的id配置")) {
                    UmengTool.showDialogWithURl(activity, result, "https://at.umeng.com/8Tfmei?cid=476");
                } else {
                    UmengTool.showDialog(activity, result);
                }
                return false;
            }
            Log.um(UmengTool.checkQQByself(activity));
            return true;
        }

        if (share_media == SHARE_MEDIA.WEIXIN) {
            String result = UmengTool.checkWxBySelf(activity);
            if (result.contains("不正确")) {
                if (result.contains("WXEntryActivity配置不正确")) {
                    UmengTool.showDialogWithURl(activity, result, "https://at.umeng.com/9D49bu?cid=476");
                } else {
                    UmengTool.showDialog(activity, result);
                }
                UmengTool.checkWx(activity);
                return false;
            }
            Log.um(UmengTool.checkWxBySelf(activity));
            return true;
        }

        if (share_media == SHARE_MEDIA.SINA) {
            if (UmengTool.checkSinaBySelf(activity).contains("不正确")) {
                UmengTool.checkSina(activity);
                return false;
            }
            Log.um(UmengTool.checkSinaBySelf(activity));
            return true;
        }

        if (share_media == SHARE_MEDIA.FACEBOOK) {
            if (UmengTool.checkFBByself(activity).contains("没有")) {
                UmengTool.checkFacebook(activity);
                return false;
            }
            Log.um(UmengTool.checkFBByself(activity));
            return true;
        }


        if (share_media == SHARE_MEDIA.VKONTAKTE) {
            Log.um(UmengTool.checkVKByself(activity));
        }
        if (share_media == SHARE_MEDIA.LINKEDIN) {
            Log.um(UmengTool.checkLinkin(activity));
        }
        if (share_media == SHARE_MEDIA.KAKAO) {
            Log.um(UmengTool.checkKakao(activity));
        }

        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (this.router != null) {
            this.router.onActivityResult(requestCode, resultCode, data);
        } else {
            Log.v("auth fail", "router=null");
        }
        Log.um("onActivityResult =" + requestCode + "  resultCode=" + resultCode);
    }


    public UMSSOHandler getHandler(SHARE_MEDIA name) {
        if (this.router != null) {
            return this.router.getHandler(name);
        }
        return null;
    }


    private static class InitThread extends QueuedWork.UMAsyncTask<Void> {
        private Context mContext;

        private boolean isToday = false;

        public InitThread(Context context) {
            this.mContext = context;
            String umId = SocializeSpUtils.getUMId(context);
            if (!TextUtils.isEmpty(umId)) {
                Config.UID = umId;
            }
            String umEk = SocializeSpUtils.getUMEk(context);
            if (!TextUtils.isEmpty(umEk)) {
                Config.EntityKey = umEk;
            }
            long lastTime = SocializeSpUtils.getTime(context);
            this.isToday = SocializeUtils.isToday(lastTime);
        }

        protected Void doInBackground() {
            boolean isNewInstall = isNewInstall();
            Log.y("----sdkversion:6.4.6---\n 如有任何错误，请开启debug模式:在设置各平台APPID的地方添加代码：Config.DEBUG = true\n所有编译问题或者设置问题，请参考文档：https://at.umeng.com/0fqeCy?cid=476");
            if ((TextUtils.isEmpty(Config.EntityKey)) || (TextUtils.isEmpty(Config.UID)) || (!this.isToday)) {
                ActionBarRequest request = new ActionBarRequest(this.mContext, isNewInstall);
                ActionBarResponse response = RestAPI.queryShareId(request);

                if ((response != null) && (response.isOk())) {
                    setInstalled();
                    Config.EntityKey = response.mEntityKey;
                    Config.SessionId = response.mSid;
                    Config.UID = response.mUid;

                    SocializeSpUtils.putUMId(this.mContext, Config.UID);
                    SocializeSpUtils.putUMEk(this.mContext, Config.EntityKey);
                    SocializeSpUtils.putTime(this.mContext);
                }
            }

            return null;
        }

        private boolean isNewInstall() {
            SharedPreferences sp = this.mContext.getSharedPreferences("umeng_socialize", 0);

            return sp.getBoolean("newinstall", false);
        }

        public void setInstalled() {
            SharedPreferences sp = this.mContext.getSharedPreferences("umeng_socialize", 0);

            Editor ed = sp.edit();
            ed.putBoolean("newinstall", true);
            ed.commit();
        }
    }


    public void release() {
        this.router.release();
    }

    public void onSaveInstanceState(Bundle bundle) {
        this.router.onSaveInstanceState(bundle);
    }

    public void fetchAuthResultWithBundle(Activity context, Bundle bundle, UMAuthListener listener) {
        this.router.fetchAuthResultWithBundle(context, bundle, listener);
    }


    public void setShareConfig(UMShareConfig config) {
        this.router.setShareConfig(config);
    }
}