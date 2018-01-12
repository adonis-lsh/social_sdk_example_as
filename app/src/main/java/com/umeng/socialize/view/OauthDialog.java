//
// Source code recreated from BitmapUtil .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.umeng.socialize.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.umeng.socialize.Config;
import com.umeng.socialize.SocializeException;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.UmengErrorCode;
import com.umeng.socialize.net.utils.AesHelper;
import com.umeng.socialize.net.utils.SocializeNetUtils;
import com.umeng.socialize.utils.DeviceConfig;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.SocializeUtils;
import com.umeng.socialize.utils.URLBuilder;
import com.umeng.socialize.utils.UmengText;

import java.lang.ref.WeakReference;

public class OauthDialog extends BaseDialog {
    private static final String TAG = "OauthDialog";
    private static final String BASE_URL = "https://log.umsns.com/";
    private OauthDialog.a mListener;
    private static String mRedirectUri = "error";

    public void setmRedirectUri(String var1) {
        mRedirectUri = var1;
    }

    public OauthDialog(Activity var1, SHARE_MEDIA var2, UMAuthListener var3) {
        super(var1, var2);
        this.mListener = new OauthDialog.a(var3, var2);
        this.initViews();
    }

    private String getUrl(SHARE_MEDIA var1) {
        URLBuilder var2 = new URLBuilder(this.mContext);
        var2.setHost("https://log.umsns.com/").setPath("share/auth/").setAppkey(SocializeUtils.getAppkey(this.mContext)).setEntityKey(Config.EntityKey).withMedia(var1).withOpId("10").withSessionId(Config.SessionId).withUID(Config.UID);
        return var2.toEncript();
    }

    public void setClient(WebView var1) {
        var1.setWebViewClient(this.getAdapterWebViewClient());
        OauthDialog.b var2 = new OauthDialog.b(this);
        this.mWebView.setWebChromeClient(var2);
    }

    private WebViewClient getAdapterWebViewClient() {
        return new OauthDialog.c(this);
    }

    private String decrypt(String var1) {
        try {
            String[] var2 = var1.split("ud_get=");
            var2[1] = AesHelper.decryptNoPadding(var2[1], "UTF-8").trim();
            var1 = var2[0] + var2[1];
        } catch (Exception var3) {
            Log.um(UmengText.DECRPT_ERROR);
            var3.printStackTrace();
        }

        return var1;
    }

    public void show() {
        super.show();
        this.mValues = null;
        if (this.mPlatform == SHARE_MEDIA.SINA) {
            this.mWebView.loadUrl(this.mWaitUrl);
        } else {
            String var1 = this.getUrl(this.mPlatform);
            this.mWebView.loadUrl(var1);
        }

    }

    public void dismiss() {
        if (this.mValues != null) {
            String var1 = this.mValues.getString("uid");
            String var2 = this.mValues.getString("error_code");
            String var3 = this.mValues.getString("error_description");
            if (this.mPlatform == SHARE_MEDIA.SINA && !TextUtils.isEmpty(var3)) {
                this.mListener.a((Exception) (new SocializeException(UmengErrorCode.AuthorizeFailed.getMessage() + "errorcode:" + var2 + " message:" + var3)));
            } else if (TextUtils.isEmpty(var1)) {
                this.mListener.a((Exception) (new SocializeException(UmengErrorCode.AuthorizeFailed.getMessage() + "unfetch usid...")));
            } else {
                Log.d("OauthDialog", "### dismiss ");
                this.mValues.putString("accessToken", this.mValues.getString("access_key"));
                this.mValues.putString("expiration", this.mValues.getString("expires_in"));
                this.mListener.a(this.mValues);
            }
        } else {
            this.mListener.onCancel();
        }

        super.dismiss();
        this.releaseWebView();
    }

    static class a {
        private UMAuthListener a = null;
        private SHARE_MEDIA b;
        private int c;

        public a(UMAuthListener var1, SHARE_MEDIA var2) {
            this.a = var1;
            this.b = var2;
        }

        public void a(Exception var1) {
            if (this.a != null) {
                this.a.onError(this.b, this.c, var1);
            }

        }

        public void a(Bundle var1) {
            if (this.a != null) {
                this.a.onComplete(this.b, this.c, SocializeUtils.bundleTomap(var1));
            }

        }

        public void onCancel() {
            if (this.a != null) {
                this.a.onCancel(this.b, this.c);
            }

        }
    }

    private static class c extends WebViewClient {
        private WeakReference<OauthDialog> a;

        private c(OauthDialog var1) {
            this.a = new WeakReference(var1);
        }

        public boolean shouldOverrideUrlLoading(WebView var1, String var2) {
            OauthDialog var3 = this.a == null ? null : (OauthDialog) this.a.get();
            if (var3 != null) {
                Context var4 = var3.mContext.getApplicationContext();
                if (!DeviceConfig.isNetworkAvailable(var4)) {
                    Toast.makeText(var4, UmengText.NET_INAVALIBLE, 0).show();
                    return true;
                }

                if (var2.contains("?ud_get=")) {
                    var2 = var3.decrypt(var2);
                }

                if (var2.contains(var3.mWaitUrl)) {
                    this.a(var2);
                }
            }

            return super.shouldOverrideUrlLoading(var1, var2);
        }

        public void onReceivedError(WebView var1, int var2, String var3, String var4) {
            Log.e("OauthDialog", "onReceivedError: " + var4 + "\nerrCode: " + var2 + " description:" + var3);
            OauthDialog var5 = this.a == null ? null : (OauthDialog) this.a.get();
            if (var5 != null) {
                View var6 = var5.mProgressbar;
                if (var6.getVisibility() == 0) {
                    var6.setVisibility(8);
                }
            }

            super.onReceivedError(var1, var2, var3, var4);
            if (var5 != null) {
                SocializeUtils.safeCloseDialog(var5);
            }

        }

        public void onReceivedSslError(WebView var1, SslErrorHandler var2, SslError var3) {
            var2.cancel();
        }

        public void onPageStarted(WebView var1, String var2, Bitmap var3) {
            OauthDialog var4 = this.a == null ? null : (OauthDialog) this.a.get();
            if (var4 != null) {
                String var5 = "";
                if (var2.contains("?ud_get=")) {
                    var5 = var4.decrypt(var2);
                }

                if (var5.contains("access_key") && var5.contains("access_secret")) {
                    if (var2.contains(var4.mWaitUrl)) {
                        this.a(var2);
                    }

                    return;
                }

                if (var2.startsWith(OauthDialog.mRedirectUri)) {
                    this.b(var2);
                }
            }

            super.onPageStarted(var1, var2, var3);
        }

        public void onPageFinished(WebView var1, String var2) {
            OauthDialog var3 = this.a == null ? null : (OauthDialog) this.a.get();
            if (var3 != null) {
                var3.mHandler.sendEmptyMessage(1);
                super.onPageFinished(var1, var2);
                if (var3.mFlag == 0 && var2.contains(var3.mWaitUrl)) {
                    this.a(var2);
                }
            }

        }

        private void a(String var1) {
            OauthDialog var2 = this.a == null ? null : (OauthDialog) this.a.get();
            if (var2 != null) {
                var2.mFlag = 1;
                var2.mValues = SocializeUtils.parseUrl(var1);
                if (var2.isShowing()) {
                    SocializeUtils.safeCloseDialog(var2);
                }
            }

        }

        private void b(String var1) {
            Log.d("OauthDialog", "OauthDialog" + var1);
            Log.e("gggggg url=" + var1);
            OauthDialog var2 = this.a == null ? null : (OauthDialog) this.a.get();
            if (var2 != null) {
                var2.mFlag = 1;
                var2.mValues = SocializeNetUtils.parseUrl(var1);
                if (var2.isShowing()) {
                    SocializeUtils.safeCloseDialog(var2);
                }
            }

        }
    }

    private static class b extends WebChromeClient {
        private WeakReference<OauthDialog> a;

        private b(OauthDialog var1) {
            this.a = new WeakReference(var1);
        }

        public void onProgressChanged(WebView var1, int var2) {
            super.onProgressChanged(var1, var2);
            OauthDialog var3 = this.a == null ? null : (OauthDialog) this.a.get();
            if (var3 != null) {
                if (var2 < 90) {
                    var3.mProgressbar.setVisibility(0);
                } else {
                    var3.mHandler.sendEmptyMessage(1);
                }
            }

        }
    }
}
