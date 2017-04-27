package com.bjfu.dbhrecognition.engine.encode;

/**
 * Created by 11827 on 2016/11/24.
 */

public class EncodeLibBaseRed {
    public static final String ENCODE_0 = "1010010";
    public static final String ENCODE_1 = "1100110";
    public static final String ENCODE_2 = "1101100";
    public static final String ENCODE_3 = "1000010";
    public static final String ENCODE_4 = "1011100";
    public static final String ENCODE_5 = "1001110";
    public static final String ENCODE_6 = "1010000";
    public static final String ENCODE_7 = "1000100";
    public static final String ENCODE_8 = "1001000";
    public static final String ENCODE_9 = "1010100";
    public static final String ENCODE_START = "101";
    public static final String ENCODE_END = "111101";
    public static final int INT_PART_MODELS = 23;
    public static final int DECIMAL_PART_MODELS = 16;

    public static int getValue(String code) {
        if (code.equals(ENCODE_0)) return 0;
        if (code.equals(ENCODE_1)) return 1;
        if (code.equals(ENCODE_2)) return 2;
        if (code.equals(ENCODE_3)) return 3;
        if (code.equals(ENCODE_4)) return 4;
        if (code.equals(ENCODE_5)) return 5;
        if (code.equals(ENCODE_6)) return 6;
        if (code.equals(ENCODE_7)) return 7;
        if (code.equals(ENCODE_8)) return 8;
        if (code.equals(ENCODE_9)) return 9;
        return -1;
    }
}
