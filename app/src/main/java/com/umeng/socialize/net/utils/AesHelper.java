package com.umeng.socialize.net.utils;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class AesHelper {
    private static byte[] pwd = null;
    private static byte[] iv = "nmeug.f9/Om+L823".getBytes();

    private static final String UTF_8 = "UTF-8";

    public static String encryptNoPadding(String paramString1, String paramString2)
            throws Exception {
        Cipher localCipher = Cipher.getInstance("AES/CBC/NoPadding");
        int i = localCipher.getBlockSize();
        byte[] arrayOfByte1 = paramString1.getBytes(paramString2);
        int j = arrayOfByte1.length;
        if (j % i != 0) {
            j += i - j % i;
        }
        byte[] arrayOfByte2 = new byte[j];
        System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, arrayOfByte1.length);

        SecretKeySpec localSecretKeySpec = new SecretKeySpec(pwd, "AES");
        IvParameterSpec localIvParameterSpec = new IvParameterSpec(iv);
        localCipher.init(1, localSecretKeySpec, localIvParameterSpec);
        byte[] arrayOfByte3 = localCipher.doFinal(arrayOfByte2);

        return Base64.encodeBase64String(arrayOfByte3);
    }


    public static String decryptNoPadding(String paramString1, String paramString2)
            throws Exception {
        Cipher localCipher = Cipher.getInstance("AES/CBC/NoPadding");
        SecretKeySpec localSecretKeySpec = new SecretKeySpec(pwd, "AES");
        IvParameterSpec localIvParameterSpec = new IvParameterSpec(iv);
        localCipher.init(2, localSecretKeySpec, localIvParameterSpec);
        byte[] arrayOfByte = localCipher.doFinal(Base64.decodeBase64(paramString1));


        return new String(arrayOfByte, paramString2);
    }


    public static void setPassword(String paramString) {
        if (!TextUtils.isEmpty(paramString)) {
            String str = md5(paramString);
            if (str.length() >= 16) {
                str = str.substring(0, 16);
            }
            pwd = str.getBytes();
        }
    }


    public static byte[] getBytesUtf8(String paramString) {
        return getBytesUnchecked(paramString, "UTF-8");
    }


    public static byte[] getBytesUnchecked(String paramString1, String paramString2) {
        if (paramString1 == null) {
            return null;
        }
        try {
            return paramString1.getBytes(paramString2);
        } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
            throw newIllegalStateException(paramString2, localUnsupportedEncodingException);
        }
    }

    private static IllegalStateException newIllegalStateException(String paramString, UnsupportedEncodingException paramUnsupportedEncodingException) {
        return new IllegalStateException(paramString + ": " + paramUnsupportedEncodingException);
    }


    public static String newString(byte[] paramArrayOfByte, String paramString) {
        if (paramArrayOfByte == null) {
            return null;
        }
        try {
            return new String(paramArrayOfByte, paramString);
        } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
            throw newIllegalStateException(paramString, localUnsupportedEncodingException);
        }
    }


    public static String newStringUtf8(byte[] paramArrayOfByte) {
        return newString(paramArrayOfByte, "UTF-8");
    }

    public static String md5(String paramString) {
        if (paramString == null) {
            return null;
        }
        try {
            byte[] arrayOfByte1 = paramString.getBytes();
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.reset();
            localMessageDigest.update(arrayOfByte1);
            byte[] arrayOfByte2 = localMessageDigest.digest();
            StringBuffer localStringBuffer = new StringBuffer();
            for (int i = 0; i < arrayOfByte2.length; i++) {
                localStringBuffer.append(String.format("%02X", new Object[]{Byte.valueOf(arrayOfByte2[i])}));
            }

            return localStringBuffer.toString();
        } catch (Exception localException) {
        }
        return paramString.replaceAll("[^[a-z][A-Z][0-9][.][_]]", "");
    }
}


/* Location:              /Users/zl/Desktop/未命名文件夹 3/umeng_social_net.jar!/com/umeng/socialize/net/utils/AesHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */