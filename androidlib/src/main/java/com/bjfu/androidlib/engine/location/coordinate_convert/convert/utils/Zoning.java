package com.bjfu.androidlib.engine.location.coordinate_convert.convert.utils;

/**
 * Created by 11827 on 2017/2/16.
 * 分带选择
 */

public enum Zoning {
    DEG_3(3), DEG_6(6);
    private int value;

    Zoning(int n) {
        this.value = n;
    }

    public int getValue() {
        return value;
    }
}
