package com.umeng.socialize;


public class Config {
    @Deprecated
    public static boolean OpenEditor = true;
    public static String UID = null;
    public static String EntityKey = null;
    public static String EntityName = "share";
    public static Boolean isUmengSina = Boolean.valueOf(true);
    public static Boolean isUmengWx = Boolean.valueOf(true);
    public static Boolean isUmengQQ = Boolean.valueOf(true);
    public static String Descriptor = "com.umeng.share";
    public static String SessionId = null;
    @Deprecated
    public static int QQWITHQZONE = 2;
    @Deprecated
    public static String QQAPPNAME = "";

    public static final boolean mEncrypt = true;

    public static boolean DEBUG = false;


    public static String shareType = "native";

    @Deprecated
    public static int KaKaoLoginType = 0;


    public static String MORE_TITLE = "分享";


    @Deprecated
    public static int LinkedInProfileScope = 0;
    @Deprecated
    public static int LinkedInShareCode = 0;

    public static int connectionTimeOut = 30000;
    public static int readSocketTimeOut = 30000;
    @Deprecated
    public static String appName;
    @Deprecated
    public static boolean isNeedAuth = false;

    public static boolean isJumptoAppStore = false;
    public static boolean isFacebookRead = false;
}
