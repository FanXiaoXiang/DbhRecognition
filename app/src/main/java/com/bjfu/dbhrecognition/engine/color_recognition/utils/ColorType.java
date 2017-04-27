package com.bjfu.dbhrecognition.engine.color_recognition.utils;

/**
 * Created by 11827 on 2017/3/19.
 */

public enum ColorType {

    WHITE(0), BLACK(1), RED(2), GREEN(3), BLUE(4),OTHER(-1);

    private int value;

    private ColorType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
