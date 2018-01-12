//
// Source code recreated from BitmapUtil .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.umeng.socialize.net.utils;

import android.net.Uri.Builder;
import android.text.TextUtils;

import com.umeng.socialize.Config;
import com.umeng.socialize.net.utils.URequest.FilePair;
import com.umeng.socialize.net.utils.URequest.PostStyle;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.UmengText;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;

public class UClient {
    private static final String TAG = "UClient";
    private static final String END = "\r\n";

    public UClient() {
    }

    public <T extends UResponse> T execute(URequest var1, Class<T> var2) {
        var1.onPrepareRequest();
        String var3 = var1.getHttpMethod().trim();
        this.verifyMethod(var3);
        UClient.ResponseObj var4 = null;
        if (URequest.GET.equals(var3)) {
            var4 = this.httpGetRequest(var1);
        } else if (URequest.POST.equals(var3)) {
            var4 = this.httpPostRequest(var1);
        }

        UResponse var5 = this.createResponse(var4, var2);
        return (T) var5;
    }

    protected <T extends UResponse> T createResponse(UClient.ResponseObj var1, Class<T> var2) {
        if (var1 == null) {
            return null;
        } else {
            try {
                Constructor var3 = var2.getConstructor(new Class[]{Integer.class, JSONObject.class});
                return (T) var3.newInstance(new Object[]{Integer.valueOf(var1.httpResponseCode), var1.jsonObject});
            } catch (SecurityException var5) {
                Log.e("UClient", "SecurityException", var5);
            } catch (NoSuchMethodException var6) {
                Log.e("UClient", "NoSuchMethodException", var6);
            } catch (IllegalArgumentException var7) {
                Log.e("UClient", "IllegalArgumentException", var7);
            } catch (InstantiationException var8) {
                Log.e("UClient", "InstantiationException", var8);
            } catch (IllegalAccessException var9) {
                Log.e("UClient", "IllegalAccessException", var9);
            } catch (InvocationTargetException var10) {
                Log.e("UClient", "InvocationTargetException", var10);
            }

            return null;
        }
    }

    private UClient.ResponseObj httpPostRequest(URequest var1) {
        String var2 = var1.toJson() == null ? "" : var1.toJson().toString();
        Log.net("URequest  = " + var1.getClass().getName());
        String var3 = UUID.randomUUID().toString();
        HttpURLConnection var4 = null;
        Object var5 = null;
        InputStream var6 = null;

        try {
            var4 = this.openUrlConnection(var1);
            Map var7;
            if (var4 == null) {
                var7 = null;
                return (ResponseObj) var7;
            }

            var7 = var1.getBodyPair();
            String var10;
            if (var1.mMimeType != null) {
                String var20 = (String) var7.get("data");
                var4.setRequestProperty("Content-Type", var1.mMimeType.toString());
                var5 = var4.getOutputStream();
                ((OutputStream) var5).write(var20.getBytes());
            } else {
                Builder var8;
                String var9;
                if (var1.postStyle == PostStyle.APPLICATION) {
                    Log.net("message:" + var2);
                    var4.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    var8 = new Builder();

                    try {
                        Iterator var22 = var7.keySet().iterator();

                        while (var22.hasNext()) {
                            var10 = (String) var22.next();
                            var8.appendQueryParameter(var10, var7.get(var10).toString());
                        }
                    } catch (Throwable var17) {
                        Log.um(UmengText.UPLOADFAIL + "[" + var17.getMessage() + "]");
                    }

                    var9 = var8.build().getEncodedQuery();
                    var5 = new DataOutputStream(var4.getOutputStream());
                    if (var9 != null) {
                        ((OutputStream) var5).write(var9.getBytes());
                    }
                } else if ((var7 == null || var7.size() <= 0) && var1.postStyle != PostStyle.MULTIPART) {
                    Log.net("message:" + var2);
                    var4.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    var8 = new Builder();
                    var8.appendQueryParameter("content", var2);
                    var9 = var8.build().getEncodedQuery();
                    var5 = new DataOutputStream(var4.getOutputStream());
                    ((OutputStream) var5).write(var9.getBytes());
                } else {
                    var4.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + var3);
                    var5 = var4.getOutputStream();
                    this.addBodyParams(var1, (OutputStream) var5, var3);
                }
            }

            ((OutputStream) var5).flush();
            int var21 = var4.getResponseCode();
            UClient.ResponseObj var23 = new UClient.ResponseObj();
            var23.httpResponseCode = var21;
            if (var21 == 200) {
                var6 = var4.getInputStream();
                var10 = var4.getContentEncoding();
                JSONObject var11 = this.parseResult(var1, var4.getRequestMethod(), var10, var6);
                Log.net("requestMethod:POST;json data:" + var11);
                var23.jsonObject = var11;
                UClient.ResponseObj var12 = var23;
                return var12;
            }

//            var10 = null;
            return null;
        } catch (IOException var18) {
            Log.e("UClient", "Caught Exception in httpPostRequest()", var18);
        } finally {
            this.closeQuietly(var6);
            this.closeQuietly((Closeable) var5);
            if (var4 != null) {
                var4.disconnect();
            }

        }

        return null;
    }

    private UClient.ResponseObj httpGetRequest(URequest var1) {
        InputStream var2 = null;
        HttpURLConnection var3 = null;
        Log.net("URequest  = " + var1.getClass().getName());

        String var6;
        try {
            var3 = this.openUrlConnection(var1);
            if (var3 == null) {
                Object var14 = null;
                return (UClient.ResponseObj) var14;
            }

            int var4 = var3.getResponseCode();
            UClient.ResponseObj var5 = new UClient.ResponseObj();
            var5.httpResponseCode = var4;
            if (var4 == 200) {
                var2 = var3.getInputStream();
                var6 = var3.getContentEncoding();
                JSONObject var7 = this.parseResult(var1, var3.getRequestMethod(), var6, var2);
                var5.jsonObject = var7;
                Log.net("result  = " + var7);
                UClient.ResponseObj var8 = var5;
                return var8;
            }

            var6 = null;
        } catch (Exception var12) {
            Log.e("UClient", "Caught Exception in httpGetRequest()", var12);
            return null;
        } finally {
            this.closeQuietly(var2);
            if (var3 != null) {
                var3.disconnect();
            }

        }

        return null;
    }

    private HttpURLConnection openUrlConnection(URequest var1) throws IOException {
        String var2 = var1.getHttpMethod().trim();
        String var3 = null;
        if (URequest.GET.equals(var2)) {
            var3 = var1.toGetUrl();
        } else if (URequest.POST.equals(var2)) {
            var3 = var1.mBaseUrl;
        }

        if (TextUtils.isEmpty(var3)) {
            return null;
        } else {
            URL var4 = new URL(var3);
            String var5 = var4.getProtocol();
            boolean var6 = false;
            if ("https".equals(var5)) {
                var6 = true;
            }

            Object var7;
            if (var6) {
                var7 = (HttpsURLConnection) var4.openConnection();
            } else {
                var7 = (HttpURLConnection) var4.openConnection();
            }

            ((HttpURLConnection) var7).setConnectTimeout(Config.connectionTimeOut);
            ((HttpURLConnection) var7).setReadTimeout(Config.readSocketTimeOut);
            ((HttpURLConnection) var7).setRequestMethod(var2);
            if (URequest.GET.equals(var2)) {
                ((HttpURLConnection) var7).setRequestProperty("Accept-Encoding", "gzip");
                if (var1.mHeaders != null && var1.mHeaders.size() > 0) {
                    Set var8 = var1.mHeaders.keySet();
                    Iterator var9 = var8.iterator();

                    while (var9.hasNext()) {
                        String var10 = (String) var9.next();
                        ((HttpURLConnection) var7).setRequestProperty(var10, (String) var1.mHeaders.get(var10));
                    }
                }
            } else if (URequest.POST.equals(var2)) {
                ((HttpURLConnection) var7).setDoOutput(true);
                ((HttpURLConnection) var7).setDoInput(true);
            }

            return (HttpURLConnection) var7;
        }
    }

    private void verifyMethod(String var1) {
        if (TextUtils.isEmpty(var1) || !(URequest.GET.equals(var1.trim()) ^ URequest.POST.equals(var1.trim()))) {
            throw new RuntimeException(UmengText.netMethodError(var1));
        }
    }

    private void addBodyParams(URequest var1, OutputStream var2, String var3) throws IOException {
        boolean var4 = false;
        StringBuilder var5 = new StringBuilder();
        Map var6 = var1.getBodyPair();
        Set var7 = var6.keySet();
        Iterator var8 = var7.iterator();

        while (var8.hasNext()) {
            String var9 = (String) var8.next();
            if (var6.get(var9) != null) {
                this.addFormField(var5, var9, var6.get(var9).toString(), var3);
            }
        }

        if (var5.length() > 0) {
            var4 = true;
            var2 = new DataOutputStream((OutputStream) var2);
            ((OutputStream) var2).write(var5.toString().getBytes());
        }

        Map var14 = var1.getFilePair();
        if (var14 != null && var14.size() > 0) {
            Set var15 = var14.keySet();
            Iterator var10 = var15.iterator();

            while (var10.hasNext()) {
                String var11 = (String) var10.next();
                FilePair var12 = (FilePair) var14.get(var11);
                byte[] var13 = var12.mBinaryData;
                if (var13 != null && var13.length >= 1) {
                    var4 = true;
                    this.addFilePart(var12.mFileName, var13, var3, (OutputStream) var2);
                }
            }
        }

        if (var4) {
            this.finishWrite((OutputStream) var2, var3);
        }

    }

    private void addFormField(StringBuilder var1, String var2, String var3, String var4) {
        var1.append("--").append(var4).append("\r\n").append("Content-Disposition: form-data; name=\"").append(var2).append("\"").append("\r\n").append("Content-Type: text/plain; charset=").append("UTF-8").append("\r\n").append("\r\n").append(var3).append("\r\n");
    }

    private void addFilePart(String var1, byte[] var2, String var3, OutputStream var4) throws IOException {
        StringBuilder var5 = new StringBuilder();
        var5.append("--").append(var3).append("\r\n").append("Content-Disposition: form-data; name=\"").append("pic").append("\"; filename=\"").append(var1).append("\"").append("\r\n").append("Content-Type: ").append("application/octet-stream").append("\r\n").append("Content-Transfer-Encoding: binary").append("\r\n").append("\r\n");
        var4.write(var5.toString().getBytes());
        var4.write(var2);
        var4.write("\r\n".getBytes());
    }

    private void finishWrite(OutputStream var1, String var2) throws IOException {
        var1.write("\r\n".getBytes());
        var1.write(("--" + var2 + "--").getBytes());
        var1.write("\r\n".getBytes());
        var1.flush();
        var1.close();
    }

    protected JSONObject parseResult(URequest var1, String var2, String var3, InputStream var4) {
        InputStream var5 = null;

        JSONObject var7;
        try {
            var5 = this.wrapStream(var3, var4);
            String var6 = this.convertStreamToString(var5);
            Log.net("requestMethod:" + var2 + ";origin data:" + var6);
            if ("POST".equals(var2)) {
                try {
                    var7 = new JSONObject(var6);
                    return var7;
                } catch (Exception var13) {
                    JSONObject var8 = this.decryptData(var1, var6);
                    return var8;
                }
            }

            if (!"GET".equals(var2)) {
                return null;
            }

            if (!TextUtils.isEmpty(var6)) {
                var7 = this.decryptData(var1, var6);
                return var7;
            }

            var7 = null;
        } catch (IOException var14) {
            Log.e("UClient", "Caught IOException in parseResult()", var14);
            return null;
        } finally {
            this.closeQuietly(var5);
        }

        return var7;
    }

    protected InputStream wrapStream(String var1, InputStream var2) throws IOException {
        if (var1 != null && !"identity".equalsIgnoreCase(var1)) {
            if ("gzip".equalsIgnoreCase(var1)) {
                return new GZIPInputStream(var2);
            } else if ("deflate".equalsIgnoreCase(var1)) {
                return new InflaterInputStream(var2, new Inflater(false), 512);
            } else {
                throw new RuntimeException("unsupported content-encoding: " + var1);
            }
        } else {
            return var2;
        }
    }

    protected String convertStreamToString(InputStream var1) {
        InputStreamReader var2 = new InputStreamReader(var1);
        BufferedReader var3 = new BufferedReader(var2, 512);
        StringBuilder var4 = new StringBuilder();

        Object var6;
        try {
            String var5;
            while ((var5 = var3.readLine()) != null) {
                var4.append(var5 + "\n");
            }

            return var4.toString();
        } catch (IOException var10) {
            Log.e("UClient", "Caught IOException in convertStreamToString()", var10);
            var6 = null;
        } finally {
            this.closeQuietly(var2);
            this.closeQuietly(var3);
        }

        return (String) var6;
    }

    private JSONObject decryptData(URequest var1, String var2) {
        try {
            String var3 = var1.getDecryptString(var2);
            return new JSONObject(var3);
        } catch (Throwable var4) {
            return null;
        }
    }

    protected void closeQuietly(Closeable var1) {
        try {
            if (var1 != null) {
                var1.close();
            }
        } catch (IOException var3) {
            Log.e("UClient", "Caught IOException in closeQuietly()", var3);
        }

    }

    protected static class ResponseObj {
        public JSONObject jsonObject;
        public int httpResponseCode;

        protected ResponseObj() {
        }
    }
}
