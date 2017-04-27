package com.bjfu.androidlib.engine.location.get.utils;

/**
 * Created by 11827 on 2017/2/19.
 */

public enum TriggerTypes {
    BaiduMap(0), GoogleMap(1);
    private int value;

    TriggerTypes(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
