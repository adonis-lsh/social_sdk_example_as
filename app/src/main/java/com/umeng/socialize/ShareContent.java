package com.umeng.socialize;

import android.text.TextUtils;

import com.umeng.socialize.media.UMEmoji;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMMin;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.media.UMediaObject;
import com.umeng.socialize.media.UMusic;

import java.io.File;


public class ShareContent {
    public String subject = "";

    public String mText = "";


    public UMediaObject mMedia;


    public UMediaObject mExtra;


    public String mFollow;


    public File file;


    public File app;


    public static final int TEXT_STYLE = 1;


    public static final int IMAGE_STYLE = 2;


    public static final int TEXT_IMAGE_STYLE = 3;


    public static final int MUSIC_STYLE = 4;


    public static final int VIDEO_STYLE = 8;


    public static final int WEB_STYLE = 16;


    public static final int FILE_STYLE = 32;


    public static final int EMOJI_STYLE = 64;


    public static final int MINAPP_STYLE = 128;


    public static final int ERROR_STYLE = 0;


    public int getShareType() {
        if ((this.mMedia == null) && (this.mExtra == null) && (this.file == null)) {
            if (TextUtils.isEmpty(this.mText)) {
                return 0;
            }

            return 1;
        }
        if (this.file != null)
            return 32;
        if (this.mMedia != null) {
            if ((this.mMedia instanceof UMEmoji)) {
                return 64;
            }
            if ((this.mMedia instanceof UMImage)) {
                if (TextUtils.isEmpty(this.mText)) {
                    return 2;
                }

                return 3;
            }
            if ((this.mMedia instanceof UMusic))
                return 4;
            if ((this.mMedia instanceof UMVideo)) {
                return 8;
            }
            if ((this.mMedia instanceof UMWeb)) {
                return 16;
            }
            if ((this.mMedia instanceof UMMin)) {
                return 128;
            }
        }
        return 0;
    }
}
