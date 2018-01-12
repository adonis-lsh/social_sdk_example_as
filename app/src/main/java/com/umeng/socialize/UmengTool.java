//
// Source code recreated from BitmapUtil .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.umeng.socialize;

import android.annotation.TargetApi;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.Toast;

import com.umeng.socialize.PlatformConfig.APPIDPlatform;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;

public class UmengTool {
    private static final char[] HEX_CHAR = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public UmengTool() {
    }

    public static void getSignature(Context context) {
        String pkName = context.getPackageName();
        if (TextUtils.isEmpty(pkName)) {
            Toast.makeText(context, "应用程序的包名不能为空！", 0).show();
        } else {
            try {
                PackageManager packageManager = context.getPackageManager();
                PackageInfo packageInfo = packageManager.getPackageInfo(pkName, 64);
                String result = getSignatureDigest(packageInfo);
                showDialog(context, "包名：" + pkName + "\n" + "签名:" + result.toLowerCase() + "\n" + "facebook keyhash:" + facebookHashKey(packageInfo));
            } catch (NameNotFoundException var5) {
                var5.printStackTrace();
            }
        }

    }

    public static String getCertificateSHA1Fingerprint(PackageInfo packageInfo) {
        Signature[] signatures = packageInfo.signatures;
        byte[] cert = signatures[0].toByteArray();
        InputStream input = new ByteArrayInputStream(cert);
        CertificateFactory cf = null;

        try {
            cf = CertificateFactory.getInstance("X509");
        } catch (CertificateException var12) {
            var12.printStackTrace();
        }

        X509Certificate c = null;

        try {
            c = (X509Certificate) cf.generateCertificate(input);
        } catch (CertificateException var11) {
            var11.printStackTrace();
        }

        String hexString = null;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(c.getEncoded());
            hexString = byte2HexFormatted(publicKey);
        } catch (NoSuchAlgorithmException var9) {
            var9.printStackTrace();
        } catch (CertificateEncodingException var10) {
            var10.printStackTrace();
        }

        return hexString;
    }

    private static String byte2HexFormatted(byte[] arr) {
        StringBuilder str = new StringBuilder(arr.length * 2);

        for (int i = 0; i < arr.length; ++i) {
            String h = Integer.toHexString(arr[i]);
            int l = h.length();
            if (l == 1) {
                h = "0" + h;
            }

            if (l > 2) {
                h = h.substring(l - 2, l);
            }

            str.append(h.toUpperCase());
            if (i < arr.length - 1) {
                str.append(':');
            }
        }

        return str.toString();
    }

    private static String facebookHashKey(PackageInfo info) {
        try {
            Signature[] var1 = info.signatures;
            int var2 = var1.length;
            byte var3 = 0;
            if (var3 < var2) {
                Signature signature = var1[var3];
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), 0);
            }
        } catch (NoSuchAlgorithmException var6) {
            ;
        }

        return null;
    }

    public static void showDialog(Context context, String result) {
        (new Builder(context)).setTitle("友盟Debug模式自检").setMessage(result).setPositiveButton("关闭", (OnClickListener) null).show();
    }

    public static void showDialogWithURl(final Context context, String result, final String url) {
        (new Builder(context)).setTitle("友盟Debug模式自检").setMessage(result).setPositiveButton("关闭", (OnClickListener) null).setNeutralButton("解决方案", new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(url);
                intent.setData(content_url);
                context.startActivity(intent);
            }
        }).show();
    }

    public static void getREDICRECT_URL(Context context) {
        showDialog(context, getStrRedicrectUrl());
    }

    public static String getStrRedicrectUrl() {
        return ((APPIDPlatform) PlatformConfig.configs.get(SHARE_MEDIA.SINA)).redirectUrl;
    }

    public static String getSignatureDigest(PackageInfo pkgInfo) {
        int length = pkgInfo.signatures.length;
        if (length <= 0) {
            return "";
        } else {
            Signature signature = pkgInfo.signatures[0];
            MessageDigest md5 = null;

            try {
                md5 = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException var5) {
                var5.printStackTrace();
            }

            byte[] digest = md5.digest(signature.toByteArray());
            return toHexString(digest);
        }
    }

    private static String toHexString(byte[] rawByteArray) {
        char[] chars = new char[rawByteArray.length * 2];

        for (int i = 0; i < rawByteArray.length; ++i) {
            byte b = rawByteArray[i];
            chars[i * 2] = HEX_CHAR[b >>> 4 & 15];
            chars[i * 2 + 1] = HEX_CHAR[b & 15];
        }

        return new String(chars);
    }

    public static String checkWxBySelf(Context context) {
        String pkName = context.getPackageName();
        String classPath = pkName + ".wxapi.WXEntryActivity";
        PackageInfo packageInfo = null;
        PackageManager packageManager = null;

        try {
            packageManager = context.getPackageManager();
            packageInfo = packageManager.getPackageInfo(pkName, 64);
        } catch (NameNotFoundException var8) {
            var8.printStackTrace();
        }

        String result = getSignatureDigest(packageInfo);

        try {
            Class<?> clz = Class.forName(classPath);
            if (clz == null) {
                return "请检查微信后台注册签名：" + result.toLowerCase() + "\n" + "包名：" + pkName + "\n" + "没有配置微信回调activity或配置不正确";
            }

            if (Config.isUmengWx.booleanValue()) {
                if (clz.getSuperclass() == null) {
                    return "WXEntryActivity配置不正确，您使用的是精简版，请使WXEntryActivity继承com.umeng.weixin.callback.WXCallbackActivity";
                }

                if (!clz.getSuperclass().toString().contains("com.umeng.weixin")) {
                    return "WXEntryActivity配置不正确，您使用的是精简版，请使WXEntryActivity继承com.umeng.weixin.callback.WXCallbackActivity";
                }
            } else {
                if (clz.getSuperclass() == null) {
                    return "WXEntryActivity配置不正确，您使用的是完整版，请使WXEntryActivity继承com.umeng.socialize.weixin.view.WXCallbackActivity";
                }

                if (!clz.getSuperclass().toString().contains("com.umeng.socialize")) {
                    return "WXEntryActivity配置不正确，您使用的是完整版，请使WXEntryActivity继承com.umeng.socialize.weixin.view.WXCallbackActivity";
                }
            }
        } catch (ClassNotFoundException var9) {
            var9.printStackTrace();
            return "请检查微信后台注册签名：" + result.toLowerCase() + "\n" + "包名：" + pkName + "\n" + "没有配置微信回调activity或配置不正确";
        }

        try {
            ComponentName componentName = new ComponentName(context.getPackageName(), classPath);
            packageManager.getActivityInfo(componentName, 0);
            return "请检查微信后台注册签名：" + result.toLowerCase() + "\n" + "包名：" + pkName + "\n" + "Activity微信配置正确";
        } catch (NameNotFoundException var7) {
            var7.printStackTrace();
            return "请检查微信后台注册签名：" + result.toLowerCase() + "\n" + "包名：" + pkName + "\n" + "没有配置微信回调activity没有在Manifest中配置";
        }
    }

    public static void checkWx(Context context) {
        showDialog(context, checkWxBySelf(context));
    }

    public static String checkSinaBySelf(Context context) {
        String pkName = context.getPackageName();
        PackageInfo packageInfo = null;

        try {
            PackageManager packageManager = context.getPackageManager();
            packageInfo = packageManager.getPackageInfo(pkName, 64);
        } catch (NameNotFoundException var8) {
            var8.printStackTrace();
        }

        String result = getSignatureDigest(packageInfo);
        String classPath = "com.sina.weibo.sdk.share.WbShareTransActivity";

        try {
            Class<?> clz = Class.forName(classPath);
            return clz == null ? "请检查sina后台注册签名：" + result.toLowerCase() + "\n" + "包名：" + pkName + "\n" + "回调地址：" + getStrRedicrectUrl() + "\n" + "没有配置新浪回调activity或配置不正确" : "请检查sina后台注册签名：" + result.toLowerCase() + "\n" + "包名：" + pkName + "\n" + "回调地址：" + getStrRedicrectUrl() + "新浪配置正确";
        } catch (ClassNotFoundException var7) {
            var7.printStackTrace();
            return "请检查sina后台注册签名：" + result.toLowerCase() + "\n" + "包名：" + pkName + "\n" + "回调地址：" + getStrRedicrectUrl() + "没有配置新浪回调activity或配置不正确";
        }
    }

    public static void checkSina(Context context) {
        showDialog(context, checkSinaBySelf(context));
    }

    public static void checkAlipay(Context context) {
        String pkName = context.getPackageName();
        String classPath = pkName + ".apshare.ShareEntryActivity";

        try {
            Class<?> clz = Class.forName(classPath);
            if (clz == null) {
                showDialog(context, "没有配置支付宝回调activity或配置不正确");
            } else {
                showDialog(context, "支付宝配置正确");
            }
        } catch (ClassNotFoundException var4) {
            showDialog(context, "没有配置支付宝回调activity或配置不正确");
            var4.printStackTrace();
        }

    }

    @TargetApi(9)
    public static String checkFBByself(Context context) {
        String var3 = "";

        ComponentName componentName;
        PackageManager packageManager;
        try {
            componentName = new ComponentName(context.getPackageName(), "com.umeng.facebook.FacebookActivity");
            packageManager = context.getPackageManager();
            packageManager.getActivityInfo(componentName, 0);
        } catch (NameNotFoundException var8) {
            var3 = "没有在AndroidManifest.xml中配置com.umeng.facebook.FacebookActivity,请阅读友盟官方文档";
            return var3;
        }

        try {
            componentName = new ComponentName(context.getPackageName(), "com.umeng.facebook.FacebookContentProvider");
            packageManager = context.getPackageManager();
            packageManager.getProviderInfo(componentName, 0);
        } catch (NameNotFoundException var7) {
            var3 = "没有在AndroidManifest.xml中配置com.umeng.facebook.FacebookContentProvider,请阅读友盟官方文档";
            return var3;
        }

        try {
            packageManager = context.getPackageManager();
            ApplicationInfo info = packageManager.getApplicationInfo(context.getPackageName(), 128);
            if (info != null) {
                Object obj = info.metaData.get("com.facebook.sdk.ApplicationId");
                if (obj == null) {
                    var3 = "没有在AndroidManifest中配置facebook的appid";
                    return var3;
                }
            }
        } catch (Exception var9) {
            var3 = "没有在AndroidManifest中配置facebook的appid";
            return var3;
        }

        int id = context.getResources().getIdentifier("facebook_app_id", "string", context.getPackageName());
        if (id <= 0) {
            var3 = "没有找到facebook_app_id，facebook的id必须写在string文件中且名字必须用facebook_app_id";
        } else {
            try {
                packageManager = context.getPackageManager();
                PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 64);
                var3 = "facebook 配置正确，请检查fb后台签名:" + facebookHashKey(packageInfo);
            } catch (NameNotFoundException var6) {
                var6.printStackTrace();
            }
        }

        return var3;
    }

    public static String checkQQByself(Context context) {
        String path1 = "com.tencent.tauth.AuthActivity";
        String path2 = "com.tencent.connect.common.AssistActivity";
        if (Config.isUmengQQ.booleanValue()) {
            path1 = "com.umeng.qq.tencent.AuthActivity";
            path2 = "com.umeng.qq.tencent.AssistActivity";
        }

        ComponentName componentName;
        String var3;
        PackageManager packageManager;
        try {
            componentName = new ComponentName(context.getPackageName(), path1);
            packageManager = context.getPackageManager();
            packageManager.getActivityInfo(componentName, 0);
        } catch (NameNotFoundException var15) {
            var3 = "没有在AndroidManifest.xml中检测到" + path1 + ",请加上" + path1 + ",并配置<data android:scheme=\"tencent" + "appid" + "\" />,详细信息请查看官网文档.";
            return var3;
        }

        try {
            componentName = new ComponentName(context.getPackageName(), path2);
            packageManager = context.getPackageManager();
            packageManager.getActivityInfo(componentName, 0);
            boolean permission = 0 == packageManager.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", context.getPackageName());
            if (permission) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.addCategory("android.intent.category.BROWSABLE");
                APPIDPlatform qqZone = (APPIDPlatform) PlatformConfig.getPlatform(SHARE_MEDIA.QQ);
                if (qqZone != null && !TextUtils.isEmpty(qqZone.appId)) {
                    String callback = "tencent" + qqZone.appId + ":";
                    Uri uri = Uri.parse(callback);
                    intent.setData(uri);
                    List<ResolveInfo> infos = context.getPackageManager().queryIntentActivities(intent, 64);
                    if (infos.size() > 0) {
                        Iterator var12 = infos.iterator();

                        ResolveInfo temp;
                        do {
                            if (!var12.hasNext()) {
                                return "qq配置不正确，AndroidManifest中AuthActivity的data中要加入自己的qq应用id";
                            }

                            temp = (ResolveInfo) var12.next();
                        } while (temp.activityInfo == null || !temp.activityInfo.packageName.equals(context.getPackageName()));

                        return "qq配置正确";
                    } else {
                        return "qq配置不正确，AndroidManifest中AuthActivity的data中要加入自己的qq应用id";
                    }
                } else {
                    return "qq配置不正确，没有检测到qq的id配置";
                }
            } else {
                return "qq 权限配置不正确，缺少android.permission.WRITE_EXTERNAL_STORAGE";
            }
        } catch (NameNotFoundException var14) {
            var3 = "没有在AndroidManifest.xml中检测到" + path2 + ",请加上" + path2 + ",详细信息请查看官网文档.";
            return var3;
        }
    }

    public static String checkVKByself(Context context) {
        String pkName = context.getPackageName();
        PackageManager manager = context.getPackageManager();
        if (TextUtils.isEmpty(pkName)) {
            return "包名为空";
        } else {
            try {
                PackageInfo packageInfo = manager.getPackageInfo(pkName, 64);
                String result = getCertificateSHA1Fingerprint(packageInfo);
                return "你使用的签名：" + result.replace(":", "");
            } catch (NameNotFoundException var5) {
                return "签名获取失败";
            }
        }
    }

    public static String checkLinkin(Context context) {
        String pkName = context.getPackageName();
        PackageManager manager = context.getPackageManager();
        if (TextUtils.isEmpty(pkName)) {
            return "包名为空";
        } else {
            try {
                manager = context.getPackageManager();
                PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), 64);
                String var3 = "领英 配置正确，请检查领英后台签名:" + facebookHashKey(packageInfo);
                return var3;
            } catch (NameNotFoundException var5) {
                return "签名获取失败";
            }
        }
    }

    public static String checkKakao(Context context) {
        String pkName = context.getPackageName();
        PackageManager manager = context.getPackageManager();
        if (TextUtils.isEmpty(pkName)) {
            return "包名为空";
        } else {
            try {
                manager = context.getPackageManager();
                PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), 64);
                String var3 = "kakao 配置正确，请检查kakao后台签名:" + facebookHashKey(packageInfo);
                return var3;
            } catch (NameNotFoundException var5) {
                return "签名获取失败";
            }
        }
    }

    public static void checkQQ(Context context) {
        showDialog(context, checkQQByself(context));
    }

    public static void checkFacebook(Context context) {
        showDialog(context, checkFBByself(context));
    }

    public static void checkVK(Context context) {
        showDialog(context, checkVKByself(context));
    }
}
