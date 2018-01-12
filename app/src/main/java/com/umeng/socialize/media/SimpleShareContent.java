//
// Source code recreated from BitmapUtil .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.umeng.socialize.media;

import android.text.TextUtils;

import com.umeng.socialize.ShareContent;
import com.umeng.socialize.cache.util.BitmapUtil;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.UmengText;

import java.io.File;

public class SimpleShareContent {
    private UMImage uMImage;
    private String mShareContent;
    private UMVideo mUMVideo;
    private UMEmoji mUMEmoji;
    private UMusic mUMusic;
    private UMMin mUMMin;
    private UMWeb mUMWeb;
    private File mFile;
    private BaseMediaObject mBaseMediaObject;
    private int shareType;
    private String strStyle;
    private String subject;
    public final int MINAPP_LIMIT = 122880;
    public final int THUMB_LIMIT = 24576;
    public final int WX_THUMB_LIMIT = 18432;
    public final int IMAGE_LIMIT = 491520;
    public final String DEFAULT_TITLE = "这里是标题";
    public final String DEFAULT_DESCRIPTION = "这里是描述";

    public SimpleShareContent(ShareContent shareContent) {
        this.mShareContent = shareContent.mText;
        if (shareContent.mMedia != null && shareContent.mMedia instanceof UMImage) {
            this.uMImage = (UMImage) shareContent.mMedia;
            this.mBaseMediaObject = this.uMImage;
        }

        if (shareContent.mMedia != null && shareContent.mMedia instanceof UMusic) {
            this.mUMusic = (UMusic) shareContent.mMedia;
            this.mBaseMediaObject = this.mUMusic;
        }

        if (shareContent.mMedia != null && shareContent.mMedia instanceof UMVideo) {
            this.mUMVideo = (UMVideo) shareContent.mMedia;
            this.mBaseMediaObject = this.mUMVideo;
        }

        if (shareContent.mMedia != null && shareContent.mMedia instanceof UMEmoji) {
            this.mUMEmoji = (UMEmoji) shareContent.mMedia;
            this.mBaseMediaObject = this.mUMEmoji;
        }

        if (shareContent.mMedia != null && shareContent.mMedia instanceof UMWeb) {
            this.mUMWeb = (UMWeb) shareContent.mMedia;
            this.mBaseMediaObject = this.mUMWeb;
        }

        if (shareContent.mMedia != null && shareContent.mMedia instanceof UMMin) {
            this.mUMMin = (UMMin) shareContent.mMedia;
            this.mBaseMediaObject = this.mUMWeb;
        }

        if (shareContent.file != null) {
            this.mFile = shareContent.file;
        }

        this.subject = shareContent.subject;
        this.shareType = shareContent.getShareType();
        this.strStyle = this.a();
    }

    private String a() {
        switch (this.shareType) {
            case 1:
                return "text";
            case 2:
                return "image";
            case 3:
                return "textandimage";
            case 4:
                return "music";
            case 8:
                return "video";
            case 16:
                return "web";
            case 32:
                return "file";
            case 64:
                return "emoji";
            case 128:
                return "minapp";
            default:
                return "error";
        }
    }

    public File getFile() {
        return this.mFile;
    }

    public UMEmoji getUmEmoji() {
        return this.mUMEmoji;
    }

    public BaseMediaObject getBaseMediaObject() {
        return this.mBaseMediaObject;
    }

    public String getSubject() {
        return this.subject;
    }

    public String getAssertSubject() {
        return TextUtils.isEmpty(this.subject) ? "umengshare" : this.subject;
    }

    public String getStrStyle() {
        return this.strStyle;
    }

    public int getmStyle() {
        return this.shareType;
    }

    public UMWeb getUmWeb() {
        return this.mUMWeb;
    }

    public UMMin getUmMin() {
        return this.mUMMin;
    }

    public void setText(String var1) {
        this.mShareContent = var1;
    }

    public String getText() {
        return this.mShareContent;
    }

    public void setImage(UMImage var1) {
        this.uMImage = var1;
    }

    public UMImage getImage() {
        return this.uMImage;
    }

    public void setMusic(UMusic var1) {
        this.mUMusic = var1;
    }

    public UMusic getMusic() {
        return this.mUMusic;
    }

    public void setVideo(UMVideo var1) {
        this.mUMVideo = var1;
    }

    public UMVideo getVideo() {
        return this.mUMVideo;
    }

    public String objectSetTitle(BaseMediaObject baseMediaObject) {
        if (TextUtils.isEmpty(baseMediaObject.getTitle())) {
            return "DEFAULT_TITLE";
        } else {
            String title = baseMediaObject.getTitle();
            if (title.length() > 512) {
                title = title.substring(0, 512);
            }

            return title;
        }
    }

    public String objectSetDescription(BaseMediaObject var1) {
        if (TextUtils.isEmpty(var1.getDescription())) {
            return "这里是描述";
        } else {
            String description = var1.getDescription();
            if (description.length() > 1024) {
                description = description.substring(0, 1024);
            }

            return description;
        }
    }

    public String objectSetText(String var1) {
        if (TextUtils.isEmpty(var1)) {
            return "这里是描述";
        } else {
            if (var1.length() > 10240) {
                var1 = var1.substring(0, 10240);
            }

            return var1;
        }
    }

    public byte[] objectSetThumb(BaseMediaObject var1) {
        if (var1.getThumbImage() == null) {
            return null;
        } else {
            Object var2 = null;
            byte[] var3 = BitmapUtil.a(var1.getThumbImage(), THUMB_LIMIT);
            if (var3 == null || var3.length <= 0) {
                Log.um(UmengText.SHARECONTENT_THUMB_ERROR);
            }

            return var3;
        }
    }

    public byte[] objectSetThumbMinApp(BaseMediaObject var1) {
        if (var1.getThumbImage() == null) {
            return null;
        } else {
            Object var2 = null;
            byte[] var3 = BitmapUtil.a(var1.getThumbImage(), MINAPP_LIMIT);
            if (var3 == null || var3.length <= 0) {
                Log.um(UmengText.SHARECONTENT_THUMB_ERROR);
            }

            return var3;
        }
    }

    public String getMusicTargetUrl(UMusic var1) {
        return TextUtils.isEmpty(var1.getmTargetUrl()) ? var1.toUrl() : var1.getmTargetUrl();
    }

    public byte[] getImageThumb(UMImage var1) {
        Object var2;
        byte[] var3;
        if (var1.getThumbImage() != null) {
            var2 = null;
            var3 = BitmapUtil.a(var1.getThumbImage(), WX_THUMB_LIMIT);
            if (var3 == null || var3.length <= 0) {
                Log.um(UmengText.SHARECONTENT_THUMB_ERROR);
            }

            return var3;
        } else {
            var2 = null;
            var3 = BitmapUtil.a(var1, WX_THUMB_LIMIT);
            if (var3 == null || var3.length <= 0) {
                Log.um(UmengText.SHARECONTENT_THUMB_ERROR);
            }

            return var3;
        }
    }

    public byte[] getImageData(UMImage var1) {
        return var1.asBinImage();
    }

    public byte[] getStrictImageData(UMImage var1) {
        if (this.getUMImageScale(var1) > IMAGE_LIMIT) {
            byte[] var2 = BitmapUtil.a(this.getImage(), IMAGE_LIMIT);
            if (var2 != null && var2.length > 0) {
                return var2;
            } else {
                Log.um(UmengText.SHARECONTENT_THUMB_ERROR);
                return null;
            }
        } else {
            return this.getImageData(var1);
        }
    }

    public int getUMImageScale(UMImage var1) {
        return BitmapUtil.a(var1);
    }

    public String subString(String var1, int var2) {
        return TextUtils.isEmpty(var1) && var1.length() > var2 ? var1.substring(0, var2) : var1;
    }

    public boolean canFileValid(UMImage var1) {
        return var1.asFileImage() != null;
    }
}
