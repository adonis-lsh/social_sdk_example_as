package com.umeng.socialize;


public class SocializeException
        extends RuntimeException {
    private static final long b = 1L;


    protected int a = 5000;
    private String c = "";

    public int getErrorCode() {
        return this.a;
    }

    public SocializeException(int paramInt, String paramString) {
        super(paramString);
        this.a = paramInt;
        this.c = paramString;
    }

    public SocializeException(String paramString, Throwable paramThrowable) {
        super(paramString, paramThrowable);
        this.c = paramString;
    }

    public SocializeException(String paramString) {
        super(paramString);
        this.c = paramString;
    }

    public String getMessage() {
        return this.c;
    }
}

