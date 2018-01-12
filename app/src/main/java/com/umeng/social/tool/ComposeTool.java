//
// Source code recreated from BitmapUtil .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.umeng.social.tool;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.Layout.Alignment;

public class ComposeTool {
    public static ComposeTool.ComposeDirection direction;
    public static int textColor;
    public static int textsize;
    public static int backgroundColor;
    public static Typeface typeface;

    public ComposeTool() {
    }

    public static Bitmap createCompose(Bitmap src1, Bitmap src2, boolean isvertical, int space) {
        if (src1 == null) {
            return null;
        } else if (src2 == null) {
            return null;
        } else {
            int w = src1.getWidth();
            int h = src1.getHeight();
            int ww = src2.getWidth();
            int wh = src2.getHeight();
            int width = isvertical ? Math.max(w, ww) : w + ww + space;
            int height = isvertical ? h + wh + space : Math.max(h, wh);
            Bitmap newb = Bitmap.createBitmap(width, height, Config.ARGB_8888);
            Canvas cv = new Canvas(newb);
            cv.drawBitmap(src1, 0.0F, 0.0F, (Paint) null);
            if (isvertical) {
                cv.drawBitmap(src2, 0.0F, (float) (h + space), (Paint) null);
            } else {
                cv.drawBitmap(src2, (float) (w + space), 0.0F, (Paint) null);
            }

            cv.save(31);
            cv.restore();
            return newb;
        }
    }

    public static Bitmap createWaterMask(Bitmap src, Bitmap watermark, int x, int y) {
        if (src == null) {
            return null;
        } else {
            int w = src.getWidth();
            int h = src.getHeight();
            int ww = watermark.getWidth();
            int wh = watermark.getHeight();
            int wc = w / 2 - ww / 2;
            int hc = h / 2 - wh / 2;
            Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);
            Canvas cv = new Canvas(newb);
            cv.drawBitmap(src, 0.0F, 0.0F, (Paint) null);
            if (direction == ComposeTool.ComposeDirection.CUSTOM) {
                cv.drawBitmap(watermark, (float) x, (float) y, (Paint) null);
            } else if (direction == ComposeTool.ComposeDirection.UP) {
                cv.drawBitmap(watermark, (float) wc, 0.0F, (Paint) null);
            } else if (direction == ComposeTool.ComposeDirection.DOWN) {
                cv.drawBitmap(watermark, (float) wc, (float) (h - wh), (Paint) null);
            } else if (direction == ComposeTool.ComposeDirection.LEFT) {
                cv.drawBitmap(watermark, 0.0F, (float) hc, (Paint) null);
            } else if (direction == ComposeTool.ComposeDirection.RIGHT) {
                cv.drawBitmap(watermark, (float) (w - ww), (float) hc, (Paint) null);
            } else if (direction == ComposeTool.ComposeDirection.LEFTUP) {
                cv.drawBitmap(watermark, 0.0F, 0.0F, (Paint) null);
            } else if (direction == ComposeTool.ComposeDirection.LEFTDOWN) {
                cv.drawBitmap(watermark, 0.0F, (float) (h - wh), (Paint) null);
            } else if (direction == ComposeTool.ComposeDirection.RIGHTUP) {
                cv.drawBitmap(watermark, (float) (w - ww), 0.0F, (Paint) null);
            } else if (direction == ComposeTool.ComposeDirection.RIGHTDOWN) {
                cv.drawBitmap(watermark, (float) (w - ww), (float) (h - wh), (Paint) null);
            }

            cv.save(31);
            cv.restore();
            return newb;
        }
    }

    public static Bitmap createTextImage(String s, Bitmap bitmap, int widthspace, int heightspace) {
        Config bitmapConfig = bitmap.getConfig();
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        if (bitmapConfig == null) {
            bitmapConfig = Config.ARGB_8888;
        }

        TextPaint paint = new TextPaint(1);
        paint.setColor(textColor);
        paint.setTextSize((float) textsize);
        paint.setDither(true);
        paint.setFilterBitmap(true);
        paint.setTypeface(typeface);
        StaticLayout layout = new StaticLayout(s, paint, w, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
        Rect bounds = new Rect();
        paint.getTextBounds(s, 0, s.length(), bounds);
        bitmap = bitmap.copy(bitmapConfig, true);
        Bitmap newb = Bitmap.createBitmap(w + widthspace * 2, h + layout.getHeight() + heightspace * 4, Config.ARGB_8888);
        Canvas canvas = new Canvas(newb);
        canvas.drawColor(backgroundColor);
        canvas.drawBitmap(bitmap, (float) widthspace, (float) (layout.getHeight() + heightspace * 3), (Paint) null);
        canvas.translate((float) widthspace, (float) heightspace);
        layout.draw(canvas);
        canvas.save(31);
        canvas.restore();
        return newb;
    }

    static {
        direction = ComposeTool.ComposeDirection.CUSTOM;
        textColor = -16777216;
        textsize = 18;
        backgroundColor = -1;
        typeface = Typeface.DEFAULT;
    }

    public static enum ComposeDirection {
        UP,
        DOWN,
        LEFT,
        RIGHT,
        LEFTUP,
        LEFTDOWN,
        RIGHTUP,
        RIGHTDOWN,
        CUSTOM;

        private ComposeDirection() {
        }
    }
}
