//
// Source code recreated from BitmapUtil .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.umeng.social.tool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.util.Log;

class UMWaterMark {
    private static final String TAG = UMWaterMark.class.getSimpleName();
    private float mScale = 0.3F;
    private int mRightMargin;
    private int mLeftMargin;
    private int mTopMargin;
    private int mBottomMargin;
    private Rect mAnchorMarkRect = new Rect();
    private int mVerticalRelativePosition = -1;
    private int mHorizontalRelativePosition = -1;
    static final int RELATIVE_POSITION_VERTICAL_BOTTOM = 1;
    static final int RELATIVE_POSITION_VERTICAL_TOP = 2;
    static final int RELATIVE_POSITION_HORIZONTAL_RIGHT = 3;
    static final int RELATIVE_POSITION_HORIZONTAL_LEFT = 4;
    private int mGravity = 51;
    private boolean mIsTransparent = false;
    private boolean mIsBringToFront = false;
    private float mAlpha = -1.0F;
    private int mDegree = -1;
    private Context mContext;
    private Rect mMeasureRect = new Rect();

    UMWaterMark() {
    }

    public void setMargins(int left, int top, int right, int bottom) {
        this.mLeftMargin = left;
        this.mTopMargin = top;
        this.mRightMargin = right;
        this.mBottomMargin = bottom;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public void setGravity(int gravity) {
        if (gravity > 0) {
            if (this.mGravity != gravity) {
                this.mGravity = gravity;
            }

        }
    }

    public void setScale(float scale) {
        if (scale >= 0.0F && scale <= 1.0F) {
            this.mScale = scale;
        }
    }

    public void setRotate(int degree) {
        if (degree >= 0 && degree <= 360) {
            this.mDegree = degree;
        }
    }

    public void bringToFront() {
        this.mIsBringToFront = true;
    }

    public void setAlpha(float alpha) {
        if (alpha >= 0.0F && alpha <= 1.0F) {
            this.mAlpha = alpha;
        }
    }

    public void setTransparent() {
        this.mIsTransparent = true;
    }

    public Bitmap compound(Bitmap src) {
        try {
            if (src == null) {
                Log.e(TAG, "scr bitmap is null");
                return null;
            } else {
                Bitmap markBitmap = this.getMarkBitmap();
                if (markBitmap == null) {
                    Log.e(TAG, "mark bitmap is null");
                    return src;
                } else {
                    int srcWidth = src.getWidth();
                    int srcHeight = src.getHeight();
                    if (srcWidth > 0 && srcHeight > 0) {
                        int markWidth = this.getMarkWidth();
                        int markHeight = this.getMarkHeight();
                        if (markWidth > 0 && markHeight > 0) {
                            Canvas canvas;
                            Bitmap bitmap;
                            if (this.mIsTransparent) {
                                bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Config.ARGB_8888);
                                canvas = new Canvas(bitmap);
                                canvas.drawColor(0);
                            } else {
                                bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Config.RGB_565);
                                canvas = new Canvas(bitmap);
                            }

                            canvas.drawBitmap(src, 0.0F, 0.0F, (Paint) null);
                            Matrix matrix = new Matrix();
                            int srcMinLength = Math.min(src.getWidth(), src.getHeight());
                            int markMaxLength = Math.max(markWidth, markHeight);
                            float scaleFactor = this.mScale * (float) srcMinLength / (float) markMaxLength;
                            matrix.postScale(scaleFactor, scaleFactor, this.getScaleAnchorX(markWidth), this.getScaleAnchorY(markHeight));
                            if (this.mDegree != -1) {
                                matrix.postRotate((float) this.mDegree, (float) (markWidth / 2), (float) (markHeight / 2));
                            }

                            float dx = this.isHorizontalRelativePosition() ? this.getRelativeDx(srcWidth) : this.getDx(srcWidth);
                            float dy = this.isVerticalRelativePosition() ? this.getRelativeDy(srcHeight) : this.getDy(srcHeight);
                            matrix.postTranslate(dx, dy);
                            if (this.mAlpha != -1.0F) {
                                Paint paint = new Paint();
                                paint.setAlpha((int) (255.0F * this.mAlpha));
                                canvas.drawBitmap(markBitmap, matrix, paint);
                            } else {
                                canvas.drawBitmap(markBitmap, matrix, (Paint) null);
                            }

                            canvas.save(31);
                            canvas.restore();
                            this.safelyRecycleBitmap(src);
                            this.safelyRecycleBitmap(markBitmap);
                            this.releaseResource();
                            return bitmap;
                        } else {
                            Log.e(TAG, "mark bitmap is error, markWidth:" + markWidth + ", markHeight:" + markHeight);
                            return src;
                        }
                    } else {
                        Log.e(TAG, "mark bitmap is error, markWidth:" + srcWidth + ", markHeight:" + srcHeight);
                        return src;
                    }
                }
            }
        } catch (Exception var16) {
            var16.printStackTrace();
            return null;
        }
    }

    private float getScaleAnchorY(int height) {
        int gravity = this.mGravity & 112;
        float y;
        switch (gravity) {
            case 16:
                y = (float) (height / 2);
                break;
            case 48:
            default:
                y = 0.0F;
                break;
            case 80:
                y = (float) height;
        }

        return y;
    }

    private float getScaleAnchorX(int width) {
        int gravity = this.mGravity & 7;
        float x;
        switch (gravity) {
            case 1:
                x = (float) (width / 2);
                break;
            case 2:
            case 3:
            case 4:
            default:
                x = 0.0F;
                break;
            case 5:
                x = (float) width;
        }

        return x;
    }

    private void safelyRecycleBitmap(Bitmap bitmap) {
        try {
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    Bitmap getMarkBitmap() {
        return null;
    }

    private float getRelativeDy(int srcHeight) {
        float anchorMarkTop = (float) this.mAnchorMarkRect.top;
        float anchorMarkBottom = (float) this.mAnchorMarkRect.bottom;
        float dy;
        switch (this.mVerticalRelativePosition) {
            case 1:
                int topMargin = this.dip2px((float) this.mTopMargin);
                dy = anchorMarkBottom + (float) topMargin;
                break;
            case 2:
                int bottomMargin = -this.dip2px((float) this.mBottomMargin);
                dy = anchorMarkTop - (float) this.getMarkHeight() + (float) bottomMargin;
                break;
            default:
                dy = this.getDy(srcHeight);
        }

        return dy;
    }

    private float getRelativeDx(int srcWidth) {
        float anchorMarkLeft = (float) this.mAnchorMarkRect.left;
        float anchorMarkRight = (float) this.mAnchorMarkRect.right;
        float dx;
        switch (this.mHorizontalRelativePosition) {
            case 3:
                int leftMargin = this.dip2px((float) this.mLeftMargin);
                dx = anchorMarkRight + (float) leftMargin;
                break;
            case 4:
                int rightMargin = -this.dip2px((float) this.mRightMargin);
                dx = anchorMarkLeft - (float) this.getMarkWidth() + (float) rightMargin;
                break;
            default:
                dx = this.getDx(srcWidth);
        }

        return dx;
    }

    private float getDy(int srcHeight) {
        int bottomMargin = -this.dip2px((float) this.mBottomMargin);
        int topMargin = this.dip2px((float) this.mTopMargin);
        int gravity = this.mGravity & 112;
        float dy;
        switch (gravity) {
            case 16:
                int offset = topMargin != 0 ? topMargin : bottomMargin;
                dy = (float) (srcHeight - this.getMarkHeight()) * 1.0F / 2.0F + (float) offset;
                break;
            case 48:
            default:
                dy = (float) topMargin;
                break;
            case 80:
                dy = (float) (srcHeight - this.getMarkHeight() + bottomMargin);
        }

        return dy;
    }

    private float getDx(int srcWidth) {
        int leftMargin = this.dip2px((float) this.mLeftMargin);
        int rightMargin = -this.dip2px((float) this.mRightMargin);
        int gravity = this.mGravity & 7;
        float dy;
        switch (gravity) {
            case 1:
                int offset = leftMargin != 0 ? leftMargin : rightMargin;
                dy = (float) (srcWidth - this.getMarkWidth()) * 1.0F / 2.0F + (float) offset;
                break;
            case 2:
            case 3:
            case 4:
            default:
                dy = (float) leftMargin;
                break;
            case 5:
                dy = (float) (srcWidth - this.getMarkWidth() + rightMargin);
        }

        return dy;
    }

    private int getMarkWidth() {
        return this.getMarkBitmap() == null ? -1 : this.getMarkBitmap().getWidth();
    }

    private int getMarkHeight() {
        return this.getMarkBitmap() == null ? -1 : this.getMarkBitmap().getHeight();
    }

    void setAnchorMarkHorizontalRect(Rect rect) {
        int top = this.mAnchorMarkRect.top;
        int bottom = this.mAnchorMarkRect.bottom;
        this.mAnchorMarkRect.set(rect.left, top, rect.right, bottom);
    }

    void setAnchorMarkVerticalRect(Rect rect) {
        this.mAnchorMarkRect = rect;
        int left = this.mAnchorMarkRect.left;
        int right = this.mAnchorMarkRect.right;
        this.mAnchorMarkRect.set(left, rect.top, right, rect.bottom);
    }

    Rect onMeasure(int srcWidth, int srcHeight) {
        int left = 0;
        int right = 0;
        int top = 0;
        int bottom = 0;
        if (!this.isHorizontalRelativePosition()) {
            left = (int) this.getDx(srcWidth);
            right = left + this.getMarkWidth();
        }

        if (!this.isVerticalRelativePosition()) {
            top = (int) this.getDy(srcHeight);
            bottom = top + this.getMarkHeight();
        }

        this.mMeasureRect.set(left, top, right, bottom);
        return this.mMeasureRect;
    }

    void setHorizontalRelativePosition(int position) {
        this.mHorizontalRelativePosition = position;
    }

    void setVerticalRelativePosition(int position) {
        this.mVerticalRelativePosition = position;
    }

    void clearRelativePosition() {
        this.mHorizontalRelativePosition = -1;
        this.mVerticalRelativePosition = -1;
    }

    boolean isVerticalRelativePosition() {
        return this.mVerticalRelativePosition != -1;
    }

    boolean isHorizontalRelativePosition() {
        return this.mHorizontalRelativePosition != -1;
    }

    boolean isBringToFront() {
        return this.mIsBringToFront;
    }

    int dip2px(float dpValue) {
        float scale = this.mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5F);
    }

    void releaseResource() {
    }
}
