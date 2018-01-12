//
// Source code recreated from BitmapUtil .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.umeng.socialize.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Build.VERSION;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.common.ResContainer;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.SocializeUtils;
import com.umeng.socialize.utils.UmengText;

import java.lang.reflect.Method;

public abstract class BaseDialog extends Dialog {
    public final ResContainer R;
    public WebView mWebView;
    public View mContent;
    public View mProgressbar;
    public int mFlag = 0;
    public Bundle mValues;
    public String mWaitUrl = "error";
    public TextView titleMidTv;
    public Context mContext;
    public Activity mActivity;
    public SHARE_MEDIA mPlatform;
    public Handler mHandler = new Handler() {
        public void handleMessage(Message var1) {
            super.handleMessage(var1);
            if (var1.what == 1 && BaseDialog.this.mProgressbar != null) {
                BaseDialog.this.mProgressbar.setVisibility(8);
            }

            if (var1.what == 2) {
                ;
            }

        }
    };

    public BaseDialog(Activity var1, SHARE_MEDIA var2) {
        super(var1, ResContainer.get(var1).style("umeng_socialize_popup_dialog"));
        this.mContext = var1.getApplicationContext();
        this.R = ResContainer.get(this.mContext);
        this.mActivity = var1;
        this.mPlatform = var2;
    }

    public void setWaitUrl(String var1) {
        this.mWaitUrl = var1;
    }

    public void initViews() {
        this.setOwnerActivity(this.mActivity);
        LayoutInflater var1 = (LayoutInflater) this.mActivity.getSystemService("layout_inflater");
        int var2 = this.R.layout("umeng_socialize_oauth_dialog");
        int var3 = this.R.id("umeng_socialize_follow");
        this.mContent = var1.inflate(var2, (ViewGroup) null);
        final View var4 = this.mContent.findViewById(var3);
        var4.setVisibility(8);
        int var5 = this.R.id("progress_bar_parent");
        int var6 = this.R.id("umeng_back");
        int var7 = this.R.id("umeng_share_btn");
        int var8 = this.R.id("umeng_title");
        int var9 = this.R.id("umeng_socialize_titlebar");
        this.mProgressbar = this.mContent.findViewById(var5);
        this.mProgressbar.setVisibility(0);
        RelativeLayout var10 = (RelativeLayout) this.mContent.findViewById(var6);
        var10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseDialog.this.dismiss();
            }
        });
        this.mContent.findViewById(var7).setVisibility(8);
        this.titleMidTv = (TextView) this.mContent.findViewById(var8);
        String var11 = null;
        if (this.mPlatform.toString().equals("SINA")) {
            var11 = UmengText.SINA;
        } else if (this.mPlatform.toString().equals("RENREN")) {
            var11 = UmengText.RENREN;
        } else if (this.mPlatform.toString().equals("DOUBAN")) {
            var11 = UmengText.DOUBAN;
        } else if (this.mPlatform.toString().equals("TENCENT")) {
            var11 = UmengText.TENCENT;
        }

        this.titleMidTv.setText("授权" + var11);
        this.setUpWebView();
        final View var12 = this.mContent.findViewById(var9);
        final int var13 = SocializeUtils.dip2Px(this.mContext, 200.0F);
        FrameLayout var14 = new FrameLayout(this.mContext) {
            protected void onSizeChanged(int var1, int var2, int var3, int var4x) {
                super.onSizeChanged(var1, var2, var3, var4x);
                if (!SocializeUtils.isFloatWindowStyle(BaseDialog.this.mContext)) {
                    this.a(var4, var12, var13, var2);
                }

            }

            private void a(final View var1, final View var2, int var3, int var4x) {
                if (var2.getVisibility() == 0 && var4x < var3) {
                    BaseDialog.this.mHandler.post(new Runnable() {
                        public void run() {
                            var2.setVisibility(8);
                            if (var1.getVisibility() == 0) {
                                var1.setVisibility(8);
                            }

                            requestLayout();
                        }
                    });
                } else if (var2.getVisibility() != 0 && var4x >= var3) {
                    BaseDialog.this.mHandler.post(new Runnable() {
                        public void run() {
                            var2.setVisibility(0);
                            requestLayout();
                        }
                    });
                }

            }
        };
        var14.addView(this.mContent, -1, -1);
        this.setContentView(var14);
        LayoutParams var15 = this.getWindow().getAttributes();
        if (SocializeUtils.isFloatWindowStyle(this.mContext)) {
            int[] var16 = SocializeUtils.getFloatWindowSize(this.mContext);
            var15.width = var16[0];
            var15.height = var16[1];
        } else {
            var15.height = -1;
            var15.width = -1;
        }

        var15.gravity = 17;
    }

    public boolean setUpWebView() {
        this.mWebView = (WebView) this.mContent.findViewById(this.R.id("webView"));
        this.setClient(this.mWebView);
        this.mWebView.requestFocusFromTouch();
        this.mWebView.setVerticalScrollBarEnabled(false);
        this.mWebView.setHorizontalScrollBarEnabled(false);
        this.mWebView.setScrollBarStyle(0);
        this.mWebView.getSettings().setCacheMode(2);
        this.mWebView.setBackgroundColor(-1);
        if (VERSION.SDK_INT >= 11) {
            this.mWebView.removeJavascriptInterface("searchBoxJavaBridge_");
            this.mWebView.removeJavascriptInterface("accessibility");
            this.mWebView.removeJavascriptInterface("accessibilityTraversal");
        }

        WebSettings var1 = this.mWebView.getSettings();
        var1.setJavaScriptEnabled(true);
        if (VERSION.SDK_INT >= 8) {
            var1.setPluginState(PluginState.ON);
        }

        var1.setSupportZoom(true);
        var1.setBuiltInZoomControls(true);
        var1.setAllowFileAccess(true);
        var1.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
        var1.setUseWideViewPort(true);
        if (VERSION.SDK_INT >= 8) {
            var1.setLoadWithOverviewMode(true);
            var1.setDatabaseEnabled(true);
            var1.setDomStorageEnabled(true);
            var1.setGeolocationEnabled(true);
            var1.setAppCacheEnabled(true);
        }

        if (VERSION.SDK_INT >= 11) {
            try {
                Method var2 = WebSettings.class.getDeclaredMethod("setDisplayZoomControls", new Class[]{Boolean.TYPE});
                var2.setAccessible(true);
                var2.invoke(var1, new Object[]{Boolean.valueOf(false)});
            } catch (Exception var5) {
                Log.um(var5.getMessage());
            }
        }

        try {
            if (this.mPlatform == SHARE_MEDIA.RENREN) {
                CookieSyncManager.createInstance(this.mContext);
                CookieManager var6 = CookieManager.getInstance();
                var6.removeAllCookie();
            }
        } catch (Exception var4) {
            ;
        }

        return true;
    }

    public abstract void setClient(WebView var1);

    public boolean onKeyDown(int var1, KeyEvent var2) {
        return super.onKeyDown(var1, var2);
    }

    public void releaseWebView() {
        try {
            ((ViewGroup) this.mWebView.getParent()).removeView(this.mWebView);
        } catch (Exception var3) {
            ;
        }

        try {
            this.mWebView.removeAllViews();
        } catch (Exception var2) {
            ;
        }

        this.mWebView = null;
    }
}
