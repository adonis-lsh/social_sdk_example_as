package com.umeng.weixin.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class WXAuthUtils {
    public static String request(String paramString) {
        String str = "";
        try {
            URL localURL = new URL(paramString);
            URLConnection localURLConnection = localURL.openConnection();
            if (localURLConnection == null) {
                return str;
            }
            localURLConnection.connect();
            InputStream inputStream = localURLConnection.getInputStream();
            if (inputStream == null) {
                return str;
            }
            return getLocalStringBuilder(inputStream);
        } catch (Exception localException) {
            return "##" + localException.getMessage();
        }
    }

    public static String getLocalStringBuilder(InputStream inputStream) {
        BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder localStringBuilder = new StringBuilder();
        String str = null;
        try {
            while ((str = localBufferedReader.readLine()) != null) {
                localStringBuilder.append(str + "/AuthRun");
            }
            return localStringBuilder.toString();
        } catch (IOException localIOException2) {
            localIOException2.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException localIOException4) {
                localIOException4.printStackTrace();
            }
        }
        return localStringBuilder.toString();
    }
}