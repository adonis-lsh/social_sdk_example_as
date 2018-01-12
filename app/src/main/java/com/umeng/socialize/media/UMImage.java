//
// Source code recreated from BitmapUtil .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.umeng.socialize.media;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory.Options;

import com.umeng.social.tool.UMImageMark;
import com.umeng.socialize.cache.util.BitmapUtil;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import com.umeng.socialize.utils.ContextUtil;
import com.umeng.socialize.utils.SocializeUtils;
import com.umeng.socialize.utils.UmengText;

import java.io.Closeable;
import java.io.File;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class UMImage extends BaseMediaObject {
    private UMImage.ConfiguredConvertor f = null;
    public boolean isLoadImgByCompress = true;
    private UMImage g;
    public UMImage.CompressStyle compressStyle;
    public CompressFormat compressFormat;
    private UMImageMark h;
    private int i;
    private boolean j;
    public static int MAX_WIDTH = 768;
    public static int MAX_HEIGHT = 1024;
    public static int FILE_IMAGE = 1;
    public static int URL_IMAGE = 2;
    public static int RES_IMAGE = 3;
    public static int BITMAP_IMAGE = 4;
    public static int BINARY_IMAGE = 5;

    public UMImage(Context var1, File var2) {
        this.compressStyle = UMImage.CompressStyle.SCALE;
        this.compressFormat = CompressFormat.JPEG;
        this.i = 0;
        this.a(var1, var2);
    }

    public UMImage(Context var1, String var2) {
        super(var2);
        this.compressStyle = UMImage.CompressStyle.SCALE;
        this.compressFormat = CompressFormat.JPEG;
        this.i = 0;
        this.a((Context) (new WeakReference(var1)).get(), var2);
    }

    public UMImage(Context var1, int var2) {
        this.compressStyle = UMImage.CompressStyle.SCALE;
        this.compressFormat = CompressFormat.JPEG;
        this.i = 0;
        this.a(var1, Integer.valueOf(var2));
    }

    public UMImage(Context var1, byte[] var2) {
        this.compressStyle = UMImage.CompressStyle.SCALE;
        this.compressFormat = CompressFormat.JPEG;
        this.i = 0;
        this.a(var1, var2);
    }

    public UMImage(Context var1, Bitmap var2) {
        this.compressStyle = UMImage.CompressStyle.SCALE;
        this.compressFormat = CompressFormat.JPEG;
        this.i = 0;
        this.a(var1, var2);
    }

    public UMImage(Context var1, Bitmap var2, UMImageMark var3) {
        this.compressStyle = UMImage.CompressStyle.SCALE;
        this.compressFormat = CompressFormat.JPEG;
        this.i = 0;
        this.a(var1, var2, var3);
    }

    public UMImage(Context var1, int var2, UMImageMark var3) {
        this.compressStyle = UMImage.CompressStyle.SCALE;
        this.compressFormat = CompressFormat.JPEG;
        this.i = 0;
        this.a(var1, Integer.valueOf(var2), var3);
    }

    public UMImage(Context var1, byte[] var2, UMImageMark var3) {
        this.compressStyle = UMImage.CompressStyle.SCALE;
        this.compressFormat = CompressFormat.JPEG;
        this.i = 0;
        this.a(var1, var2, var3);
    }

    private void a(Context var1, Object var2) {
        this.a(var1, var2, (UMImageMark) null);
    }

    private void a(Context var1, Object var2, UMImageMark var3) {
        if (var3 != null) {
            this.j = true;
            this.h = var3;
            this.h.setContext(var1);
        }

        if (ContextUtil.getContext() == null) {
            ContextUtil.setContext(var1.getApplicationContext());
        }

        if (var2 instanceof File) {
            this.i = FILE_IMAGE;
            this.f = new UMImage.FileConvertor((File) var2);
        } else if (var2 instanceof String) {
            this.i = URL_IMAGE;
            this.f = new UMImage.UrlConvertor((String) var2);
        } else {
            Bitmap var4;
            if (var2 instanceof Integer) {
                this.i = RES_IMAGE;
                var4 = null;
                if (this.isHasWaterMark()) {
                    var4 = this.a(var1, ((Integer) var2).intValue());
                }

                if (var4 != null) {
                    this.f = new UMImage.BitmapConvertor(var4);
                } else {
                    this.f = new UMImage.ResConvertor(var1.getApplicationContext(), ((Integer) var2).intValue());
                }
            } else if (var2 instanceof byte[]) {
                this.i = BINARY_IMAGE;
                var4 = null;
                if (this.isHasWaterMark()) {
                    var4 = this.a((byte[]) ((byte[]) var2));
                }

                if (var4 != null) {
                    this.f = new UMImage.BitmapConvertor(var4);
                } else {
                    this.f = new UMImage.BinaryConvertor((byte[]) ((byte[]) var2));
                }
            } else {
                if (!(var2 instanceof Bitmap)) {
                    throw new RuntimeException(UmengText.UNKNOW_UMIMAGE + "  类型：" + var2.getClass().getSimpleName());
                }

                this.i = BITMAP_IMAGE;
                var4 = null;
                if (this.isHasWaterMark()) {
                    var4 = this.a((Bitmap) var2, true);
                }

                if (var4 == null) {
                    var4 = (Bitmap) var2;
                }

                this.f = new UMImage.BitmapConvertor(var4);
            }
        }

    }

    public byte[] toByte() {
        return this.asBinImage();
    }

    public void setThumb(UMImage var1) {
        this.g = var1;
    }

    public UMImage getThumbImage() {
        return this.g;
    }

    public final Map<String, Object> toUrlExtraParams() {
        HashMap var1 = new HashMap();
        if (this.isUrlMedia()) {
            var1.put(SocializeProtocolConstants.PROTOCOL_KEY_FURL, this.url);
            var1.put(SocializeProtocolConstants.PROTOCOL_KEY_FTYPE, this.getMediaType());
        }

        return var1;
    }

    public MediaType getMediaType() {
        return MediaType.IMAGE;
    }

    public int getImageStyle() {
        return this.i;
    }

    public File asFileImage() {
        return this.f == null ? null : this.f.asFile();
    }

    public String asUrlImage() {
        return this.f == null ? null : this.f.asUrl();
    }

    public byte[] asBinImage() {
        return this.f == null ? null : this.f.asBinary();
    }

    public Bitmap asBitmap() {
        return this.f == null ? null : this.f.asBitmap();
    }

    private Bitmap a(Bitmap var1, boolean var2) {
        if (this.h == null) {
            return var1;
        } else if (var1 == null) {
            return null;
        } else {
            try {
                if (var2) {
                    var1 = this.a(var1);
                }

                return this.h.compound(var1);
            } catch (Exception var4) {
                var4.printStackTrace();
                return null;
            }
        }
    }

    private Bitmap a(Context var1, int var2) {
        if (var2 != 0 && var1 != null && this.h != null) {
            InputStream var3 = null;

            try {
                Options var4 = new Options();
                var4.inJustDecodeBounds = true;
                var3 = var1.getResources().openRawResource(var2);
                BitmapFactory.decodeStream(var3, (Rect) null, var4);
                this.a((Closeable) var3);
                int var5 = (int) this.a((float) var4.outWidth, (float) var4.outHeight, (float) MAX_WIDTH, (float) MAX_HEIGHT);
                if (var5 > 0) {
                    var4.inSampleSize = var5;
                }

                var4.inJustDecodeBounds = false;
                var3 = var1.getResources().openRawResource(var2);
                Bitmap var6 = BitmapFactory.decodeStream(var3, (Rect) null, var4);
                Bitmap var7 = this.a(var6, false);
                return var7;
            } catch (Exception var11) {
                var11.printStackTrace();
            } finally {
                this.a((Closeable) var3);
            }

            return null;
        } else {
            return null;
        }
    }

    private void a(Closeable var1) {
        try {
            if (var1 != null) {
                var1.close();
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    private Bitmap a(byte[] var1) {
        if (var1 != null && this.h != null) {
            try {
                Options var2 = new Options();
                var2.inJustDecodeBounds = true;
                BitmapFactory.decodeByteArray(var1, 0, var1.length, var2);
                int var3 = (int) this.a((float) var2.outWidth, (float) var2.outHeight, (float) MAX_WIDTH, (float) MAX_HEIGHT);
                if (var3 > 0) {
                    var2.inSampleSize = var3;
                }

                var2.inJustDecodeBounds = false;
                Bitmap var4 = BitmapFactory.decodeByteArray(var1, 0, var1.length, var2);
                return this.a(var4, false);
            } catch (Exception var5) {
                var5.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    private Bitmap a(Bitmap var1) {
        int var2 = var1.getWidth();
        int var3 = var1.getHeight();
        float var4 = this.a((float) var2, (float) var3, (float) MAX_WIDTH, (float) MAX_HEIGHT);
        if (var4 < 0.0F) {
            return var1;
        } else {
            var4 = 1.0F / var4;
            Matrix var5 = new Matrix();
            var5.postScale(var4, var4);
            Bitmap var6 = Bitmap.createBitmap(var1, 0, 0, var2, var3, var5, false);
            this.b(var1);
            return var6;
        }
    }

    private float a(float var1, float var2, float var3, float var4) {
        if (var1 <= var4 && var2 <= var4) {
            return -1.0F;
        } else {
            float var5 = var1 / var3;
            float var6 = var2 / var4;
            return var5 > var6 ? var5 : var6;
        }
    }

    private void b(Bitmap var1) {
        try {
            if (var1 != null && !var1.isRecycled()) {
                var1.recycle();
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public boolean isHasWaterMark() {
        return this.j;
    }

    interface IImageConvertor {
        File asFile();

        String asUrl();

        byte[] asBinary();

        Bitmap asBitmap();
    }

    abstract static class ConfiguredConvertor implements UMImage.IImageConvertor {
        ConfiguredConvertor() {
        }
    }

    class ResConvertor extends UMImage.ConfiguredConvertor {
        private Context b;
        private int c = 0;

        public ResConvertor(Context var2, int var3) {
            this.b = var2;
            this.c = var3;
        }

        public File asFile() {
            return SocializeUtils.assertBinaryInvalid(this.asBinary()) ? BitmapUtil.c(this.asBinary()) : null;
        }

        public String asUrl() {
            return null;
        }

        public byte[] asBinary() {
            return BitmapUtil.a(this.b, this.c, UMImage.this.isLoadImgByCompress, UMImage.this.compressFormat);
        }

        public Bitmap asBitmap() {
            return SocializeUtils.assertBinaryInvalid(this.asBinary()) ? BitmapUtil.b(this.asBinary()) : null;
        }
    }

    class BinaryConvertor extends UMImage.ConfiguredConvertor {
        private byte[] b;

        public BinaryConvertor(byte[] var2) {
            this.b = var2;
        }

        public File asFile() {
            return SocializeUtils.assertBinaryInvalid(this.asBinary()) ? BitmapUtil.c(this.asBinary()) : null;
        }

        public String asUrl() {
            return null;
        }

        public byte[] asBinary() {
            return this.b;
        }

        public Bitmap asBitmap() {
            return SocializeUtils.assertBinaryInvalid(this.asBinary()) ? BitmapUtil.b(this.asBinary()) : null;
        }
    }

    class UrlConvertor extends UMImage.ConfiguredConvertor {
        private String b = null;

        public UrlConvertor(String var2) {
            this.b = var2;
        }

        public File asFile() {
            return SocializeUtils.assertBinaryInvalid(this.asBinary()) ? BitmapUtil.c(this.asBinary()) : null;
        }

        public String asUrl() {
            return this.b;
        }

        public byte[] asBinary() {
            return BitmapUtil.a(this.b);
        }

        public Bitmap asBitmap() {
            return SocializeUtils.assertBinaryInvalid(this.asBinary()) ? BitmapUtil.b(this.asBinary()) : null;
        }
    }

    class FileConvertor extends UMImage.ConfiguredConvertor {
        private File b;

        public FileConvertor(File var2) {
            this.b = var2;
        }

        public File asFile() {
            return this.b;
        }

        public String asUrl() {
            return null;
        }

        public byte[] asBinary() {
            return BitmapUtil.a(this.b, UMImage.this.compressFormat);
        }

        public Bitmap asBitmap() {
            return SocializeUtils.assertBinaryInvalid(this.asBinary()) ? BitmapUtil.b(UMImage.this.asBinImage()) : null;
        }
    }

    class BitmapConvertor extends UMImage.ConfiguredConvertor {
        private Bitmap b;

        public BitmapConvertor(Bitmap var2) {
            this.b = var2;
        }

        public File asFile() {
            byte[] var1 = BitmapUtil.b(this.b, UMImage.this.compressFormat);
            return SocializeUtils.assertBinaryInvalid(this.asBinary()) ? BitmapUtil.c(var1) : null;
        }

        public String asUrl() {
            return null;
        }

        public byte[] asBinary() {
            return BitmapUtil.b(this.b, UMImage.this.compressFormat);
        }

        public Bitmap asBitmap() {
            return this.b;
        }
    }

    public static enum CompressStyle {
        SCALE,
        QUALITY;

        private CompressStyle() {
        }
    }
}
