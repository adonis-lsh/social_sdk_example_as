package com.umeng.socialize.media;

import android.content.Context;
import android.graphics.Bitmap;

import com.umeng.social.tool.UMImageMark;

import java.io.File;


public class UMEmoji
        extends UMImage {
    public UMEmoji(Context paramContext, File paramFile) {
        super(paramContext, paramFile);
    }

    public UMEmoji(Context paramContext, String paramString) {
        super(paramContext, paramString);
    }

    public UMEmoji(Context paramContext, int paramInt) {
        super(paramContext, paramInt);
    }

    public UMEmoji(Context paramContext, byte[] paramArrayOfByte) {
        super(paramContext, paramArrayOfByte);
    }

    public UMEmoji(Context paramContext, Bitmap paramBitmap) {
        super(paramContext, paramBitmap);
    }

    public UMEmoji(Context paramContext, Bitmap paramBitmap, UMImageMark paramUMImageMark) {
        super(paramContext, paramBitmap, paramUMImageMark);
    }

    public UMEmoji(Context paramContext, int paramInt, UMImageMark paramUMImageMark) {
        super(paramContext, paramInt, paramUMImageMark);
    }

    public UMEmoji(Context paramContext, byte[] paramArrayOfByte, UMImageMark paramUMImageMark) {
        super(paramContext, paramArrayOfByte, paramUMImageMark);
    }
}
