//
// Source code recreated from BitmapUtil .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.umeng.socialize.cache.util;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;

import com.umeng.socialize.cache.umengcache.UmengCache;
import com.umeng.socialize.cache.umengcache.UmengCacheContants;
import com.umeng.socialize.cache.umengcache.UmengCacheUtils;
import com.umeng.socialize.cache.umengcache.CheckImgFormat;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMImage.CompressStyle;
import com.umeng.socialize.net.utils.SocializeNetUtils;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.SocializeUtils;
import com.umeng.socialize.utils.UmengText;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BitmapUtil {
    public BitmapUtil() {
    }

    public static byte[] a(Bitmap var0, CompressFormat var1) {
        ByteArrayOutputStream var2 = null;
        if (var0 != null && !var0.isRecycled()) {
            try {
                var2 = new ByteArrayOutputStream();
                int var3 = var0.getRowBytes() * var0.getHeight() / 1024;
                int var4 = 100;
                if ((float) var3 > UmengCacheContants.g) {
                    var4 = (int) (UmengCacheContants.g / (float) var3 * (float) var4);
                }

                Log.d("BitmapUtil", "compress quality:" + var4);
                var0.compress(var1, var4, var2);
                byte[] var5 = var2.toByteArray();
                byte[] var6 = var5;
                return var6;
            } catch (Exception var16) {
                Log.um(var16.getMessage());
            } finally {
                if (var2 != null) {
                    try {
                        var2.close();
                    } catch (IOException var15) {
                        Log.um("bitmap2Bytes exception:" + var15.getMessage());
                    }
                }

            }

            return null;
        } else {
            return null;
        }
    }

    public static Options a(byte[] var0) {
        Options var1 = new Options();
        var1.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(var0, 0, var0.length, var1);
        int var2 = (int) Math.ceil((double) (var1.outWidth / UMImage.MAX_WIDTH));
        int var3 = (int) Math.ceil((double) (var1.outHeight / UMImage.MAX_HEIGHT));
        if (var3 > 1 && var2 > 1) {
            if (var3 > var2) {
                var1.inSampleSize = var3;
            } else {
                var1.inSampleSize = var2;
            }
        } else if (var3 > 2) {
            var1.inSampleSize = var3;
        } else if (var2 > 2) {
            var1.inSampleSize = var2;
        }

        var1.inJustDecodeBounds = false;
        return var1;
    }

    public static byte[] a(UMImage var0, int var1) {
        if (var0 == null) {
            return new byte[1];
        } else if (var0.asBinImage() != null && a(var0) >= var1) {
            if (var0.compressStyle != CompressStyle.QUALITY) {
                try {
                    byte[] var2 = var0.asBinImage();
                    Bitmap var3 = null;
                    if (var2 == null) {
                        return new byte[1];
                    } else if (var2.length <= 0) {
                        return var0.asBinImage();
                    } else {
                        var3 = BitmapFactory.decodeByteArray(var2, 0, var2.length);
                        if (var3 == null) {
                            return var0.asBinImage();
                        } else {
                            ByteArrayOutputStream var4 = new ByteArrayOutputStream();
                            var4.write(var2, 0, var2.length);

                            for (byte var5 = 0; var4.toByteArray().length > var1 && var5 < 10; var2 = var4.toByteArray()) {
                                double var6 = Math.sqrt(1.0D * (double) var2.length / (double) var1);
                                int var8 = (int) ((double) var3.getWidth() / var6);
                                int var9 = (int) ((double) var3.getHeight() / var6);
                                var3 = Bitmap.createScaledBitmap(var3, var8, var9, true);
                                var4.reset();
                                var3.compress(var0.compressFormat, 100, var4);
                            }

                            return var4.toByteArray().length > var1 ? new byte[1] : var2;
                        }
                    }
                } catch (Error var10) {
                    Log.um(UmengText.OOM + var10.getMessage());
                    return new byte[1];
                }
            } else {
                return a(var0.asBinImage(), var1, var0.compressFormat);
            }
        } else {
            return var0.asBinImage();
        }
    }

    public static byte[] a(String var0) {
        return SocializeNetUtils.getNetData(var0);
    }

    public static Bitmap b(byte[] var0) {
        return var0 != null ? BitmapFactory.decodeByteArray(var0, 0, var0.length) : null;
    }

    public static File c(byte[] var0) {
        try {
            File var1 = UmengCacheUtils.a().b();
            var1 = a(var0, var1);
            return var1;
        } catch (IOException var2) {
            Log.um("binary2File:" + var2.getMessage());
            return null;
        }
    }

    private static File a(byte[] var0, File var1) {
        BufferedOutputStream var2 = null;
        File var3 = var1;

        try {
            FileOutputStream var4 = new FileOutputStream(var3);
            var2 = new BufferedOutputStream(var4);
            var2.write(var0);
        } catch (Exception var13) {
            Log.um(UmengText.GET_FILE_FROM_BINARY + var13.getMessage());
        } finally {
            if (var2 != null) {
                try {
                    var2.close();
                } catch (IOException var12) {
                    ;
                }
            }

        }

        return var1;
    }

    public static byte[] b(Bitmap var0, CompressFormat var1) {
        return a(var0, var1);
    }

    static Bitmap a(Drawable var0) {
        int var1 = var0.getIntrinsicWidth();
        int var2 = var0.getIntrinsicHeight();
        Config var3 = var0.getOpacity() != -1 ? Config.ARGB_8888 : Config.RGB_565;
        Bitmap var4 = Bitmap.createBitmap(var1, var2, var3);
        Canvas var5 = new Canvas(var4);
        var0.setBounds(0, 0, var1, var2);
        var0.draw(var5);
        return var4;
    }

    public static byte[] a(Context var0, int var1, boolean var2, CompressFormat var3) {
        ByteArrayOutputStream var4 = new ByteArrayOutputStream();
        if (!var2) {
            Resources var10 = var0.getResources();
            Drawable var11;
            if (VERSION.SDK_INT >= 21) {
                var11 = var10.getDrawable(var1, (Theme) null);
            } else {
                var11 = var10.getDrawable(var1);
            }

            Bitmap var12 = a(var11);
            var12.compress(var3, 100, var4);
            return var4.toByteArray();
        } else {
            byte[] var5 = new byte[0];

            try {
                Options var6 = new Options();
                var6.inPreferredConfig = Config.RGB_565;
                InputStream var7 = var0.getResources().openRawResource(var1);
                Bitmap var8 = BitmapFactory.decodeStream(var7, (Rect) null, var6);
                var8.compress(var3, 100, var4);
                var5 = var4.toByteArray();
            } catch (Error var9) {
                Log.um("加载图片过大=" + var9.getMessage());
            }

            return var5;
        }
    }

    public static byte[] a(File var0, CompressFormat var1) {
        return b(var0, var1);
    }

    public static String d(byte[] var0) {
        return CheckImgFormat.a(var0);
    }

    public static int a(UMImage var0) {
        return var0.getImageStyle() == UMImage.FILE_IMAGE ? a(var0.asFileImage()) : e(var0.asBinImage());
    }

    private static byte[] b(File var0, CompressFormat var1) {
        if (var0 != null && var0.getAbsoluteFile().exists()) {
            byte[] var2 = UmengCacheUtils.a().a(var0);
            if (SocializeUtils.assertBinaryInvalid(var2)) {
                String var3 = CheckImgFormat.a(var2);
                return CheckImgFormat.m[1].equals(var3) ? var2 : a(var2, var1);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private static byte[] a(byte[] var0, CompressFormat var1) {
        Bitmap var2 = null;
        ByteArrayOutputStream var3 = null;
        byte[] var4 = null;

        try {
            Options var5 = a(var0);
            var2 = BitmapFactory.decodeByteArray(var0, 0, var0.length, var5);
            var3 = new ByteArrayOutputStream();
            if (var2 != null) {
                var2.compress(var1, 100, var3);
                var2.recycle();
                System.gc();
            }

            var4 = var3.toByteArray();
        } catch (Exception var14) {
            Log.um(UmengText.FILE_TO_BINARY_ERROR + var14.getMessage());
        } finally {
            if (var3 != null) {
                try {
                    var3.close();
                } catch (IOException var13) {
                    Log.um(UmengText.FILE_TO_BINARY_ERROR + var13.getMessage());
                }
            }

        }

        return var4;
    }

    public static byte[] a(byte[] var0, int var1, CompressFormat var2) {
        boolean var3 = false;
        if (var0 != null && var0.length >= var1) {
            ByteArrayOutputStream var4 = new ByteArrayOutputStream();
            Bitmap var5 = BitmapFactory.decodeByteArray(var0, 0, var0.length);
            int var6 = 1;
            double var7 = 1.0D;

            while (true) {
                while (!var3 && var6 <= 10) {
                    var7 = Math.pow(0.8D, (double) var6);
                    int var9 = (int) (100.0D * var7);
                    var5.compress(var2, var9, var4);
                    if (var4 != null && var4.size() < var1) {
                        var3 = true;
                    } else {
                        var4.reset();
                        ++var6;
                    }
                }

                if (var4 != null) {
                    byte[] var10 = var4.toByteArray();
                    if (!var5.isRecycled()) {
                        var5.recycle();
                    }

                    if (var10 != null && var10.length <= 0) {
                        Log.um(UmengText.THUMB_ERROR);
                    }

                    return var10;
                }
                break;
            }
        }

        return var0;
    }

    private static int e(byte[] var0) {
        return var0 != null ? var0.length : 0;
    }

    private static int a(File var0) {
        if (var0 != null) {
            FileInputStream var1 = null;

            try {
                var1 = new FileInputStream(var0);
                return var1.available();
            } catch (FileNotFoundException var3) {
                Log.um(UmengText.GET_IMAGE_SCALE_ERROR + var3.getMessage());
            } catch (IOException var4) {
                Log.um(UmengText.GET_IMAGE_SCALE_ERROR + var4.getMessage());
            }
        }

        return 0;
    }

    static {
        UmengCache.getInstance();
    }
}
