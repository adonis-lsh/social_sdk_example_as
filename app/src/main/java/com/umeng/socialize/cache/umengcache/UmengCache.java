//
// Source code recreated from BitmapUtil .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.umeng.socialize.cache.umengcache;

import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.UmengText;

import java.io.File;

public class UmengCache {
    public UmengCache() {
    }

    public static void getInstance() {
        boolean var0 = Environment.getExternalStorageDirectory() != null && !TextUtils.isEmpty(Environment.getExternalStorageDirectory().getPath());
        if (var0) {
            UmengCacheContants.d = Environment.getExternalStorageDirectory().getPath() + File.separator + "umeng_cache" + File.separator;
        } else {
            UmengCacheContants.d = Environment.getDataDirectory().getPath() + File.separator + "umeng_cache" + File.separator;
        }

        File var1 = new File(UmengCacheContants.d);
        if (!var1.exists()) {
            boolean var2 = var1.mkdir();
        }

        try {
            getInstance(UmengCacheContants.d);
        } catch (Exception var3) {
            Log.um(UmengText.CLEAN_CACHE_ERROR + var3.toString());
        }

    }

    private static void getInstance(String var0) {
        File var1 = new File(var0);
        File[] var2 = var1.listFiles();
        if (var2 != null && var2.length != 0) {
            int var3 = 0;

            int var4;
            for (var4 = 0; var4 < var2.length; ++var4) {
                var3 = (int) ((long) var3 + var2[var4].length());
            }

            if (var3 > 0 || 40 > c()) {
                var4 = var2.length;
//                Arrays.sort(var2, new BitmapUtil.BitmapUtil());

                for (int var5 = 0; var5 < var4; ++var5) {
                    var2[var5].delete();
                }
            }

        }
    }

    private static int c() {
        StatFs var0 = new StatFs(Environment.getExternalStorageDirectory().getPath());
        double var1 = (double) var0.getAvailableBlocks() * (double) var0.getBlockSize() / 1048576.0D;
        return (int) var1;
    }

    public static void b() {
        getInstance();
    }

//    private static class BitmapUtil implements Comparator<File> {
//        private BitmapUtil() {
//        }
//
//        public int BitmapUtil(File var1, File var2) {
//            return var1.lastModified() > var2.lastModified()?1:(var1.lastModified() == var2.lastModified()?0:-1);
//        }
//    }
}
