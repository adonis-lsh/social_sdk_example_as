package com.umeng.socialize.net.utils;


public abstract class BaseNCodec {
    public static final int MIME_CHUNK_SIZE = 76;


    private static final int DEFAULT_BUFFER_RESIZE_FACTOR = 2;


    private static final int DEFAULT_BUFFER_SIZE = 8192;


    protected static final int MASK_8BITS = 255;


    protected static final byte PAD_DEFAULT = 61;


    protected static final byte PAD = 61;


    private final int mUnencodedBlockSize;


    private final int mEncodedBlockSize;


    protected final int mLineLength;


    private final int mChunkSeparatorLength;


    protected byte[] mBuffer;


    protected int mPos;


    private int mReadPos;


    protected boolean mEof;


    protected int mCurrentLinePos;


    protected int mModulus;


    protected BaseNCodec(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        this.mUnencodedBlockSize = paramInt1;
        this.mEncodedBlockSize = paramInt2;
        this.mLineLength = ((paramInt3 > 0) && (paramInt4 > 0) ? paramInt3 / paramInt2 * paramInt2 : 0);
        this.mChunkSeparatorLength = paramInt4;
    }


    boolean hasData() {
        return this.mBuffer != null;
    }


    int available() {
        return this.mBuffer != null ? this.mPos - this.mReadPos : 0;
    }


    protected int getDefaultBufferSize() {
        return 8192;
    }

    private void resizeBuffer() {
        if (this.mBuffer == null) {
            this.mBuffer = new byte[getDefaultBufferSize()];
            this.mPos = 0;
            this.mReadPos = 0;
        } else {
            byte[] arrayOfByte = new byte[this.mBuffer.length * 2];
            System.arraycopy(this.mBuffer, 0, arrayOfByte, 0, this.mBuffer.length);
            this.mBuffer = arrayOfByte;
        }
    }


    protected void ensureBufferSize(int paramInt) {
        if ((this.mBuffer == null) || (this.mBuffer.length < this.mPos + paramInt)) {
            resizeBuffer();
        }
    }


    int readResults(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        if (this.mBuffer != null) {
            int i = Math.min(available(), paramInt2);
            System.arraycopy(this.mBuffer, this.mReadPos, paramArrayOfByte, paramInt1, i);
            this.mReadPos += i;
            if (this.mReadPos >= this.mPos) {
                this.mBuffer = null;
            }
            return i;
        }
        return this.mEof ? -1 : 0;
    }


    protected static boolean isWhiteSpace(byte paramByte) {
        switch (paramByte) {
            case 9:
            case 10:
            case 13:
            case 32:
                return true;
        }
        return false;
    }


    private void reset() {
        this.mBuffer = null;
        this.mPos = 0;
        this.mReadPos = 0;
        this.mCurrentLinePos = 0;
        this.mModulus = 0;
        this.mEof = false;
    }


    public Object encode(Object paramObject)
            throws Exception {
        if (!(paramObject instanceof byte[])) {
            throw new Exception("Parameter supplied to ApiResponse-N encode is not BitmapUtil byte[]");
        }
        return encode((byte[]) paramObject);
    }


    public String encodeToString(byte[] paramArrayOfByte) {
        return AesHelper.newStringUtf8(encode(paramArrayOfByte));
    }


    public Object decode(Object paramObject)
            throws Exception {
        if ((paramObject instanceof byte[]))
            return decode((byte[]) paramObject);
        if ((paramObject instanceof String)) {
            return decode((String) paramObject);
        }
        throw new Exception("Parameter supplied to ApiResponse-N decode is not BitmapUtil byte[] or BitmapUtil String");
    }


    public byte[] decode(String paramString) {
        return decode(AesHelper.getBytesUtf8(paramString));
    }


    public byte[] decode(byte[] paramArrayOfByte) {
        reset();
        if ((paramArrayOfByte == null) || (paramArrayOfByte.length == 0)) {
            return paramArrayOfByte;
        }
        decode(paramArrayOfByte, 0, paramArrayOfByte.length);
        decode(paramArrayOfByte, 0, -1);
        byte[] arrayOfByte = new byte[this.mPos];
        readResults(arrayOfByte, 0, arrayOfByte.length);
        return arrayOfByte;
    }


    public byte[] encode(byte[] paramArrayOfByte) {
        reset();
        if ((paramArrayOfByte == null) || (paramArrayOfByte.length == 0)) {
            return paramArrayOfByte;
        }
        encode(paramArrayOfByte, 0, paramArrayOfByte.length);
        encode(paramArrayOfByte, 0, -1);
        byte[] arrayOfByte = new byte[this.mPos - this.mReadPos];
        readResults(arrayOfByte, 0, arrayOfByte.length);
        return arrayOfByte;
    }


    public String encodeAsString(byte[] paramArrayOfByte) {
        return AesHelper.newStringUtf8(encode(paramArrayOfByte));
    }


    abstract void encode(byte[] paramArrayOfByte, int paramInt1, int paramInt2);


    abstract void decode(byte[] paramArrayOfByte, int paramInt1, int paramInt2);


    protected abstract boolean isInAlphabet(byte paramByte);


    public boolean isInAlphabet(byte[] paramArrayOfByte, boolean paramBoolean) {
        for (int i = 0; i < paramArrayOfByte.length; i++) {
            if ((!isInAlphabet(paramArrayOfByte[i])) && ((!paramBoolean) || ((paramArrayOfByte[i] != 61) &&
                    (!isWhiteSpace(paramArrayOfByte[i]))))) {
                return false;
            }
        }
        return true;
    }


    public boolean isInAlphabet(String paramString) {
        return isInAlphabet(AesHelper.getBytesUtf8(paramString), true);
    }


    protected boolean containsAlphabetOrPad(byte[] paramArrayOfByte) {
        if (paramArrayOfByte == null) {
            return false;
        }
        for (int i = 0; i < paramArrayOfByte.length; i++) {
            if ((61 == paramArrayOfByte[i]) || (isInAlphabet(paramArrayOfByte[i]))) {
                return true;
            }
        }
        return false;
    }


    public long getEncodedLength(byte[] paramArrayOfByte) {
        long l = (paramArrayOfByte.length + this.mUnencodedBlockSize - 1) / this.mUnencodedBlockSize * this.mEncodedBlockSize;
        if (this.mLineLength > 0) {
            l += (l + this.mLineLength - 1L) / this.mLineLength * this.mChunkSeparatorLength;
        }
        return l;
    }
}


/* Location:              /Users/zl/Desktop/未命名文件夹 3/umeng_social_net.jar!/com/umeng/socialize/net/utils/BaseNCodec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */