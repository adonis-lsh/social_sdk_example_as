package com.umeng.socialize;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.text.TextUtils;

import com.umeng.socialize.uploadlog.UMLog;
import com.umeng.socialize.utils.ContextUtil;


public final class UMShareConfig {
    public boolean isNeedAuthOnGetUserInfo;
    public static final int LINED_IN_BASE_PROFILE = 0;
    public static final int LINED_IN_FULL_PROFILE = 1;
    private int linkedInProfileScope;
    private boolean isOpenShareEditActivity = true;
    private String platformName = "";

    private boolean isHideQzoneOnQQFriendList;

    public static final int LINKED_IN_FRIEND_SCOPE_ANYONE = 0;

    public static final int LINKED_IN_FRIEND_SCOPE_CONNECTIONS = 1;

    private int linkedInFriendScope;

    public static final int AUTH_TYPE_SSO = 1;

    public static final int AUTH_TYPE_WEBVIEW = 2;

    private int sinaAuthType;
    private int facebookAuthType;
    public static final int KAKAO_TALK = 0;
    public static final int KAKAO_STORY = 1;
    public static final int KAKAO_ACCOUNT = 2;
    private int kakaoAuthType;

    public UMShareConfig() {
        setShareToLinkedInFriendScope(Config.LinkedInShareCode);
        setShareToQQFriendQzoneItemHide(Config.QQWITHQZONE == 2);
        setShareToQQPlatformName(Config.appName);
        setSinaAuthType(1);
        setFacebookAuthType(1);
        setKaKaoAuthType(Config.KaKaoLoginType);
        isNeedAuthOnGetUserInfo(Config.isNeedAuth);
        setLinkedInProfileScope(Config.LinkedInProfileScope);
    }


    public UMShareConfig isNeedAuthOnGetUserInfo(boolean paramBoolean) {

        this.isNeedAuthOnGetUserInfo = paramBoolean;

        return this;
    }


    public UMShareConfig setLinkedInProfileScope(int paramInt) {

        if ((paramInt == 0) || (paramInt == 1)) {

            this.linkedInProfileScope = paramInt;
        }

        return this;
    }


    public UMShareConfig isOpenShareEditActivity(boolean paramBoolean) {

        this.isOpenShareEditActivity = paramBoolean;

        UMLog.setIsOpenShareEdit(paramBoolean);

        return this;
    }


    public UMShareConfig setShareToQQPlatformName(String paramString) {

        if (!TextUtils.isEmpty(paramString)) {

            this.platformName = paramString;
        }

        return this;
    }


    public UMShareConfig setShareToQQFriendQzoneItemHide(boolean paramBoolean) {

        this.isHideQzoneOnQQFriendList = paramBoolean;

        return this;
    }


    public UMShareConfig setShareToLinkedInFriendScope(int paramInt) {

        if ((paramInt == 0) || (paramInt == 1)) {

            this.linkedInFriendScope = paramInt;
        }

        return this;
    }


    public UMShareConfig setSinaAuthType(int paramInt) {
        if ((paramInt == 1) || (paramInt == 2)) {
            this.sinaAuthType = paramInt;
        }
        return this;
    }


    public UMShareConfig setFacebookAuthType(int paramInt) {
        if ((paramInt == 1) || (paramInt == 2)) {
            this.facebookAuthType = paramInt;
        }
        return this;
    }


    public UMShareConfig setKaKaoAuthType(int paramInt) {
        if ((paramInt == 0) || (paramInt == 2) || (paramInt == 1)) {
            this.kakaoAuthType = paramInt;
        }
        return this;
    }

    public final String getAppName() {
        if (TextUtils.isEmpty(this.platformName)) {
            Context localContext = ContextUtil.getContext();
            if (localContext != null) {
                CharSequence localCharSequence = localContext.getApplicationInfo().loadLabel(localContext.getPackageManager());
                if (!TextUtils.isEmpty(localCharSequence)) {
                    this.platformName = localCharSequence.toString();
                }
            }
        }
        return this.platformName;
    }

    public final boolean isHideQzoneOnQQFriendList() {
        return this.isHideQzoneOnQQFriendList;
    }

    public final boolean isLinkedInShareToAnyone() {
        return this.linkedInFriendScope == 0;
    }

    public final boolean isLinkedInProfileBase() {
        return this.linkedInProfileScope == 0;
    }

    public final boolean isKakaoAuthWithTalk() {
        return this.kakaoAuthType == 0;
    }

    public final boolean isKakaoAuthWithStory() {
        return this.kakaoAuthType == 1;
    }

    public final boolean isKakaoAuthWithAccount() {
        return this.kakaoAuthType == 2;
    }

    public final boolean isSinaAuthWithWebView() {
        return this.sinaAuthType == 2;
    }

    public final boolean isFacebookAuthWithWebView() {
        return this.facebookAuthType == 2;
    }

    public final boolean isNeedAuthOnGetUserInfo() {
        return this.isNeedAuthOnGetUserInfo;
    }

    public final boolean isOpenShareEditActivity() {
        return this.isOpenShareEditActivity;

    }
}
