//
// Source code recreated from BitmapUtil .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.umeng.socialize.net.analytics;

import android.content.Context;

import com.umeng.socialize.media.UMediaObject;
import com.umeng.socialize.net.base.SocializeClient;
import com.umeng.socialize.utils.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class SocialAnalytics {
    private static SocializeClient a = new SocializeClient();
    private static ExecutorService b = Executors.newCachedThreadPool();

    public SocialAnalytics() {
    }

    public static void log(final Context var0, final String var1, final String var2, final UMediaObject var3) {
        Runnable var4 = new Runnable() {
            public void run() {
                AnalyticsRequest var1x = new AnalyticsRequest(var0, var1, var2);
                var1x.a(var3);
                ShareMultiResponse var2x = (ShareMultiResponse) SocialAnalytics.a.execute(var1x);
                if (var2x != null && var2x.isOk()) {
                    Log.d(" send log succeed");
                } else {
                    Log.d(" fail to send log");
                }

            }
        };
        a(var4);
    }

    private static void a(Runnable var0) {
        if (b != null && var0 != null) {
            b.submit(var0);
        }

    }
}
