package com.bjfu.dbhrecognition.engine.color_recognition.utils;

/**
 * Created by 11827 on 2017/3/20.
 */

public enum OrientType {
    HORIZONTAL(0), VERTICAL(1);

    private int value;

    OrientType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
