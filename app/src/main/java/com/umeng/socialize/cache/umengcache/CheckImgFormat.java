//
// Source code recreated from BitmapUtil .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.umeng.socialize.cache.umengcache;

import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.UmengText;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class CheckImgFormat {
    public static final int a = 0;
    public static final int b = 1;
    public static final int c = 2;
    public static final int d = 3;
    public static final int e = 4;
    public static final int f = 5;
    public static final int g = 6;
    public static final int h = 7;
    public static final int i = 8;
    public static final int j = 9;
    public static final int k = 10;
    public static final int l = 11;
    public static final String[] m = new String[]{"jpeg", "gif", "png", "bmp", "pcx", "iff", "ras", "pbm", "pgm", "ppm", "psd", "swf"};

    public CheckImgFormat() {
    }

    public static String a(byte[] var0) {
        ByteArrayInputStream var1 = null;

        String var4;
        try {
            var1 = new ByteArrayInputStream(var0);
            int var2 = var1.read();
            int var30 = var1.read();
            if (var2 == 71 && var30 == 73) {
                var4 = m[1];
                return var4;
            }

            if (var2 == 137 && var30 == 80) {
                var4 = m[2];
                return var4;
            }

            if (var2 == 255 && var30 == 216) {
                var4 = m[0];
                return var4;
            }

            if (var2 == 66 && var30 == 77) {
                var4 = m[3];
                return var4;
            }

            if (var2 != 10 || var30 >= 6) {
                if (var2 == 70 && var30 == 79) {
                    var4 = m[5];
                    return var4;
                }

                if (var2 == 89 && var30 == 166) {
                    var4 = m[6];
                    return var4;
                }

                if (var2 == 80 && var30 >= 49 && var30 <= 54) {
                    int var31 = var30 - 48;
                    if (var31 >= 1 && var31 <= 6) {
                        int[] var32 = new int[]{7, 8, 9};
                        int var6 = var32[(var31 - 1) % 3];
                        String var7 = m[var6];
                        return var7;
                    }

                    String var5 = "";
                    return var5;
                }

                if (var2 == 56 && var30 == 66) {
                    var4 = m[10];
                    return var4;
                }

                if (var2 == 70 && var30 == 87) {
                    var4 = m[11];
                    return var4;
                }

                var4 = "";
                return var4;
            }

            var4 = m[4];
        } catch (Exception var28) {
            Log.um(UmengText.CHECK_FORMAT_ERROR + var28.getMessage());
            String var3 = "";
            return var3;
        } finally {
            if (var1 != null) {
                try {
                    var1.close();
                } catch (IOException var27) {
                    Log.um(UmengText.CHECK_FORMAT_ERROR + var27.getMessage());
                }
            }

        }

        return var4;
    }
}
