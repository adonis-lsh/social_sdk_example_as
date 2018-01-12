//
// Source code recreated from BitmapUtil .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.umeng.socialize.handler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.umeng.socialize.ShareContent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.PlatformConfig.Platform;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.UmengErrorCode;
import com.umeng.socialize.common.QueuedWork;
import com.umeng.socialize.editorpage.IEditor;
import com.umeng.socialize.net.RestAPI;
import com.umeng.socialize.net.SharePostRequest;
import com.umeng.socialize.net.base.SocializeReseponse;
import com.umeng.socialize.utils.Log;

import java.util.Map;
import java.util.Stack;

public abstract class UMAPIShareHandler extends UMSSOHandler implements IEditor {
    private Stack<UMAPIShareHandler.StatHolder> mStatStack = new Stack();

    public UMAPIShareHandler() {
    }

    public void onCreate(Context context, Platform p) {
        super.onCreate(context, p);
    }

    public abstract String getUID();

    public abstract SHARE_MEDIA getPlatform();

    public abstract void authorizeCallBack(int var1, int var2, Intent var3);

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == this.getRequestCode()) {
            final UMAPIShareHandler.StatHolder holder;
            if (resultCode == 1000) {
                if (!this.mStatStack.isEmpty()) {
                    holder = (UMAPIShareHandler.StatHolder) this.mStatStack.pop();
                    if (holder != null) {
                        holder.Listener.onCancel(this.getPlatform());
                    }
                }
            } else if (data != null && data.hasExtra("txt")) {
                if (!this.mStatStack.empty()) {
                    holder = (UMAPIShareHandler.StatHolder) this.mStatStack.pop();
                    final Bundle extras = data.getExtras();
                    if (resultCode == -1) {
                        QueuedWork.runInBack(new Runnable() {
                            public void run() {
                                ShareContent content = UMAPIShareHandler.this.getResult(holder.Content, extras);
                                UMAPIShareHandler.this.sendShareRequest(content, holder.Listener);
                                Log.d("act", "sent share request");
                            }
                        }, true);
                    } else if (holder.Listener != null) {
                        holder.Listener.onCancel(this.getPlatform());
                    }
                }
            } else {
                this.authorizeCallBack(requestCode, resultCode, data);
            }

        }
    }

    public abstract void deleteAuth();

    public boolean share(final ShareContent content, final UMShareListener listener) {
        if (this.isAuthorize()) {
            this.doShare(content, listener);
        } else {
            this.authorize(new UMAuthListener() {
                public void onStart(SHARE_MEDIA platform) {
                    listener.onStart(platform);
                }

                public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
                    QueuedWork.runInBack(new Runnable() {
                        public void run() {
                            UMAPIShareHandler.this.doShare(content, listener);
                        }
                    }, true);
                }

                public void onError(SHARE_MEDIA platform, int action, Throwable t) {
                    listener.onError(platform, t);
                }

                public void onCancel(SHARE_MEDIA platform, int action) {
                    listener.onCancel(platform);
                }
            });
        }

        return false;
    }

    protected void doShare(ShareContent shareContent, UMShareListener listener) {
        if (this.getShareConfig().isOpenShareEditActivity()) {
            UMAPIShareHandler.StatHolder holder = new UMAPIShareHandler.StatHolder();
            holder.Content = shareContent;
            holder.Listener = listener;
            this.mStatStack.push(holder);
            if (this.mWeakAct.get() != null && !((Activity) this.mWeakAct.get()).isFinishing()) {
                try {
                    Class object = Class.forName("com.umeng.socialize.editorpage.ShareActivity");
                    Intent in = new Intent((Context) this.mWeakAct.get(), object);
                    in.putExtras(this.getEditable(shareContent));
                    ((Activity) this.mWeakAct.get()).startActivityForResult(in, this.getRequestCode());
                } catch (ClassNotFoundException var6) {
                    this.sendShareRequest(shareContent, listener);
                    Log.e("没有加入界面jar");
                    var6.printStackTrace();
                }
            }
        } else {
            this.sendShareRequest(shareContent, listener);
        }

    }

    public void sendShareRequest(final ShareContent shareContent, final UMShareListener listener) {
        final SHARE_MEDIA media = this.getPlatform();
        String platform = media.toString().toLowerCase();
        String usid = this.getUID();
        SharePostRequest request = new SharePostRequest(this.getContext(), platform, usid, shareContent);
        request.setReqType(0);
        final SocializeReseponse resp = RestAPI.doShare(request);
        if (resp == null) {
            QueuedWork.runInMain(new Runnable() {
                public void run() {
                    listener.onError(media, new Throwable(UmengErrorCode.ShareFailed.getMessage() + "response is null"));
                }
            });
        } else if (!resp.isOk()) {
            QueuedWork.runInMain(new Runnable() {
                public void run() {
                    if (resp.mStCode == 5027) {
                        UMAPIShareHandler.this.deleteAuth();
                        UMAPIShareHandler.this.share(shareContent, listener);
                    } else {
                        listener.onError(media, new Throwable(UmengErrorCode.ShareFailed.getMessage() + resp.mMsg));
                    }

                }
            });
        } else {
            QueuedWork.runInMain(new Runnable() {
                public void run() {
                    listener.onResult(media);
                }
            });
        }

    }

    private static class StatHolder {
        public ShareContent Content;
        private UMShareListener Listener;

        private StatHolder() {
        }
    }
}
