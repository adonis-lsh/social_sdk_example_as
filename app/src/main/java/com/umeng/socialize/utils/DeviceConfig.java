//
// Source code recreated from BitmapUtil .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.umeng.socialize.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Environment;
import android.os.Build.VERSION;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class DeviceConfig {
    protected static final String LOG_TAG = "DeviceConfig";
    protected static final String UNKNOW = "Unknown";
    private static final String MOBILE_NETWORK = "2G/3G";
    private static final String WIFI = "Wi-Fi";
    public static Context context;
    private static Object object = new Object();

    public DeviceConfig() {
    }

    public static boolean isAppInstalled(String var0, Context var1) {
        if (var1 == null) {
            return false;
        } else {
            Object var2 = object;
            synchronized (object) {
                boolean var3 = false;

                try {
                    PackageManager var4 = var1.getPackageManager();
                    var4.getPackageInfo(var0, 1);
                    var3 = true;
                } catch (NameNotFoundException var6) {
                    var3 = false;
                } catch (RuntimeException var7) {
                    var3 = false;
                }

                return var3;
            }
        }
    }

    public static String getAppVersion(String var0, Context var1) {
        if (var1 == null) {
            return "";
        } else {
            try {
                PackageManager var2 = var1.getPackageManager();
                PackageInfo var3 = var2.getPackageInfo(var0, 0);
                String var4 = var3.versionName;
                return var4;
            } catch (Exception var5) {
                return null;
            }
        }
    }

    public static boolean checkPermission(Context var0, String var1) {
        if (var0 == null) {
            return false;
        } else if (var0 == null) {
            return false;
        } else {
            PackageManager var2 = var0.getPackageManager();
            return var2.checkPermission(var1, var0.getPackageName()) == 0;
        }
    }

    public static String getDeviceId(Context var0) {
        if (var0 == null) {
            return "";
        } else {
            TelephonyManager var1 = (TelephonyManager) var0.getSystemService("phone");
            if (var1 == null) {
                Log.w("DeviceConfig", "No IMEI.");
            }

            String var2 = "";

            try {
                if (checkPermission(var0, "android.permission.READ_PHONE_STATE")) {
                    var2 = var1.getDeviceId();
                }
            } catch (Exception var4) {
                Log.w("DeviceConfig", "No IMEI.", var4);
            }

            if (TextUtils.isEmpty(var2)) {
                Log.w("DeviceConfig", "No IMEI.");
                var2 = getMac(var0);
                if (TextUtils.isEmpty(var2)) {
                    Log.w("DeviceConfig", "Failed to take mac as IMEI. Try to use Secure.ANDROID_ID instead.");
                    var2 = Secure.getString(var0.getContentResolver(), "android_id");
                    Log.w("DeviceConfig", "getDeviceId: Secure.ANDROID_ID: " + var2);
                    if (TextUtils.isEmpty(var2)) {
                        var2 = getDeviceSN();
                        return var2;
                    }
                }
            }

            return var2;
        }
    }

    public static String getDeviceSN() {
        String var0 = null;

        try {
            Class var1 = Class.forName("android.os.SystemProperties");
            Method var2 = var1.getMethod("get", new Class[]{String.class, String.class});
            var0 = (String) ((String) var2.invoke(var1, new Object[]{"ro.serialno", "unknown"}));
        } catch (Exception var3) {
            ;
        }

        return var0;
    }

    public static String[] getNetworkAccessMode(Context var0) {
        if (var0 == null) {
            return new String[]{"", ""};
        } else {
            String[] var1 = new String[]{"Unknown", "Unknown"};
            PackageManager var2 = var0.getPackageManager();
            if (var2.checkPermission("android.permission.ACCESS_NETWORK_STATE", var0.getPackageName()) != 0) {
                var1[0] = "Unknown";
                return var1;
            } else {
                ConnectivityManager var3 = (ConnectivityManager) var0.getSystemService("connectivity");
                if (var3 == null) {
                    var1[0] = "Unknown";
                    return var1;
                } else {
                    NetworkInfo var4 = var3.getNetworkInfo(1);
                    if (var4 != null && var4.getState() == State.CONNECTED) {
                        var1[0] = "Wi-Fi";
                        return var1;
                    } else {
                        NetworkInfo var5 = var3.getNetworkInfo(0);
                        if (var5 != null && var5.getState() == State.CONNECTED) {
                            var1[0] = "2G/3G";
                            var1[1] = var5.getSubtypeName();
                            return var1;
                        } else {
                            return var1;
                        }
                    }
                }
            }
        }
    }

    public static boolean isOnline(Context var0) {
        if (var0 == null) {
            return false;
        } else {
            try {
                ConnectivityManager var1 = (ConnectivityManager) var0.getSystemService("connectivity");
                NetworkInfo var2 = var1.getActiveNetworkInfo();
                return var2 != null ? var2.isConnectedOrConnecting() : false;
            } catch (Exception var3) {
                return true;
            }
        }
    }

    public static boolean isNetworkAvailable(Context var0) {
        return var0 == null ? false : checkPermission(var0, "android.permission.ACCESS_NETWORK_STATE") && isOnline(var0);
    }

    public static boolean isSdCardWrittenable() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static String getAndroidID(Context var0) {
        return var0 == null ? "" : Secure.getString(var0.getContentResolver(), "android_id");
    }

    public static String getOsVersion() {
        return VERSION.RELEASE;
    }

    public static String getMac(Context var0) {
        if (var0 == null) {
            return "";
        } else {
            String var1 = "";

            try {
                var1 = getMacShell();
                if (TextUtils.isEmpty(var1)) {
                    Log.w("DeviceConfig", "Could not get mac address.[no permission android.permission.ACCESS_WIFI_STATE");
                }

                if (TextUtils.isEmpty(var1)) {
                    var1 = getMacByJava();
                    if (TextUtils.isEmpty(var1)) {
                        Log.w("DeviceConfig", "Could not get mac address by java");
                    }
                }

                if (TextUtils.isEmpty(var1)) {
                    var1 = SocializeSpUtils.getMac(var0);
                } else {
                    SocializeSpUtils.putMac(var0, var1);
                }

                return var1;
            } catch (Exception var3) {
                Log.w("DeviceConfig", "Could not get mac address." + var3.toString());
                return var1;
            }
        }
    }

    private static String getMacByJava() {
        try {
            Enumeration var0 = NetworkInterface.getNetworkInterfaces();

            while (var0.hasMoreElements()) {
                NetworkInterface var1 = (NetworkInterface) var0.nextElement();
                if (var1.getName().equals("wlan0")) {
                    byte[] var2 = var1.getHardwareAddress();
                    if (var2 != null && var2.length != 0) {
                        StringBuilder var3 = new StringBuilder();
                        byte[] var4 = var2;
                        int var5 = var2.length;

                        for (int var6 = 0; var6 < var5; ++var6) {
                            byte var7 = var4[var6];
                            var3.append(String.format("%02X:", new Object[]{Byte.valueOf(var7)}));
                        }

                        if (var3.length() > 0) {
                            var3.deleteCharAt(var3.length() - 1);
                        }

                        String var9 = var3.toString();
                        return var9;
                    }

                    return null;
                }
            }
        } catch (Exception var8) {
            ;
        }

        return null;
    }

    public static String getPackageName(Context var0) {
        return var0 == null ? "" : var0.getPackageName();
    }

    private static String getMacShell() {
        String[] var0 = new String[]{"/sys/class/net/wlan0/address", "/sys/class/net/eth0/address", "/sys/devices/virtual/net/wlan0/address"};

        for (int var2 = 0; var2 < var0.length; ++var2) {
            try {
                String var1 = reaMac(var0[var2]);
                if (var1 != null) {
                    return var1;
                }
            } catch (Exception var4) {
                ;
            }
        }

        return null;
    }

    private static String reaMac(String var0) throws FileNotFoundException {
        String var1 = null;
        FileReader var2 = new FileReader(var0);
        BufferedReader var3 = null;
        if (var2 != null) {
            try {
                var3 = new BufferedReader(var2, 1024);
                var1 = var3.readLine();
            } catch (IOException var17) {
                ;
            } finally {
                if (var2 != null) {
                    try {
                        var2.close();
                    } catch (IOException var16) {
                        ;
                    }
                }

                if (var3 != null) {
                    try {
                        var3.close();
                    } catch (IOException var15) {
                        ;
                    }
                }

            }
        }

        return var1;
    }
}
