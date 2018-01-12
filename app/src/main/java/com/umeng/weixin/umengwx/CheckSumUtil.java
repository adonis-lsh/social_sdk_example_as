package com.umeng.weixin.umengwx;

import java.security.MessageDigest;

public class CheckSumUtil {
    public static byte[] checkSum(String contentUrl, int appid, String packageName) {
        StringBuffer localStringBuffer = new StringBuffer();
        if (contentUrl != null) {
            localStringBuffer.append(contentUrl);
        }
        localStringBuffer.append(appid);
        localStringBuffer.append(packageName);
        localStringBuffer.append("mMcShCsTr");
        return ToMD5(localStringBuffer.toString().substring(1, 9).getBytes()).getBytes();
    }

    public static final String ToMD5(byte[] paramArrayOfByte) {
        char[] arrayOfChar1 = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest localMessageDigest;
            (localMessageDigest = MessageDigest.getInstance("MD5")).update(paramArrayOfByte);
            int i;
            char[] arrayOfChar2 = new char[(i = (paramArrayOfByte = localMessageDigest.digest()).length) * 2];
            int j = 0;
            for (int k = 0; k < i; k++) {
                int m = paramArrayOfByte[k];
                arrayOfChar2[(j++)] = arrayOfChar1[(m >>> 4 & 0xF)];
                arrayOfChar2[(j++)] = arrayOfChar1[(m & 0xF)];
            }
            return new String(arrayOfChar2);
        } catch (Exception localException) {
        }
        return null;
    }
}