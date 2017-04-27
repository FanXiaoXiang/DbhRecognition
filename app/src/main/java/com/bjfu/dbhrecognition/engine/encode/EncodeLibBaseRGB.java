package com.bjfu.dbhrecognition.engine.encode;

/**
 * Created by 11827 on 2016/12/5.
 */

public class EncodeLibBaseRGB {
    public static final String ENCODE_0 = "120";
    public static final String ENCODE_1 = "130";
    public static final String ENCODE_2 = "110";
    public static final String ENCODE_3 = "200";
    public static final String ENCODE_4 = "210";
    public static final String ENCODE_5 = "230";
    public static final String ENCODE_6 = "220";
    public static final String ENCODE_7 = "300";
    public static final String ENCODE_8 = "330";
    public static final String ENCODE_9 = "310";
    public static final String ENCODE_START = "10";
    public static final String ENCODE_END = "21";
    public static final int INT_PART_MODELS = 10;
    public static final int DECIMAL_PART_MODELS = 7;

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
