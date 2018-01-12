//
// Source code recreated from BitmapUtil .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.umeng.socialize.common;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;

import com.umeng.socialize.utils.UmengText;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public final class ResContainer {
    private static ResContainer R = null;
    private Map<String, Integer> map = new HashMap();
    private Context context = null;
    private Map<String, ResContainer.SocializeResource> mResources;
    private static String mPackageName = "";

    private ResContainer(Context var1) {
        this.context = var1.getApplicationContext();
    }

    public static synchronized ResContainer get(Context var0) {
        if (R == null) {
            R = new ResContainer(var0);
        }

        return R;
    }

    public int layout(String var1) {
        return getResourceId(this.context, "layout", var1);
    }

    public int id(String var1) {
        return getResourceId(this.context, "id", var1);
    }

    public int drawable(String var1) {
        return getResourceId(this.context, "drawable", var1);
    }

    public int style(String var1) {
        return getResourceId(this.context, "style", var1);
    }

    public int string(String var1) {
        return getResourceId(this.context, "string", var1);
    }

    public int color(String var1) {
        return getResourceId(this.context, "color", var1);
    }

    public int dimen(String var1) {
        return getResourceId(this.context, "dimen", var1);
    }

    public int raw(String var1) {
        return getResourceId(this.context, "raw", var1);
    }

    public int anim(String var1) {
        return getResourceId(this.context, "anim", var1);
    }

    public int styleable(String var1) {
        return getResourceId(this.context, "styleable", var1);
    }

    public ResContainer(Context var1, Map<String, ResContainer.SocializeResource> var2) {
        this.mResources = var2;
        this.context = var1;
    }

    public static int getResourceId(Context var0, String var1, String var2) {
        Resources var3 = var0.getResources();
        if (TextUtils.isEmpty(mPackageName)) {
            mPackageName = var0.getPackageName();
        }

        int var4 = var3.getIdentifier(var2, var1, mPackageName);
        if (var4 <= 0) {
            throw new RuntimeException(UmengText.errorWithUrl(UmengText.resError(mPackageName, var1, var2), "https://at.umeng.com/KzKfWz?cid=476"));
        } else {
            return var4;
        }
    }

    public static String getString(Context var0, String var1) {
        int var2 = getResourceId(var0, "string", var1);
        return var0.getString(var2);
    }

    public synchronized Map<String, ResContainer.SocializeResource> batch() {
        if (this.mResources == null) {
            return this.mResources;
        } else {
            Set var1 = this.mResources.keySet();

            ResContainer.SocializeResource var4;
            for (Iterator var2 = var1.iterator(); var2.hasNext(); var4.mIsCompleted = true) {
                String var3 = (String) var2.next();
                var4 = (ResContainer.SocializeResource) this.mResources.get(var3);
                var4.mId = getResourceId(this.context, var4.mType, var4.mName);
            }

            return this.mResources;
        }
    }

    public static int[] getStyleableArrts(Context var0, String var1) {
        return getResourceDeclareStyleableIntArray(var0, var1);
    }

    private static final int[] getResourceDeclareStyleableIntArray(Context var0, String var1) {
        try {
            Field[] var2 = Class.forName(var0.getPackageName() + ".R$styleable").getFields();
            Field[] var3 = var2;
            int var4 = var2.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                Field var6 = var3[var5];
                if (var6.getName().equals(var1)) {
                    int[] var7 = (int[]) ((int[]) var6.get((Object) null));
                    return var7;
                }
            }
        } catch (Throwable var8) {
            var8.printStackTrace();
        }

        return null;
    }

    public static class SocializeResource {
        public String mType;
        public String mName;
        public boolean mIsCompleted = false;
        public int mId;

        public SocializeResource(String var1, String var2) {
            this.mType = var1;
            this.mName = var2;
        }
    }
}
