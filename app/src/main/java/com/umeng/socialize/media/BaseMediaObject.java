//
// Source code recreated from BitmapUtil .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.umeng.socialize.media;

import android.os.Parcel;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseMediaObject implements UMediaObject {
    public String mText = null;
    protected String url = "";
    protected String b = "";
    protected Map<String, Object> c = new HashMap();
    protected String d = "";
    protected UMImage e;

    public BaseMediaObject() {
    }

    public void setThumb(UMImage var1) {
        this.e = var1;
    }

    public BaseMediaObject(String var1) {
        this.url = var1;
    }

    public String getDescription() {
        return this.d;
    }

    public Map<String, Object> getmExtra() {
        return this.c;
    }

    public void setmExtra(String var1, Object var2) {
        this.c.put(var1, var2);
    }

    public void setDescription(String var1) {
        this.d = var1;
    }

    public String toUrl() {
        return this.url;
    }

    public UMImage getThumbImage() {
        return this.e;
    }

    public boolean isUrlMedia() {
        return !TextUtils.isEmpty(this.url);
    }

    public String getTitle() {
        return this.b;
    }

    public void setTitle(String var1) {
        this.b = var1;
    }

    protected BaseMediaObject(Parcel var1) {
        if (var1 != null) {
            this.url = var1.readString();
            this.b = var1.readString();
        }

    }

    public String toString() {
        return "BaseMediaObject [media_url=" + this.url + ", qzone_title=" + this.b + ", qzone_thumb=" + "]";
    }
}
