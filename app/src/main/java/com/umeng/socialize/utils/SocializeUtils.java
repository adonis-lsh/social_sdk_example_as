//
// Source code recreated from BitmapUtil .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.umeng.socialize.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.umeng.socialize.Config;
import com.umeng.socialize.common.ResContainer;
import com.umeng.socialize.common.SocializeConstants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

public class SocializeUtils {
    protected static final String TAG = "SocializeUtils";
    public static Set<Uri> deleteUris = new HashSet();
    private static Pattern mDoubleByte_Pattern = null;
    private static int smDip = 0;

    public SocializeUtils() {
    }

    public static String getAppkey(Context var0) {
        if (var0 == null) {
            return "";
        } else {
            String var1 = SocializeConstants.APPKEY;
            if (TextUtils.isEmpty(var1)) {
                try {
                    PackageManager var2 = var0.getPackageManager();
                    ApplicationInfo var3 = var2.getApplicationInfo(var0.getPackageName(), 128);
                    if (var3 != null) {
                        Object var4 = var3.metaData.get("UMENG_APPKEY");
                        if (var4 != null) {
                            var1 = var4.toString();
                        } else {
                            Log.e("com.umeng.socialize", "Could not read UMENG_APPKEY meta-data from AndroidManifest.xml.");
                        }
                    }
                } catch (Exception var5) {
                    Log.e("com.umeng.socialize", "Could not read UMENG_APPKEY meta-data from AndroidManifest.xml.", var5);
                }
            }

            return var1;
        }
    }

    public static void safeCloseDialog(Dialog var0) {
        try {
            if (var0 != null && var0.isShowing()) {
                var0.dismiss();
                var0 = null;
            }
        } catch (Exception var2) {
            Log.e("SocializeUtils", "dialog dismiss error", var2);
        }

    }

    public static void openApplicationMarket(Context var0, String var1) throws Exception {
        if (Config.isJumptoAppStore) {
            String var2 = "market://details?id=" + var1;
            Intent var3 = new Intent("android.intent.action.VIEW");
            var3.setData(Uri.parse(var2));
            var0.startActivity(var3);
        }

    }

    public static void safeShowDialog(Dialog var0) {
        try {
            if (var0 != null && !var0.isShowing()) {
                var0.show();
            }
        } catch (Exception var2) {
            Log.e("SocializeUtils", "dialog show error", var2);
        }

    }

    public static Bundle parseUrl(String var0) {
        try {
            URL var1 = new URL(var0);
            Bundle var2 = decodeUrl(var1.getQuery());
            var2.putAll(decodeUrl(var1.getRef()));
            return var2;
        } catch (MalformedURLException var3) {
            return new Bundle();
        }
    }

    public static Bundle decodeUrl(String var0) {
        Bundle var1 = new Bundle();
        if (var0 != null) {
            String[] var2 = var0.split("&");
            String[] var3 = var2;
            int var4 = var2.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                String var6 = var3[var5];
                String[] var7 = var6.split("=");
                var1.putString(URLDecoder.decode(var7[0]), URLDecoder.decode(var7[1]));
            }
        }

        return var1;
    }

    public static int countContentLength(String var0) {
        var0 = var0.trim();
        boolean var1 = false;
        int var2 = 0;
        Pattern var3 = getDoubleBytePattern();

        for (Matcher var4 = var3.matcher(var0); var4.find(); ++var2) {
            ;
        }

        int var5 = var0.length() - var2;
        if (var5 % 2 != 0) {
            var2 += (var5 + 1) / 2;
        } else {
            var2 += var5 / 2;
        }

        return var2;
    }

    private static Pattern getDoubleBytePattern() {
        if (mDoubleByte_Pattern == null) {
            mDoubleByte_Pattern = Pattern.compile("[^\\x00-\\xff]");
        }

        return mDoubleByte_Pattern;
    }

    public static int[] getFloatWindowSize(Context var0) {
        if (var0 == null) {
            return new int[2];
        } else {
            ResContainer var1 = ResContainer.get(var0);
            Resources var2 = var0.getResources();
            int[] var3 = new int[]{(int) var2.getDimension(var1.dimen("umeng_socialize_pad_window_width")), (int) var2.getDimension(var1.dimen("umeng_socialize_pad_window_height"))};
            return var3;
        }
    }

    public static boolean isFloatWindowStyle(Context var0) {
        if (var0 == null) {
            return false;
        } else {
            if (SocializeConstants.SUPPORT_PAD) {
                if (smDip == 0) {
                    WindowManager var1 = (WindowManager) var0.getSystemService("window");
                    Display var2 = var1.getDefaultDisplay();
                    int var3 = var2.getWidth();
                    int var4 = var2.getHeight();
                    int var5 = var3 > var4 ? var4 : var3;
                    DisplayMetrics var6 = new DisplayMetrics();
                    var1.getDefaultDisplay().getMetrics(var6);
                    smDip = (int) ((float) var5 / var6.density + 0.5F);
                }

                int var7 = var0.getResources().getConfiguration().screenLayout;
                var7 &= 15;
                if (var7 >= 3 && smDip >= 550) {
                    return true;
                }
            }

            return false;
        }
    }

    public static Uri insertImage(Context var0, String var1) {
        if (!TextUtils.isEmpty(var1) && (new File(var1)).exists()) {
            try {
                String var2 = Media.insertImage(var0.getContentResolver(), var1, "umeng_social_shareimg", (String) null);
                if (TextUtils.isEmpty(var2)) {
                    return null;
                }

                Uri var3 = Uri.parse(var2);
                return var3;
            } catch (IllegalArgumentException var4) {
                Log.e("com.umeng.socialize", "", var4);
            } catch (Exception var5) {
                Log.e("com.umeng.socialize", "", var5);
            }

            return null;
        } else {
            return null;
        }
    }

    public static int dip2Px(Context var0, float var1) {
        float var2 = var0.getResources().getDisplayMetrics().density;
        return (int) (var1 * var2 + 0.5F);
    }

    public static Map<String, String> jsonToMap(String var0) {
        HashMap var1 = new HashMap();

        try {
            JSONObject var2 = new JSONObject(var0);
            Iterator var3 = var2.keys();

            while (var3.hasNext()) {
                String var4 = (String) var3.next();
                var1.put(var4, var2.get(var4) + "");
            }
        } catch (Exception var5) {
            Log.e("social", "jsontomap fail=" + var5);
        }

        return var1;
    }

    public static byte[] File2byte(File var0) {
        byte[] var1 = null;

        try {
            FileInputStream var2 = new FileInputStream(var0);
            ByteArrayOutputStream var3 = new ByteArrayOutputStream();
            byte[] var4 = new byte[1024];

            int var5;
            while ((var5 = var2.read(var4)) != -1) {
                var3.write(var4, 0, var5);
            }

            var2.close();
            var3.close();
            var1 = var3.toByteArray();
        } catch (FileNotFoundException var6) {
            Log.um(UmengText.FILE_TO_BINARY_ERROR + var6.getMessage());
        } catch (IOException var7) {
            var7.printStackTrace();
        }

        return var1;
    }

    public static Map<String, String> bundleTomap(Bundle var0) {
        if (var0 != null && !var0.isEmpty()) {
            Set var1 = var0.keySet();
            HashMap var2 = new HashMap();

            String var4;
            for (Iterator var3 = var1.iterator(); var3.hasNext(); var2.put(var4, var0.getString(var4))) {
                var4 = (String) var3.next();
                if (var4.equals("com.sina.weibo.intent.extra.USER_ICON")) {
                    var2.put("icon_url", var0.getString(var4));
                }
            }

            return var2;
        } else {
            return null;
        }
    }

    public static Bundle mapToBundle(Map<String, String> var0) {
        Bundle var1 = new Bundle();
        Iterator var2 = var0.keySet().iterator();

        while (var2.hasNext()) {
            String var3 = (String) var2.next();
            var1.putString(var3, (String) var0.get(var3));
        }

        return var1;
    }

    public static boolean assertBinaryInvalid(byte[] var0) {
        return var0 != null && var0.length > 0;
    }

    public static boolean isToday(long var0) {
        Calendar var2 = Calendar.getInstance();
        Date var3 = new Date(System.currentTimeMillis());
        var2.setTime(var3);
        Calendar var4 = Calendar.getInstance();
        Date var5 = new Date(var0);
        var4.setTime(var5);
        if (var4.get(1) == var2.get(1)) {
            int var6 = var4.get(6) - var2.get(6);
            if (var6 == 0) {
                return true;
            }
        }

        return false;
    }
}
