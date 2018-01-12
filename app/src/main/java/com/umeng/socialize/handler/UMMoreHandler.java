package com.umeng.socialize.handler;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareContent;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.utils.SocializeUtils;


public class UMMoreHandler extends UMSSOHandler {
    private Activity mAct;

    public void onCreate(Context context, PlatformConfig.Platform p) {
        super.onCreate(context, p);
        this.mAct = ((Activity) context);
    }

    public boolean share(ShareContent content, UMShareListener listener) {
        Intent share_intent = new Intent();
        share_intent.setAction("android.intent.action.SEND");

        if ((content.mMedia != null) && ((content.mMedia instanceof UMImage))) {
            share_intent.setType("image/*");
            UMImage image = (UMImage) content.mMedia;
            if (image.asFileImage().getPath() != null) {
                Uri imgUri = SocializeUtils.insertImage(getContext(), image.asFileImage().getPath());
                share_intent.putExtra("android.intent.extra.STREAM", imgUri);
            }
        } else {
            share_intent.setType("text/plain");
        }

        share_intent.putExtra("android.intent.extra.SUBJECT", content.subject);
        share_intent.putExtra("android.intent.extra.TEXT", content.mText);

        share_intent = Intent.createChooser(share_intent, Config.MORE_TITLE);
        share_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            if ((this.mAct != null) && (!this.mAct.isFinishing())) {
                this.mAct.startActivity(share_intent);
            }
            listener.onResult(SHARE_MEDIA.MORE);
        } catch (Exception e) {
            listener.onError(SHARE_MEDIA.MORE, e);
        }
        return true;
    }

    public void release() {
        super.release();
        this.mAct = null;
    }
}