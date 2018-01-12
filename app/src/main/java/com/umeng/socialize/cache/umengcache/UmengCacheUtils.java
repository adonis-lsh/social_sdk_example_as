//
// Source code recreated from BitmapUtil .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.umeng.socialize.cache.umengcache;

import android.os.Environment;
import android.text.TextUtils;

import com.umeng.socialize.net.utils.AesHelper;
import com.umeng.socialize.utils.ContextUtil;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.UmengText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class UmengCacheUtils {
    private String path = "";
    private static UmengCacheUtils b = new UmengCacheUtils();

    private UmengCacheUtils() {
        try {
            this.path = ContextUtil.getContext().getCacheDir().getCanonicalPath();
        } catch (IOException var2) {
            Log.um(UmengText.FET_CACHE_PATH_ERROR + var2.getMessage());
        }

    }

    public static UmengCacheUtils a() {
        return b == null ? new UmengCacheUtils() : b;
    }

    public File b() throws IOException {
        UmengCache.b();
        File var1 = new File(this.c(), this.d());
        if (var1.exists()) {
            var1.delete();
        }

        var1.createNewFile();
        return var1;
    }

    public File c() throws IOException {
        String var1;
        if (Environment.getExternalStorageDirectory() != null && !TextUtils.isEmpty(Environment.getExternalStorageDirectory().getCanonicalPath())) {
            var1 = Environment.getExternalStorageDirectory().getCanonicalPath();
        } else {
            if (TextUtils.isEmpty(this.path)) {
                throw new IOException("dirpath is unknow");
            }

            var1 = this.path;
            Log.um(UmengText.SD_NOT_FOUNT);
        }

        File var2 = new File(var1 + "/umeng_cache/");
        if (var2 != null && !var2.exists()) {
            var2.mkdirs();
        }

        return var2;
    }

    public byte[] a(File var1) {
        Object var2 = null;
        FileInputStream var3 = null;
        ByteArrayOutputStream var4 = null;

        Object var6;
        try {
            var3 = new FileInputStream(var1);
            var4 = new ByteArrayOutputStream();
            byte[] var5 = new byte[4096];

            int var19;
            while ((var19 = var3.read(var5)) != -1) {
                var4.write(var5, 0, var19);
            }

            byte[] var18 = var4.toByteArray();
            return var18;
        } catch (Exception var16) {
            Log.um(UmengText.READ_IMAGE_ERROR + var16.getMessage());
            var6 = null;
        } finally {
            try {
                if (var3 != null) {
                    var3.close();
                }

                if (var4 != null) {
                    var4.close();
                }
            } catch (IOException var15) {
                Log.um(UmengText.READ_IMAGE_ERROR + var15.getMessage());
            }

        }

        return (byte[]) var6;
    }

    public String d() {
        long var1 = System.currentTimeMillis();
        String var3 = AesHelper.md5(String.valueOf(var1));
        return var3;
    }
}
