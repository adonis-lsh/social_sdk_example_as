package com.umeng.socialize.net.utils;


public class Base64
        extends BaseNCodec {
    private static final int BITS_PER_ENCODED_BYTE = 6;


    private static final int BYTES_PER_UNENCODED_BLOCK = 3;


    private static final int BYTES_PER_ENCODED_BLOCK = 4;


    static final byte[] CHUNK_SEPARATOR = {13, 10};


    private static final byte[] STANDARD_ENCODE_TABLE = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};


    private static final byte[] URL_SAFE_ENCODE_TABLE = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95};


    private static final byte[] DECODE_TABLE = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, 62, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51};


    private static final int MASK_6BITS = 63;


    private final byte[] mEncodeTable;


    private final byte[] mDecodeTable = DECODE_TABLE;


    private final byte[] mLineSeparator;


    private final int mDecodeSize;


    private final int mEncodeSize;


    private int mBitWorkArea;


    public Base64() {
        this(0);
    }


    public Base64(boolean paramBoolean) {
        this(76, CHUNK_SEPARATOR, paramBoolean);
    }


    public Base64(int paramInt) {
        this(paramInt, CHUNK_SEPARATOR);
    }


    public Base64(int paramInt, byte[] paramArrayOfByte) {
        this(paramInt, paramArrayOfByte, false);
    }


    public Base64(int paramInt, byte[] paramArrayOfByte, boolean paramBoolean) {
        super(3, 4, paramInt, paramArrayOfByte == null ? 0 : paramArrayOfByte.length);


        if (paramArrayOfByte != null) {
            if (containsAlphabetOrPad(paramArrayOfByte)) {
                String str = AesHelper.newStringUtf8(paramArrayOfByte);
                throw new IllegalArgumentException("lineSeparator must not contain base64 characters: [" + str + "]");
            }
            if (paramInt > 0) {
                this.mEncodeSize = (4 + paramArrayOfByte.length);
                this.mLineSeparator = new byte[paramArrayOfByte.length];
                System.arraycopy(paramArrayOfByte, 0, this.mLineSeparator, 0, paramArrayOfByte.length);
            } else {
                this.mEncodeSize = 4;
                this.mLineSeparator = null;
            }
        } else {
            this.mEncodeSize = 4;
            this.mLineSeparator = null;
        }
        this.mDecodeSize = (this.mEncodeSize - 1);
        this.mEncodeTable = (paramBoolean ? URL_SAFE_ENCODE_TABLE : STANDARD_ENCODE_TABLE);
    }


    void encode(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        if (this.mEof) {
            return;
        }

        int i;
        if (paramInt2 < 0) {
            this.mEof = true;
            if ((0 == this.mModulus) && (this.mLineLength == 0)) {
                return;
            }
            ensureBufferSize(this.mEncodeSize);
            i = this.mPos;
            switch (this.mModulus) {
                case 1:
                    this.mBuffer[(this.mPos++)] = this.mEncodeTable[(this.mBitWorkArea >> 2 & 0x3F)];
                    this.mBuffer[(this.mPos++)] = this.mEncodeTable[(this.mBitWorkArea << 4 & 0x3F)];

                    if (this.mEncodeTable == STANDARD_ENCODE_TABLE) {
                        this.mBuffer[(this.mPos++)] = 61;
                        this.mBuffer[(this.mPos++)] = 61;
                    }

                    break;
                case 2:
                    this.mBuffer[(this.mPos++)] = this.mEncodeTable[(this.mBitWorkArea >> 10 & 0x3F)];
                    this.mBuffer[(this.mPos++)] = this.mEncodeTable[(this.mBitWorkArea >> 4 & 0x3F)];
                    this.mBuffer[(this.mPos++)] = this.mEncodeTable[(this.mBitWorkArea << 2 & 0x3F)];

                    if (this.mEncodeTable == STANDARD_ENCODE_TABLE) {
                        this.mBuffer[(this.mPos++)] = 61;
                    }
                    break;
            }
            this.mCurrentLinePos += this.mPos - i;

            if ((this.mLineLength > 0) && (this.mCurrentLinePos > 0)) {
                System.arraycopy(this.mLineSeparator, 0, this.mBuffer, this.mPos, this.mLineSeparator.length);
                this.mPos += this.mLineSeparator.length;
            }
        } else {
            for (i = 0; i < paramInt2; i++) {
                ensureBufferSize(this.mEncodeSize);
                this.mModulus = ((this.mModulus + 1) % 3);
                int j = paramArrayOfByte[(paramInt1++)];
                if (j < 0) {
                    j += 256;
                }
                this.mBitWorkArea = ((this.mBitWorkArea << 8) + j);
                if (0 == this.mModulus) {
                    this.mBuffer[(this.mPos++)] = this.mEncodeTable[(this.mBitWorkArea >> 18 & 0x3F)];
                    this.mBuffer[(this.mPos++)] = this.mEncodeTable[(this.mBitWorkArea >> 12 & 0x3F)];
                    this.mBuffer[(this.mPos++)] = this.mEncodeTable[(this.mBitWorkArea >> 6 & 0x3F)];
                    this.mBuffer[(this.mPos++)] = this.mEncodeTable[(this.mBitWorkArea & 0x3F)];
                    this.mCurrentLinePos += 4;
                    if ((this.mLineLength > 0) && (this.mLineLength <= this.mCurrentLinePos)) {
                        System.arraycopy(this.mLineSeparator, 0, this.mBuffer, this.mPos, this.mLineSeparator.length);
                        this.mPos += this.mLineSeparator.length;
                        this.mCurrentLinePos = 0;
                    }
                }
            }
        }
    }


    void decode(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        if (this.mEof) {
            return;
        }
        if (paramInt2 < 0) {
            this.mEof = true;
        }
        for (int i = 0; i < paramInt2; i++) {
            ensureBufferSize(this.mDecodeSize);
            int j = paramArrayOfByte[(paramInt1++)];
            if (j == 61) {
                this.mEof = true;
                break;
            }
            if ((j >= 0) && (j < DECODE_TABLE.length)) {
                int k = DECODE_TABLE[j];
                if (k >= 0) {
                    this.mModulus = ((this.mModulus + 1) % 4);
                    this.mBitWorkArea = ((this.mBitWorkArea << 6) + k);
                    if (this.mModulus == 0) {
                        this.mBuffer[(this.mPos++)] = ((byte) (this.mBitWorkArea >> 16 & 0xFF));
                        this.mBuffer[(this.mPos++)] = ((byte) (this.mBitWorkArea >> 8 & 0xFF));
                        this.mBuffer[(this.mPos++)] = ((byte) (this.mBitWorkArea & 0xFF));
                    }
                }
            }
        }


        if ((this.mEof) && (this.mModulus != 0)) {
            ensureBufferSize(this.mDecodeSize);


            switch (this.mModulus) {

                case 2:
                    this.mBitWorkArea >>= 4;
                    this.mBuffer[(this.mPos++)] = ((byte) (this.mBitWorkArea & 0xFF));
                    break;
                case 3:
                    this.mBitWorkArea >>= 2;
                    this.mBuffer[(this.mPos++)] = ((byte) (this.mBitWorkArea >> 8 & 0xFF));
                    this.mBuffer[(this.mPos++)] = ((byte) (this.mBitWorkArea & 0xFF));
            }

        }
    }


    public static String encodeBase64String(byte[] paramArrayOfByte) {
        return AesHelper.newStringUtf8(encodeBase64(paramArrayOfByte, false));
    }


    public static byte[] encodeBase64(byte[] paramArrayOfByte, boolean paramBoolean) {
        return encodeBase64(paramArrayOfByte, paramBoolean, false);
    }


    public static byte[] encodeBase64(byte[] paramArrayOfByte, boolean paramBoolean1, boolean paramBoolean2) {
        return encodeBase64(paramArrayOfByte, paramBoolean1, paramBoolean2, Integer.MAX_VALUE);
    }


    public static byte[] encodeBase64(byte[] paramArrayOfByte, boolean paramBoolean1, boolean paramBoolean2, int paramInt) {
        if ((paramArrayOfByte == null) || (paramArrayOfByte.length == 0)) {
            return paramArrayOfByte;
        }


        Base64 localBase64 = paramBoolean1 ? new Base64(paramBoolean2) : new Base64(0, CHUNK_SEPARATOR, paramBoolean2);
        long l = localBase64.getEncodedLength(paramArrayOfByte);
        if (l > paramInt) {
            throw new IllegalArgumentException("Input array too big, the output array would be bigger (" + l + ") than the specified maximum size of " + paramInt);
        }


        return localBase64.encode(paramArrayOfByte);
    }


    public static byte[] decodeBase64(String paramString) {
        return new Base64().decode(paramString);
    }


    protected boolean isInAlphabet(byte paramByte) {
        return (paramByte >= 0) && (paramByte < this.mDecodeTable.length) && (this.mDecodeTable[paramByte] != -1);
    }
}


/* Location:              /Users/zl/Desktop/未命名文件夹 3/umeng_social_net.jar!/com/umeng/socialize/net/utils/Base64.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */