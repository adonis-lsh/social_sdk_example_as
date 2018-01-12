//
// Source code recreated from BitmapUtil .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.umeng.socialize.net.utils;

import android.os.Bundle;
import android.text.TextUtils;

import com.umeng.socialize.Config;
import com.umeng.socialize.utils.ContextUtil;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.UmengText;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;

public class SocializeNetUtils {
    private static final String TAG = "SocializeNetUtils";

    public SocializeNetUtils() {
    }

    public static boolean isConSpeCharacters(String var0) {
        return var0.replaceAll("[一-龥]*[a-z]*[A-Z]*\\d*-*_*\\s*", "").length() != 0;
    }

    public static boolean isSelfAppkey(String var0) {
        return var0.equals("5126ff896c738f2bfa000438") && !ContextUtil.getPackageName().equals("com.umeng.soexample");
    }

    public static byte[] getNetData(String var0) {
        if (TextUtils.isEmpty(var0)) {
            return null;
        } else {
            ByteArrayOutputStream var1 = null;
            InputStream var2 = null;

            try {
                String var4;
                try {
                    var1 = new ByteArrayOutputStream();
                    HttpURLConnection var3 = (HttpURLConnection) (new URL(var0)).openConnection();
                    var3.setInstanceFollowRedirects(true);
                    var3.setConnectTimeout(Config.connectionTimeOut);
                    var3.setReadTimeout(Config.readSocketTimeOut);
                    if (var3.getResponseCode() == 301) {
                        var4 = var3.getHeaderField("Location");
                        if (var4.equals(var0)) {
                            Log.um(UmengText.NET_AGAIN_ERROR);
                            Object var158 = null;
                            return (byte[]) var158;
                        } else {
                            byte[] var157 = getNetData(var4);
                            return var157;
                        }
                    } else {
                        var2 = var3.getInputStream();
                        Log.d("image", "getting image from url" + var0);
                        byte[] var156 = new byte[4096];

                        int var5;
                        while ((var5 = var2.read(var156)) != -1) {
                            var1.write(var156, 0, var5);
                        }

                        byte[] var6 = var1.toByteArray();
                        return var6;
                    }
                } catch (Exception var154) {
                    Log.um(UmengText.IMAGE_DOWNLOAD_ERROR + var154.getMessage());
                    return null;
                }
            } finally {
                if (var2 != null) {
                    try {
                        var2.close();
                    } catch (IOException var152) {
                        ;
                    } finally {
                        if (var1 != null) {
                            try {
                                var1.close();
                            } catch (IOException var151) {
                                ;
                            }
                        }

                    }
                }

            }
        }
    }

    public static boolean startWithHttp(String var0) {
        return var0.startsWith("http://") || var0.startsWith("https://");
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

    public static Bundle parseUri(String var0) {
        try {
            URI var1 = new URI(var0);
            Bundle var2 = decodeUrl(var1.getQuery());
            return var2;
        } catch (Exception var3) {
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

                try {
                    var1.putString(URLDecoder.decode(var7[0], "UTF-8"), URLDecoder.decode(var7[1], "UTF-8"));
                } catch (UnsupportedEncodingException var9) {
                    var9.printStackTrace();
                }
            }
        }

        return var1;
    }

    public static String request(String var0) {
        String var1 = "";

        try {
            URL var2 = new URL(var0);
            URLConnection var3 = var2.openConnection();
            if (var3 == null) {
                return var1;
            } else {
                var3.connect();
                InputStream var4 = var3.getInputStream();
                return var4 == null ? var1 : convertStreamToString(var4);
            }
        } catch (Exception var5) {
            var5.printStackTrace();
            return var1;
        }
    }

    public static String convertStreamToString(InputStream var0) {
        BufferedReader var1 = new BufferedReader(new InputStreamReader(var0));
        StringBuilder var2 = new StringBuilder();
        String var3 = null;

        try {
            while ((var3 = var1.readLine()) != null) {
                var2.append(var3 + "/n");
            }
        } catch (IOException var13) {
            var13.printStackTrace();
        } finally {
            try {
                var0.close();
            } catch (IOException var12) {
                var12.printStackTrace();
            }

        }

        return var2.toString();
    }
}
