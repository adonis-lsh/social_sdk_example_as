//
// Source code recreated from BitmapUtil .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.umeng.social.tool;

import android.graphics.Bitmap;

public class UMImageMark extends UMWaterMark {
    private Bitmap mMarkBitmap;

    public UMImageMark() {
    }

    public void setMarkBitmap(Bitmap markBitmap) {
        this.mMarkBitmap = markBitmap;
    }

    Bitmap getMarkBitmap() {
        return this.mMarkBitmap;
    }
}
